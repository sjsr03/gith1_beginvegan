<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Insert title here</title>
	<link href="../resource/team05_style.css" rel="stylesheet" type="text/css">
</head>
<body>
<br/>
<h1 align="center"> 회원 탈퇴 </h1>
<form action="memberDeleteForm.jsp" method="post">
	<table>
		<tr>
			<td> 아이디 </td>
		</tr>
		<tr>
			<td><input type="text" name="id"/></td>
		</tr>
		<tr>
			<td> 비밀번호 </td>
		</tr>
		<tr>
			<td><input typr="password" name="pw"/> </td>
		</tr>
	</table>
</form>

</body>
</html>