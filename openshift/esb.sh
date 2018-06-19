#!/bin/bash

# Execute in script dir
cd $(dirname ${0})

SECRET_SERVICES=(app integration-db)
MANAGE_SERVICES=(integration-db)

function createServices() {
  for SERVICE in "${MANAGE_SERVICES[@]}"
  do
    ./services/${SERVICE}/oc.sh createService
  done
}

function deleteServices() {
  for SERVICE in "${MANAGE_SERVICES[@]}"
  do
    ./services/${SERVICE}/oc.sh deleteService
  done
}

function recreateServices() {
  for SERVICE in "${MANAGE_SERVICES[@]}"
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

function deployAll() {
  for SERVICE in "${SECRET_SERVICES[@]}"
  do
    ./services/${SERVICE}/oc.sh deploy
  done
}

case ${1} in
   createSecrets|deleteSecrets|recreateSecrets|\
   createService|deleteService|recreateService|\
   createAll|deleteAll|recreateAll|\
   deployAll)
      ${1}
      ;;
   *)
     echo -e "${0} [createSecrets|deleteSecrets|recreateSecrets|\
     createService|deleteService|recreateService|\
     createAll|deleteAll||\
     deployAll]"
     exit 1
      ;;
esac
