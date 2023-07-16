THREAD_COUNT=50

callNative() {
 responseNative=$(curl "http://localhost:9753/nativeSQLNew")
}

for ((i=0; i<$THREAD_COUNT; i++)); do
   callNative &
done

wait
