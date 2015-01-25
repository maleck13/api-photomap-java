CATALINA_OPTS="-Xdebug -Xrunjdwp:transport=dt_socket,address=62911,server=y,suspend=n"
CATALINA_OPTS="${CATALINA_OPTS} -Dcom.sun.management.jmxremote="
CATALINA_OPTS="${CATALINA_OPTS} -Dcom.sun.management.jmxremote.port=1898 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false"
CATALINA_OPTS="${CATALINA_OPTS} -Djava.rmi.server.hostname=192.168.33.10"