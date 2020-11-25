#!/bin/bash
#set -x

export SERVER_PORT="${PORT:-$SERVER_PORT}"

echo "Starting at mode of ${ENVIRONMENT}, on port ${SERVER_PORT}"

if [[ "${ENVIRONMENT}" == 'local' ]]; then
  echo "Copying from ${BUILD_LIBS_PATH} to ${APP_HOME}"
  cp $BUILD_LIBS_PATH/*.jar $APP_HOME/app.jar
fi

echo ":::Severino is preparing for [CARA <-> CRACHA]:::"

java -jar "$APP_HOME/app.jar"
