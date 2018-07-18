#!/bin/bash

# Execute in script dir
cd $(dirname ${0})
# secret-service-app
SERVICE_NAME=""
VERSION="latest"
MEM_LIM='128Mi'
VOL_LIM='256Mi'

function createService() {
  oc new-app -f ./postgres-full.json \
    -p "APP_NAME=${1}" \
    -p "MEMORY_LIMIT=${MEM_LIM}" \
    -p "DATABASE_SERVICE_NAME=${2}" \
    -p "POSTGRESQL_USER=${4}" \
    -p "POSTGRESQL_PASSWORD=${5}" \
    -p "POSTGRESQL_DATABASE=${3}" \
    -p "INIT_SECRET=${6}" \
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
  oc scale --replicas=${1} dc/${2}
}

case ${1} in
   scale)
      if [ $# -eq 3 ]; then
        scale ${2} ${3}
      else
        echo "Scale | service_name must be given !!!!"
        exit 1
      fi
      ;;
   createService|recreateService)
     if [ $# -eq 7 ]; then
       ${1} ${2} ${3} ${4} ${5} ${6} ${7}
     else
       echo "app_name / service name / db_name / db_user / db_password / init_secret must be given !!!!"
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
        scale]"
        exit 1
      ;;
esac
