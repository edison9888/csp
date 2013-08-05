pass=$1
. /etc/profile
export LANG=C
daily_user=$2
daily_ip=$3
checkoutDirName=$4
class=$5
runDir=$6
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
jar_name=monitorstat-alarm-1.1.2-SNAPSHOT.jar
lib_name=lib.zip
auto_scp $pass $daily_user@$daily_ip:~/$checkoutDirName/target/$jar_name ~
auto_scp $pass $daily_user@$daily_ip:~/$checkoutDirName/target/$lib_name ~
# 鐒跺悗灏嗙嚎涓婄殑鍙戝竷鑴氭湰鎷疯礉锟斤拷~K
auto_sudo (){
        expect -c "spawn sudo  ${@:2}; expect *assword* {send -- $1\r;exp_continue;}"
}


deploy() {

# pid=`ps -ef |grep ${class}|grep -v 'grep'|grep -v "sh /home/wb-lixing/$0"|awk '{print $2}'`
# echo "pid: $pid"
# 鏇挎崲jar涓嶉渶瑕佹潃杩涚▼
# auto_sudo $pass -u admin kill -9 $pid

cd $runDir
auto_sudo $pass -u admin rm -rvf $jar_name
auto_sudo $pass -u admin cp -v ~/$jar_name .
auto_sudo $pass -u admin rm -rvf $lib_name
auto_sudo $pass -u admin cp -v ~/$lib_name .
# 
# auto_sudo $pass -u admin ./run.sh

}

deploy
