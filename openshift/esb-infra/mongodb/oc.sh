#!/bin/bash

# Execute in script dir
cd $(dirname ${0})

function createService() {
  oc new-app mongodb-persistent  \
    -p "DATABASE_SERVICE_NAME=${2}" \
    -p "MONGODB_USER=${4}" \
    -p "MONGODB_PASSWORD=${5}" \
    -p "MONGODB_DATABASE=${3}" \
    -p "MONGODB_ADMIN_PASSWORD=${5}" \
    -p "MONGODB_VERSION=3.2"

  oc label --overwrite dc/${2} app=${1}
  oc label --overwrite pvc/${2} app=${1}
  oc label --overwrite svc/${2} app=${1}
  oc label --overwrite secret/${2} app=${1}
} # createBc

function deleteService() {
  oc delete svc/${1}
  oc delete dc/${1}
  oc delete secret/${1}
  oc delete pvc/${1}
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
     if [ $# -eq 6 ]; then
       ${1} ${2} ${3} ${4} ${5} ${6}
     else
       echo "app_name / service name / db_name / db_user / db_password must be given !!!!"
       exit 1
     fi
      ;;
   deleteService)
     if [ $# -eq 2 ]; then
       ${1} ${2}
     else
       echo "Service name must be given !!!!"
       exit 1
     fi
      ;;
   *)
     echo "${0} [createService|deleteService|recreateService|\
     scaleUp|scaleDown]"
        exit 1
      ;;
esac
