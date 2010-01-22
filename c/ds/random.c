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

/*This doesn't deal with negative numbers 
 *or letters that could be in the string. 
 *Maybe I'll go back and fix that.....or not.
 */
int myatoi(char* in)
{
	int result = 0;
	//The end of char array end with a 0(null) in memory 
	while(*in != 0)
	{
		/*This works because the dereference and 
		 *post increment unary operators associate 
		 *right to left.
		*/
		result = result * 10 + (*in++ - 48);
	}
	return result;
}
int main()
{
	char a = 'a';
	char b = 'b';
	
 	printf("before the swap %c %c \n",a ,b);
	swap(&a,&b,1);
	printf("after the swap %c %c\n",a,b); 
	printf("%d\n", myatoi("45")); 
	return 0;
}
