package pos; // 이 클래스가 포함된 패키지 위치

// DB 연동을 위해 필요한 라이브러리 import
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


//MenuDAO 클래스 
public class MenuDAO {

	// DB 작업에 필요한 JDBC 객체들 선언
	private Connection conn = null;         // DB 연결 객체
	private PreparedStatement pstmt = null; // SQL 실행 객체
	private ResultSet rs = null;            // SELECT 결과를 담는 객체

	// DB 접속을 위한 정보 (로컬 MySQL 기준)
	private String driver = "com.mysql.cj.jdbc.Driver"; // MySQL JDBC 드라이버 클래스명
	private String url = "jdbc:mysql://localhost:3306/pos_db"; // 접속할 DB URL
	private String id = "pos";        // DB 사용자명
	private String pw = "pos";        // DB 비밀번호

	// 기본 생성자
	public MenuDAO() {}

	
	//DB 연결 메서드
	public void connect() {
		try {
			Class.forName(driver); // 드라이버 로딩
			conn = DriverManager.getConnection(url, id, pw); // DB 연결 수행
		} catch (ClassNotFoundException e) {
			System.out.println("error: 드라이버 로딩 실패 - " + e);
		} catch (SQLException e) {
			System.out.println("error: " + e);
		}
	}

	
	 //DB 자원 정리 메서드
	private void close() {
		try {
			if (rs != null) rs.close();           // 결과셋 닫기
			if (pstmt != null) pstmt.close();     // SQL 실행 객체 닫기
			if (conn != null) conn.close();       // DB 연결 닫기
		} catch (SQLException e) {
			System.out.println("error: " + e);
		}
	}

	
	// 메뉴 등록 메서드
	public int addMenu(int categoryId, String name, int price) {
		int count = -1; // 결과 행 수 초기값
		this.connect(); // DB 연결

		try {
			String query = "INSERT INTO menu VALUES (NULL, ?, ?, ?)"; // menu_id는 auto_increment
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, name);        // name 설정
			pstmt.setInt(2, price);          // price 설정
			pstmt.setInt(3, categoryId);     // category_id 설정
			count = pstmt.executeUpdate();   // SQL 실행 → 행 삽입
		} catch (SQLException e) {
			System.out.println("error: " + e);
		} finally {
			this.close(); // 자원 정리
		}

		this.close(); // 중복 호출이긴 하지만 혹시 모를 누락 방지
		return count;
	}

	//메뉴 전체 조회 메서드
	public List<MenuVO> getAllMenus() {
		List<MenuVO> list = new ArrayList<>(); // 결과 저장할 리스트
		this.connect(); // DB 연결

		try {
			String query = "SELECT * FROM menu"; // 전체 메뉴 조회
			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery(); // SQL 실행 → 결과 받아오기

			// 결과 행 하나씩 MenuVO로 변환하여 리스트에 추가
			while (rs.next()) {
				list.add(new MenuVO(
					rs.getInt("menu_id"),
					rs.getInt("category_id"),
					rs.getString("name"),
					rs.getInt("price")
				));
			}
		} catch (SQLException e) {
			System.out.println("error: " + e);
		} finally {
			this.close(); // 자원 정리
		}

		this.close(); // 추가적인 안전 정리
		return list;
	}

	//특정 메뉴 1개 조회 메서드
	public MenuVO getMenu(int id) {
		MenuVO menu = null; // 결과 담을 객체
		this.connect();

		try {
			String query = "SELECT * FROM menu WHERE menu_id = ?";
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, id); // 조건 설정
			rs = pstmt.executeQuery(); // 실행

			if (rs.next()) { // 결과가 있으면 객체 생성
				menu = new MenuVO(
					rs.getInt("menu_id"),
					rs.getInt("category_id"),
					rs.getString("name"),
					rs.getInt("price")
				);
			}
		} catch (SQLException e) {
			System.out.println("error: " + e);
		} finally {
			this.close();
		}

		this.close(); // 혹시 누락 방지
		return menu;
	}

	
	 // 메뉴 수정 메서드
	public int updateMenu(int id, String name, int price) {
		int count = -1; // 수정 결과 초기값
		this.connect();

		try {
			String query = "UPDATE menu SET name = ?, price = ? WHERE menu_id = ?";
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, name);
			pstmt.setInt(2, price);
			pstmt.setInt(3, id);
			count = pstmt.executeUpdate(); // SQL 실행 → 수정
		} catch (SQLException e) {
			System.out.println("error: " + e);
		} finally {
			this.close();
		}

		this.close(); // 중복이지만 안정성 고려
		return count;
	}

	
	 //메뉴 삭제 메서드
	public int deleteMenu(int id) {
		int count = -1; // 삭제 결과 초기값
		this.connect();

		try {
			String query = "DELETE FROM menu WHERE menu_id = ?";
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, id);
			count = pstmt.executeUpdate(); // SQL 실행 → 삭제
		} catch (SQLException e) {
			System.out.println("error: " + e);
		} finally {
			this.close();
		}

		this.close(); // 안정성 고려한 정리
		return count;
	}
}
