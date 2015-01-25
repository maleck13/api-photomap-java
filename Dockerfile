FROM tutum/tomcat:8.0
ADD setenv.sh /tomcat/bin/
ADD target/api-photomap.war /tomcat/webapps/

