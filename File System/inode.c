#include "inode.h"


void print_inode(inode* node){
    printf("inode- refs: %d, mode: %d, size: %d\n", node->refs, node->mode, node->size);
}
