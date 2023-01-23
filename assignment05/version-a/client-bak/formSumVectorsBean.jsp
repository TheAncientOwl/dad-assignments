<%@page import="java.util.*" %>
<HTML>
  <HEAD>
    <TITLE>Raspuns la form</TITLE>
  </HEAD>
  <BODY>
    <jsp:useBean id="svp" class="rmiweb.SumVectorsProcessorBean" />
    <%! private String[] rmis = new String[]{"172.31.45.37", "172.31.45.38", "172.31.45.39"}; %>
    <%
    String vector1Path = request.getParameter("vector1");
    String vector2Path = request.getParameter("vector2");
    String vectorSumPath = request.getParameter("vectorSum");

    System.setProperty("java.rmi.server.hostname", "172.31.45.37");
    out.println("Web Server IP = " + java.net.InetAddress.getLocalHost());
	  out.println("Web Server RMI Server IP = " + System.getProperty("java.rmi.server.hostname"));

    rmiweb.SumVectorsProcessorBean.setRMIs(rmis);

    Date begin = new Date();
    int res = svp.sumVectors(vector1Path, vector2Path, vectorSumPath);
    Date end = new Date();

    if (res == 0) {
    %>
    <H1 ALIGN=CENTER>Response from JSP to the request from the HTML page : The request has been processed!</H1>
    <%
      out.println("From: " + begin + ", To: " + end);
    } else {
    %>
    <H1 ALIGN=CENTER>Response from JSP to the request from the HTML page : The request has NOT been processed!</H1>
    <%
    }
    %>
  </BODY>
</HTML>
