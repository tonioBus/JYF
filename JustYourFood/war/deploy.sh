export DEPLOY_DIR=$(dirname $0)
echo "deploy dir [$DEPLOY_DIR]"

set -x

export CATALINA_HOME=/opt/apache-tomcat-7.0.47/
export DEPLOY_PATH=/opt/apache-tomcat-7.0.47/webapps
export WAR_FILE=$DEPLOY_PATH/nutrition.war
export WAR_DIR=$DEPLOY_PATH/nutrition
export ARCH_DIR=~/Dev/archives
export ARCH_FILE=$ARCH_DIR/nutrition.war

# set +x
echo "deployment on $WAR_DIR"
echo "archive: $ARCH_FILE"
echo "Removing old nutrition, ok ? "
read A

sudo cp -p $WAR_FILE ${WAR_FILE}_$(date +"%d-%m-%Y_%H:%M")
cp -p $WAR_FILE ${WAR_FILE}_$(date +"%d-%m-%Y_%H:%M")
cp -p $ARCH_FILE ${ARCH_FILE}_$(date +"%d-%m-%Y_%H:%M")
sudo $CATALINA_HOME/bin/shutdown.sh
echo "Waiting for the stop of apache ..."
sleep 5

echo "destroying the previous webapp"
sudo rm -rf $WAR_DIR
sudo rm $WAR_FILE

cd "$DEPLOY_DIR"
rm $ARCH_FILE
zip -r $ARCH_FILE *
cd -

# remove not desired files
zip -d $ARCH_FILE WEB-INF/lib/gwt-dev.jar
# zip -d $ARCH_FILE WEB-INF/lib/jwnl-1.3.3.jar
zip -d $ARCH_FILE WEB-INF/lib/JustYourFood.jar
zip -d $ARCH_FILE WEB-INF/lib/TestCrawl.jar
zip -d $ARCH_FILE WEB-INF/lib/opennlp-tools-1.5.3.jar
zip -d $ARCH_FILE WEB-INF/lib/opennlp-maxent-3.0.3.jar
zip -d $ARCH_FILE slideshow/*

sudo cp -p $ARCH_FILE $WAR_FILE

echo "List of wabapps: $DEPLOY_PATH"
echo "----------------------------------------"
ls -latr $DEPLOY_PATH
echo 
echo "List of archives: $ARCH_DIR"
echo "----------------------------------------"
ls -latr $ARCH_DIR

sudo $CATALINA_HOME/bin/startup.sh

echo "The End."
exit 0