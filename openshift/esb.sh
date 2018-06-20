#!/bin/bash

# Execute in script dir
cd $(dirname ${0})

SECRET_SERVICES=(client app integration-db)
MANAGE_SERVICES=(integration-db)

function createServices() {
  for SERVICE in "${MANAGE_SERVICES[@]}"
  do
    ./services/${SERVICE}/oc.sh createService
  done
}

function deleteServices() {
  for SERVICE in "${MANAGE_SERVICES[@]}"
  do
    ./services/${SERVICE}/oc.sh deleteService
  done
}

function recreateServices() {
  for SERVICE in "${MANAGE_SERVICES[@]}"
  do
    ./services/${SERVICE}/oc.sh recreateService
  done
}

function createSecrets() {
  for SERVICE in "${SECRET_SERVICES[@]}"
  do
    ./services/${SERVICE}/oc.sh createSecrets
  done
}

function deleteSecrets() {
  for SERVICE in "${SECRET_SERVICES[@]}"
  do
    ./services/${SERVICE}/oc.sh deleteSecrets
  done
}

function recreateSecrets() {
  deleteSecrets
  createSecrets
}

function createAll() {
    createSecrets
    createServices
}

function deleteAll() {
  deleteServices
  deleteSecrets
}

function recreateAll() {
  deleteAll
  createAll
}

function deployAll() {
  for SERVICE in "${SECRET_SERVICES[@]}"
  do
    ./services/${SERVICE}/oc.sh deploy
  done
}

function scaleAll() {
    for SERVICE in "${SECRET_SERVICES[@]}"
    do
      ./services/${SERVICE}/oc.sh scale ${1}
    done
}

case ${1} in
   createSecrets|deleteSecrets|recreateSecrets|\
   createServices|deleteServices|recreateServices|\
   createAll|deleteAll|recreateAll|\
   deployAll)
      ${1}
      ;;
   scaleAll)
      if [ $# -eq 2 ]; then
        scaleAll ${2}
      else
        echo "Scale count must be given"
      fi
      ;;
   *)
     echo -e "${0} [createSecrets|deleteSecrets|recreateSecrets|\
     createServices|deleteServices|recreateServices|\
     createAll|deleteAll|\
     deployAll|scaleAll]"
     exit 1
      ;;
esac
