FROM openjdk:11
WORKDIR /app
COPY src/main/resources/google_cloud_storage.json google_cloud_storage.json
ENV GOOGLE_APPLICATION_CREDENTIALS=google_cloud_storage.json
COPY  /target/*.jar /app/ROOT.jar
EXPOSE 8080
CMD ["java", "-jar", "-Dspring.profiles.active=dev", "-Dserver.port=8080", "ROOT.jar"]
