#!/bin/bash

# Execute in script dir
cd $(dirname ${0})
# secret-service-app
SERVICE_NAME="client-service"
SECRET_SERVIVE_NAME="secret-${SERVICE_NAME}"

function createSecrets() {
  ARGS=""
  for LINE in $(cat ./config.properties)
  do
    IFS='='        # space is set as delimiter
    read -ra PARTS <<< "${LINE}"
    ARGS="${ARGS} --from-literal=${PARTS[0]}=${PARTS[1]}"
  done

  # Need to do so, no --from-env-file option available in version 3.5
  eval "oc create secret generic ${SECRET_SERVIVE_NAME} ${ARGS}"
}

function deleteSecrets() {
  oc delete secret/${SECRET_SERVIVE_NAME}
}

function recreateSecrets() {
  deleteSecrets
  createSecrets
}

function scale() {
  oc scale --replicas=${1} dc/${SERVICE_NAME}
}

function deploy() {
  cd ../../../services/${SERVICE_NAME}
  mvn fabric8:deploy -Dpfabric8
}

case ${1} in
   scale)
      if [ $# -eq 2 ]; then
        scale ${2}
      fi
      ;;
   createSecrets|deleteSecrets|recreateSecrets|\
   deploy)
      ${1}
      ;;
   *)
     echo "${0} [createSecrets|deleteSecrets|recreateSecrets\n\
     scale|deploy]"
     exit 1
      ;;
esac
