. /etc/profile
export LANG=C
pass=$1
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

auto_sudo (){
        expect  -c "spawn sudo  ${@:2}; expect *assword* {send -- $1\r;exp_continue;}"
}

auto_scp $pass $daily_user@$daily_ip:~/$checkoutDirName/target/depend.war ~


# 部署目录，包的名��~W
dir_name=depend
pack_name=$dir_name

tomcat='/home/admin/apache-tomcat-7.0.27'
alias start_tomcat="auto_sudo $pass -u admin setsid ${tomcat}/bin/startup.sh"
alias stop_tomcat="auto_sudo $pass -u admin kill -9 \`ps axo pid,cmd|grep org.apache.catalina.startup.Bootstrap|grep -v grep|awk '{print \$1}'\`"
log_f="${tomcat}/logs/catalina.out"
alias log="tail -f -n 1000 $log_f"


deploy() {
        stop_tomcat;
      #  sleep 3;
        auto_sudo $pass -u admin rm -rf ${tomcat}/webapps/${dir_name}*
        auto_sudo $pass -u admin cp ~/${pack_name}*.war ${tomcat}/webapps/${pack_name}.war
        start_tomcat;
        log

}

deploy

