Docker java webapp in tomcat8 with java8


to run 

```
  mvn war:war

  docker build -t java-api .

  docker run -it --rm -p 8280:8080 java-api

```

```
  curl http://localhost:8280/api-photomap/

```

