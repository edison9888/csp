pass=99lzhlmcl*1
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

# daily_ip, daily_user, checkoutDirName是写死的
daily_ip=10.232.68.108
daily_user=wb-lixing

# daily更新git
checkoutDir=~/monitorstat/monitorstat-time-web
auto_scp $pass tool_syn_helper.sh $daily_user@${daily_ip}:~
auto_ssh $pass $daily_user@$daily_ip sh tool_syn_helper.sh $checkoutDir
# 从daily拉脚本
auto_scp  $pass  -r $daily_user@${daily_ip}:$checkoutDir/shell/* .
