#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include <unistd.h>

typedef enum {
  false = 0,
  true = 1
} bool;

typedef struct {char symbol; int destination;} Arc;
typedef struct {int num_arcs; bool is_final; Arc* arcs;} State;
typedef struct {int num_states; int initial_state; State* states;} Automaton;


void *realloc(void *ptr, size_t size){

if (ptr == NULL ) ptr=malloc(size);
else
ptr=realloc(ptr,size);

}


/* Alloue de la mémoire pour représenter un automate et initialise ses champs*/
void create_automaton(Automaton *automate){


automate->num_states=1;
automate->initial_state=0;

automate->states= realloc(automate->states,sizeof(State));
automate->states[0].num_arcs=0;
automate->states[0].is_final=true;
automate->states[0].arcs=NULL;


}







int main()
{ 
Automaton automate ;
create_automaton(&automate);

return 0;
}
