package jspNcsProject.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import jspNcsProject.dto.MemberDTO;

public class MemberDAO {
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;

	//singleton
	private MemberDAO() {}
	private static MemberDAO instance = new MemberDAO();
	public static MemberDAO getInstance() {
		return instance;
	}
	
	public Connection getConnection() throws Exception {
		Context ctx = (Context)new InitialContext();
		Context env = (Context)ctx.lookup("java:comp/env");
		DataSource ds = (DataSource)env.lookup("jdbc/orcl");
		return ds.getConnection();
	}
	public int insertMember(MemberDTO dto) {
		int result = 0;
		try {
			//11개
			String sql = "INSERT INTO MEMBER values(?,?,?,?,?,?,?,?,0,?,'활동',sysdate)";
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,dto.getId());
			pstmt.setString(2,dto.getPw());
			pstmt.setString(3,dto.getName());
			pstmt.setString(4,dto.getId_number());
			pstmt.setString(5,dto.getAge());
			pstmt.setString(6,dto.getGender());
			pstmt.setString(7,dto.getVegi_type());
			pstmt.setString(8,dto.getProfile_img());
			pstmt.setString(9,null);//offence URL
			result = pstmt.executeUpdate();
			System.out.println("[Insert된 회원의 수"+result+"]");
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			//close block
			if(rs!=null) {try {rs.close();} catch (SQLException e) {e.printStackTrace();}}
			if(pstmt!=null) {try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}}
			if(conn!=null) {try {conn.close();} catch (SQLException e) {e.printStackTrace();}}
		}
		return result;
	}
	
	public int loginCheck(String id, String pw) {
		
		//1 > 로그인가능
		//0 > 회원가입필요
		//-1 > 강퇴당한 회원
		
		int result = 0;
		System.out.println(id);
		System.out.println(pw);
		try {
			String sql = "select * from member where id=? and pw=?";
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = 1;		
				if(rs.getString("state").equals("강퇴")) {
					result = -1;
				}
			}else {
				System.out.println("return FALSE");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(rs != null)try {rs.close();}catch(Exception e) {e.printStackTrace();}
			if(pstmt != null)try {pstmt.close();}catch(Exception e) {e.printStackTrace();}
			if(conn != null)try {conn.close();}catch(Exception e) {e.printStackTrace();}		
		}
		return result;
	}
	
	public String getMemberName(String id, String pw) {
		String result="";
		try {
			conn = getConnection();
			String sql = "SELECT name FROM MEMBER WHERE id=? AND pw=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getString("name");
				System.out.println(result);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(rs != null)try {rs.close();}catch(Exception e) {e.printStackTrace();}
			if(pstmt != null)try {pstmt.close();}catch(Exception e) {e.printStackTrace();}
			if(conn != null)try {conn.close();}catch(Exception e) {e.printStackTrace();}		
		}
		return result;
	}
	//아이디 중복검사
	public boolean confirmId(String id) {
		boolean result = false;
		try {
			String sql = "select * from member where id = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = true;
			}
			System.out.println("confirmId "+result);
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(rs!=null)try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			if(pstmt!=null)try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			if(conn!=null)try {rs.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return result;
	}
	
	//활동명 중복검사
		public boolean confirmName(String name) {
			boolean result = false;
			try {
				String sql = "select * from member where name = ?";
				conn = getConnection();
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, name);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					result = true;
				}
				System.out.println("confirmId "+result);
			}catch (Exception e) {
				e.printStackTrace();
			}finally {
				if(rs!=null)try {rs.close();} catch (SQLException e) {e.printStackTrace();}
				if(pstmt!=null)try {rs.close();} catch (SQLException e) {e.printStackTrace();}
				if(conn!=null)try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			}
			return result;
		}
	
	public int deleteMember(String id) {
		int result=0;
		try {
			conn = getConnection();
			String sql = "delete from member where id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			result = pstmt.executeUpdate();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(rs != null)try {rs.close();}catch(Exception e) {e.printStackTrace();}
			if(pstmt != null)try {pstmt.close();}catch(Exception e) {e.printStackTrace();}
			if(conn != null)try {conn.close();}catch(Exception e) {e.printStackTrace();}
		}
		System.out.println(result);
		return result;
	}
	
	public MemberDTO modifyData(String id) {
		MemberDTO result =null;
		try {
			String sql = "select * from member where id = ?";
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if(rs.next()){
				result = new MemberDTO();
				result.setId(rs.getString("id"));
				result.setPw(rs.getString("pw"));
				result.setName(rs.getString("name"));
				result.setId_number(rs.getString("id_number"));
				result.setAge(rs.getString("age"));
				result.setGender(rs.getString("gender"));
				result.setVegi_type(rs.getString("vegi_type"));
				result.setProfile_img(rs.getString("profile_img"));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(rs!=null)try {rs.close();} catch (Exception e) {e.printStackTrace();}
			if(pstmt!=null)try {pstmt.close();} catch (Exception e) {e.printStackTrace();}
			if(conn!=null)try {conn.close();} catch (Exception e) {e.printStackTrace();}
		}
		return result;
	}
	public int updateMember(MemberDTO dto) {
		int result = 0;
		try {
			String sql = "UPDATE MEMBER SET profile_img = ? ,pw=?,name=?,vegi_type=? WHERE id =?";
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,dto.getProfile_img());
			pstmt.setString(2,dto.getPw());
			pstmt.setString(3,dto.getName());
			pstmt.setString(4,dto.getVegi_type());
			pstmt.setString(5,dto.getId());
			result = pstmt.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(rs!=null)try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			if(pstmt!=null)try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
			if(conn!=null)try {conn.close();} catch (SQLException e) {e.printStackTrace();}
		}
		System.out.println("Update Count : "+result);
		return result;
	}
	
	public int selectAllMember() {
		int result=0;
		try {
			conn = getConnection();
			String sql = "select count(*) from member";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt(1);
				System.out.println(result);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(rs!=null)try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			if(pstmt!=null)try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
			if(conn!=null)try {conn.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return result;
	}
	
	//search overloading
	public int selectAllMember(String option,String search) {
		int result=0;
		try {
			conn = getConnection();
			String sql = "select count(*) from member where "+option+" like '%"+search+"%'";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt(1);
				System.out.println(result);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(rs!=null)try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			if(pstmt!=null)try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
			if(conn!=null)try {conn.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return result;
	}
	
	//Offence member
	public int selectAllMemberByOffence() {
		int result=0;
		try {
			conn = getConnection();
			String sql = "select count(*) from member where OFFENCE_COUNT > 0";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt(1);
				System.out.println(result);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(rs!=null)try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			if(pstmt!=null)try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
			if(conn!=null)try {conn.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return result;
	}
	
	public List getSearchMemberList(int start, int end) {
		List memberList = new ArrayList<MemberDTO>();
		try {
			conn = getConnection();
			String sql = "select id,pw,age,gender,name,regdate,offence_count,offence_url,state,r from "
					+ "(select id,pw,age,gender,name,regdate,offence_count,offence_url,state, rownum r from "
					+ "(select * from MEMBER ORDER BY OFFENCE_COUNT desc)) where r>=? and r<=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, start);
			pstmt.setInt(2, end);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				MemberDTO dto = new MemberDTO();
				dto.setId(rs.getString("id"));
				dto.setPw(rs.getString("pw"));
				dto.setAge(rs.getString("age"));
				dto.setGender(rs.getString("gender"));
				dto.setName(rs.getString("name"));
				dto.setRegdate(rs.getTimestamp("regdate"));
				dto.setOffence_count(rs.getInt("offence_count"));
				dto.setOffence_url(rs.getString("offence_url"));
				dto.setState(rs.getString("state"));				
				memberList.add(dto);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(rs!=null)try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			if(pstmt!=null)try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
			if(conn!=null)try {conn.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return memberList;
	}
	
	//search overloading
	public List getSearchMemberList(int start, int end,String option,String search) {
		List memberList = new ArrayList<MemberDTO>();
		try {
			conn = getConnection();
			String sql = "select id,pw,age,gender,name,regdate,offence_count,offence_url,state,r from "
					+ "(select id,pw,age,gender,name,regdate,offence_count,offence_url,state, rownum r from "
					+ "(select * from MEMBER where "+option+" like '%"+search+"%' ORDER BY OFFENCE_COUNT desc)) where r>=? and r<=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, start);
			pstmt.setInt(2, end);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				MemberDTO dto = new MemberDTO();
				dto.setId(rs.getString("id"));
				dto.setPw(rs.getString("pw"));
				dto.setAge(rs.getString("age"));
				dto.setGender(rs.getString("gender"));
				dto.setName(rs.getString("name"));
				dto.setRegdate(rs.getTimestamp("regdate"));
				dto.setOffence_count(rs.getInt("offence_count"));
				dto.setOffence_url(rs.getString("offence_url"));
				dto.setState(rs.getString("state"));				
				memberList.add(dto);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(rs!=null)try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			if(pstmt!=null)try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
			if(conn!=null)try {conn.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return memberList;
	}
	
	//offnectMember
	public List getSearchMemberListByOffence(int start, int end) {
		List memberList = new ArrayList<MemberDTO>();
		try {
			conn = getConnection();
			String sql = "select id,pw,age,gender,name,regdate,offence_count,offence_url,state,r from "
					+ "(select id,pw,age,gender,name,regdate,offence_count,offence_url,state, rownum r from "
					+ "(select * from MEMBER where OFFENCE_COUNT>0 ORDER BY OFFENCE_COUNT desc)) where r>=? and r<=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, start);
			pstmt.setInt(2, end);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				MemberDTO dto = new MemberDTO();
				dto.setId(rs.getString("id"));
				dto.setPw(rs.getString("pw"));
				dto.setAge(rs.getString("age"));
				dto.setGender(rs.getString("gender"));
				dto.setName(rs.getString("name"));
				dto.setRegdate(rs.getTimestamp("regdate"));
				dto.setOffence_count(rs.getInt("offence_count"));
				dto.setOffence_url(rs.getString("offence_url"));
				dto.setState(rs.getString("state"));				
				memberList.add(dto);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(rs!=null)try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			if(pstmt!=null)try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
			if(conn!=null)try {conn.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return memberList;
	}
	
	public int kickOffMember(String id) {
		int result=0;
		try {
			conn = getConnection();
			String sql ="UPDATE MEMBER SET state=? WHERE id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "강퇴");
			pstmt.setString(2, id);
			result = pstmt.executeUpdate();
			System.out.println("result"+result);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(rs!=null)try {rs.close();} catch (SQLException e) {e.printStackTrace();}
			if(pstmt!=null)try {pstmt.close();} catch (SQLException e) {e.printStackTrace();}
			if(conn!=null)try {conn.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return result;
	}
}