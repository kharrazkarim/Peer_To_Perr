#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <sys/msg.h>
#include <pthread.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>

static pthread_mutex_t horloge_mutex;
static pthread_mutex_t printf_mutex;
static pthread_mutex_t file_mutex;
static pthread_mutex_t cp_mutex;


int id_file;
int n;
static int horloge;
static int cp=0;


//structure message
typedef struct Msg Msg ;

struct Msg
{
long int  adr_source ;
long int adr_dest ;
int operation ;
int date ;

};

    // Fonction executée par le thread

     void *thread_operation(void *arg)

    {

     int stat ;
     Msg message ;
     Msg message2 ;
     int nb_msg;

     srand(time(NULL));

     //nb de messages a envoyer
     nb_msg=(rand()%n)+1;

     int a = (intptr_t ) arg;



     for(int i=1 ; i<=nb_msg ;i++)
     {
             message.adr_source=a;

             do {
                message.adr_dest=rand()%n +1;

                }while(message.adr_dest==message.adr_source);


                message.operation=rand()%2;

                pthread_mutex_lock (&horloge_mutex);

                horloge++;
                message.date=horloge;

                pthread_mutex_unlock (&horloge_mutex);


              pthread_mutex_lock (&file_mutex);
              stat = msgsnd(id_file,&message, sizeof(struct Msg),IPC_NOWAIT);


             if ( stat < 0) {
                printf ("Erreur lors de l'envoie du message \n");
                perror("msgsnd");

                            }

            else {

            pthread_mutex_unlock (&file_mutex);


            //compteur de message

            pthread_mutex_lock (&cp_mutex);
            cp++;
            pthread_mutex_unlock(&cp_mutex);



                 // Rendre les opérations Atomique
             pthread_mutex_lock (&printf_mutex);

             //affichage
             printf("Thread %d envoie  %d messages : \n",a,nb_msg);
             printf("adresse source : %ld \n",message.adr_source);
             printf("adresse destination : %ld \n",message.adr_dest);
             printf("operation : %d \n",message.operation);
             printf("Date : %d \n",message.date);
             printf("Message envoye \n");

             pthread_mutex_unlock (&printf_mutex);



                }


        }

        sleep(1);


       do

        {

         pthread_mutex_lock (&file_mutex);


         if((msgrcv(id_file, &message2, sizeof(struct Msg), 0,0)) == -1)
        {

        printf ("Erreur lors de la reception du message \n");
        perror("msgrcv failed");

        } else {


         if(message2.adr_dest == a)  {


           pthread_mutex_lock(&cp_mutex);
           cp--;
           pthread_mutex_unlock (&cp_mutex);

           pthread_mutex_lock (&printf_mutex);

     //affichage
     printf("Thread %d reception message : \n",a);
     printf("adresse source : %ld \n",message2.adr_source);
     printf("adresse destination : %ld \n",message2.adr_dest);
     printf("operation : %d \n",message2.operation);
     printf("Date : %d \n",message2.date);



      pthread_mutex_unlock(&printf_mutex);

           }else

            {
                stat = msgsnd(id_file,&message2, sizeof(message2),0);


                if ( stat < 0) {
                printf ("Erreur lors de l'envoie du message \n");
                perror("msgsnd");

                            }

            }
             pthread_mutex_unlock(&file_mutex);
            }




            }while(cp>0);


}

    void Creation_threads()

    {
       pthread_t *ta;

       ta=malloc(n*sizeof(pthread_t));

    for(int i=1;i<=n;i++)
    {

    if (pthread_create(&ta[i], NULL,&thread_operation,(void *)(intptr_t)i))
     {

    perror("pthread_create");
    exit(EXIT_FAILURE);

     }

}
sleep(1);

for(int j =1 ; j<=n ; j++)
    {
        int s;
       s= pthread_join(ta[j],NULL);

        if (s != 0)
                {
                    perror("pthread_join");
                    exit(EXIT_FAILURE);
                    }
    }

    free(ta);

}


 void Creation_file()
    {

    if((id_file = msgget(IPC_PRIVATE,IPC_CREAT|IPC_EXCL|0600)) == -1)
    {
        perror("msgget failure");
        exit(EXIT_FAILURE);
    }

    printf("Id file : %d \n",id_file);

    }


int main()
{
    //lecture du nombre de processus
    printf("Veuillez donner le nombre de processus \n");
    scanf("%d",&n);

    //Initialisation du mutex de l'horloge
    pthread_mutex_init (&horloge_mutex, NULL);

    //Initialisation du mutex de l'affichage (printf)
     pthread_mutex_init(&printf_mutex, NULL);

    //Initialisation du mutex de la file
    pthread_mutex_init (&file_mutex, NULL);


    //Initialisation du mutex du compteur de messages
    pthread_mutex_init (&cp_mutex, NULL);


    //Creation file
    Creation_file();

    //Creation des threads
    Creation_threads();


return 0;

}

