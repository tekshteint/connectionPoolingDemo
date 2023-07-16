THREAD_COUNT=20

callDBCP() {
 responseDBCP=$(curl "http://localhost:9753/dbcp")
}

for ((i=0; i<$THREAD_COUNT; i++)); do
  callDBCP &
done

wait
