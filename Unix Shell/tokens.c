
/* Pseudocode:

   while (1) {
     printf("tokens$ ");
     fflush(stdout);
     line = read_line()
     if (that was EOF) {
        exit(0);
     }
     tokens = tokenize(line);
     foreach token in reverse(tokens):
       puts(token)
   }

*/

#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <assert.h>
#include <string.h>
#include <ctype.h>

#include "tokens.h"
#include "svec.h"

/* -------- Code taken from Nat Tuck's lecture. */

svec*
make_svec()
{
    svec* sv = malloc(sizeof(svec));
    sv->size = 0;
    sv->cap  = 4;
    sv->data = malloc(4 * sizeof(char*));
    memset(sv->data, 0, 4 * sizeof(char*));
    return sv;
}

void
free_svec(svec* sv)
{
    for (int ii = 0; ii < sv->size; ++ii) {
        if (sv->data[ii] != 0) {
            free(sv->data[ii]);
        }
    }
    free(sv->data);
    free(sv);
}

char*
svec_get(svec* sv, int ii)
{
    assert(ii >= 0 && ii < sv->size);
    return sv->data[ii];
}

void
svec_put(svec* sv, int ii, char* item)
{
    assert(ii >= 0 && ii < sv->size);
    sv->data[ii] = strdup(item);

}

void svec_push_back(svec* sv, char* item)
{
    int ii = sv->size;

    if (ii >= sv->cap) {
        sv->cap *= 2;
        sv->data = (char**) realloc(sv->data, sv->cap * sizeof(char*));
    }

    sv->size = ii + 1;
    svec_put(sv, ii, item);
}

/* ---------------------------------------*/

/* code referenced from mergesort readline function from last assignment*/
void
read_line(char* ret, long num) {
	long ii = 0;

	for (; ii < num; ++ii) {
		int cc = getchar();
		if (cc == '\n') {
			break;
		}
		else {
			ret[ii] = cc;
		}
	}

	ret[ii] = 0;
}

//converts a char into a char pointer
char*
convert_char(char c) {
	char* ret = malloc(2);
	ret[0] = c;
	ret[1] = 0;
	return ret;
}

//turns a vector into a char* 
char*
get_token(svec* string) {
	int end = string->size - 1;
	char* token = malloc(end + 2);
	for (int ii = 0; ii <= end; ++ii) {
		token[ii] = svec_get(string, ii)[0];
	}
	token[end + 1] = 0;
	return token;
}

svec*
tokenize(const char* line) {
	svec* ret = make_svec(); //make return vector
	svec* string;            // temporary token holder while the tokens are being built.
	string = make_svec();
	
	for (int ii = 0; ii < strlen(line); ++ii) {
		if (isspace(line[ii])) {               //if there's a space and there is a token to return
			if (string->size > 0) {
				char* t = get_token(string);
				svec_push_back(ret, t);
				free(t);
				free_svec(string);
				string = make_svec();
			}
		}
		else if (line[ii] == '<'|| line[ii] == '>' || line[ii] == ';') { 
			if (string->size == 0) {	//if there's an operator and there is no token to return
				char* c = convert_char(line[ii]);
				svec_push_back(ret, c);
				free(c);
			} 
			else {				//else if there is a token to return, return it and then the operator
				char* t = get_token(string);
				svec_push_back(ret, t);
				free(t);
				char* c = convert_char(line[ii]);
                                svec_push_back(ret, c);
                                free(c);
				free_svec(string);
				string = make_svec();
			}
		}
		else if (line[ii] == '|') { 		//special operator, must check if there are more than one together
			if ((ii + 1) >= strlen(line)) { //if this is the last char in the line
				if (string->size == 0) {//if there is no token to return
					char* c = convert_char(line[ii]);
					svec_push_back(ret, c);
					free(c);
				}
				else {			//if there is a token to return
					char* t = get_token(string);
					svec_push_back(ret, t);
					free(t);
					char* c = convert_char(line[ii]);
					svec_push_back(ret, c);
					free(c);
					free_svec(string);
					string = make_svec();
				}
			}
			else if (string->size == 0) { 	//if its not the last char and there is no token to return
				char* c = convert_char(line[ii]);
				svec_push_back(string, c);
				free(c);
			}
			else if (svec_get(string, 0)[0] == '|') { //if the operator is || and not |
				char* c = convert_char(line[ii]);
				svec_push_back(string, c);
				free(c);
				char* t = get_token(string);
				svec_push_back(ret, t);
				free(t);
				free_svec(string);
				string = make_svec();
			}	
			else {				//if there is a token to return and the operator is |
				char* t = get_token(string);
				svec_push_back(ret, t);
				free(t);
				free_svec(string);
				string = make_svec();
				char* c = convert_char(line[ii]);
				svec_push_back(string, c);
				free(c);
			}
		}
		else if (line[ii] == '&') {		//same as above but for & instead
                        if ((ii + 1) >= strlen(line)) {
                                if (string->size == 0) {
					char* c = convert_char(line[ii]);
                                        svec_push_back(ret, c);
					free(c);
                                }
                                else {
					char* t = get_token(string);
                                        svec_push_back(ret, t);
					free(t);
					char* c = convert_char(line[ii]);
                                        svec_push_back(ret, c);
					free(c);
                                        free_svec(string);
					string = make_svec();
                                }
                        }
                        else if (string->size == 0) {
				char* c = convert_char(line[ii]);
                                svec_push_back(string, c);
				free(c);
                        }
                        else if (svec_get(string, 0)[0] == '&') {
				char* c = convert_char(line[ii]);
                                svec_push_back(string, c);
				free(c);
				char* t = get_token(string);
                                svec_push_back(ret, t);
				free(t);
                                free_svec(string);
				string = make_svec();
                        }
                        else {
				char* t = get_token(string);
                                svec_push_back(ret, t);
				free(t);
                                free_svec(string);
				string = make_svec();
				char* c = convert_char(line[ii]);
                                svec_push_back(string, c);
				free(c);
                        }
                }

		else {							//if line[ii] isnt a special char
			if (ii + 1 >= strlen(line)) {			// if its the last char return everything
				char* c = convert_char(line[ii]);
				svec_push_back(string, c);
				free(c);
				char* t = get_token(string);
				svec_push_back(ret, t);
				free(t);
				free_svec(string);
			}
			else if (string->size > 0) {			//else if the token to return is a special char
				if (svec_get(string, 0)[0] == '|' || svec_get(string, 0)[0] == '&') {
					char* t = get_token(string);
					svec_push_back(ret, t);
					free(t);
					free_svec(string);
					string = make_svec();
					char* c = convert_char(line[ii]);
					svec_push_back(string, c);
					free(c);
				}
				else {					//else if its not
					char* c = convert_char(line[ii]);
					svec_push_back(string, c);
					free(c);
				}
			}
			else {
				char* c = convert_char(line[ii]);
				svec_push_back(string, c);
				free(c);
			}
		}
		

	}
	free_svec(string);
	return ret;
}

//reverse the tokens
svec*
reverse(svec* list) {
	svec* reversed = make_svec();
	for (int ii = list->size - 1; ii >= 0; --ii) {
		svec_push_back(reversed, svec_get(list, ii));
	}
	return reversed;
}

svec*
make_tokens(char* argv) {
	svec* tokens = tokenize(argv);
	//svec* reversed = reverse(tokens);
	//free_svec(tokens);
	return tokens;
	//free_svec(reversed);
}

