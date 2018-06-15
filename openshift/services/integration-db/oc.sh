#!/bin/bash

# Execute in script dir
cd $(dirname ${0})
# secret-service-app
SERVICE_NAME="integration-service-db"
SECRET_SERVIVE_APP="secret-${SERVICE_NAME}"

function createSecrets() {
  ARGS=""
  for LINE in $(cat ./config.properties)
  do
    IFS='='        # space is set as delimiter
    read -ra PARTS <<< "${LINE}"
    ARGS="${ARGS} --from-literal=${PARTS[0]}=${PARTS[1]}"
  done

  # Need to do so, no --from-env-file option available in version 3.5
  eval "oc create secret generic ${SECRET_SERVIVE_APP} ${ARGS}"
}

function deleteSecrets() {
  oc delete secret/${SECRET_SERVIVE_APP}
}

function recreateSecrets() {
  deleteSecrets
  createSecrets
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
   createSecrets|deleteSecrets|recreateSecrets|\
   scale)
      ${1}
      ;;
   *)
     echo "${0} [createSecrets|deleteSecrets|recreateSecrets\n\
     scale]"
     exit 1
      ;;
esac
