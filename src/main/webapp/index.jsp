<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<title>Bulletin Board</title>
</head>
<body>
<h1>Bulletin Board</h1>
<a href="${pageContext.request.contextPath}/login.jsp">Login</a>
<c:if test="${loggedIn}">
	| <a href="/lab15_war_exploded/bulletin-board/logout">Logout</a> |
	<a href="/lab15_war_exploded/bulletin-board/add-bulletin">Add Bulletin</a>
</c:if>
<ul>
	<c:forEach var="entry" items="${requestScope.bulletinBoard}">
		<li class="text">${entry.key}: ${entry.value}</li>
	</c:forEach>
</ul>
</body>
</html>