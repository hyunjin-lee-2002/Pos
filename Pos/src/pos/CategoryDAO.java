package pos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

//카테고리 데이터베이스 접근을 담당하는 클래스
public class CategoryDAO {
	// 필드
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private Statement stmt = null; // getAllCategories에서 Statement 사용
	private ResultSet rs = null;

	private static final String driver = "com.mysql.cj.jdbc.Driver";
	static final String db_url = "jdbc:mysql://localhost:3306/pos_db";
	static final String user = "pos";
	static final String pass = "pos";

	private void connect() {

		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(db_url, user, pass);

		} catch (ClassNotFoundException e) {
			System.out.println("error: 드라이버 로딩 실패 - " + e);
		
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

	}

	// 자원 정리 메소드
	private void close() {

		// 5. 자원정리
		try {
			if (rs != null)
				rs.close();
		} catch (SQLException ignored) {
		}

		try {
			if (stmt != null)
				stmt.close();
		} catch (SQLException ignored) {
		}

		try {
			if (pstmt != null)
				pstmt.close();
		} catch (SQLException ignored) {
		}

		try {
			if (conn != null)
				conn.close();
		} catch (SQLException ignored) {
		}

	}

	// 카테고리 등록
	public int categoryInsert(String emoji
							 ,String name
							 ,String explanation
							 ) throws SQLException {

		int count = -1;

		this.connect();
		
		try {
			String query = "";
				   query += " insert into category ";
				   query += " values(null, ?, ?, ?) ";
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, emoji);
			pstmt.setString(2, name);
			pstmt.setString(3, explanation);

			count = pstmt.executeUpdate();

			// 4.결과처리
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		this.close();
		return count;
	}

	// 카테고리 조회 (전체)
	public List<CategoryVO> getAllCategories() throws SQLException {

		List<CategoryVO> cList = new ArrayList<>();

		this.connect();

		try {
			String query = " select * "
						 + " from category ";
					stmt = conn.createStatement();
					  rs = stmt.executeQuery(query);

			while (rs.next()) {
				int categoryId = rs.getInt("category_id");
				String emoji = rs.getString("emoji");
				String name = rs.getString("name");
				String explanation = rs.getString("explanation");

				CategoryVO categoryVo = new CategoryVO(categoryId, emoji, name, explanation);

				cList.add(categoryVo);
			}
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		this.close();
		return cList;
	}

	// 카테고리 조회 (단일)
	public CategoryVO getCategory(int categoryId) throws SQLException {

		CategoryVO vo = null;
		
		this.connect();

		try {
			String query = " select * from category ";
				   query += " where category_id = ? ";

			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, categoryId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				vo = new CategoryVO(rs.getInt("category_id")
								   ,rs.getString("emoji")
								   ,rs.getString("name")
								   ,rs.getString("explanation")
								   );
			}
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		this.close();
		return vo;
	}

	// 카테고리 수정
	public int updateCategory(int categoryId
							 ,String emoji
							 ,String name
							 ,String explanation) throws SQLException {

		int count = -1;

		this.connect();

		try {
			String query = " update category ";
				   query += " set emoji=?, ";
				   query += " name=?, ";
				   query += " explanation=? ";
				   query += " where category_id=? ";

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, emoji);
			pstmt.setString(2, name);
			pstmt.setString(3, explanation);
			pstmt.setInt(4, categoryId);
			count = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		this.close();
		return count;
	}

	// 카테고리 삭제
	public int deleteCategory(int categoryId) throws SQLException {

		int count = -1;
		
		this.connect();

		try {
			String query = " delete from category ";
				   query += " where category_id = ? ";

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, categoryId);
			count = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		this.close();
		return count;
	}
}