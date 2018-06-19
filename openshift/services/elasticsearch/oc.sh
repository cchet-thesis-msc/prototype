#!/bin/bash

# Execute in script dir
cd $(dirname ${0})

GIT_URL='git@github.com:cchet-thesis-msc/prototype.git'
GIT_REF='master'

VERSION='5.6.2'

function createService() {
  oc new-app -f ./elasticsearch.yml  \
    -p "APP=${1}" \
    -p "SERVICE_NAME=${1}" \
    -p "GIT_URL=${GIT_URL}" \
    -p "GIT_REF=${GIT_REF}" \
    -p "CONTEXT_DIR=docker/elasticsearch" \
    -p "SECRET_GIT=${2}" \
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
     if [ $# -eq 3 ]; then
       ${1} ${2} ${3}
     else
       echo "Service_name | secret_git must be given !!!!"
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
