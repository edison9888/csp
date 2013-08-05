call mvn -Dmaven.test.skip=true -e  clean package

echo+
echo 打包结束

del /f /s /q D:\pf\apache-tomcat-7.0.29\webapps\cost* 
rd /s /q D:\pf\apache-tomcat-7.0.29\webapps\cost

cd  D:\workspace\csp\monitorstat-cost-web\target
ren monitorstat-cost-web-1.0.0-SNAPSHOT.war cost.war

copy D:\workspace\csp\monitorstat-cost-web\target\cost.war D:\pf\apache-tomcat-7.0.29\webapps\
rem cd  D:\pf\apache-tomcat-7.0.29\webapps
rem ren monitorstat-cost-web-1.0.0-SNAPSHOT.war cost.war

echo+
echo 拷贝结束

pause