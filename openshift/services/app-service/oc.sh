#!/bin/bash

# Execute in script dir
CUR_DIR=$(dirname ${0})
cd $CUR_DIR

# secret-service-app
SERVICE_NAME="app-service"
SECRET_SERVIVE="secret-${SERVICE_NAME}"
SECRET_SERVIVE_KEYCLOAK="${SECRET_SERVIVE}-keycloak"

if [ ! "$STAGE" ];
then
  echo "No \$STAGE env variable set"
  exit 1
fi

function createSecrets() {
  oc create secret generic ${SECRET_SERVIVE} \
    --from-env-file=./config/${STAGE}/config.properties

  oc create secret generic ${SECRET_SERVIVE_KEYCLOAK} \
    --from-file=./config/${STAGE}/keycloak.json
}

function deleteSecrets() {
  oc delete secret/${SECRET_SERVIVE}
  oc delete secret/${SECRET_SERVIVE_KEYCLOAK}
}

function recreateSecrets() {
  deleteSecrets
  createSecrets
}

function createService() {
  buildAndDeploy
}

function deleteService() {
  undeploy
}

function recreateService() {
  deleteService
  createService
}

function scale() {
  oc scale --replicas=${1} dc/${SERVICE_NAME}
}

function deploy() {
  mvn -f ../../../services/${SERVICE_NAME}/pom.xml fabric8:deploy -Pfabric8
}

function undeploy() {
  mvn -f ../../../services/${SERVICE_NAME}/pom.xml fabric8:undeploy -Pfabric8
}

function buildAndDeploy() {
  mvn -f ../../../services/${SERVICE_NAME}/pom.xml clean install
  deploy
}

case ${1} in
   scale)
      if [ $# -eq 2 ]; then
        scale ${2}
      fi
      ;;
   createSecrets|deleteSecrets|recreateSecrets|\
   createService|deleteService|recreateService|\
   deploy|undeploy|buildAndDeploy)
      ${1}
      ;;
   *)
     echo "${0} [createSecrets|deleteSecrets|recreateSecrets\n\
     createService|deleteService|recreateService|\
     scale|deploy|undeploy|buildAndDeploy]"
     exit 1
      ;;
esac
