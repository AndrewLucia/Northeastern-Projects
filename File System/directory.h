// based on cs3650 starter code

#ifndef DIRECTORY_H
#define DIRECTORY_H

#define DIR_NAME 48

#include "slist.h"
#include "pages.h"
#include "inode.h"


typedef struct dirent {
    char name[DIR_NAME];
    int  inum;
} dirent;

typedef struct dir{
    int num; //page number
    inode* inode;
    dirent* dirents;
}dir;


void directory_init(dir* d);
int directory_lookup(dir* d, const char* name);
int tree_lookup(dir* d, const char* path);
dir dir_get(int inum);
int file_in_dir(const char* file, dir d);

#endif

