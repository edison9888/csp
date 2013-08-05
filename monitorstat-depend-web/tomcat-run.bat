set MAVEN_OPTS=-Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=9494,suspend=n,server=y -Xms256m -Xmx512m -XX:MaxPermSize=128m
call mvn tomcat:run  -Pdev
@pause