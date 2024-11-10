# Start with a base image with JDK 17 (or your app's JDK version)
FROM openjdk:17-jdk-slim
  
  # Set a working directory
WORKDIR /app
  
  # Copy the built JAR file
COPY target/coffe-shop-0.0.1-SNAPSHOT.jar app.jar
  
  # Expose the app’s port
EXPOSE 8080
  
  # Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
