#! /bin/sh
${JAVA_HOME}/bin/java -cp $1:${JAVA_HOME}/lib/tools.jar:${JAVA_HOME}/../Classes/classes.jar com.taobao.csp.btrace.inject.Main $2 $1 $3