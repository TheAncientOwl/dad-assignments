# OS: Ubuntu 22.04.1 LTS on Windows 10 x86_64

# Task:
  > Download laboratory/seminary with TCP HTTP Server server 
  proof of concept, in Linux Ubuntu (download the virtual machine from the current page - http://acs.ase.ro/dad), 
  in order to solve HTTP requests for resources of type *.class. 
  
  > The URL in Internet browser - Mozilla Firefox/Google Chrome 
  * for http://ip_server:port_server/myfile.html will display the HTML file if exists, 
  * BUT when the browser asks http://ip_server:port_server/MyClassFile.class will display 
  the string chars produced by method 'public String sampleDoGet();'. 
  
  > The one should modify method 'public String processInput(String theInput);' 
  from class 'eu.ase.httpserver.HTTPSeminarProtocol' in order to handle through Java reflection the requiered task."

# How to run:
  ./compile-and-run.sh

# in browser enter link: http://localhost:25565/ReflectionClass.class
