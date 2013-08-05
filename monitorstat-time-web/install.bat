call mvn clean
@REM 
set MAVEN_OPTS=-Xms800m -Xmx800m
call mvn install -Dmaven.test.skip=true
@pause