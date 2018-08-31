#!/bin/bash

# Execute in script dir
cd $(dirname ${0})

if [ ! "$STAGE" ];
then
  echo "No \$STAGE env variable set"
  exit 1
fi

function createEsbInfra() {
  ./esb-infra/esb-infra.sh createAll
}

function deleteEsbInfra() {
  ./esb-infra/esb-infra.sh deleteAll
  oc delete project esb-infra
}

function createEsb() {
  ./esb/esb.sh createAll
}

function deleteEsb() {
  ./esb/esb.sh deleteAll
  oc delete project esb
}

function createAll() {
  createEsbInfra
  createEsb
}

function deleteAll() {
  deleteEsb
  deleteEsbInfra
}

case ${1} in
   createEsbInfra|deleteEsbInfra|createEsb|deleteEsb|createAll|deleteAll)
      ${1}
      ;;
   *)
     echo -e "${0} [createEsbInfra|deleteEsbInfra|createEsb|deleteEsb|createAll|deleteAll]"
     exit 1
      ;;
esac
