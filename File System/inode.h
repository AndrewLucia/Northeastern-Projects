// based on cs3650 starter code

#ifndef INODE_H
#define INODE_H

#include "pages.h"
#include <sys/stat.h>

typedef struct inode {
    int refs; // reference count
    int mode; // permission & type
    int size; // bytes
    int pages[10]; // the pages this inodes data is located
    time_t create_time; //time created
    time_t change_time; // time modified
} inode;

void print_inode(inode* node);


#endif
