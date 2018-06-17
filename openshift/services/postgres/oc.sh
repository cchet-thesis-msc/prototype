#!/bin/bash

# Execute in script dir
cd $(dirname ${0})
# secret-service-app
SERVICE_NAME=""
VERSION="9.6"
MEM_LIM='128Mi'
VOL_LIM='512Mi'
USER='postgres'
PASSWORD='postgres'

function createService() {
  oc new-app -f ./postgres-full.yml \
    -p "APP=${1}" \
    -p "MEMORY_LIMIT=${MEM_LIM}" \
    -p "DATABASE_SERVICE_NAME=${1}" \
    -p "POSTGRESQL_USER=${2}" \
    -p "POSTGRESQL_PASSWORD=${3}" \
    -p "POSTGRESQL_DATABASE=${1}" \
    -p "VOLUME_CAPACITY=${VOL_LIM}" \
    -p "POSTGRESQL_VERSION=${VERSION}" \
} # createBc

function deleteService() {
    oc delete all -l app=${1}
    oc delete pv/${1}
    oc delete secret/${1}
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
      if [ $# -ne 1 ]; then
        scale ${2}
      else
        echo "Scaling count must be given !!!!"
        exit 1
      fi
      ;;
   createService|recreateService)
     if [ $# -ne 3 ]; then
       ${1} ${2} ${3} ${4}
     else
       echo "Service name / user / password must be given !!!!"
       exit 1
     fi
      ;;
   deleteService)
     if [ $# -ne 1 ]; then
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
