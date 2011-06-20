<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="ro.calin.ubiquity.Constants"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%
	String feed = request.getParameter("feed");
%>

<title><%=feed%></title>
<link rel="commands" 
	href="/service?<%=Constants.REQ_TYPE_PARAM%>=<%=Constants.REQ_GETFEEDJS%>&<%=Constants.REQ_FEEDNAME_PARAM%>=<%=feed%>" 
    name="<%=feed%>">
</head>
<body>

</body>
</html>