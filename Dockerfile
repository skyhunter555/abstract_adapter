FROM openjdk:8-jdk-alpine

# The application's jar file
ARG JAR_FILE=target/*.jar
ARG JAVA_OPTS="-Xmx128m -Xms128m"
# Add the application's jar to the container
ADD ${JAR_FILE} /app.jar

# HTTP port
EXPOSE 8098

ENTRYPOINT exec java $JAVA_OPTS -jar /app.jar