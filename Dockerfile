FROM openjdk:8-jdk-alpine
# VOLUME /tmp
ARG JASYPT_ENCRYTOR_PASSWORD
ADD target/service-scheduler-1.0.0-SNAPSHOT.jar app.jar
EXPOSE 8762
ENV JAVA_OPTS="-Dspring.profiles.active=prod -Djasypt.encryptor.password=$JASYPT_ENCRYTOR_PASSWORD"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]