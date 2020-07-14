<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Insert title here</title>
	<link href="../resource/team05_style.css" rel="stylesheet" type="text/css">
</head>
<body>
	<h1 align="center">회원정보수정</h1>
	<form method="post" action="signupPro.jsp" ctype="multipart/form-data">
	<table>
		<tr>
			<td>프로필 사진</td>
			<td>*</td> 
		</tr>
		<tr>
			<td>아이디*</td>
			<td><input type="text" name="id" /></td>
		</tr>
		<!--중복 id 체크버튼-->
		<tr>
			<td>중복체크</td>
			<td><input type="button" value="중복확인" onclick="confirmId(this.form)"></td>
		</tr>
		<tr>
			<td>비밀번호*</td>
			<td><input type="password" name="pw" /></td>
		</tr>
		<tr>
			<td>비밀번호 확인*</td>
			<td><input type="password" name="pwCh" /></td>
		</tr>
		<tr>
			<td>이름*</td>
			<td>***</td>
		</tr>
		<tr>
			<td>주민번호*</td> 
			<td>
				******-*
			</td>
		</tr>
		<tr>
			<td>채식주의 타입</td>
			<td>
				<select name="vegi_type">
						<option value="비건">비건</option>
						<option value="락토 베지터리언">락토 베지터리언</option>
						<option value="오보 베지터리언">오보 베지터리언</option>
						<option value="락토 오보 베지터리언">락토 오보 베지터리언</option>
						<option value="페스코 베지터리언">페스코 베지터리언</option>
						<option value="폴로 베지터리언">폴로 베지터리언</option>
						<option value="플렉시터리언">플렉시터리언</option>
				</select> 
			</td>
		</tr>
		<tr>
			<td>프로필 사진</td>
			<td><input type="file" name="profile_img" /></td>
		</tr>		
		<tr>
			<td colspan="2" align="center">
				<input type="submit" value="수정"/>
				<input type="reset" name="reset" value="재입력" />
				<input type="button" value="취소" onclick="window.location='main.jsp'"/>
			</td> 
		</tr>
	</table> 
	</form>
</body>
</html>