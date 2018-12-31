#ifndef TOKENS_C
#define TOKENS_C

#include "svec.h"

void read_line(char* ret, long num);

char* convert_char(char c);

char* get_token(svec* string);

svec* tokenize(const char* line);

svec* reverse(svec* list);

svec* make_tokens(char* argv);


#endif
