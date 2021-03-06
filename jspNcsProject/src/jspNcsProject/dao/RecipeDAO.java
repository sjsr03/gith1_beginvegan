package jspNcsProject.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import jspNcsProject.dto.ProductDTO;
import jspNcsProject.dto.RecipeDTO;

public class RecipeDAO {
	private Connection conn  = null;
	private ResultSet rs = null;
	private PreparedStatement pstmt = null;
	
	private static RecipeDAO instance = new RecipeDAO();
	public static RecipeDAO getInstance() {
		return instance;
	}
	
	public Connection getConnection() throws Exception{
		Context ctx = (Context)new InitialContext();
		Context env  = (Context)ctx.lookup("java:comp/env");
		DataSource ds = (DataSource)env.lookup("jdbc/orcl");
		return ds.getConnection();	
	}
	
	//recipe_board 글 수 가져오기
	public int getRecipeCount() {
		int count = 0;
		try {
			conn= getConnection();
			String sql = "SELECT count(*) FROM recipe_board";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				count = rs.getInt(1);
			} 	
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs!=null)try { rs.close();}catch(Exception e) {e.printStackTrace();}
			if(pstmt!=null)try { pstmt.close();}catch(Exception e) {e.printStackTrace();}
			if(conn!=null)try { conn.close();}catch(Exception e) {e.printStackTrace();}		
		}
		return count;
	}
	
	//recipeList 최신순으로 가져오기
	public List seletAllReceipe(int startrow, int endrow, String mode) {
		ArrayList recipeList = null;
		try {
			conn= getConnection();
			String sql = "";
			if(mode.equals("num")) {
				sql = "select b.* from(select rownum r, a.* "
					+ "from(select * from recipe_board order by num desc)a order by num desc)b where r >= ? and r<=?";
			}else if(mode.equals("rating")) {//mode가 rating
				sql="select b.* from(select rownum r, a.* "
						+ "from(select * from recipe_board order by rating desc, num desc)a order by rating desc,num desc)b where r>=? and r<=?";			
			}
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, startrow);
			pstmt.setInt(2, endrow);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				recipeList = new ArrayList();
				do{
					RecipeDTO recipe = new RecipeDTO();
					recipe.setNum(rs.getInt("num"));
					recipe.setRecipeName(rs.getString("recipe_name"));
					recipe.setThumbnail(rs.getString("thumbnail"));
					recipe.setWriter(rs.getString("writer"));	
					recipe.setRating(rs.getDouble("rating"));
					recipeList.add(recipe);				
				}while(rs.next());			
			}
			
		} catch (Exception e) {
			e.printStackTrace();
					
		} finally {
			if(rs!=null)try { rs.close();}catch(Exception e) {e.printStackTrace();}
			if(pstmt!=null)try { pstmt.close();}catch(Exception e) {e.printStackTrace();}
			if(conn!=null)try { conn.close();}catch(Exception e) {e.printStackTrace();}
			
		}
		return recipeList;
		
	} 
	
	
	// 레시피내용만 가져오는 메서드
	public RecipeDTO selectRecipeBoard(int num) {
		RecipeDTO recipeBoard = null;
		try {
			conn = getConnection();
			String sql = "select * from recipe_board where num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				recipeBoard = new RecipeDTO();
				recipeBoard.setNum(Integer.parseInt(rs.getString("num")));
				recipeBoard.setRecipeStep(Integer.parseInt(rs.getString("recipe_step")));
				recipeBoard.setRecipeName(rs.getString("recipe_name"));
				recipeBoard.setThumbnail(rs.getString("thumbnail"));
				recipeBoard.setWriter(rs.getString("writer"));
				recipeBoard.setReg(rs.getTimestamp("reg"));
				recipeBoard.setVegiType(rs.getString("vegi_type"));
				recipeBoard.setCookingTime(Integer.parseInt(rs.getString("cooking_time")));
				recipeBoard.setDifficulty(rs.getString("difficulty"));
				recipeBoard.setCal(Integer.parseInt(rs.getString("cal")));
				recipeBoard.setQuantity(Integer.parseInt(rs.getString("quantity")));
				recipeBoard.setIngredients(rs.getString("ingredients"));
				recipeBoard.setRating(Double.parseDouble(rs.getString("rating")));
				recipeBoard.setTag(rs.getString("tag"));
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(rs != null)try {rs.close();}catch(Exception e) {e.printStackTrace();}
			if(pstmt != null)try {pstmt.close();}catch(Exception e) {e.printStackTrace();}
			if(conn != null)try {conn.close();}catch(Exception e) {e.printStackTrace();}
		}
		return recipeBoard;
	}
	
	public int getCountSearchRecipeList(String whereQuery) {
		int count = 0;
		try {
			conn = getConnection();
			String sql = "select count(*) from recipe_board "+whereQuery;
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				count = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null)try {rs.close();}catch(Exception e) {e.printStackTrace();}
			if(pstmt != null)try {pstmt.close();}catch(Exception e) {e.printStackTrace();}
			if(conn != null)try {conn.close();}catch(Exception e) {e.printStackTrace();}
		}
		
		
		return count;
	}
	
	public List searchRecipeList(int startrow, int endrow, String whereQuery, String mode) {
		//mode는 최신순인 경우 num, 평점순인경우 rating
		ArrayList searchRecipeList =null;
		try {
			String sql = "";
			conn = getConnection();
			if(mode.equals("num")) {
				sql = "select b.* from(select rownum r, a.* "
					+ "from(select * from recipe_board "+ whereQuery +" order by num desc)a order by num desc)b where r >= ? and r<=?";
			}else if(mode.equals("rating")) {//mode가 rating
				sql="select b.* from(select rownum r, a.* "
						+ "from(select * from recipe_board "+ whereQuery +" order by rating desc, num desc)a order by rating desc,num desc)b where r>=? and r<=?";			
			}
						
			//String sql ="select * from recipe_board "+ whereQuery + " order by "+ mode +" desc";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, startrow);
			pstmt.setInt(2, endrow);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				searchRecipeList = new ArrayList();
				do {
					RecipeDTO recipe = new RecipeDTO();
					recipe.setNum(rs.getInt("num"));
					recipe.setRecipeName(rs.getString("recipe_name"));
					recipe.setWriter(rs.getString("writer"));
					recipe.setRating(rs.getInt("rating"));
					recipe.setVegiType(rs.getString("vegi_type"));
					recipe.setDifficulty(rs.getString("difficulty"));
					recipe.setCookingTime(rs.getInt("cooking_time"));
					recipe.setQuantity(rs.getInt("quantity"));
					recipe.setCal(rs.getInt("cal"));
					recipe.setIngredients(rs.getString("ingredients"));
					recipe.setThumbnail(rs.getString("thumbNail"));					
					searchRecipeList.add(recipe);					
				}while(rs.next());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null)try {rs.close();}catch(Exception e) {e.printStackTrace();}
			if(pstmt != null)try {pstmt.close();}catch(Exception e) {e.printStackTrace();}
			if(conn != null)try {conn.close();}catch(Exception e) {e.printStackTrace();}
		}
				
		return searchRecipeList;		
	}
	
	
	//레시피 삭제
	public void deleteRecipeBoard(int num) {
		try {
			
			//레시피 세부내용 댓글 삭제
			RecipeContentCommentDAO rccDAO = RecipeContentCommentDAO.getInstance();
			rccDAO.deleteRecipeContentCommentAll(num);
			
			//레시피 세부내용 삭제
			RecipeContentDAO rcDAO = RecipeContentDAO.getInstance();
			rcDAO.deleteRecipeContent(num);
			
			//레시피 댓글 내용 삭제
			RecipeCommentDAO rcmDAO = RecipeCommentDAO.getInstance();
			rcmDAO.deleteRecipeCommentAll(num);
			
			//레시피 평점 삭제
			RatingDAO rtDAO = RatingDAO.getInstance();
			rtDAO.deleteRatingAll(num);
			
			//레시피 스크랩 정보 삭제
			ScrapDAO sDAO = ScrapDAO.getInstance();
			sDAO.deleteScrapAllByNum(num);
			
			//레시피 태그 삭제
			conn = getConnection();
			String tag = instance.selectRecipeBoard(num).getTag();
			
			if(tag != null && !tag.equals("")) {
				//콤마 기준으로 나누기
				String[] tagSplit = tag.split(",");
				
				for(int i = 0; i<tagSplit.length; i++) {
					tagSplit[i] = tagSplit[i].trim(); //양쪽 공백 없애고 
					//tag table 태그 삭제
					TagDAO daoo = TagDAO.getInstance();
					daoo.deleteTag(tagSplit[i]);
				}
			}
			
			
			//레시피 정보 삭제
			conn = getConnection();
			String sql = "delete from recipe_board where num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			pstmt.executeUpdate();
					
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt != null)try {pstmt.close();}catch(Exception e) {e.printStackTrace();}
			if(conn != null)try {conn.close();}catch(Exception e) {e.printStackTrace();}
		}
	}
	
	
	
	//id 받고 활동명 반환
	public String selectNameById(String id) {
		String name = null;
		
		try {
			
			conn = getConnection();
			
			String sql = "select name from member where id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);

			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				name = rs.getString(1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null)try {rs.close();}catch(Exception e) {e.printStackTrace();}
			if(pstmt != null)try {pstmt.close();}catch(Exception e) {e.printStackTrace();}
			if(conn != null)try {conn.close();}catch(Exception e) {e.printStackTrace();}
		}
		
		return name;
	}
	//활동명으로 아이디찾기
	public String selectIdByName(String name) {
		String id = null;
		
		try {
			
			conn = getConnection();
			
			String sql = "select id from member where name=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				id = rs.getString(1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null)try {rs.close();}catch(Exception e) {e.printStackTrace();}
			if(pstmt != null)try {pstmt.close();}catch(Exception e) {e.printStackTrace();}
			if(conn != null)try {conn.close();}catch(Exception e) {e.printStackTrace();}
		}
		
		return id;
	}
	
	//id 받고 이미지 반환
	public String selectImgById(String id) {
		String img = null;
		
		try {
			
			conn = getConnection();
			
			String sql = "select profile_img from member where id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				img = rs.getString(1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null)try {rs.close();}catch(Exception e) {e.printStackTrace();}
			if(pstmt != null)try {pstmt.close();}catch(Exception e) {e.printStackTrace();}
			if(conn != null)try {conn.close();}catch(Exception e) {e.printStackTrace();}
		}
		
		return img;
	}
	
	
	//태그 잘라서 배열로 리턴
	public String[] selectTagSplit(int num) {
		String[] tags = null;
		
		try {
			
			conn = getConnection();
			
			String sql = "select tag from recipe_board where num=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				tags = rs.getString(1).split(",");
			}
			
					
			
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null)try {rs.close();}catch(Exception e) {e.printStackTrace();}
			if(pstmt != null)try {pstmt.close();}catch(Exception e) {e.printStackTrace();}
			if(conn != null)try {conn.close();}catch(Exception e) {e.printStackTrace();}
		}
		
		return tags;
	}
	
	
	
	//재료 가져오기
	public HashMap<String, String> selectIngredients(int num) {
		HashMap<String, String> ingre = null;
		String ingredients = null;
		
		try {
			conn = getConnection();
			
			String sql = "select ingredients from recipe_board where num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				ingredients = rs.getString(1);
			}
			
			ingre = new HashMap<String, String>();
			
			//콤마로 먼저 나누기
			String[] ingre1 = ingredients.split(",");
			//콜론으로 나누기
			for(int i = 1; i < ingre1.length; i++) {
				String[] ingre2 = ingre1[i].split(":");
				ingre.put(ingre2[0], ingre2[1]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null)try {rs.close();}catch(Exception e) {e.printStackTrace();}
			if(pstmt != null)try {pstmt.close();}catch(Exception e) {e.printStackTrace();}
			if(conn != null)try {conn.close();}catch(Exception e) {e.printStackTrace();}
		}
		
		return ingre;
	}
	
	//재료 이름으로 가장 추천수 높은 제품 가져오기
	public ProductDTO selectProductByIngredient(String ingre) {
		ProductDTO dto = null;
		
		String search = "";
		if(ingre.length() < 2) {
			search = "name like '% " + ingre + " %'";
		} else {
			search = "name like '%" + ingre + "%'";
		}
		
		try {
			
			conn = getConnection();
			
			String sql = "select * from product where "+search+" and re_level=0 order by recommend desc";
			pstmt = conn.prepareStatement(sql);
			
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new ProductDTO();
				
				dto.setNum(rs.getInt("num"));
				dto.setName(rs.getString("name"));
				dto.setProduct_img(rs.getString("product_img"));
				dto.setRecommend(rs.getInt("recommend"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null)try {rs.close();}catch(Exception e) {e.printStackTrace();}
			if(pstmt != null)try {pstmt.close();}catch(Exception e) {e.printStackTrace();}
			if(conn != null)try {conn.close();}catch(Exception e) {e.printStackTrace();}
		}
		
		return dto;
	}
	
	// 작성자 아이디로 레시피 가져오기 (범위만큼)
		public List selectMyRecipe(int start, int end, String writer) {
			ArrayList myRecipeList = null;
			try {
				conn = getConnection();
				String sql = "SELECT rb.* FROM(SELECT rownum AS r, rb.* FROM (SELECT rb.* FROM RECIPE_BOARD rb WHERE writer = ? ORDER BY rb.reg desc) rb)rb WHERE r >= ? AND r <= ?";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, writer);
				pstmt.setInt(2, start);
				pstmt.setInt(3, end);
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					myRecipeList = new ArrayList();
					do {
						RecipeDTO recipe = new RecipeDTO();
						recipe.setNum(rs.getInt("num"));
						recipe.setRecipeName(rs.getString("recipe_name"));
						recipe.setWriter(rs.getString("writer"));
						recipe.setRating(rs.getInt("rating"));
						recipe.setVegiType(rs.getString("vegi_type"));
						recipe.setDifficulty(rs.getString("difficulty"));
						recipe.setCookingTime(rs.getInt("cooking_time"));
						recipe.setQuantity(rs.getInt("quantity"));
						recipe.setCal(rs.getInt("cal"));
						recipe.setIngredients(rs.getString("ingredients"));
						recipe.setThumbnail(rs.getString("thumbNail"));					
						myRecipeList.add(recipe);					
					}while(rs.next());
				}
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				if(rs != null)try {rs.close();}catch(Exception e) {e.printStackTrace();}
				if(pstmt != null)try {pstmt.close();}catch(Exception e) {e.printStackTrace();}
				if(conn != null)try {conn.close();}catch(Exception e) {e.printStackTrace();}
			}
			return myRecipeList;
		}
		
		// 작성자 아이디로 레시피 글 수 가져오기
		
		public int getMyRecipeCount(String writer) {
			int count = 0;
			try {
				conn= getConnection();
				String sql = "SELECT count(*) FROM recipe_board where writer=?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, writer);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					count = rs.getInt(1);
				} 	
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(rs!=null)try { rs.close();}catch(Exception e) {e.printStackTrace();}
				if(pstmt!=null)try { pstmt.close();}catch(Exception e) {e.printStackTrace();}
				if(conn!=null)try { conn.close();}catch(Exception e) {e.printStackTrace();}		
			}
			return count;
		}
	
}