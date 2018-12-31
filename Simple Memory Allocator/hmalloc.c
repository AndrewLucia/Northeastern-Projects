
#include <stdlib.h>
#include <sys/mman.h>
#include <stdio.h>

#include "hmalloc.h"

/*
typedef struct hm_stats {
    long pages_mapped;
    long pages_unmapped;
    long chunks_allocated;
    long chunks_freed;
    long free_length;
} hm_stats; */

typedef struct node {
    size_t size;
    struct node* next;
}node;

const size_t PAGE_SIZE = 4096;
static hm_stats stats; // This initializes the stats to 0.
node* head = NULL;

long
free_list_length()
{
    long count = 0;
    node* start = head;
    while (start != NULL) {
	start = start->next;
	count += 1;
    }
    return count;
}

hm_stats*
hgetstats()
{
    stats.free_length = free_list_length();
    return &stats;
}

void
hprintstats()
{
    stats.free_length = free_list_length();
    fprintf(stderr, "\n== husky malloc stats ==\n");
    fprintf(stderr, "Mapped:   %ld\n", stats.pages_mapped);
    fprintf(stderr, "Unmapped: %ld\n", stats.pages_unmapped);
    fprintf(stderr, "Allocs:   %ld\n", stats.chunks_allocated);
    fprintf(stderr, "Frees:    %ld\n", stats.chunks_freed);
    fprintf(stderr, "Freelen:  %ld\n", stats.free_length);
}

//finds the node before the given one
node*
find_before(node* start, node* node) {
    if (start->next == NULL) {
	return start;
    }
    else if ((long) start->next > (long) node) {
        return start;
    }
    else {
        return find_before(start->next, node);
    }
}

//insert a node into the linked list
void
insert(node* node) {
    struct node* b = NULL;
    if (head == NULL) {
	head = node;
	head->next = NULL;
	head->size = node->size;
    }
    if ((long)node < (long)head) {
        node->next = head;
        head = node;
    }
    else {
        b = find_before(head, node);
        node->next = b->next;
        b->next = node;
    }
}

void
replace(node* old, node* replacement) {
    if (old == head) {
	head = replacement;
	head->size = replacement->size;
	head->next = old->next;
    }
    else {
	node* b = find_before(head, old);
	b->next = replacement;
	replacement->next = old->next;
    }
}

static
size_t
div_up(size_t xx, size_t yy)
{
    // This is useful to calculate # of pages
    // for large allocations.
    size_t zz = xx / yy;

    if (zz * yy == xx) {
        return zz;
    }
    else {
        return zz + 1;
    }
}

//finds a free block of memory
node*
find_free(size_t need) {
    node* current = head;
    node* before = head;
    while (current != NULL && current->size < need) {
	before = current;
	current = current->next;
    }
    if (current != NULL) {
        before->next = current->next;
    }
    if(current == head && before == head) {
	head = NULL;
    }
    return current;
}


//finds the last node in the linked list
node*
find_last(node* start) {
    if (start->next == NULL) {
        return start;
    }
    else {
        return find_last(start->next);
    }
}

void*
hmalloc(size_t size)
{
    stats.chunks_allocated += 1;
    if (size + sizeof(size_t) < 16) {
	size = 16;
    }
    else {
	size += sizeof(size_t);
    }
    void* chunk;

    /*if (head == NULL) {
	stats.pages_mapped += 1;
	head = mmap(0, PAGE_SIZE, PROT_READ|PROT_WRITE, MAP_PRIVATE|MAP_ANONYMOUS, -1, 0);
	head->size = PAGE_SIZE - sizeof(node);
	head->next = NULL;
    }*/
    node* free = find_free(size);

    //if there is not enough free space on any current pages, make a new page
    if (free == NULL) {
	size_t pages = div_up(size + sizeof(node), PAGE_SIZE);
	stats.pages_mapped += (long) pages;
	node* next = mmap(0, PAGE_SIZE * pages, PROT_READ|PROT_WRITE, MAP_PRIVATE|MAP_ANONYMOUS, -1, 0);

	chunk = (void*) next;
	((size_t*) chunk)[0] = size;

	next = ((void*) next) + size;
       	next->size = (PAGE_SIZE * pages) - sizeof(node) - size;
	next->next = NULL;

	if (head == NULL) {
	    head = next;
	    head->size = next->size;
	    head->next = NULL;
	}
	else {
	    insert(next);
	}	    
    }
    //found a free space, use it.
    else {
	chunk = (void*)free;

	long tempsize = (long)free->size;

	node* free1 = ((void*) free) + size;
	free1->size = tempsize - size;
	if (head == NULL) {
	    head = free1;
	    head->next = NULL;
	    head->size = free1->size;
	}
	else {
	    replace(free, free1);
	}
	((size_t*) chunk)[0] = size;
    }
    return chunk + 8;
}

void
coalesce() {
    node* current = head;
    while (current != NULL && current->next != NULL) {
	
	if ((void*)current + current->size + sizeof(node) == current->next) {
	    current->size += current->next->size;
	    current->next = current->next->next;
	}
	current = current->next;
    }
}

void
hfree(void* item) {
    stats.chunks_freed += 1;

    node* new = (node*) (item - 8);
    item = item - 8;
    size_t size = ((size_t*) item)[0];

    new->size = size - sizeof(node);
    new->next = NULL;

    if (size >= PAGE_SIZE) {
	node* b = find_before(head, new);
	if (b->next != NULL) {
    	    b->next = b->next->next;
	}
	size_t pages = div_up(size, PAGE_SIZE);
	munmap((void*) new, (pages - 1) * PAGE_SIZE);
	stats.pages_unmapped += (long) pages;
    }
    else {
	insert(new);
    }

    coalesce();
}

