#!/bin/bash

# Execute in script dir
CUR_DIR=$(dirname ${0})
cd $CUR_DIR

# secret-service-app
SERVICE_NAME="client-service"
SECRET_SERVIVE_NAME="secret-${SERVICE_NAME}"

function createSecrets() {
  oc create secret generic ${SECRET_SERVIVE_NAME} \
    --from-env-file=./config/${STAGE}/config.properties
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
  mvn fabric8:deploy -Pfabric8
  cd $CUR_DIR
}

function buildAndDeploy() {
  cd ../../../services/${SERVICE_NAME}
  mvn clean install fabric8:deploy -Pfabric8
  cd $CUR_DIR
}

case ${1} in
   scale)
      if [ $# -eq 2 ]; then
        scale ${2}
      fi
      ;;
   createSecrets|deleteSecrets|recreateSecrets|\
   deploy|buildAndDeploy)
      ${1}
      ;;
   *)
     echo "${0} [createSecrets|deleteSecrets|recreateSecrets\n\
     scale|deploy|buildAndDeploy]"
     exit 1
      ;;
esac
