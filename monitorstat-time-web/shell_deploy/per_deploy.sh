pass=$1
. /etc/profile
export LANG=C
daily_user=$2
daily_ip=$3
checkoutDirName=$4
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
auto_scp $pass $daily_user@$daily_ip:~/$checkoutDirName/target/time.war ~
# 然后将线上的发布脚本拷贝��~K
auto_sudo (){
        expect -c "spawn sudo  ${@:2}; expect *assword* {send -- $1\r;exp_continue;}"
}

# 部署目录，包的名��~W
dir_name=time
pack_name=$dir_name
# /home/admin/taobao-tomcat-7.0.26/deploy/time
tomcat='/home/admin/taobao-tomcat-7.0.26'
# with hsf, deploy dir is diff 
deploy_dir=$tomcat/deploy
alias start_tomcat="auto_sudo $pass -u admin setsid ${tomcat}/bin/startup.sh"
alias stop_tomcat="auto_sudo $pass -u admin kill -9 \`ps axo pid,cmd|grep org.apache.catalina.startup.Bootstrap|grep -v grep|awk '{print \$1}'\`"
log_f="${tomcat}/logs/catalina.out"
alias log="tail -f -n 1000 $log_f"


deploy() {
        stop_tomcat;
#        sleep 3;
        auto_sudo $pass -u admin rm -rvf $deploy_dir/$dir_name
        auto_sudo $pass -u admin cp -v ~/${pack_name}.war $deploy_dir/${pack_name}.war
        cd $deploy_dir
        auto_sudo $pass -u admin mkdir $dir_name
        cd $dir_name
#       read -p "before jar"
           auto_sudo $pass -u admin jar -xvf ../${pack_name}.war
#       read -p "end jar"
        auto_sudo $pass rm -vf ../${pack_name}.war
        # hsf.configuration, hsf.log会�成到这里
        cd $tomcat
        start_tomcat;
        log
}

deploy
