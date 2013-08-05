hosts_1=~/.hosts_csp_caiji
runDir=/home/admin/time
class=com.taobao.csp.monitor.Main

echo '批量拷贝脚本'
pgmscp  -f $hosts_1 kill_and_start.sh ~
echo '执行脚本'
pgm  -f $hosts_1 sudo -u admin sh ~/kill_and_start.sh $class $runDir
echo '验证是否重启成功'
pgm  -f $hosts_1  "ps -ef|grep $class|grep -v 'grep' |grep -v 'bash -c'"

