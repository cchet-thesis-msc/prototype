#!/bin/bash

# Execute in script dir
cd $(dirname ${0})

ALL_SERVICES=(service-app service-integration-db)

function createMonitoringServices() {
  echo "Not impleted yet"
}

function deleteMonitoringServices() {
  echo "Not impleted yet"
}

function recreateMonitoringServices() {
  deleteMonitoringServices
  createMonitoringServices
}

function createSecrets() {
  for SERVICE in "${ALL_SERVICES[@]}"
  do
    ./services/${SERVICE}/oc.sh createSecrets
  done
}

function deleteSecrets() {
  for SERVICE in "${ALL_SERVICES[@]}"
  do
    ./services/${SERVICE}/oc.sh deleteSecrets
  done
}

function recreateSecrets() {
  deleteSecrets
  createSecrets
}

case ${1} in
   createMonitoringServices|deleteMonitoringServices|recreateMonitoringServices|\
   createSecrets|deleteSecrets|recreateSecrets)
      ${1}
      ;;
   *)
     echo "${0} [createMonitoringServices|deleteMonitoringServices|recreateMonitoringServices|\
          createSecrets|deleteSecrets|recreateSecrets]"
     exit 1
      ;;
esac
