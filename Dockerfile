FROM tomcat:8.0
MAINTAINER um-si
# COPY path-to-your-application-war path-to-webapps-in-docker-tomcat
COPY out/artifacts/de4a_agent /usr/local/tomcat/webapps/