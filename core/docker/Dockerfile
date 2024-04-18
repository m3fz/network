FROM eclipse-temurin:21-jdk-alpine as builder
WORKDIR /app
COPY ./ /app/.
RUN ./gradlew core:clean core:bootJar

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/core/build/libs/*.jar /app/*.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/*.jar"]