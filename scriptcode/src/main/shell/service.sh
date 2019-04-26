function stopService(){
    MAIN_CLASS=$1
    SERVER_PID=`ps auxf | grep ${MAIN_CLASS} | grep -v "grep"| awk '{print $2}'`
    echo "server pid is [${SERVER_PID}]"
    for pid in ${SERVER_PID}
    do
        kill -9 $pid
        echo "$pid is killed!"
    done
}

stopService 'aaa.war'





