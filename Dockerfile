# ==========================================
# Stage 1: Build the application using JDK 25
# ==========================================
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app

# Install Maven locally in the container
RUN apt-get update && apt-get install -y maven

# Copy your source setup
COPY pom.xml .
COPY src ./src

# Build your application using a cache mount for your dependencies.
# This ensures Maven ONLY downloads what your pom.xml needs, and caches it for next time.
RUN --mount=type=cache,target=/root/.m2 mvn clean package -DskipTests

# ==========================================
# Stage 2: Run the application using JRE 25
# ==========================================
FROM eclipse-temurin:25-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]