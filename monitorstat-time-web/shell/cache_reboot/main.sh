hosts_1=~/.hosts_csp_cache
class=com.taobao.csp.dataserver.Main
runDir=/home/admin/datacache


echo '批量拷贝脚本'
pgmscp  -f $hosts_1 kill_and_start.sh ~
echo '执行脚本'
pgm  -f $hosts_1 sudo -u admin "sh ~/kill_and_start.sh $class $runDir"
echo '验证是否重启成功'
pgm  -f $hosts_1  "ps -ef|grep $class|grep -v 'grep' |grep -v 'bash -c'"

