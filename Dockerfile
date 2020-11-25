FROM openjdk:8-jre-alpine

ENV ENVIRONMENT production
ENV BUILD_LIBS_PATH /source/build/libs/
ENV APP_HOME /root

WORKDIR $APP_HOME

COPY . /source
COPY bin bin
COPY build/libs/*.jar app.jar

RUN apk update && \
  apk add --no-cache \
  bash ca-certificates \
  openssl tzdata && \
  chmod +x ./bin/start.sh

EXPOSE 9200

CMD ["./bin/start.sh"]
