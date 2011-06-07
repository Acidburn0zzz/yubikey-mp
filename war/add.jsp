<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.Iterator"%>
<%@ page import="com.yubico.yubikeymp.*"%>
<%@ page import="com.google.appengine.api.datastore.DatastoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreService" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.datastore.Query" %>
<%@ page import="com.google.appengine.api.datastore.Blob"%>
<%
	YubikeyServer server = YubikeyServer.getInstance();
    String admin = server.getAdminName();
    
	final YubikeyOTP auth = YubikeyOTP.createInstance(request.getParameter("auth"));
	boolean isAdmin = server.hasAdminAccess(auth);

%><!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
<%  if (isAdmin) {
	    %><title>Yubikey Master Password server - add new password</title><%
	} else {
	    %><title></title><%
	}
%>
</head>

<body onLoad="document.forms[0].elements[0].focus();">
<%  if (isAdmin) {
    	YubikeySecret secret = YubikeySecret.createInstance(request.getParameter("secret"), request.getParameter("pid"), request.getParameter("secret"));
    	if (server.put(secret)){
			%>New password saved.<br /><br /><%
    	} else {
    	    %>Password was not saved.<br /><br /><%
    	}
    	// Show add password form
    	%><form name="add" action="/add.jsp" method="post">
    		<label>
    			<span>User</span>
    			<input type="text" name="user" />
    		</label>
			<label>
				<span>PID</span>
				<input type="text" name="pid" />
			</label>
			<label>
				<span>Secret</span>
				<input type="text" name="secret" />
			</label>
			<label>
				<span>Auth</span>
				<input type="text" name="auth" />
				<input type="submit" value="Submit" />
			</label>
		</form><%
	}
%>
</body>
</html>