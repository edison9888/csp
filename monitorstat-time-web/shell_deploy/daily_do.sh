source /etc/bashrc;
source ~/.bashrc;
cd /home/wb-lixing/monitorstat/monitorstat-time-web;svn up;/opt/taobao/mvn/bin/mvn -Dmaven.test.skip=true -U clean install
