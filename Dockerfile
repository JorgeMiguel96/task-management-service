# Stage 1: Build the application using a Gradle image
FROM gradle:8.5.0-jdk21-alpine AS build
WORKDIR /home/gradle/src
COPY --chown=gradle:gradle . .
# Execute the build, skipping tests as they are run in a separate CI step
RUN gradle build --no-daemon -x test

# Stage 2: Create the final, lightweight runtime image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy the built JAR from the 'build' stage
COPY --from=build /home/gradle/src/build/libs/*.jar app.jar

# Expose the port the application runs on (default is 8080)
EXPOSE 8080

# The command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]