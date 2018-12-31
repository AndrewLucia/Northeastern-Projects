.global main

.text

/* void merge(long* ys, long* as, long an, long* bs, long bn) */
/* ys = %rdi */
/* as = %rsi */
/* an = %rdx */
/* bs = %rcx */
/* bn = %r8 */
merge:
	enter $16, $0

	mov $0, %r9   /* r9 = ii */
	mov $0, %r10  /* r10 = jj */
	mov $0, %r11  /* r11 = kk */
	
while:

	cmp %rdx, %r9
	jge orr

/* if (ii == an) {
       ys[kk++] = bs[jj++];
        }
*/
if:
	cmp %r9, %rdx
	jne else_if

	mov (%rcx, %r10, 8), %r12
	mov %r12, (%rdi, %r11, 8)

	inc %r11
	inc %r10
	jmp while
/* else if (jj == bn) {
            ys[kk++] = as[ii++];
        }
*/	
else_if:
	cmp %r10, %r8
	jne else

	mov (%rsi, %r9, 8), %r12
	mov %r12, (%rdi, %r11, 8)

	inc %r11
	inc %r9

	jmp while
/* else {
        long aa = as[ii];
        long bb = bs[jj];
*/
else:
	mov (%rsi, %r9, 8), %r12  /* r12 = aa */
	mov (%rcx, %r10, 8), %rax /* rax = bb */
/* if (aa < bb) {
       ys[kk++] = as[ii++];
            } */
	cmp %rax, %r12
	jge inner_else

	mov (%rsi, %r9, 8), %r12
	mov %r12, (%rdi, %r11, 8)

	inc %r9
	inc %r11
	
	jmp while
/* else {
        ys[kk++] = bs[jj++];
            }
 */
inner_else:
	
	mov (%rcx, %r10, 8), %r12
	mov %r12, (%rdi, %r11, 8)

	inc %r10
	inc %r11

	jmp while
orr:
	cmp %r8, %r10
	jge end
	
	jmp if
end:
	leave
	ret

/* long* msort(long* xs, long nn) */
/* xs = %rdi */
/* nn = %rsi */
msort:	
	push %r9  /* bn */
	push %r11 /* an */
	push %r12 /* ys  */
	push %r13 /* as */
	push %r14 /* bs */
	push %r15 /* xs*/
	push %rbx /* nn */
	enter $0, $0
	
	mov %rdi, %r15 # xs
	mov %rsi, %rbx # nn

	mov %rsi, %rax
	imul $8, %rax
	mov %rax, %rdi

	call malloc

	mov %rax, %r12

	cmp $1, %rbx
	jg next

	mov %r12, %rdi
	mov %r15, %rsi
	mov %rbx, %rdx
	mov $0, %rcx
	mov $0, %r8

	call merge
	mov %rdi, %rax
	jmp end_msort

next:
	mov $0, %rdx
	mov $2, %r10
	mov %rbx, %rax
	idiv %r10
	mov %rax, %r11 #r11 = an
	
	sub %r11, %rbx
	mov %rbx, %r9  #r9 = bn
	
	#msort(xs, an)
	mov %r15, %rdi
	mov %r11, %rsi
	call msort

	mov %rax, %r13

	#msort(xs + an, bn)
	mov $8, %rax
	imul %r11, %rax
	add %r15, %rax
	mov %rax, %rdi
	mov %r9, %rsi
	call msort
	mov %rax, %r14

	#merge(ys, as, an, bs, bn)
	mov %r12, %rdi
	mov %r13, %rsi
	mov %r11, %rdx
	mov %r14, %rcx
	mov %r9, %r8
	call merge
	mov %rdi, %r12

	mov %r13, %rdi
	call free
	mov %r14, %rdi
	call free

	mov %r12, %rax
end_msort:

	leave
	pop %rbx
        pop %r15
        pop %r14
        pop %r13
        pop %r12
	pop %r11
	pop %r9

	ret

/* long getch() */	
getch:
	enter $16, $0

	mov $0, %rdi
	lea 0(%rsp), %rsi

	mov $1, %rdx
	mov $0, %rax #read

	syscall
	
	mov $0, %rax
	movb 0(%rsp), %al

	leave
	ret

/*void read_line(char* ys, long nn) */
/* ys = %rdi */
/* nn = %rsi */
read_line:
	push %rbx
	push %r12
	push %r13	
	enter $16, $0

	mov $0, %rbx  /* ii = %rbx */
	mov %rdi, %r12 /* ys = %r12 */
	mov %rsi, %r13 /* nn = -8(%rbp) */
	decq %r13
loop:
	cmp %r13, %rbx
	jge loop_done

	call getch /* cc = %rax */

	cmp $'\n, %al
	je loop_done

	movb %al, (%r12, %rbx)
	inc %rbx
	jmp loop
loop_done:
	movb $0, (%r12, %rbx)
	mov %r12, %rax
	leave
	pop %r13
	pop %r12
	pop %rbx
	ret

/* long read_int() */
read_int:
	enter $80, $0  /* line[80] */
	lea 0(%rsp), %rdi
	mov $80, %rsi
	call read_line
	mov %rax, %rdi
	call atol

	leave
	ret
 
main:
	push %r13 /* ys */
	push %r14 /* nn */
	push %r15 /* xs */
	push %rbx /* counter */
	enter $16, $0

	mov $prompt, %rdi
	mov $0, %al
	call printf
	call read_int
	mov %rax, %r14
	
	imul $8, %rax
	mov %rax, %rdi
	call malloc
	mov %rax, %r15	
	mov $0, %rbx
loop2:	
	cmp %r14, %rbx
	jge loop2_done

	mov $num, %rdi
	mov $0, %al
	call printf
	call read_int

	mov %rax, (%r15, %rbx, 8)

	inc %rbx
	jmp loop2

loop2_done:
	mov %r15, %rdi

	mov %r14, %rsi
	call msort
	mov %rax, %r13

	mov $sorted, %rdi
	mov $0, %al
	call printf

	mov $0, %rbx
loop3:
	cmp %r14, %rbx
	jge loop3_done
	
	mov $fmt, %rdi
	mov (%r13, %rbx, 8), %rsi
	mov $0, %al	
	call printf

	inc %rbx
	jmp loop3

loop3_done:
	mov $eol, %rdi
	mov $0, %al
	call printf

	mov $unsorted, %rdi
	mov $0, %al
	call printf
	
	mov $0, %rbx
loop4:
	cmp %r14, %rbx
	jge loop4_done

	mov $fmt, %rdi
	mov (%r15, %rbx, 8), %rsi

	mov $0, %al
	call printf

	inc %rbx

	jmp loop4

loop4_done:
	mov $eol, %rdi
	mov $0, %al
	call printf

	mov %r15, %rdi
	call free
	mov %r13, %rdi
	call free

	leave
	pop %rbx
	pop %r15
	pop %r14
	pop %r13
	ret


.data
prompt: .string "How many numbers?\n"
num: .string "number?\n"
sorted: .string "Sorted array:\n"
unsorted: .string "Unsorted array:\n"
fmt: .string "%ld "
eol: .string "\n"



