#!/bin/bash

# Execute in script dir
cd $(dirname ${0})

SECRET_SERVICES=(swagger-ui jaeger keycloak app integration-db)
MANAGE_SERVICES=(swagger-ui jaeger keycloak)

function createServices() {
  for SERVICE in "${SECRET_SERVICES[@]}"
  do
    ./services/${SERVICE}/oc.sh createService
  done
}

function deleteServices() {
  for SERVICE in "${SECRET_SERVICES[@]}"
  do
    ./services/${SERVICE}/oc.sh deleteService
  done
}

function recreateServices() {
  for SERVICE in "${SECRET_SERVICES[@]}"
  do
    ./services/${SERVICE}/oc.sh recreateService
  done
}

function createSecrets() {
  for SERVICE in "${SECRET_SERVICES[@]}"
  do
    ./services/${SERVICE}/oc.sh createSecrets
  done
}

function deleteSecrets() {
  for SERVICE in "${SECRET_SERVICES[@]}"
  do
    ./services/${SERVICE}/oc.sh deleteSecrets
  done
}

function recreateSecrets() {
  deleteSecrets
  createSecrets
}

case ${1} in
   createSecrets|deleteSecrets|recreateSecrets|\
   createService|deleteService|recreateService)
      ${1}
      ;;
   *)
     echo -e "${0} [createSecrets|deleteSecrets|recreateSecrets|\
     createService|deleteService|recreateService]"
     exit 1
      ;;
esac
