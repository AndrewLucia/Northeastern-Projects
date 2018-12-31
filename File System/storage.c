#include "storage.h"
#include "directory.h"
#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>
#include <string.h>
#include <stdio.h>
#include <libgen.h>
#include <stdlib.h>
#include "util.h"
#include "bitmap.h"

super *super_block = NULL;

void
storage_init(const char *path) {
    pages_init(path);

    super_block = (super *) pages_get_page(0); //super block allocated at 0-th page

    super_block->nodes = (inode *) pages_get_page(1); //inodes stored in first page
    super_block->start = pages_get_page(3); // data starts in third page
    bitmap_put(super_block->map_i, 0, 1); // initialize bitmap
    bitmap_put(super_block->map_i, 1, 1);
    bitmap_put(super_block->map_d, 0, 1);
    bitmap_put(super_block->map_d, 1, 1);
    bitmap_put(super_block->map_d, 2, 1);

    dir *root;
    directory_init(root); //initialize meta data of root directory
    bitmap_put(super_block->map_d, 3, 1);
}


inode *get_nodes() {
    return super_block->nodes;
}


int
storage_stat(const char *path, struct stat *st) {
    if (streq(path, "/.xdg-volume-info") || streq(path, "/autorun.inf") || streq(path, "/.Trash") ||
        streq(path, "/.Trash-1000")) { // files not related to fuse that can cause odd behavior
        return -1;
    }
    dir root = dir_get(1);
    dir *d = &root;
    int i = tree_lookup(d, path);
    inode *n;
    if (i <= 0) {
        return -1;
    }
    n = &(super_block->nodes[i]);
    st->st_mode = n->mode;
    st->st_size = n->size;
    st->st_nlink = n->refs;
    st->st_uid = getuid();
    struct timespec t;
    t.tv_sec = n->change_time;
    t.tv_nsec = (long) n->change_time;
    st->st_mtim = t;
    return 0;
}

int
storage_readdir(const char *path, void *buf, fuse_fill_dir_t filler, off_t offset) {
    dir d;
    inode *root = (inode *) get_node(1);
    dirent *ents = (dirent *) pages_get_page(root->pages[0]);
    d.num = root->pages[0];
    d.inode = root;
    d.dirents = ents;
    dir *dr = &d;
    int i = tree_lookup(dr, path);
    inode *n = get_node(i);

    dir dir;
    dirent *es = (dirent *) pages_get_page(n->pages[0]);
    dir.num = n->pages[0];
    dir.inode = n;
    dir.dirents = es;

    struct stat s;
    char p[128];

    for (int i = 0; i < 64; ++i) {
        char *ent_name = dir.dirents[i].name;
        if (ent_name[0]) {
            strcpy(p, path);
            strcat(p, ent_name);
            storage_stat(p, &s);
            filler(buf, ent_name, &s, 0);
        }
    }
    return 0;
}

//finds the directory name from the path
char *
mknod_start_dname(const char *path) {
    char *dir_name = malloc(strlen(path) * sizeof(char));
    memset(dir_name, 0, strlen(path));
    strcpy(dir_name, path);

    char *dname = dirname(dir_name); //found this c function that cuts off the file name from a path
    return dname;
}

//finds the file name from the path
char *
mknod_start_fname(const char *path) {
    char *file_name = malloc(strlen(path) * sizeof(char));
    memset(file_name, 0, strlen(path));
    strcpy(file_name, path);

    char *file = basename(file_name); //found this c function to find the name of the file from path
    return file;
}

int
storage_mknod(const char *path, int mode) {

    char *file = mknod_start_fname(path);
    char *dname = mknod_start_dname(path);

    dir root = dir_get(1);
    dir *dd = &root;
    dir d = dir_get(tree_lookup(dd, dname));
    if (d.inode == 0) {
        return -1;
    }

    if (file_in_dir(file, d) != -1) { //file is found
        return -2;
    }

    int node_n = inode_next();
    int page_n = page_next();

    inode *node = get_node(node_n);

    node->refs = 1;
    node->mode = mode;
    node->size = 0;
    node->change_time = time(NULL);
    node->create_time = time(NULL);
    node->pages[0] = page_n;

    for (int kk = 1; kk < 10; ++kk) {
        node->pages[kk] = 0;
    }

    super_block->nodes[node_n] = *(node);
    bitmap_put(super_block->map_d, page_n, 1);
    bitmap_put(super_block->map_i, node_n, 1);

    int q = -1;
    for (int i = 0; i < 64; ++i) {
        if (d.dirents[i].inum == 0) {
            q = i;
            strcpy(d.dirents[i].name, file);
            d.dirents[i].inum = node_n;
            break;
        }
    }

    return 0;
}

int
storage_truncate(const char *path, off_t size) {
    dir root = dir_get(1);
    dir *dd = &root;
    int num = tree_lookup(dd, path);
    inode *n = get_node(num);
    n->size = size;

    return 0;
}

int
storage_set_time(const char *path, const struct timespec ts[2]) {

    dir root = dir_get(1);
    dir *dd = &root;
    int i = tree_lookup(dd, path);
    if (i <= 0) {
        return -1;
    }
    inode *n = get_node(i);
    n->create_time = ts[0].tv_sec;
    n->change_time = ts[1].tv_sec;
    return 0;

}

int
storage_rmdir(const char *path) {

    char *pname = mknod_start_dname(path);
    char *child = mknod_start_fname(path);
    dir root = dir_get(1);
    dir *dd = &root;
    dir d = dir_get(tree_lookup(dd, pname));

    if (d.inode == 0) {
        return -1;
    }

    dirent *remove = 0;

    for (int ii = 0; ii < 64; ++ii) {
        if (streq(child, d.dirents[ii].name)) {
            remove = &(d.dirents[ii]);
            break;
        }
    }

    if (remove == 0) {
        return -1;
    }

    dir rm = dir_get(remove->inum);
    if (rm.inode == 0) {
        return -1;
    }

    for (int jj = 0; jj < 64; ++jj) {
        if (rm.dirents[jj].inum != 0) {
            return -2;
        }
    }

    rm.dirents = 0;
    rm.num = 0;
    rm.inode = 0;

    int num = remove->inum;
    remove->inum = 0;
    remove->name[0] = 0;

    if (num < 0) {
        return num;
    }

    inode *n = get_node(num);

    for (int kk = 0; kk < 10; ++kk) {
        int page = n->pages[kk];
        if (page > 2) {
            n->pages[kk] = 0;
            bitmap_put(super_block->map_d, page, 0);
        }
    }
    n->size = 0;
    bitmap_put(super_block->map_i, num, 0);

    return 0;

}

int
storage_unlink(const char *path) {

    char *pname = mknod_start_dname(path);
    char *child = mknod_start_fname(path);

    dir root = dir_get(1);
    dir *dd = &root;
    dir d = dir_get(tree_lookup(dd, pname));

    int num = -1;

    for (int ii = 0; ii < 64; ++ii) {
        if (streq(child, d.dirents[ii].name)) {
            num = d.dirents[ii].inum;
            d.dirents[ii].name[0] = 0;
            d.dirents[ii].inum = 0;
            break;
        }
    }


    inode *n = get_node(num);

    if (n->refs - 1 == 0) {
        n->refs -= 1;
        for (int kk = 0; kk < 10; ++kk) {
            int page = n->pages[kk];
            if (page > 2) {
                n->pages[kk] = 0;
                bitmap_put(super_block->map_d, page, 0);
            }
        }
        n->size = 0;
        bitmap_put(super_block->map_i, num, 0);
    }

    return 0;
}

int
storage_link(const char *from, const char *to) {

    dir root = dir_get(1);
    dir *dd = &root;
    int num = tree_lookup(dd, from);

    char *dname = mknod_start_dname(to);
    char *child = mknod_start_fname(to);

    dir link_dir = dir_get(tree_lookup(dd, dname));

    for (int ii = 0; ii < 64; ++ii) {
        if (link_dir.dirents[ii].inum == 0) {
            link_dir.dirents[ii].inum = num;
            strcpy(link_dir.dirents[ii].name, child);
            break;
        }
    }

    inode *n = get_node(num);
    n->refs += 1;

    return 0;
}

int
storage_read(const char *path, char *buf, size_t size, off_t offset) {

    dir root = dir_get(1);
    dir *dd = &root;
    int num = tree_lookup(dd, path);

    inode *n = get_node(num);

    int num_pages = 1;
    if (size + offset > 4096) {
        num_pages = bytes_to_pages(size + offset);
    }

    int start_page = 0;
    if (offset >= 4096) {
        start_page = bytes_to_pages(offset);
    }

    char *data_read = malloc(10 * 4096);
    data_read[0] = 0;

    size_t bytes = size;

    int place = 0;

    for (int ii = start_page; ii < num_pages; ++ii) {

        if (n->pages[ii] == 0) {
            break;
        }
        if (ii == start_page) {
            if (size > (4096 - (offset - (start_page * 4096)))) {
                memcpy(data_read + place,
                       pages_get_page(n->pages[ii]) + (offset - (start_page * 4096)),
                       4096 - (offset - (start_page * 4096)));
                size -= 4096 - (offset - (start_page * 4096));
                place += 4096 - (offset - (start_page * 4096));
            } else {
                memcpy(data_read + place,
                       pages_get_page(n->pages[ii]) + (offset - (start_page * 4096)),
                       size);
                break;
            }
        } else if (size > 4096) {
            memcpy(data_read + place, pages_get_page(n->pages[ii]), 4096);
            place += 4096;
            size -= 4096;
        } else {
            memcpy(data_read + place, pages_get_page(n->pages[ii]), size);
            break;
        }
    }

    if (data_read[0] == 0) {
        return 0;
    }

    int length = strlen(data_read) + 1;
    if (bytes < length) {
        length = bytes;
    }


    memcpy(buf, data_read, length);
    return length;

}

int
storage_write(const char *path, const char *buf, size_t size, off_t offset) {

    dir root = dir_get(1);

    dir *dd = &root;

    int num = tree_lookup(dd, path);

    inode *n = get_node(num);

    int num_pages = 1;
    if (size + offset > 4096) {
        num_pages = bytes_to_pages(size + offset);
    }
    if (num_pages > 10) {
        num_pages = 10;
    }


    int start_page = 0;
    if (offset >= 4096) {
        start_page = bytes_to_pages(offset);
    }
    size_t bytes = size;

    for (int ii = start_page; ii < num_pages; ++ii) {
        if (n->pages[ii] == 0) {
            int page_n = page_next();
            n->pages[ii] = page_n;
            bitmap_put(super_block->map_d, page_n, 1);

        }
        if (ii == start_page) {
            if (size > (4096 - (offset - (start_page * 4096)))) {
                memcpy(pages_get_page(n->pages[ii]) + (offset - (start_page * 4096)), buf,
                       4096 - (offset - (start_page * 4096)));
                size -= 4096 - (offset - (start_page * 4096));
            } else {
                memcpy(pages_get_page(n->pages[ii]) + (offset - (start_page * 4096)), buf, size);
                break;
            }
        } else if (size > 4096) {
            memcpy(pages_get_page(n->pages[ii]), buf, 4096);
            size -= 4096;
        } else {
            memcpy(pages_get_page(n->pages[ii]), buf, size);
            break;
        }
    }

    n->size += bytes;
    n->change_time = time(NULL);

    return bytes;
}


