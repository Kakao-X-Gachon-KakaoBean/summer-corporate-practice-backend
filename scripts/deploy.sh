#!/bin/bash
PROJECT_PATH=/home/ec2-user/code-deploy
BUILD_JAR=$(ls $PROJECT_PATH/api/build/libs/*.jar)
JAR_NAME=$(basename $BUILD_JAR)
echo "> build 파일명: $JAR_NAME" >> $PROJECT_PATH/deploy.log

echo "> 현재 실행중인 애플리케이션 pid 확인" >> $PROJECT_PATH/deploy.log
CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z $CURRENT_PID ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다." >> $PROJECT_PATH/deploy.log
else
  echo "> kill -15 $CURRENT_PID" >> $PROJECT_PATH/deploy.log
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> 프로젝트 ROOT 경로로 이동" >> $PROJECT_PATH/deploy.log
cd $PROJECT_PATH

DEPLOY_JAR=$PROJECT_PATH/api/build/libs/$JAR_NAME
echo "> DEPLOY_JAR 배포"    >> $PROJECT_PATH/deploy.log
nohup java -jar $DEPLOY_JAR >> $PROJECT_PATH/deploy_std.log 2>$PROJECT_PATH/deploy_err.log &