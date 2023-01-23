# OS: Ubuntu 22.04.1 LTS on Windows 10 x86_64

# Task:
  > Download lecture and laboratory/seminary in Linux Ubuntu (download the virtual machine from this page 
  - http://acs.ase.ro), in order to solve the following problem:

  > Develop the JSP and Java bean necessary for Apache Tomcat deploy 
  that will receive via HTTP to vectors 
  and will responde with the sum of the vectors. 
  
  > The Java bean from web will be the RMI client for 3 RMI servers objects, 
  each RMI server object is registred in the same rmiregistry or in different rmiregistry program. 
  
  > Both versions, a) and b) are required.
    Version a) - The RMI Server is developed in standard mode as in lecture sample (RMI01 and RMI03 directories).
    Version b) - The RMI Server is developed in order to receive the bytecode of business logic 
    (adding two vectors) in the same way as in ComputePi solution - ./downloads_readme/rmidocs/rmiDocsExec
    
# How to run: (version-a)
  # server part - in one terminal window:
  export JAVA_HOME=/opt/software/jdk-17.0.2
  export CATALINA_HOME=/opt/software/apache-tomcat-10.0.20
  export PATH=$JAVA_HOME/bin:$CATALINA_HOME/bin:$PATH
  export CLASSPATH=.:$CATALINA_HOME/lib/jsp-api.jar
  cd server
  javac rmiweb/*.java
  rmiregistry & java -classpath $CLASSPATH -Djava.security.policy=./policy.all -Xmx1000000000 -Djava.rmi.server.hostname=172.31.45.37 rmiweb.SumVectorsProgMain

  # client part - in another terminal window:
  export JAVA_HOME=/opt/software/jdk-17.0.2
  export CATALINA_HOME=/opt/software/apache-tomcat-10.0.20
  export PATH=$JAVA_HOME/bin:$CATALINA_HOME/bin:$PATH
  export CLASSPATH=.:$CATALINA_HOME/lib/jsp-api.jar
  cd client
  javac rmiweb/*.java
  # sudo rm -rf $CATALINA_HOME/webapps/sum-vectors
  sudo mkdir -p $CATALINA_HOME/webapps/sum-vectors/WEB-INF/classes/rmiweb
  sudo cp ./sumVectorsForm.html $CATALINA_HOME/webapps/sum-vectors
  sudo cp ./sumVectorsFormBean.jsp $CATALINA_HOME/webapps/sum-vectors
  sudo cp -R rmiweb/* $CATALINA_HOME/webapps/sum-vectors/WEB-INF/classes/rmiweb
  cd $CATALINA_HOME/bin
  sudo ./startup.sh

  # access localhost:8080/sum-vectors/sumVectorsForm.html
