FROM lippert/adoptopenjdk-17

WORKDIR /app

COPY ./cuptea-account/build/libs/*.jar /app/cuptea-account.jar
COPY ./cuptea-api/build/libs/*.jar /app/cuptea-api.jar
COPY ./cuptea-db/build/libs/*.jar /app/cuptea-db.jar
COPY ./cuptea-cloud/build/libs/*.jar /app/cuptea-cloud.jar

COPY run-jars.sh /app/run-jars.sh

COPY ./docker-compose.yml /app/docker-compose.yml

RUN chmod +x /app/run-jars.sh

ENTRYPOINT ["/app/run-jars.sh"]