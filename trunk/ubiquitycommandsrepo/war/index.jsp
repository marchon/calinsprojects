<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="javax.jdo.PersistenceManager"%>
<%@page import="ro.calin.ubiquity.Feed"%>
<%@page import="ro.calin.ubiquity.PMF"%>
<%@page import="java.util.List"%>
<%@page import="ro.calin.ubiquity.Constants"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Calin's ubiquity commands repository</title>
<link rel="commands" href="/cmd/ubirepo.js" name="ubirepo">
</head>
<body>
	<h3>Available feeds</h3>
<%
    PersistenceManager pm = PMF.get().getPersistenceManager();
    String query = "select from " + Feed.class.getName();
    List<Feed> feeds = (List<Feed>) pm.newQuery(query).execute();

    for (Feed g : feeds) {
        %>
        	<a href="/feed.jsp?<%=Constants.REQ_TYPE_PARAM%>=<%=Constants.REQ_GETFEEDJS%>&<%=Constants.REQ_FEEDNAME_PARAM%>=<%=g.getFeedName()%>">
        		<%=g.getFeedName()%>
        	</a>
        	<br/>
        <%
    }
    
    pm.close();
%>
</body>
</html>