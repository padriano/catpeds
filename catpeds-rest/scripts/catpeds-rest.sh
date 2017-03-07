#!/bin/sh

RETVAL=0
prog="catpeds-rest"

start() {
    echo "Starting $prog..."
    nohup java -jar ${project.build.finalName}.jar > console.log 2>&1&
    echo $! > $prog.pid
}

stop() {
    echo "Stopping $prog..."
    kill -TERM $(cat $prog.pid)
    rm -f $prog.pid
}

status() {
    echo "Status $prog:"
    if [ -f "$prog.pid" ]
    then
        if [ "$(ps -ef | grep -f $prog.pid | grep -v 'grep')" != "" ];
        then
            echo "Running"
        else
            echo "Not Running"
        fi
    else
        echo "Not Running"
    fi
}

case "$1" in
    start)
        start
        ;;
    stop)
        stop
        ;;
    restart)
        stop
        start
        ;;
    status)
        status
        ;;
    *)
        echo $"Usage: $0 {start|stop|restart|status}"
        RETVAL=1
esac
exit $RETVAL
