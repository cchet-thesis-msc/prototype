#!/bin/bash

# Execute in script dir
cd $(dirname ${0})

GIT_URL='git@github.com:cchet-thesis-msc/prototype.git'
GIT_REF=${GIT_REF:-'master'}

VERSION='5.6.2'

if [ ! "$STAGE" ];
then
  echo "No \$STAGE env variable set"
  exit 1
fi

function createService() {
  oc new-app -f ./elasticsearch.yml  \
    -p "APP=${1}" \
    -p "SERVICE_NAME=${2}" \
    -p "GIT_URL=${GIT_URL}" \
    -p "GIT_REF=${GIT_REF}" \
    -p "CONTEXT_DIR=docker/elasticsearch" \
    -p "SECRET_GIT=${3}" \
    -p "VERSION=${VERSION}"
} # createBc

function deleteService() {
    oc delete all -l app=${1}
    oc delete configmap ${1}
    oc delete secret -l app=${1}
    oc delete pvc -l app=${1}
}

function recreateService() {
  deleteService
  createService
}

function scaleUp() {
  oc scale --replicas=1 dc/${1}
}

function scaleDown() {
  oc scale --replicas=0 dc/${1}
}

case ${1} in
   scaleUp|scaleDown)
      if [ $# -eq 2 ]; then
        ${1} ${2}
      else
        echo "service_name must be given !!!!"
        exit 1
      fi
      ;;
   createService|recreateService)
     if [ $# -eq 4 ]; then
       ${1} ${2} ${3} ${4}
     else
       echo "app_name | Service_name | secret_git must be given !!!!"
       exit 1
     fi
      ;;
   deleteService)
     if [ $# -eq 2 ]; then
       ${1} ${2}
     else
       echo "App name must be given !!!!"
       exit 1
     fi
      ;;
   *)
     echo "${0} [createService|deleteService|recreateService|\
        scaleUp|scaleDown]"
        exit 1
      ;;
esac
