#!/bin/bash

# Execute in script dir
cd $(dirname ${0})
# secret-service-app
APP_NAME='security'
SERVICE_NAME="keycloak"
SERVICE_DB="${SERVICE_NAME}-db"
SECRET_SERVICE_RESTORE="secret-restore-${SERVICE_NAME}"
SECRET_SERVICE_DB="secrets-sql-${SERVICE_NAME}"
VERSION="4.0.0.Final"
KEYCLOAK_USER='keycloak'
KEYCLOAK_PASSWORD='keycloak'
DB_NAME='postgres'
DB_USER='postgres'
DB_PASSWORD='psotgres'
CPU_LIM='500m'
MEM_LIM='1'
CPU_MAX='1000m'
MEM_MAX='1'

if [ ! "$STAGE" ];
then
  echo "No \$STAGE env variable set"
  exit 1
fi

function createSecrets() {
  oc create secret generic ${SECRET_SERVICE_RESTORE} \
    --from-file=./config/${STAGE}/master-realm.json \
    --from-file=./config/${STAGE}/master-users-0.json

  oc create secret generic ${SECRET_SERVICE_DB} \
    --from-file=./init.sql
}

function deleteSecrets() {
  oc delete secret/${SECRET_SERVICE_DB}
  oc delete secret/${SECRET_SERVICE_RESTORE}
}

function recreateSecrets() {
  deleteSecrets
  createSecrets
}

function createService() {
  ../postgres/oc.sh createService ${APP_NAME} ${SERVICE_DB} ${DB_NAME} ${DB_USER} ${DB_PASSWORD} ${SECRET_SERVICE_DB}

  oc new-app -f ./keycloak-full.json \
    -p "APPLICATION_NAME=${APP_NAME}" \
    -p "KEYCLOAK_USER=${KEYCLOAK_USER}" \
    -p "KEYCLOAK_PASSWORD=${KEYCLOAK_PASSWORD}" \
    -p "DB_ADDR=${SERVICE_DB}" \
    -p "DB_PORT=5432" \
    -p "DB_VENDOR=POSTGRES" \
    -p "DB_DATABASE=${DB_NAME}" \
    -p "DB_USER=${DB_USER}" \
    -p "DB_PASSWORD=${DB_PASSWORD}" \
    -p "SECRET_NAME_RESTORE=${SECRET_SERVICE_RESTORE}"
} # createBc

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

function createAll() {
  createSecrets
  createService
}

function deleteAll() {
  deleteService
  deleteSecrets
}

function recreateAll() {
  deleteAll
  createAll
}

function scaleUp() {
  ../postgres/oc.sh scaleUp ${SERVICE_DB}
  oc scale --replicas=1 dc/${SERVICE_NAME}
}

function scaleDown() {
  oc scale --replicas=0 dc/${SERVICE_NAME}
  ../postgres/oc.sh scaleDown ${SERVICE_DB}
}

case ${1} in
   createAll|deleteAll|recreateAll|\
   createSecrets|deleteSecrets|recreateSecrets|\
   createService|deleteService|recreateService|\
   scaleUp|scaleDown)
      ${1}
      ;;
   *)
     echo "${0} [createAll|deleteAll|recreateAll|\
     createSecrets|deleteSecrets|recreateSecrets|\
     createService|deleteService|recreateService|\
     scaleUp|scaleDown]"
     exit 1
      ;;
esac
