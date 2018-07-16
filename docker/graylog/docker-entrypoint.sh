#!/bin/bash

"$JAVA_HOME/bin/java" $GRAYLOG_SERVER_JAVA_OPTS \
  -jar \
  -Dlog4j.configurationFile=/tmp/config/log4j2.xml \
  -Djava.library.path=/usr/share/graylog/lib/sigar/ \
  -Dgraylog2.installation_source=docker /usr/share/graylog/graylog.jar \
  server \
  -f /tmp/config/graylog.conf ${GRAYLOG_SERVER_OPTS}
