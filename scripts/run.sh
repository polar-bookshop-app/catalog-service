#!/usr/bin/env bash

# JVM
JMX_REMOTE_CONNECTION="-Dcom.sun.management.jmxremote \
  -Djava.rmi.server.hostname=localhost"

REMOTE_DEBUGGER=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005

SPRING_ADDITIONAL_PROPERTIES="-Dcatalog.service.testdata.enabled=true"

export JAVA_TOOL_OPTIONS="-XX:+ExitOnOutOfMemoryError \
  -XX:MaxDirectMemorySize=10M \
  -Xmx1506507K \
  -XX:MaxMetaspaceSize=78644K \
  -XX:ReservedCodeCacheSize=240M \
  -Xss1M \
  $JMX_REMOTE_CONNECTION \
  $REMOTE_DEBUGGER \
  $SPRING_ADDITIONAL_PROPERTIES"

# Some ENVs
#export TOMCAT_THREADS_MIN=5
#export TOMCAT_THREADS_MAX=25

# enabled/disable flyway migrations
#export SPRING_FLYWAY_ENABLED=true

java -jar build/libs/catalog-service-0.0.1-SNAPSHOT.jar