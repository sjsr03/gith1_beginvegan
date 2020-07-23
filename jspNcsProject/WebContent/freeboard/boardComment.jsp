<%@page import="java.text.SimpleDateFormat"%>
<%@page import="jspNcsProject.dto.BoardCommentDTO"%>
<%@page import="java.util.List"%>
<%@page import="jspNcsProject.dao.BoardCommentDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<%	
	int freeboard_num = Integer.parseInt(request.getParameter("num"));//글번호
	String form = request.getParameter("form");//modify, insert
	

	//댓글리스트 가져오기
	BoardCommentDAO dao = BoardCommentDAO.getInstance();
	List commentList = dao.selectAllBoardComment(freeboard_num);

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	String loginId = null;
	if(session.getAttribute("memId")!=null){
		loginId = (String)session.getAttribute("memId");
	}
	
%>

<body>
	<table class="comment_list">		
		<%if(commentList == null){%>
			<tr>
				<td>작성된 댓글이 없습니다.</td>
			</tr>
		<%}else{
			for(int i = 0 ; i<commentList.size();i++){
				BoardCommentDTO comment = (BoardCommentDTO)commentList.get(i);
				String name = dao.selectNameById(comment.getWriter());
				String img = dao.selectImgById(comment.getWriter());
				if(comment.getRe_level()==0){
				%>
				<tr>
					<td rowspan='2'><img width="60px" height="60px" src="/jnp/save/<%=img%>"/></td>
					<td><%=name%></td>
					<td>
					<% if(loginId !=null && loginId.equals(comment.getWriter())){ %>
						<button onclick="modifyComment('<%=comment.getComment_num()%>')">수정</button>
					<%}%>
					<%if (loginId !=null && (loginId.equals(comment.getWriter())|| loginId.equals("admin"))){%>
						<button onclick="deleteComment('<%=comment.getComment_num()%>')">삭제</button>
					<%}%>
					<% if(loginId !=null && !loginId.equals(comment.getWriter()) &&!loginId.equals("admin") ){ %>
						<button onclick="reply('<%=freeboard_num%>','<%=comment.getRef()%>','<%=comment.getRe_level()%>','<%=comment.getWriter()%>')">답글</button>
						<button onclick="report('FC','<%=comment.getComment_num()%>','<%=comment.getWriter()%>')">신고</button>
					<%} %>
					</td>
					<td><%=sdf.format(comment.getReg())%></td>
				</tr>
				<tr>
					<td colspan='4'><%=comment.getContent()%></td>		
				</tr>	
		<% 		}else{
					String receiverName = dao.selectNameById(comment.getReceiver());
					String img2 = dao.selectImgById(comment.getWriter());
		%>
				<tr>
					<td rowspan='2'><img width="20px" src="/jnp/freeboard/img/replyImg.png"/></td>
					<td rowspan='2'><img width="60px" height="60px" src="/jnp/save/<%=img2%>"/></td>
					<td><%=name%></td>
					<td><% if(loginId !=null && loginId.equals(comment.getWriter())){ %>
						<button onclick="modifyComment('<%=comment.getComment_num()%>')">수정</button>
					<%}%>
					<%if (loginId !=null && (loginId.equals(comment.getWriter())|| loginId.equals("admin"))){%>
						<button onclick="deleteComment('<%=comment.getComment_num()%>')">삭제</button>
					<%}%>
					<% if(loginId !=null && !loginId.equals(comment.getWriter()) &&!loginId.equals("admin") ){ %>
						<button onclick="reply('<%=freeboard_num%>','<%=comment.getRef()%>','<%=comment.getRe_level()%>','<%=comment.getWriter()%>')">답글</button>
						<button onclick="report('FC','<%=comment.getComment_num()%>','<%=comment.getWriter()%>')">신고</button>
					<%} %></td>
					<td><%=sdf.format(comment.getReg())%></td>
				</tr>
				<tr>
					<td><%=receiverName%></td>
					<td><%=comment.getContent()%></td>
				</tr>
		<% 		}
			}
		}%>
	</table>
	<%if(session.getAttribute("memId")!=null){%>
	<form action="boardCommentPro.jsp" method="post">
		<input type="hidden" name="freeboard_num" value="<%=freeboard_num%>"/>
		<input type="hidden" name="writer" value="<%=session.getAttribute("memId")%>"/>		
		<table class="comment_input">
			<tr>
				<td><textarea name="content" cols="70" rows="3" style="resize:none;"></textarea></td> 
				<td><input type="submit" value="댓글작성"/></td>
			</tr>
		</table>
	</form>
	<%}else{%>
		<table class="comment_input">
			<tr>
				<td>로그인 후 댓글을 작성해주세요.</td> 
			</tr>
		</table>
	<%}%>
	
</body>
<script>

	//리댓달기 창
	function reply(freeboard_num, ref, re_level, receiver) {
		
		var url = "boardCommentReplyForm.jsp?freeboard_num="+freeboard_num+"&ref="+ref+"&re_level="+re_level+"&receiver="+receiver;
		var name = "댓글 달기";
		var option = "width=600,height=,left=600,toolbar=no,menubar=no,location=no,scrollbar=no,status=no,resizable=no";
		
		window.open(url,name,option);
		
	}
	
	//수정
	function modifyComment(comment_num) {
		var url = "boardCommentModifyForm.jsp?comment_num="+comment_num;
		var name = "댓글 수정하기";
		var option = "width=550,height=400,left=600,toolbar=no,menubar=no,location=no,scrollbar=no,status=no,resizable=no";
		
		window.open(url,name,option);
	}
	
	//삭제
	function deleteComment(comment_num) {
		if(confirm("정말 삭제하시겠습니까?")){
			window.location="boardCommentDeletePro.jsp?comment_num=" + comment_num;
		}
			
	}

</script>
</html>