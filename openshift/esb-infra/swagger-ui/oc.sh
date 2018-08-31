#!/bin/bash

# Execute in script dir
cd $(dirname ${0})
# secret-service-app
SERVICE_NAME="swagger"
SECRET_SERVIVE="secret-${SERVICE_NAME}"
VERSION='3.13.4'
GIT_URL='git@github.com:cchet-thesis-msc/prototype.git'
GIT_REF=${GIT_REF:-'master'}
CONTEXT_DIR='docker/swagger-ui/'

function createSecrets() {
  oc create secret generic ${SECRET_SERVIVE} \
    --from-file=ssh-privatekey="${HOME}/.ssh/id_rsa"
}

function deleteSecrets() {
  oc delete secret/${SECRET_SERVIVE}
}

function recreateSecrets() {
  deleteSecrets
  createSecrets
}

function createService() {
  oc new-app -f ./swagger-full.yml \
    -p "SERVICE_NAME=${SERVICE_NAME}" \
    -p "VERSION=${VERSION}" \
    -p "GIT_URL=${GIT_URL}" \
    -p "GIT_REF=${GIT_REF}" \
    -p "CONTEXT_DIR=${CONTEXT_DIR}" \
    -p "SECRET_GIT=${SECRET_SERVIVE}"
} # createBc

function deleteService() {
    oc delete all -l app=${SERVICE_NAME}
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
  oc scale --replicas=${1} dc/${SERVICE_NAME}
}

case ${1} in
   scale)
      if [ $# -ne 1 ]; then
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
