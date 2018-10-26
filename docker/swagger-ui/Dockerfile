FROM nginx:1.13
MAINTAINER <thomas.herzog@gepardec.com>

ARG VERSION

LABEL name="SwaggerUI" \
      summary="Integrate SwaggerUI with Wildfly Swarm" \
      description="The Swagger Editor is an open source editor to design, define and document RESTful APIs in the Swagger Specification." \
      url="http://directory.apache.org/apacheds/" \
      vendor="Gepardec" \
      version="${VERSION}" \
      release="${VERSION}" \
      run='docker run --name ${NAME} \
        -p 8080:8080 \
        -d  \
        ${IMAGE}' \
      io.k8s.description="Integrate SwaggerUI with Wildfly Swarm" \
      io.k8s.display-name="The Swagger Editor is an open source editor to design, define and document RESTful APIs in the Swagger Specification."

ENV NGINX_RUN_USER swag
ENV NGINX_RUN_GROUP 0
ENV NGINX_HOME_RUN_USER /home/${NGINX_RUN_USER}
ENV DEBIAN_FRONTEND noninteractive

USER root

RUN mkdir -p ${NGINX_HOME_RUN_USER} \
    && useradd \
      --home-dir ${NGINX_HOME_RUN_USER} \
      --shell /sbin/nologin \
      -g ${NGINX_RUN_GROUP} \
      -u 1001 \
      ${NGINX_RUN_USER} \
    && apt-get update -y \
    && apt-get install -y curl \
    && apt-get autoremove -y \
    && apt-get autoclean -y \
    && rm -r /var/lib/apt/lists/*

RUN mkdir -p /tmp/swagger-ui \
    && curl -L https://github.com/swagger-api/swagger-ui/archive/v${VERSION}.tar.gz \
      | tar xz --strip-components=1 -C /tmp/swagger-ui \
    && mv /tmp/swagger-ui/dist/* /usr/share/nginx/html/ \
    && mv -f /tmp/swagger-ui/nginx.conf /etc/nginx/nginx.conf \
    && rm -rf /tmp/swagger-ui

COPY index.html /usr/share/nginx/html/index.html

RUN chown -R ${NGINX_RUN_USER}:${NGINX_RUN_GROUP} /var/cache/nginx \
    && chown -R ${NGINX_RUN_USER}:${NGINX_RUN_GROUP} /var/log/nginx \
    && chown -R ${NGINX_RUN_USER}:${NGINX_RUN_GROUP} /usr/share/nginx \
    && chown -R ${NGINX_RUN_USER}:${NGINX_RUN_GROUP} /var/run \
    && chmod -R g+rwX  /usr/share/nginx/html \
    && chmod -R g+rwX  /var/cache/nginx \
    && chmod -R g+rwX  /var/log/nginx \
    && chmod -R g+rwX  /usr/share/nginx \
    && chmod -R g+rwX  /var/run

USER ${NGINX_RUN_USER}
