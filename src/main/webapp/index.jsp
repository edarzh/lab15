<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/style.css">
	<title>Bulletin Board</title>
</head>
<body>
<h1>Bulletin Board</h1>
<a class="link" href="${pageContext.request.contextPath}/resources/jsp/login.jsp">Login</a>
<c:if test="${loggedIn}">
	| <a class="link" href="/lab15/bulletin-board/logout">Logout</a> |
	<a class="link" href="/lab15/bulletin-board/add-bulletin">Add Bulletin</a>
</c:if>
<c:forEach var="entry" items="${requestScope.bulletinBoard}">
	<div class="bulletin-out">
		<div class="bulletin">
			<p class="bulletin-header">${entry.key}</p>
			<p class="bulletin-body">${entry.value}</p>
		</div>
	</div>
</c:forEach>
</body>
</html>