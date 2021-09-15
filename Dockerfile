FROM tomcat:8.5.71-jdk16-openjdk

ARG WAR_NAME=de4a-agent.war
ARG CONNECTOR_NAME=DE4AEBSIConnector-0.1-launcher.jar

#create tc webapp folder
WORKDIR $CATALINA_HOME/webapps

ENV CATALINA_OPTS="$CATALINA_OPTS -Dorg.apache.tomcat.util.buf.UDecoder.ALLOW_ENCODED_SLASH=true -Djava.security.egd=file:/dev/urandom"

ADD out/artifacts/de4a_agent/${WAR_NAME} ./
COPY conf/web.xml ./../conf
COPY conf/service-matrix.properties ./../conf
COPY conf/de4a-metrics-log.txt ./../conf
COPY ${CONNECTOR_NAME} ./

EXPOSE 8080

CMD["catalina.sh", "run"]
#RUN rm -fr manager host-manager docs examples ROOT && \
#    unzip $WAR_NAME -d ROOT  && \
#    rm -fr $WAR_NAME
