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
	| <a class="link" href="/lab15/board/logout">Logout</a> |
	<a class="link" href="${pageContext.request.contextPath}/resources/jsp/new-bulletin.jsp">Add Bulletin</a>
</c:if>
<c:forEach var="entry" items="${requestScope.bulletinBoard}">
	<div class="bulletin-out">
		<div class="bulletin">
			<c:forEach var="bulletinHeader" items="${entry.key}">
				<cite class="bulletin-header">${bulletinHeader}</cite>
			</c:forEach>
			<article class="bulletin-body">${entry.value}</article>
			<c:if test="${loggedIn}">
				<c:if test="${name == entry.key[0]}">
					<a class="small link" href="/lab15/board/remove-bulletin">Remove</a>
				</c:if>
			</c:if>
		</div>
	</div>
</c:forEach>
</body>
</html>