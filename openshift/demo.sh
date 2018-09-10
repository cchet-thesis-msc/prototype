#!/bin/bash

# Execute in script dir
cd $(dirname ${0})

PROJ_ESB='esb'
PROJ_ESB_INFRA='esb-infra'

if [ ! "$STAGE" ];
then
  echo "No \$STAGE env variable set"
  exit 1
fi

function switchProject() {
  if [ -n "${1}" ];
  then
    oc project ${1}
    if [ $? -ne 0 ];
    then
      echo "Could not switch to project"
      exit 1
    fi
  else
    echo "Project name is not defined"
    exit 1
  fi
}

function createProject() {
  if [ -n "${1}" ];
  then
    oc new-project ${1}
    if [ $? -ne 0 ];
    then
      echo "${1} project could not be created. Does it already exist ?"
    fi
  else
    echo "${1} project could not be created, because project name is not defined"
  fi
}

function deleteProject(){
  if [ -n "${1}" ];
  then
    oc delete project ${1}
    if [ $? -ne 0 ];
    then
      echo "${1} project could not be deleted. Does it exist ?"
    fi
  else
    echo "${1} project could not be deleted, because project name is not defined"
  fi
}

function createEsbInfra() {
  echo -e "###############################################\n
  Creating ${PROJ_ESB_INFRA} Openshift Project...\n
  ###############################################"
  createProject ${PROJ_ESB_INFRA}
  switchProject ${PROJ_ESB_INFRA}
  ./esb-infra/oc.sh createAll
  echo -e "###############################################\n
  cREATED ${PROJ_ESB_INFRA} Openshift Project\n
  ###############################################"
}

function deleteEsbInfra() {
  echo -e "###############################################\n
  Deleting ${PROJ_ESB_INFRA} Openshift Project...\n
  ###############################################"
  switchProject ${PROJ_ESB_INFRA}
  ./esb-infra/oc.sh deleteAll
  switchProject myproject
  deleteProject ${PROJ_ESB_INFRA}
  echo -e "###############################################\n
  Deleted ${PROJ_ESB_INFRA} Openshift Project\n
  ###############################################"
}

function createEsb() {
  echo -e "###############################################\n
  Creating ${PROJ_ESB} Openshift Project...\n
  ###############################################"
  createProject ${PROJ_ESB}
  switchProject ${PROJ_ESB}
  ./esb/oc.sh createAll
  echo -e "###############################################\n
  Created ${PROJ_ESB} Openshift Project\n
  ###############################################"
}

function deleteEsb() {
  echo -e "###############################################\n
  Deleting ${PROJ_ESB} Openshift Project...\n
  ###############################################"
  switchProject ${PROJ_ESB}
  ./esb/oc.sh deleteAll
  deleteProject ${PROJ_ESB}
  echo -e "###############################################\n
  Deleted ${PROJ_ESB} Openshift Project\n
  ###############################################"
}

function createAll() {
  createEsbInfra
  createEsb
}

function deleteAll() {
  deleteEsb
  deleteEsbInfra
}

function scaleDown() {
  switchProject ${PROJ_ESB}
  ./esb/oc.sh scaleAll 0
  switchProject ${PROJ_ESB_INFRA}
  ./esb-infra/oc.sh scaleDown
}

function scaleUp() {
  switchProject ${PROJ_ESB_INFRA}
  ./esb-infra/oc.sh scaleUp
  switchProject ${PROJ_ESB}
  ./esb/oc.sh scaleAll 1
}

case ${1} in
   createEsbInfra|deleteEsbInfra|createEsb|deleteEsb|createAll|deleteAll|scaleDown|scaleUp)
      ${1}
      ;;
   *)
     echo -e "${0} [createEsbInfra|deleteEsbInfra|createEsb|deleteEsb|createAll|deleteAll|scaleDown|scaleUp]"
     exit 1
      ;;
esac
