#include "bitmap.h"
#include <stdio.h>

//researched how to set up bitmap

int
bitmap_get(void *bm, int ii) {
    char *bit_map = (char *) bm;
    int bit_count = 0;
    char c;
    for (int i = 0; i < 32; i++) { //loop through each char
        c = bit_map[i];
        for (int j = 0; j < 8; j++) { //loop through bits in char
            if (bit_count == ii) {
                int bit = c & (1 << j);
                return bit;
            }
            bit_count++;
        }
    }
}

void
bitmap_put(void *bm, int ii, int vv) {
    int bit_count = 0;
    char c;
    for (int i = 0; i < 32; i++) {
        c = ((char *) bm)[i];
        for (int j = 0; j < 8; j++) {
            if (bit_count == ii) {
                if (vv == 1) {    //set bit
                    c |= (1 << j);
                    ((char *) bm)[i] = c;
                    return;
                } else {           //or clear bit
                    c &= ~(1 << j);
                    ((char *) bm)[i] = c;
                    return;
                }
            }
            bit_count++;
        }
    }
}


