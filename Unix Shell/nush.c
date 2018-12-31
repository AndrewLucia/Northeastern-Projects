#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <fcntl.h>

#include "svec.h"
#include "tokens.h"

//finds if the vector contains the target
int
contains(svec* tokens, char* target) {
    for(int ii = 0; ii < tokens->size; ++ii) {
	if (strcmp(svec_get(tokens, ii), target) == 0) {
	    return ii;
	}
    }
    return -1;
}

//converts a vector into a char*
char*
convert(svec* target) {
    char* ret = malloc(strlen(svec_get(target, 0)) + 1);
    for (int ii = 0; ii < strlen(svec_get(target, 0)); ++ii) {
	    ret[ii] = svec_get(target, 0)[ii];
    }
    return ret;
}

//splits the vector based on the start and end point.
svec*
undo(svec* tokens, int s, int e) {
    svec* ret = make_svec();
    for (int ii = s; ii < e; ++ii) {
        svec_push_back(ret, svec_get(tokens, ii));
    }	
    return ret;
}

//executes based on the vectore passed in, looks for operators first then acts accordingly
int
execute(svec* tokens)
{
    for (int jj = 0; jj < tokens->size; ++jj) {
        if (strcmp(svec_get(tokens, jj), ";") == 0) {
	    svec* left = undo(tokens, 0, jj);
	    svec* right = undo(tokens, jj + 1, tokens->size);
            execute(left);
            execute(right);
	    free_svec(left);
	    free_svec(right);
            return 0;
        }
        else if(strcmp(svec_get(tokens, jj), "&&") == 0) { //gives precedence to certain operators
	    svec* right = undo(tokens, jj + 1, tokens->size);
	    svec* left = undo(tokens, 0, jj);
	    int t = -1;
	    if (contains(right, ";") != -1) {
	    	t = contains(right, ";");
	    }
	    else if (contains(right, ">") != -1) {
		t = contains(right, ">");
	    }
	    else if (contains(right, "<") != -1) {
                t = contains(right, "<");
            }
	    else if (contains(right, "|") != -1) {
                t = contains(right, "|");
            }
	    else if (contains(right, "&") != -1) {
                t = contains(right, "&");
            }
	    else if (contains(right, "&&") != -1) {
                t = contains(right, "&&");
            }
	    else if (contains(right, "||") != -1) {
                t = contains(right, "||");
            }
	    if (t != -1) {
		if (execute(left) == 0) {
		    svec* middle = undo(tokens, jj + 1, jj + 1 + t);
		    execute(middle);
		    free_svec(middle);
		    svec* nright = undo(tokens, t + 1, tokens->size);
		    execute(nright);
		    free_svec(nright);
		    free_svec(left);
		    free_svec(right);
		    return 0;
		}
		else {
		    svec* nright = undo(tokens, t + jj + 2, tokens->size);
		    execute(nright);
		    free_svec(nright);
		    free_svec(left);
		    free_svec(right);
		    return 0;
		}
	    }
            if (execute(left) == 0) {
                execute(right);
		free_svec(right);
            }
	    free_svec(left);
            return 0;
        }
        else if (strcmp(svec_get(tokens, jj), "||") == 0) {
	    svec* left = undo(tokens, 0, jj);
            if (execute(left) == 0) {
		free_svec(left);
                return 0;
            }
	    svec* right = undo(tokens, jj + 1, tokens->size);
            execute(right);
	    free_svec(left);
	    return 0;
        }
        else if (strcmp(svec_get(tokens, jj), ">") == 0) { //refeerences Nat Tuck's lecture on redir
            int ccpid;
            if ((ccpid = fork())) {
                waitpid(ccpid, 0, 0);
            }
            else {
		svec* right = undo(tokens, jj + 1, tokens->size);
                int fd = open(svec_get(right, 0), O_CREAT | O_APPEND | O_WRONLY, 0644);
		close(1);
                dup(fd);
                close(fd);
		svec* left = undo(tokens, 0, jj);
                execute(left);
		free_svec(right);
		free_svec(left);
            }
            return 0;
        }
        else if (strcmp(svec_get(tokens, jj), "<") == 0) { //references Nat Tuck's lecture on redir
	    int ccpid;
            if ((ccpid = fork())) {
                waitpid(ccpid, 0, 0);
            }
            else {
                svec* right = undo(tokens, jj + 1, tokens->size);
                int fd = open(convert(right), O_RDONLY);
                close(0);
                dup(fd);
                close(fd);
                svec* left = undo(tokens, 0, jj);
                execute(left);
                free_svec(right);
                free_svec(left);
            }
            return 0;
        }
	else if (strcmp(svec_get(tokens, jj), "&") == 0) {
	    int cpid2;
	    if ((cpid2 = fork())) {
	    }
	    else { //execvp immediately so that the process isnt run again
		int size;
        	if (tokens->size > 0) {
            	    size = tokens->size;
        	}
        	else {
            	    size = 1;
        	}
        	char* args[size];
        	int ii = 0;
        	for (; ii < tokens->size; ++ii) {
            	    args[ii] = svec_get(tokens, ii);
        	}
        	char* cmd = svec_get(tokens, 0);
        	args[ii] = 0;

        	execvp(cmd, args);
	    }
	    return 0;
	}
	else if (strcmp(svec_get(tokens, jj), "cd") == 0) {
	    svec* right = undo(tokens, jj + 1, tokens->size);
	    char* dir = svec_get(right, 0);
	    chdir(dir);
	    free_svec(right);
	    return 0;
	}
	else if (strcmp(svec_get(tokens, jj), "exit") == 0) {
	    exit(0);
	}
	else if (strcmp(svec_get(tokens, jj), "|") == 0) { //references Nat Tucks lecture on pipes... heavily:)
	    int cpid3;
	    if ((cpid3 = fork())) {
		waitpid(cpid3, 0, 0);
	    }
	    else {
		int pipe_fds[2];
    		pipe(pipe_fds);
  
    		int p_read  = pipe_fds[0];
    		int p_write = pipe_fds[1];

		if ((cpid3 = fork())) { //hook and close other side
		    close(p_write);
		    close(0);
		    dup(p_read);
		    close(p_read);

		    svec* right = undo(tokens, jj + 1, tokens->size);
		    execute(right);
		    free_svec(right);
		    waitpid(cpid3, 0, 0);
		}
		else {
		    close(p_read);
		    close(1);
		    dup(p_write);
		    close(p_write);
		    svec* left = undo(tokens, 0, jj);
		    execute(left);
		    free_svec(left);
		}
	    }
	    return 0;
	}

    }

    int cpid;
    if ((cpid = fork())) {
        // parent process

        int status;
        waitpid(cpid, &status, 0);
	return status;
    }
    else {
        // child process
	
	//creates the args array
	int size;
	if (tokens->size > 0) {
	    size = tokens->size;
	}
	else {
	    size = 1;
	}
	char* args[size];
	int ii = 0;
	for (; ii < tokens->size; ++ii) {
	    args[ii] = svec_get(tokens, ii);
	}
	char* cmd = svec_get(tokens, 0);
	args[ii] = 0;
	
        execvp(cmd, args);
   }
}

int
main(int argc, char* argv[])
{

    while (1) {
        char cmd[256];
	//if no script is passed in
        if (argc == 1) {
            printf("nush$ ");
            fflush(stdout);
            fgets(cmd, 256, stdin);
	    if (feof(stdin)) {
                goto loop;
            }
	    if (cmd[0] == '\n') {
	        continue;
	    }   
        }
	//if a script is passed in
        else if (argc == 2) {
            FILE* fp;
	    char* name = argv[1];
	    fp = fopen(name, "r");
	    char* line = malloc(256);
	    char* s = fgets(line, 256, fp);
        while(s != 0) { //while there is still another line
	    if (strlen(line) == 0) {
		free(line);
	        return 0;
		}
	        svec* tokens;
        	tokens = make_tokens(line);
  	        if (execute(tokens) != 0) {
		free(line);
	        return 0;
	    }
	    free_svec(tokens);
	    s = fgets(line, 256, fp);
	    }
	    free(line);	    
	    return 0;
        }
        if (strcmp(cmd, "exit\n") == 0) {
	    exit(0);
        }

        svec* tokens;
        tokens = make_tokens(cmd);
        if (execute(tokens) != 0) {
	    free_svec(tokens);
	    break;
	}
	free_svec(tokens);
    }
loop:
    return 0;
}
