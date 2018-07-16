#!/bin/bash

# Execute in script dir
cd $(dirname ${0})

function createService() {
  oc new-app mongodb-persistent  \
    -p "DATABASE_SERVICE_NAME=${1}" \
    -p "MONGODB_USER=${3}" \
    -p "MONGODB_PASSWORD=${4}" \
    -p "MONGODB_DATABASE=${2}" \
    -p "MONGODB_ADMIN_PASSWORD=${4}" \
    -p "MONGODB_VERSION=3.2"
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
