FROM openjdk:17-ea-11-jdk-slim

        COPY ./bulid/libs/JHTA_3Team_FinalProject-0.0.1-SNAPSHOT.jar /security.jar 

        ENTRYPOINT ["java", "-jar", "security.jar"]
