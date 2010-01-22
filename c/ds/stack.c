//Stephen Bromfield
#include <stdio.h>
#include <string.h>
/*
* This is a swap function that takes a generic type
* with another generic type. This can be a very bad
* thing if used wrong. You could overwrite data you
* didn't mean to. 
*/
void swap( void* rhs, void* lhs, int size)
{
	char buffer[size];
	memcpy(buffer, rhs, size);
	memcpy(rhs, lhs, size);
	memcpy(lhs, buffer, size);
}

int main()
{
	char a = 'a';
	char b = 'b';
	
 	printf("before the swap %c %c \n",a ,b);
	swap(&a,&b,1);
	printf("after the swap %c %c\n",a,b); 
	return 0;
}
