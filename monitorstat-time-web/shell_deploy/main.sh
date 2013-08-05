## 此脚本放在login
export LANG=C
pass="99lzhlmcl*1"
online_hosts=(coremonitor152138.cm3 )
# online_hosts=(coremonitor210117.cm3 )
# online_hosts=(coremonitor152138.cm3 coremonitor210117.cm3) 
auto_scp () {
    expect  -c "set timeout -1;
                spawn scp -o StrictHostKeyChecking=no  ${@:2};
                expect {
                    *assword:* {send -- $1\r;
                                 expect {
                                    *denied* {exit 1;}
                                    eof
                                 }
                    }
                    eof         {exit 1;}
                }
                "
}
auto_ssh () {
	expect -c "set timeout -1;spawn ssh -t -o StrictHostKeyChecking=no  ${@:2};expect *assword:* {send -- $1\r;exp_continue;}"
}
# daily_ip, daily_user, gitDirName是写死的
daily_ip=10.232.68.108
daily_user=wb-lixing
checkoutPath=monitorstat/monitorstat-time-web
# 创建版本库,不需要在批处理中，因为一个应用只执行一次
# 打包
auto_scp $pass daily_do.sh $daily_user@$daily_ip:~
auto_ssh $pass $daily_user@$daily_ip  sh daily_do.sh $checkoutPath 
deploy_per_host(){
    temp_host=$1
    echo 部署机器: $temp_host
    ## copy部署脚本
    auto_scp $pass  per_deploy.sh wb-lixing@${temp_host}:/home/wb-lixing
        ##   
    auto_ssh $pass wb-lixing@$temp_host "sh ~/per_deploy.sh $pass  $daily_user $daily_ip $checkoutPath"
}
for host in ${online_hosts[@]};do
    deploy_per_host $host
done


