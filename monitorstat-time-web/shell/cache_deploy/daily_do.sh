source /etc/bashrc;
source ~/.bashrc;
cd /home/wb-lixing/monitorstat/monitorstat-alarm;svn up;/opt/taobao/mvn/bin/mvn -Dmaven.test.skip=true -U clean install;cd /home/wb-lixing/monitorstat/monitorstat-alarm/target;zip -r lib.zip lib
