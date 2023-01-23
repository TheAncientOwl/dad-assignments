<%@ page import = "java.util.*" %>
<HTML>
<HEAD><TITLE>Raspuns la formula card JSP</TITLE></HEAD>

<BODY>
<jsp:useBean id="sv1" class="rmiweb.SumVectorsProcessorBean" />
<%-- <%! private String[] rmis = new String[]{"172.31.45.37", "172.31.45.38", "172.31.45.39"}; %> --%>
<%! private String[] rmis = new String[]{"127.0.0.1", "127.0.0.1", "127.0.0.1"}; %>
<%
 
	String v1FileName = request.getParameter("v1FileName");
	String v2FileName = request.getParameter("v2FileName");
	String sumFileName = request.getParameter("sumFileName");

	System.setProperty("java.rmi.server.hostname", "127.0.0.1");
  out.println("Web Server IP = " + java.net.InetAddress.getLocalHost());
	out.println("Web Server RMI Server IP = " + System.getProperty("java.rmi.server.hostname"));

	rmiweb.SumVectorsProcessorBean.setRMIs(rmis);

	Date d1 = new Date();
  int res = sv1.processSumVectors(v1FileName, v2FileName, sumFileName);
	Date d2 = new Date();

	if (res == 0) {
%>
<H1 ALIGN=CENTER>Response from JSP to the request from the HTML page : The request has been processed!</H1>
<%
		out.println("From: " + d1 + " , To:" + d2);
	} else {
%>
<H1 ALIGN=CENTER>Response from JSP to the request from the HTML page : The request has NOT been processed!</H1>
<%
	}
%>
</BODY></HTML>
