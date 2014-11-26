#!/bin/bash

if [ ! -f /.tomcat_admin_created ]; then
    /data/create_tomcat_admin_user.sh
fi

exec ${CATALINA_HOME}/bin/catalina.sh run
