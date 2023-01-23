export JAVA_HOME=/opt/software/jdk-17.0.2
export CATALINA_HOME=/opt/software/apache-tomcat-10.0.20
export PATH=$JAVA_HOME/bin:$CATALINA_HOME/bin:$PATH
export CLASSPATH=.:$CATALINA_HOME/lib/jsp-api.jar
cd client
javac rmiweb/*.java
sudo mkdir -p $CATALINA_HOME/webapps/sum-vectors/WEB-INF/classes/rmiweb
sudo cp ./formSumVectors.html $CATALINA_HOME/webapps/sum-vectors
sudo cp ./formSumVectorsBean.jsp $CATALINA_HOME/webapps/sum-vectors
sudo cp -R rmiweb/* $CATALINA_HOME/webapps/sum-vectors/WEB-INF/classes/rmiweb
cd $CATALINA_HOME/bin
sudo ./startup.sh
