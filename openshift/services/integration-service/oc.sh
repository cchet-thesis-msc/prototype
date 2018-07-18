#!/bin/bash

# Execute in script dir
cd $(dirname ${0})
# secret-SERVICE_NAME-app
SERVICE_NAME="integration-service"
SERVICE_DB="${SERVICE_NAME}-db"
SECRET_SERVIVE="secret-${SERVICE_NAME}"
SECRET_SERVIVE_KEYCLOAK="${SECRET_SERVIVE}-keycloak"
SECRET_SERVICE_DB="secret-${SERVICE_DB}"
DB_NAME='postgres'
DB_USER='postgres'
DB_PASSWORD='postgres'

function createSecrets() {
  oc create secret generic ${SECRET_SERVIVE} \
    --from-env-file=./config/${STAGE}/config.properties

  oc create secret generic ${SECRET_SERVIVE_KEYCLOAK} \
    --from-file=./config/${STAGE}/keycloak.json

  oc create secret generic ${SECRET_SERVICE_DB} \
    --from-file=./init.sql
}

function deleteSecrets() {
  oc delete secret/${SECRET_SERVICE_DB}
  oc delete secret/${SECRET_SERVIVE_KEYCLOAK}
  oc delete secret/${SECRET_SERVIVE}
}

function recreateSecrets() {
  deleteSecrets
  createSecrets
}

function createService() {
  ../postgres/oc.sh createService ${SERVICE_NAME} ${SERVICE_DB} ${DB_NAME} ${DB_USER} ${DB_PASSWORD} ${SECRET_SERVICE_DB}
}

function deleteService() {
  ../postgres/oc.sh deleteService ${SERVICE_DB}
}

function recreateService() {
  deleteService
  createService
}

function scale() {
  ../postgres/oc.sh scale ${1} ${SERVICE_DB}
  oc scale --replicas=${1} dc/${SERVICE_NAME}
}

function deploy() {
  cd ../../../services/${SERVICE_NAME}
  mvn fabric8:deploy -Pfabric8
}

function buildAndDeploy() {
  cd ../../../services/${SERVICE_NAME}
  mvn clean install fabric8:deploy -Pfabric8
}

case ${1} in
   scale)
      if [ $# -eq 2 ]; then
        scale ${2}
      fi
      ;;
   createSecrets|deleteSecrets|recreateSecrets|\
   createService|deleteService|recreateService|\
   deploy|buildAndDeploy)
      ${1}
      ;;
   *)
     echo "${0} [createSecrets|deleteSecrets|recreateSecrets|\
     createService|deleteService|recreateService|\
     scale|deploy|buildAndDeploy]"
     exit 1
      ;;
esac
