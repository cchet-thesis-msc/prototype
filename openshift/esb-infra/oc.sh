#!/bin/bash

# Execute in script dir
cd $(dirname ${0})

SERVICES=(swagger-ui jaeger keycloak graylog)

if [ ! "$STAGE" ];
then
  echo "No \$STAGE env variable set"
  exit 1
fi

function createServices() {
  for SERVICE in "${SERVICES[@]}"
  do
    ./${SERVICE}/oc.sh createService
  done
}

function deleteServices() {
  for SERVICE in "${SERVICES[@]}"
  do
    ./${SERVICE}/oc.sh deleteService
  done
}

function recreateServices() {
  for SERVICE in "${SERVICES[@]}"
  do
    ./${SERVICE}/oc.sh recreateService
  done
}

function createSecrets() {
  for SERVICE in "${SERVICES[@]}"
  do
    ./${SERVICE}/oc.sh createSecrets
  done
}

function deleteSecrets() {
  for SERVICE in "${SERVICES[@]}"
  do
    ./${SERVICE}/oc.sh deleteSecrets
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

function scaleDown() {
  for SERVICE in "${SERVICES[@]}"
  do
    ./${SERVICE}/oc.sh scaleDown
  done
}

function scaleUp() {
  for SERVICE in "${SERVICES[@]}"
  do
    ./${SERVICE}/oc.sh scaleUp
  done
}

case ${1} in
   createSecrets|deleteSecrets|recreateSecrets|\
   createServices|deleteServices|recreateServices|\
   createAll|deleteAll|recreateAll|\
   scaleDown|scaleUp)
      ${1}
      exit ${?}
      ;;
   *)
     echo -e "${0} [createSecrets|deleteSecrets|recreateSecrets|\
     createServices|deleteServices|recreateServices|\
     createAll|deleteAll|recreateAll|\
     scaleDown|scaleUp]"
     exit 1
      ;;
esac
