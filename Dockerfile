FROM tomcat:jdk16

ARG CONNECTOR_NAME=DE4AEBSIConnector-0.1-launcher.jar

#create tc webapp folder
WORKDIR $CATALINA_HOME/webapps

ENV CATALINA_OPTS="$CATALINA_OPTS -Dorg.apache.tomcat.util.buf.UDecoder.ALLOW_ENCODED_SLASH=true -Djava.security.egd=file:/dev/urandom"

COPY target/DE4AEnterpriseAgent-0.1.war ./ROOT.war
COPY conf/web.xml ./../conf
COPY conf/service-matrix.properties ./../conf
COPY conf/de4a-metrics-log.txt ./../conf
COPY ${CONNECTOR_NAME} ./

RUN rm -fr manager host-manager docs examples ROOT && \
    unzip $WAR_NAME -d ROOT  && \
    rm -fr $WAR_NAME
