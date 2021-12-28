ARG BASE_IMAGE=""
FROM $BASE_IMAGE
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /application.jar

USER root
RUN mkdir /config/

EXPOSE 8088
ENTRYPOINT exec java $JAVA_OPTS -jar /application.jar