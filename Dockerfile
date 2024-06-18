FROM openjdk:17-ea-11-jdk-slim

COPY ./build/libs/JHTA_3Team_FinalProject-0.0.1-SNAPSHOT.jar /app.jar

ENV JAVA_OPTS="-Djasypt.encryptor.password=mbti1234"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app.jar"]

