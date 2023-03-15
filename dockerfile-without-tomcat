FROM openjdk:11
WORKDIR /app
COPY  /target/*.jar /app/ROOT.jar
EXPOSE 8080
CMD ["java", "-jar", "-Dspring.profiles.active=dev", "-Dserver.port=8080", "ROOT.jar"]
