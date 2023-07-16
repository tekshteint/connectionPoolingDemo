THREAD_COUNT=20

callOwners() {
 responseOwners=$(curl "http://localhost:9753/owners")
}

for ((i=0; i<$THREAD_COUNT; i++)); do
   callOwners &
done

wait
