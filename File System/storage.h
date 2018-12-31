// based on cs3650 starter code

#ifndef NUFS_STORAGE_H
#define NUFS_STORAGE_H

#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <time.h>
#include <stdbool.h>

#include "slist.h"
#include "inode.h"

#define FUSE_USE_VERSION 26
#include <fuse.h>

typedef struct super{
    char map_d[32];
    char map_i[32];
    inode* nodes;
    void* start;
    int total_inodes;
} super;

void   storage_init(const char* path);
int    storage_stat(const char* path, struct stat* st);
int    storage_rmdir(const char* path);
int    storage_read(const char* path, char* buf, size_t size, off_t offset);
int    storage_write(const char* path, const char* buf, size_t size, off_t offset);
int    storage_truncate(const char *path, off_t size);
int    storage_mknod(const char* path, int mode);
int    storage_unlink(const char* path);
int    storage_link(const char *from, const char *to);
int    storage_set_time(const char* path, const struct timespec ts[2]);

int    storage_readdir(const char* path, void* buf, fuse_fill_dir_t filler, off_t offset);

#endif
