FROM openjdk:8-jre
MAINTAINER Graylog, Inc. <hello@graylog.com>

# Build-time metadata as defined at http://label-schema.org
#2.4.4
ARG GRAYLOG_VERSION
# 1.10
ARG GOSU_VERSION

ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64/jre
ENV GRAYLOG_SERVER_JAVA_OPTS "-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -XX:NewRatio=1 -XX:MaxMetaspaceSize=256m -server -XX:+ResizeTLAB -XX:+UseConcMarkSweepGC -XX:+CMSConcurrentMTEnabled -XX:+CMSClassUnloadingEnabled -XX:+UseParNewGC -XX:-OmitStackTraceInFastThrow"
ENV PATH /usr/share/graylog/bin:$PATH

LABEL org.label-schema.name="Graylog Docker Image" \
      org.label-schema.description="Official Graylog Docker image" \
      org.label-schema.url="https://www.graylog.org/" \
      org.label-schema.vcs-url="https://github.com/Graylog2/graylog-docker" \
      org.label-schema.vendor="Graylog, Inc." \
      org.label-schema.version=$GRAYLOG_VERSION \
      org.label-schema.schema-version="1.0" \
      com.microscaling.docker.dockerfile="/Dockerfile" \
      com.microscaling.license="Apache 2.0"

RUN set -ex \
    && wget -nv -O /usr/local/bin/gosu "https://github.com/tianon/gosu/releases/download/$GOSU_VERSION/gosu-$(dpkg --print-architecture)" \
    && wget -nv -O /usr/local/bin/gosu.asc "https://github.com/tianon/gosu/releases/download/$GOSU_VERSION/gosu-$(dpkg --print-architecture).asc" \
    && export GNUPGHOME="$(mktemp -d)" \
    && gpg --keyserver keyserver.ubuntu.com --recv-keys B42F6819007F00F88E364FD4036A9C25BF357DD4 \
    && gpg --batch --verify /usr/local/bin/gosu.asc /usr/local/bin/gosu \
    && rm -rf "$GNUPGHOME" /usr/local/bin/gosu.asc \
    && chmod +x /usr/local/bin/gosu \
    && gosu nobody true

RUN set -ex \
    && adduser --disabled-password --disabled-login --gecos '' --uid 1100 --gid 0 graylog \
    && mkdir /usr/share/graylog \
    && wget -nv -O "/tmp/graylog-${GRAYLOG_VERSION}.tgz" "https://packages.graylog2.org/releases/graylog/graylog-${GRAYLOG_VERSION}.tgz" \
    && wget -nv -O "/tmp/graylog-${GRAYLOG_VERSION}.tgz.sha256.txt" "https://packages.graylog2.org/releases/graylog/graylog-${GRAYLOG_VERSION}.tgz.sha256.txt" \
    && cd /tmp \
    && sha256sum -c "/tmp/graylog-${GRAYLOG_VERSION}.tgz.sha256.txt" \
    && tar -xzf "/tmp/graylog-${GRAYLOG_VERSION}.tgz" --strip-components=1 -C /usr/share/graylog \
    && rm -f "/tmp/graylog-${GRAYLOG_VERSION}.tgz" \
    && apt-get update && apt-get -y install libcap2-bin \
    && setcap 'cap_net_bind_service=+ep' $JAVA_HOME/bin/java \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /usr/share/graylog

RUN set -ex \
  && for path in \
    ./data/journal \
    ./data/log \
    ./data/config \
  ; do \
    mkdir -p "$path"; \
  done

COPY docker-entrypoint.sh /

RUN chown -R graylog:0 /usr/share/graylog /docker-entrypoint.sh \
    && chmod -R ug=rxw,o= /usr/share/graylog /docker-entrypoint.sh

USER graylog

EXPOSE 9000
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["graylog"]
