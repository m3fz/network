FROM eclipse-temurin:21-jdk-alpine as builder
WORKDIR /app
COPY ./ /app/.
RUN ./gradlew dialog:clean dialog:bootJar

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/dialog/build/libs/*.jar /app/*.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/*.jar"]