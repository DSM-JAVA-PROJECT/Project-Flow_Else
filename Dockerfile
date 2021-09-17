FROM openjdk:11-jdk
EXPOSE 8080
ARG JAR_FILE=build/libs/*.jar

ARG SECRET_KEY
ARG EMAIL_ID
ARG EMAIL_PWD
ARG MONGO_DB
ARG SMTP_CLASS

ENV SECRET_KEY=${SECRET_KEY}
ENV EMAIL_ID=${EMAIL_ID}
ENV EMAIL_PWD=${EMAIL_PWD}
ENV MONGO_DB=${MONGO_DB}
ENV SMTP_CLASS=${SMTP_CLASS}

COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]