#build executable jar with maven
FROM maven:alpine as BUILDER
#ENV HOME=/usr/app
RUN mkdir -p /usr/app
WORKDIR /usr/app
ADD src /usr/app
ADD pom.xml /usr/app
ADD settings.xml /usr/app
ADD setenv.sh /usr/app
RUN mvn clean package -Dmaven.test.skip=true -s settings.xml
FROM openjdk:11
FROM tomcat:8.5
RUN rm -rf /usr/local/tomcat/webapps/*
#COPY --from=BUILDER /usr/target/course-managment-backoffice-0.0.1-SNAPSHOT.jar /usr-service/course-managment-backoffice-0.0.1-SNAPSHOT.jar
COPY --from=BUILDER  /usr/app/target/*.jar /usr/local/tomcat/webapps/ROOT.jar
COPY setenv.sh /usr/local/tomcat/bin
EXPOSE 8080
CMD ["catalina.sh", "run"]
