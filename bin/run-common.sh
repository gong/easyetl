#!/usr/bin/env bash

RUN_OPE=$1
MAIN_CLASS="cn.gong.easyetl.SyncEngine"
cd `dirname $0`
BIN_DIR=`pwd`
cd ..
DEPLOY_DIR=`pwd`
CONF_DIR=${DEPLOY_DIR}/conf
LIB_JARS=${DEPLOY_DIR}/lib/*

LOGS_DIR=${DEPLOY_DIR}/logs
if [ ! -d ${LOGS_DIR} ]; then
	mkdir ${LOGS_DIR}
fi
if [ ! -d ${DEPLOY_DIR}/tmp ]; then
    mkdir ${DEPLOY_DIR}/tmp                                                                                                                                                                    
fi

STDOUT_FILE=${LOGS_DIR}/stdout.log

start_service(){
  local PIDS=`ps  --no-heading -C java -f --width 1000 |grep "$CONF_DIR"| grep "$MAIN_CLASS" |awk '{print $2}'`

  if [ -n "$PIDS" ]; then
    echo "ERROR: The service already started!"
    echo "PID: $PIDS"
    exit 1
  fi

  local JAVA_OPTS="-server -Xmx4g -Xms4g -XX:NewSize=128m -XX:MaxNewSize=2g -XX:+UseConcMarkSweepGC"

  echo -e "Starting the service ...\c"

  echo "nohup java $JAVA_OPTS -Ddruid.logType=slf4j -classpath $CONF_DIR:$LIB_JARS $MAIN_CLASS > /dev/null 2>&1 &"

  nohup java $JAVA_OPTS -Djava.io.tmpdir=${DEPLOY_DIR}/tmp -Dlog.dir=${LOGS_DIR} -Ddruid.logType=slf4j -classpath ${CONF_DIR}:${LIB_JARS} ${MAIN_CLASS} > $STDOUT_FILE 2>&1 &

  echo "OK!"
  PIDS=`ps  --no-heading -C java -f --width 1000 | grep "$DEPLOY_DIR" | awk '{print $2}'`
  echo "PID: $PIDS"
  echo "STDOUT: $STDOUT_FILE"
}

stop_service(){
  local PIDS=`ps  --no-heading -C java -f --width 1000 |grep "$CONF_DIR"| grep "$MAIN_CLASS" |awk '{print $2}'`

  if [ -z "$PIDS" ]; then
    echo "ERROR: The service does not started!"
    exit 1
  fi

  echo -e "Stopping the service ...\c"
  for PID in ${PIDS} ; do
	  kill ${PID} > /dev/null 2>&1
  done

  COUNT=0
  TOTAL=0
  while [ ${COUNT} -lt 1 ]; do
    echo -e ".\c"
    sleep 1
    COUNT=1
    let TOTAL+=1
    for PID in $PIDS ; do
		  PID_EXIST=`ps --no-heading -p $PID`
		  if [ -n "$PID_EXIST" ]; then
			  COUNT=0
			  break
		  fi
		  if [ ${COUNT} -gt 30 ]; then
			  for PID in ${PIDS} ; do
				  kill -9 ${PID} > /dev/null 2>&1
			  done
			  COUNT=0
			  break
		  fi
	  done
  done
  echo "OK!"
  echo "PID: $PIDS"
}

case ${RUN_OPE} in
  start )
    start_service
    ;;
  START )
    start_service
    ;;

  stop )
    stop_service
    ;;
  STOP )
    stop_service
    ;;

  * )
    echo "Unknown operation:${RUN_OPE}"
    exit 1
    ;;
esac

