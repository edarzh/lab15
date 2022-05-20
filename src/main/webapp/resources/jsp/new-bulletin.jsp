<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>New bulletin</title>
</head>
<body>
<form action="/lab15/board/add-bulletin" method="POST">
    <p><label for="bulletin-text">New bulletin:</label></p>
    <textarea id="bulletin-text" name="bulletin-text" rows="4" cols="50"></textarea>
    <br>
    <input type="submit" value="Submit">
</form>
<br/>
<a class="link" href="/lab15/board/main-page">Main page</a>
</body>
</html>