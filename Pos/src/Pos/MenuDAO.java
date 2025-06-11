package Pos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MenuDAO {

	// ===========================
	// [필드: DB 관련 객체 선언]
	// ===========================

	private Connection conn = null;        	// DB에 연결할 객체
	private PreparedStatement pstmt = null; // SQL 실행 준비 객체 (바인딩 기능 포함)
	private ResultSet rs = null;          	// SELECT 결과 저장 객체

	// ===========================
	// [DB 접속 정보]
	// ===========================
	private String driver = "com.mysql.cj.jdbc.Driver"; 		// MySQL 드라이버 클래스
	private String url = "jdbc:mysql://localhost:3306/pos_db"; 	// 연결할 DB의 주소
	private String id = "pos"; 									// DB 사용자명
	private String pw = "pos"; 									// DB 비밀번호

	// ===========================
	// [기본 생성자]
	// ===========================
	public MenuDAO() {
		// 생성 시 특별한 초기화는 없음
	}

	// ===========================
	// [DB 연결 수행 메서드]
	// ===========================
	public void connect() {
		try {
			// 1단계: JDBC 드라이버 로딩
			Class.forName(driver);

			// 2단계: DB에 연결 시도
			conn = DriverManager.getConnection(url, id, pw);
		} catch (ClassNotFoundException e) {
			System.out.println("error: 드라이버 로딩 실패 - " + e);
		} catch (SQLException e) {
			System.out.println("error: " + e);
		}
	}

	// ===========================
	// [DB 자원 정리 메서드]
	// ===========================
	private void close() {
		try {
			if (rs != null) rs.close();         // SELECT 결과 종료
			if (pstmt != null) pstmt.close();   // SQL 실행 객체 종료
			if (conn != null) conn.close();     // DB 연결 종료
		} catch (SQLException e) {
			System.out.println("error: " + e);
		}
	}

	// ===========================
	// [메뉴 등록 메서드]
	// ===========================
	public int addMenu(int categoryId, String name, int price) {
		int count = -1; 			// 영향을 받은 행 수 (결과값 저장용)
		this.connect(); 			// DB 연결

		try {
			// menu_id는 AUTO_INCREMENT라 null 넣음
			String query = "INSERT INTO menu VALUES (NULL, ?, ?, ?)";

			pstmt = conn.prepareStatement(query); // SQL 준비

			// ? 자리에 값 바인딩
			pstmt.setString(1, name);			 // 1번 ? → 메뉴 이름
			pstmt.setInt(2, price);				 // 2번 ? → 가격
			pstmt.setInt(3, categoryId); 		 // 3번 ? → 카테고리 번호

			count = pstmt.executeUpdate(); 		 // SQL 실행 후 처리된 행 수 반환
		} catch (SQLException e) {
			System.out.println("error: " + e);
		} finally {
			this.close(); 	// 자원 정리
		}
		return count; 		// 등록된 행 수 반환
	}

	// ===========================
	// [전체 메뉴 조회 메서드]
	// ===========================
	public List<MenuVO> getAllMenus() {
		List<MenuVO> list = new ArrayList<>(); // 결과 저장할 리스트 생성
		this.connect(); // DB 연결

		try {
			String query = "SELECT * FROM menu"; // 전체 메뉴 SELECT
			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery(); // 쿼리 실행 → 결과는 rs에 저장

			// 결과 집합 반복
			while (rs.next()) {
				// 각각의 행을 MenuVO로 변환해서 리스트에 저장
				list.add(new MenuVO(
					rs.getInt("menu_id"),		// 메뉴 ID
					rs.getInt("category_id"),	// 카테고리 ID
					rs.getString("name"),		// 이름
					rs.getInt("price")			// 가격
				));
			}
		} catch (SQLException e) {
			System.out.println("error: " + e);
		} finally {
			this.close(); // 자원 정리
		}
		return list; // 메뉴 리스트 반환
	}

	// ===========================
	// [특정 메뉴 1개 조회 메서드]
	// ===========================
	public MenuVO getMenu(int id) {
		MenuVO menu = null; 		// 결과 저장용 객체
		this.connect(); 			// DB 연결

		try {
			String query = "SELECT * FROM menu WHERE menu_id = ?"; // 조건부 조회
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, id); 		// ?에 메뉴 ID 바인딩
			rs = pstmt.executeQuery(); // 쿼리 실행

			if (rs.next()) {
				// 결과가 있으면 MenuVO 객체 생성
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
		return menu; // 조회 결과 반환 (없으면 null)
	}

	// ===========================
	// [메뉴 수정 메서드]
	// ===========================
	public int updateMenu(int id, String name, int price) {
		int count = -1; // 영향받은 행 수
		this.connect(); // DB 연결

		try {
			String query = "UPDATE menu SET name = ?, price = ? WHERE menu_id = ?";
			pstmt = conn.prepareStatement(query);

			// ?에 값 바인딩
			pstmt.setString(1, name); 	// 수정할 이름
			pstmt.setInt(2, price); 	// 수정할 가격
			pstmt.setInt(3, id); 		// 수정 대상 menu_id

			count = pstmt.executeUpdate(); // 실행 후 영향받은 행 수 저장
		} catch (SQLException e) {
			System.out.println("error: " + e);
		} finally {
			this.close(); // 자원 정리
		}
		return count;
	}

	// ===========================
	// [메뉴 삭제 메서드]
	// ===========================
	public int deleteMenu(int id) {
		int count = -1; // 영향받은 행 수 저장용
		this.connect(); // DB 연결

		try {
			String query = "DELETE FROM menu WHERE menu_id = ?"; // 삭제 쿼리 준비
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, id); // 삭제할 메뉴 번호 바인딩

			count = pstmt.executeUpdate(); // 삭제 실행
		} catch (SQLException e) {
			System.out.println("error: " + e);
		} finally {
			this.close(); // 자원 정리
		}
		return count;
	}
}
