FROM openjdk:17-oracle
FROM gradle:8.1.1 AS build

WORKDIR /home/gradle
ADD ./ ./
RUN gradle build -x test

FROM openjdk:17-oracle
RUN mkdir /app
WORKDIR /app
COPY --from=build /home/gradle/build/libs/java.exam-0.0.1-SNAPSHOT.jar /app
HEALTHCHECK --interval=5s --timeout=3s CMD curl --fail http://localhost:9000/ || exit 1
ENTRYPOINT ["java", "-jar", "/app/java.exam-0.0.1-SNAPSHOT.jar"]


