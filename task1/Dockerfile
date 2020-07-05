FROM adoptopenjdk/openjdk11:alpine-slim
WORKDIR /opt/edgger/
COPY target/*.jar /opt/edgger/app.jar
ENTRYPOINT ["java","-jar","app.jar"]
EXPOSE 8080