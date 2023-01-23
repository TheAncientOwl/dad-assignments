export JAVA_HOME=/opt/software/jdk-17.0.2
export CATALINA_HOME=/opt/software/apache-tomcat-10.0.20
export PATH=$JAVA_HOME/bin:$CATALINA_HOME/bin:$PATH
export CLASSPATH=.:$CATALINA_HOME/lib/jsp-api.jar
cd server
javac rmiweb/*.java
rmiregistry & java -classpath $CLASSPATH -Djava.security.policy=./policy.all -Xmx1000000000 -Djava.rmi.server.hostname=10.2.65.95 rmiweb.Main
