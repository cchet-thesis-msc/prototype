FROM centos:7
MAINTAINER <thomas.herzog@gepardec.com>

ARG VERSION
ARG URL="https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-${VERSION}.tar.gz"

ENV ELASTIC_CONTAINER true
ENV PATH /usr/share/elasticsearch/bin:$PATH
ENV JAVA_HOME /usr/lib/jvm/jre-1.8.0-openjdk

# 5.6.2
RUN yum update -y \
    && yum install -y \
      java-1.8.0-openjdk-headless \
      wget \
      which \
    && adduser \
      -u 1001 \
      -g 0 \
      -d /usr/share/elasticsearch \
      elasticsearch \
    && yum clean all \
    && rm -rf /var/cache/yum

WORKDIR /usr/share/elasticsearch
USER elasticsearch

# Download and extract defined ES version.
RUN curl -fsSL ${URL} \
      | tar zx --strip-components=1 \
    && set -ex && for esdirs in config data logs; do \
        mkdir -p "$esdirs"; \
    done \
    && for PLUGIN in x-pack ingest-user-agent ingest-geoip; do \
      elasticsearch-plugin install --batch "${PLUGIN}"; \
    done

COPY elasticsearch.yml log4j2.properties config/
COPY x-pack config/x-pack
COPY docker-entrypoint.sh /docker-entrypoint.sh

USER root
RUN chown -R elasticsearch:0 ./config /docker-entrypoint.sh \
    && chmod -R ug=rxw,o= /usr/share/elasticsearch /docker-entrypoint.sh

USER elasticsearch

ENTRYPOINT ["/docker-entrypoint.sh"]

EXPOSE 9200 \
       9300
