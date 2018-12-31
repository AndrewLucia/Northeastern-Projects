#include "directory.h"
#include "slist.h"
#include "storage.h"
#include "util.h"


void directory_init(dir *d) {
    inode *root = get_node(1);
    root->refs = 1;
    root->mode = 040755;
    root->size = 0;
    root->create_time = time(NULL);
    root->change_time = time(NULL);
    root->pages[0] = 3;
    root->size += 4096;
}

//gets the directory from the inum
dir dir_get(int inum) {
    dir d;
    inode *n = (inode *) get_node(inum);
    dirent *ents = (dirent *) pages_get_page(n->pages[0]);
    d.dirents = ents;
    d.inode = n;
    d.num = n->pages[0];
    return d;
}

// gets the inode numbner of the entry with the given name
int directory_lookup(dir *d, const char *name) {
    int rv = -1;
    for (int i = 0; i < 64; ++i) {
        if (streq(name, d->dirents[i].name)) {
            rv = d->dirents[i].inum;
            break;
        }
    }
    return rv;
}

//finds the inode number from the given path
int tree_lookup(dir *d, const char *path) {
    slist *ds = s_split(path, '/');
    if (!streq(ds->data, "")) {
        return -1;          //error checking
    }
    if (ds->next == 0) {
        return 1;
    }

    int i = -1;
    ds = ds->next;

    while (ds != NULL) {
        for (int ii = 0; ii < 64; ii++) { //loops through entries and finds the matching name
            if (streq(ds->data, d->dirents[ii].name) && (ds->next == NULL)) {
                i = d->dirents[ii].inum; //inode num of entry we want
                break;
            }
        }
        dir tmp = dir_get(directory_lookup(d, ds->data));
        d = &tmp;
        ds = ds->next;
    }

    return i;
}

int
file_in_dir(const char *file, dir d) {
    for (int i = 0; i < 64; i++) {
        if (streq(file, d.dirents[i].name)) {
            return d.dirents[i].inum;
        }
    }

    return -1;
}
