# setenv.sh

-server -Xms2G -Xmx2G -XX:MaxPermSize=128m -XX:+DisableExplicitGC\
 -XX:-HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/var/log/tomcat7/dump\
 -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+UseTLAB -XX:+CMSIncrementalMode\
 -XX:+CMSIncrementalPacing -XX:CMSIncrementalDutyCycleMin=0\
 -XX:CMSIncrementalDutyCycle=10 -XX:MaxTenuringThreshold=0 -XX:SurvivorRatio=256
\
 -XX:CMSInitiatingOccupancyFraction=60 -Djava.net.preferIPv4Stack=true
 