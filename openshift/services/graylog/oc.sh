#!/bin/bash

# Execute in script dir
cd $(dirname ${0})
# secret-service-app
APP_NAME='logging'
SERVICE_NAME="graylog"
SERVICE_NAME_MONGO="${SERVICE_NAME}-mongo"
SERVICE_NAME_ELASTIC="${SERVICE_NAME}-elastic"
SECRET_SERVIVE="secret-${SERVICE_NAME}"
SECRET_SERVIVE_GIT="secret-${SERVICE_NAME}-git"
GIT_URL='git@github.com:cchet-thesis-msc/prototype.git'
GIT_REF='master'

DB_NAME='graylog'
DB_USER='graylog'
DB_PASSWORD='graylog'

VERSION_GRAYLOG='2.4.4'
VERSION_GOSU='1.10'

if [ ! "$STAGE" ];
then
  echo "No \$STAGE env variable set"
  exit 1
fi

function createSecrets() {
  oc create secret generic ${SECRET_SERVIVE} \
      --from-file=./config/${STAGE}/graylog.conf \
      --from-file=./config/${STAGE}/log4j2.xml

  oc create secret generic ${SECRET_SERVIVE_GIT} \
    --from-file=ssh-privatekey="${HOME}/.ssh/id_rsa"
}

function deleteSecrets() {
  oc delete secret/${SECRET_SERVIVE}
  oc delete secret/${SECRET_SERVIVE_GIT}
}

function recreateSecrets() {
  deleteSecrets
  createSecrets
}

function createService() {
  ../elasticsearch/oc.sh createService ${APP_NAME} ${SERVICE_NAME_ELASTIC} ${SECRET_SERVIVE_GIT}
  ../mongodb/oc.sh createService ${APP_NAME} ${SERVICE_NAME_MONGO} ${DB_NAME} ${DB_USER} ${DB_PASSWORD}

  oc new-app -f ./graylog.yml  \
    -p "APP=${APP_NAME}" \
    -p "SERVICE_NAME=${SERVICE_NAME}" \
    -p "SERVICE_ELASTIC_NAME=${SERVICE_NAME_ELASTIC}" \
    -p "SERVICE_MONGO_NAME=${SERVICE_NAME_MONGO}" \
    -p "GIT_URL=${GIT_URL}" \
    -p "GIT_REF=${GIT_REF}" \
    -p "VERSION_GRAYLOG=${VERSION_GRAYLOG}" \
    -p "VERSION_GOSU=${VERSION_GOSU}" \
    -p "CONTEXT_DIR=docker/graylog" \
    -p "SECRET=${SECRET_SERVIVE}" \
    -p "SECRET_GIT=${SECRET_SERVIVE_GIT}"
}

function deleteService() {
    oc delete all -l app=${APP_NAME}
    oc delete configmap ${APP_NAME}
    oc delete secret -l app=${APP_NAME}
    oc delete pvc -l app=${APP_NAME}
}

function recreateService() {
  deleteService
  createService
}

function scale() {
  oc scale --replicas=${1} dc/${SERVICE_NAME}
  ../mongodb/oc.sh scale ${1} ${SERVICE_NAME_MONGO}
  ../elasticsearch/oc.sh scale ${1} ${SERVICE_NAME_ELASTIC}
}

case ${1} in
   scale)
      if [ $# -eq 2 ]; then
        scale ${2}
      fi
      ;;
   createSecrets|deleteSecrets|recreateSecrets|\
   createService|deleteService|recreateService|\
   deploy)
      ${1}
      ;;
   *)
     echo "${0} [createSecrets|deleteSecrets|recreateSecrets|\
     createService|deleteService|recreateService|\
     scale]"
     exit 1
      ;;
esac
