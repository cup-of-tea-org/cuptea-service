for jar in /app/*.jar; do
  echo "Running $jar with profile dev..."
  java -jar -Dspring.profiles.active=dev "$jar" &
done

wait