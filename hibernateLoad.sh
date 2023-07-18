THREAD_COUNT=80

callHibernate(){
 responseHibernate=$(curl "http://localhost:9753/hibernate") 
}

for ((i=0; i<$THREAD_COUNT; i++)); do
   callHibernate &
done

wait
