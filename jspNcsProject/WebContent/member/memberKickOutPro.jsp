<%@page import="jspNcsProject.dao.MemberDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Insert title here</title>
</head>
<%
	if(session.getAttribute("memId")==null||!session.getAttribute("memId").toString().equals("admin")
	||request.getParameter("id")==null||request.getParameter("option")==null){%>
		<%if(session.getAttribute("memId")==null){%>
			<script>
			alert("로그인 후 이용가능합니다.");
			window.location="../main.jsp";
			</script>
		<%}else if(!session.getAttribute("memId").toString().equals("admin")){ %>
			<script>
				alert("관리자 페이지 입니다.");
				window.location="../main.jsp";
			</script>
		<%}else if(request.getParameter("id")==null||request.getParameter("option")==null){ %>
			<script>
				alert("잘못된 접근입니다");
				window.location="../main.jsp";
			</script>
		<%}
	}else{ 
		MemberDAO dao = MemberDAO.getInstance(); 
		String id = request.getParameter("id");
		String option = request.getParameter("option");
		int result = dao.kickOffMember(id,option);
		if(result == 1&&option.equals("kickOff")){%>
			<script>
				alert("회원이 강퇴되었습니다.");
				window.location="memberList.jsp"; 
			</script> 
		<%}else if(result == 1&&option.equals("kickOffCancle")){%>
			<script>
			alert("회원이 복구되었습니다.");
			window.location="memberList.jsp";
			</script>
		<% }
	}%>

<body>
</body>
</html>