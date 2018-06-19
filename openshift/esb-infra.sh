#!/bin/bash

# Execute in script dir
cd $(dirname ${0})

SERVICES=(swagger-ui jaeger keycloak graylog)

function createServices() {
  for SERVICE in "${SERVICES[@]}"
  do
    ./services/${SERVICE}/oc.sh createService
  done
}

function deleteServices() {
  for SERVICE in "${SERVICES[@]}"
  do
    ./services/${SERVICE}/oc.sh deleteService
  done
}

function recreateServices() {
  for SERVICE in "${SERVICES[@]}"
  do
    ./services/${SERVICE}/oc.sh recreateService
  done
}

function createSecrets() {
  for SERVICE in "${SERVICES[@]}"
  do
    ./services/${SERVICE}/oc.sh createSecrets
  done
}

function deleteSecrets() {
  for SERVICE in "${SERVICES[@]}"
  do
    ./services/${SERVICE}/oc.sh deleteSecrets
  done
}

function recreateSecrets() {
  deleteSecrets
  createSecrets
}

function createAll() {
    createSecrets
    createServices
}

function deleteAll() {
  deleteServices
  deleteSecrets
}

function recreateAll() {
  deleteAll
  createAll
}

case ${1} in
   createSecrets|deleteSecrets|recreateSecrets|\
   createService|deleteService|recreateService|\
   createAll|deleteAll|recreateAll)
      ${1}
      ;;
   *)
     echo -e "${0} [createSecrets|deleteSecrets|recreateSecrets|\
     createService|deleteService|recreateService|\
     createAll|deleteAll|recreateAll]"
     exit 1
      ;;
esac
