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

function scale() {
  ../postgres/oc.sh scale ${1} ${SERVICE_DB}
  oc scale --replicas=${1} dc/${SERVICE_NAME}
}

case ${1} in
   scale)
      if [ $# -eq 2 ]; then
        scale ${2}
      fi
      ;;
   createAll|deleteAll|recreateAll|\
   createSecrets|deleteSecrets|recreateSecrets|\
   createService|deleteService|recreateService|\
   scale)
      ${1}
      ;;
   *)
     echo "${0} [   createAll|deleteAll|recreateAll|\
        createSecrets|deleteSecrets|recreateSecrets|\
        createService|deleteService|recreateService|\
        scale]"
     exit 1
      ;;
esac
