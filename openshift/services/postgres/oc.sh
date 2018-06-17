#!/bin/bash

# Execute in script dir
cd $(dirname ${0})
# secret-service-app
SERVICE_NAME=""
VERSION="9.6"
MEM_LIM='128Mi'
VOL_LIM='256Mi'
USER='postgres'
PASSWORD='postgres'

function createService() {
  oc new-app -f ./postgres-full.json \
    -p "MEMORY_LIMIT=${MEM_LIM}" \
    -p "DATABASE_SERVICE_NAME=${1}" \
    -p "POSTGRESQL_USER=${3}" \
    -p "POSTGRESQL_PASSWORD=${4}" \
    -p "POSTGRESQL_DATABASE=${2}" \
    -p "VOLUME_CAPACITY=${VOL_LIM}" \
    -p "POSTGRESQL_VERSION=${VERSION}"
} # createBc

function deleteService() {
    oc delete all -l app=${1}
    oc delete pvc/${1}
    oc delete secret/${1}
    oc delete svc/${1}
    oc delete dc/${1}
}

function recreateService() {
  deleteService
  createService
}

function scale() {
  oc scale --replicas=${1} dc/${SERVICE_NAME}
}

case ${1} in
   scale)
      if [ $# -eq 2 ]; then
        scale ${2}
      else
        echo "Scaling count must be given !!!!"
        exit 1
      fi
      ;;
   createService|recreateService)
     if [ $# -eq 5 ]; then
       ${1} ${2} ${3} ${4} ${5}
     else
       echo "Service name / db_name / db_user / db_password must be given !!!!"
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
        scale]"
        exit 1
      ;;
esac
