source /etc/bashrc;
cd /home/wb-lixing/monitorstat/monitorstat-depend-web;
svn up;
/opt/taobao/mvn/bin/mvn -Dmaven.test.skip=true -U clean install
