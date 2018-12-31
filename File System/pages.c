// based on cs3650 starter code

#define _GNU_SOURCE

#include <string.h>

#include <sys/mman.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>
#include <assert.h>
#include <fcntl.h>
#include <errno.h>
#include <stdio.h>
#include <stdint.h>

#include "pages.h"
#include "util.h"
#include "bitmap.h"
#include "storage.h"
#include "inode.h"

const int PAGE_COUNT = 256;
const int NUFS_SIZE = 4096 * 256; // 1MB

static int pages_fd = -1;
static void *pages_base = 0;
super *super_block;

void
pages_init(const char *path) {
    pages_fd = open(path, O_CREAT | O_RDWR, 0644);
    assert(pages_fd != -1);

    int rv = ftruncate(pages_fd, NUFS_SIZE);
    assert(rv == 0);

    pages_base = mmap(0, NUFS_SIZE, PROT_READ | PROT_WRITE, MAP_SHARED, pages_fd, 0);
    assert(pages_base != MAP_FAILED);

    super_block = (super *) pages_base;
    super_block->total_inodes = 8192 / sizeof(inode);

    for (int i = 0; i < 32; ++i) {
        super_block->map_i[i] = 0;
        super_block->map_d[i] = 0;
    }

}

void
pages_free() {
    int rv = munmap(pages_base, NUFS_SIZE);
    assert(rv == 0);
}

void*
get_node(int inum) {
    if (inum < super_block->total_inodes) {
        inode *n = &(super_block->nodes[inum]);
        return n;
    }
    return NULL;
}

void *
pages_get_page(int pnum) {

    return pages_base + 4096 * pnum;
}

void *
get_pages_bitmap() {
    return pages_get_page(0);
}

void *
get_inode_bitmap() {
    uint8_t *page = pages_get_page(0);
    return (void *) (page + 32);
}

int
alloc_page() {
    void *pbm = get_pages_bitmap();

    for (int ii = 1; ii < PAGE_COUNT; ++ii) {
        if (!bitmap_get(pbm, ii)) {
            bitmap_put(pbm, ii, 1);
            printf("+ alloc_page() -> %d\n", ii);
            return ii;
        }
    }

    return -1;
}

void
free_page(int pnum) {
    void *pbm = get_pages_bitmap();
    bitmap_put(pbm, pnum, 0);
}

//gets the next free page
int
page_next() {
    for (int i = 3; i < 256; i++) {
        if(bitmap_get(super_block->map_d, i) == 0){
            return i;
        }
    }
    return -1;
}

//gets the next free inode
int
inode_next(){
    for(int i =2; i < 256; i++){
        if(bitmap_get(super_block->map_i, i) == 0){
            return i;
        }
    }
    return -1;
}

