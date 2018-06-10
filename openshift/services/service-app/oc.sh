#!/bin/bash

# Execute in script dir
cd $(dirname ${0})

SECRET_SERVIVE_APP='secret-service-app'

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

case ${1} in
   createSecrets|deleteSecrets|recreateSecrets)
      ${1}
      ;;
   *)
     echo "${0} [createSecrets|deleteSecrets|recreateSecrets]"
     exit 1
      ;;
esac
