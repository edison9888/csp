class=$1
runDir=$2

pid=`ps -ef |grep ${class}|grep -v 'grep'|grep -v 'sh /home/wb-lixing/kill_and_start.sh'|awk '{print $2}'`
echo "pid: $pid"
kill -9 $pid
cd $runDir
./run.sh
