#include <pthread.h>
#include <stdio.h>
#define True 1
#define FASLE 0
#define UP 1
#define DOWN 0
	
typedef struct 
{
	int Maxfloors;
	int numPeople;
}elevator;

typedef struct 
{
	int current_floor;
	int going_to;
}person;

void* start(void* floors)
{
	int i = (int) floors;
	elevator e;
	e.Maxfloors = i;
}

void* movefloors( void* p)
{
	person* i = (person *) p;	 
}

void start_elevator(int num_floors)
{
	pthread_t myElevator;	
	pthread_create(&myElevator, NULL, start, (void *) num_floors);	
}

void take_elevator(int from_floor, int to_floor)
{
	person p;
	pthread_t somebody;
	p.current_floor = from_floor;
	p.going_to = to_floor; 
	pthread_create(&somebody, NULL, movefloors, (void*) &p);
}

int main()
{
	take_elevator(1,2);
	return 0;
}
