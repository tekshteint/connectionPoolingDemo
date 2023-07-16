THREAD_COUNT=20

callHibernate(){
 responseHibernate=$(curl "http://localhost:9753/api/v1/hibernate") 
}

for ((i=0; i<$THREAD_COUNT; i++)); do
   callHibernate &
done

wait
