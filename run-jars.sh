for jar in /app/*.jar; do
  java -jar -Dspring.profiles.active=dev "$jar" &
done

wait