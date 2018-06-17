#!/bin/bash

# Execute in script dir
cd $(dirname ${0})
# secret-service-app
SERVICE_NAME="jaeger-tracing"
SERVICE_NAME_ZIPKIN="${SERVICE_NAME}-zipkin"
SECRET_SERVIVE="secret-${SERVICE_NAME}"
VERSION="1.5.0"

function createSecrets() {
  oc create secret generic ${SECRET_SERVIVE} \
    --from-file="./secrets"
}

function deleteSecrets() {
  oc delete secret/${SECRET_SERVIVE}
}

function recreateSecrets() {
  deleteSecrets
  createSecrets
}

function createService() {
  oc new-app -f ./jaeger-full.yml \
  -p "APP=${SERVICE_NAME}" \
  -p "JAEGER_SERVICE_NAME=${SERVICE_NAME}" \
  -p "JAEGER_ZIPKIN_SERVICE_NAME=${SERVICE_NAME_ZIPKIN}" \
  -p "IMAGE_VERSION=${VERSION}"
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
