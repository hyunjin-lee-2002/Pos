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

	private String driver = "com.mysql.cj.jdbc.Driver";
	private String db_url = "jdbc:mysql://localhost:3306/pos_db";
	private String user = "pos";
	private String pass = "pos";

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
		} catch (SQLException ignored) {}

	}

	// 카테고리 등록 : 새로운 카테고리를 데이터베이스에 추가
	public int categoryInsert(String emoji
							 ,String name
							 ,String explanation
							 ) throws SQLException {

		int count = -1;	// 결과값 초기화

		this.connect();	// DB 연결
		
		try {
			// SQL 쿼리 작성: category 테이블에 새로운 카테고리 추가
			String query = "";
				   query += " insert into category ";
				   query += " values(null, ?, ?, ?) ";
			pstmt = conn.prepareStatement(query);
			
			// 쿼리에 파라미터(값) 입력
			pstmt.setString(1, emoji);
			pstmt.setString(2, name);
			pstmt.setString(3, explanation);
			
			// 쿼리 실행 (DB에 데이터 저장)
			count = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("error:" + e);
		
		} finally {
			this.close();
		}
		
		return count;
	}

	// 카테고리 조회 (전체) : 데이터베이스에서 모든 카테고리를 가져와서 리스트로 반환
	public List<CategoryVO> getAllCategories() throws SQLException {

		// 결과를 저장할 리스트
		List<CategoryVO> cList = new ArrayList<>();
		
		this.connect(); 	// DB 연결

		try {
			// SQL 쿼리 작성: category 테이블의 모든 카테고리 조회
			String query = " select * "
						 + " from category ";
					stmt = conn.createStatement();
					  rs = stmt.executeQuery(query);
					  
			// 결과를 한 줄씩 읽어서 CategoryVO 객체로 만들어 리스트에 추가
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
		
		} finally {
			this.close();
		}
		
		return cList;
	}

	// 카테고리 조회 (단일) : 카테고리 ID로 하나의 카테고리를 조회
	public CategoryVO getCategory(int categoryId) throws SQLException {

		CategoryVO vo = null;	// 결과값 초기화
		
		this.connect();		// DB 연결

		try {
			// SQL 쿼리 작성: category 테이블에서 특정 카테고리 조회
			String query = " select * from category ";
				   query += " where category_id = ? ";
			pstmt = conn.prepareStatement(query);

			// 파라미터 입력
			pstmt.setInt(1, categoryId);

			// 쿼리 실행 및 결과 받기
			rs = pstmt.executeQuery();

			// 결과가 있으면 CategoryVO 객체 생성
			if (rs.next()) {
				vo = new CategoryVO(rs.getInt("category_id")
								   ,rs.getString("emoji")
								   ,rs.getString("name")
								   ,rs.getString("explanation")
								   );
			}
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		
		} finally {
			this.close();
		}
		
		return vo;
	}

	// 카테고리 수정
	public int updateCategory(int categoryId
							 ,String emoji
							 ,String name
							 ,String explanation) throws SQLException {

		int count = -1;		// 결과값 초기화

		this.connect();		// DB 연결

		try {
			// SQL 쿼리 작성: category 테이블에서 특정 카테고리 수정
			String query = " update category ";
				   query += " set emoji=?, ";
				   query += " name=?, ";
				   query += " explanation=? ";
				   query += " where category_id=? ";
			pstmt = conn.prepareStatement(query);
			
			// 파라미터 입력
			pstmt.setString(1, emoji);
			pstmt.setString(2, name);
			pstmt.setString(3, explanation);
			pstmt.setInt(4, categoryId);
			
			// 쿼리 실행 (DB 데이터 수정)
			count = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("error:" + e);
		
		} finally {
			this.close();
		}
		
		return count;	// 수정된 행의 개수 반환
	}

	// 카테고리 삭제
	public int deleteCategory(int categoryId) throws SQLException {

		int count = -1;	// 결과값 초기화
		
		this.connect();	// DB 연결

		try {
			// SQL 쿼리 작성: category 테이블에서 특정 카테고리 삭제
			String query = 	" delete "
						 + 	" from category ";
				   query += " where category_id = ? ";
			pstmt = conn.prepareStatement(query);

			// 파라미터 입력
			pstmt.setInt(1, categoryId);
			
			// 쿼리 실행 (DB 데이터 삭제)
			count = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("error:" + e);
		
		} finally {
			this.close();
		}
		
		return count;
	}
}
