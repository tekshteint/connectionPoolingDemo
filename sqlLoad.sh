THREAD_COUNT=80

callNative() {
 responseNative=$(curl "http://localhost:9753/nativeSQL")
}

for ((i=0; i<$THREAD_COUNT; i++)); do
   callNative &
done

wait
