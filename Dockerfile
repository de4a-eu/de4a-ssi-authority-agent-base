FROM tomcat:8.5.71-jdk16-openjdk

ARG WAR_NAME=de4a-agent.war
ARG CONNECTOR_NAME=DE4AEBSIConnector-2.1-launcher.jar

#create tc webapp folder
WORKDIR $CATALINA_HOME/webapps

ENV CATALINA_OPTS="$CATALINA_OPTS -Dorg.apache.tomcat.util.buf.UDecoder.ALLOW_ENCODED_SLASH=true -Djava.security.egd=file:/dev/urandom"

COPY out/artifacts/de4a_agent/${WAR_NAME} ./ROOT.war
COPY conf/web.xml ./../conf
COPY conf/service-matrix.properties ./../conf
COPY ${CONNECTOR_NAME} ./
COPY lib/WaltID-ServiceMatrix-1.1.2.jar ./../lib
COPY lib/waltid-ssi-kit-1.13.0-SNAPSHOT5.jar ./../lib
COPY lib/waltid-ssi-kit-1.0-20211006.180057-61.jar ./../lib
COPY lib/waltid-ssikit-vclib-1.23.5.jar ./../lib
COPY lib/bcprov-jdk15on-1.70.jar ./../lib
COPY lib/bcprov-jdk15to18-1.72.jar ./../lib

EXPOSE 8080
CMD ["catalina.sh", "run"]
#RUN rm -fr manager host-manager docs examples ROOT && \
#    unzip $WAR_NAME -d ROOT  && \
#    rm -fr $WAR_NAME
