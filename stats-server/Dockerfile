FROM amazoncorretto:19-alpine-jdk
COPY http-server/target/*.jar stat.jar
ENTRYPOINT ["java","-jar","/stat.jar"]