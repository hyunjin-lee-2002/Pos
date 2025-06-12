package pos;

import java.sql.Connection;             // DB 연결을 위한 클래스
import java.sql.DriverManager;         // DB 접속 도우미 클래스
import java.sql.PreparedStatement;     // SQL 실행 준비 클래스
import java.sql.ResultSet;             // SELECT 결과 저장 클래스
import java.sql.SQLException;          // SQL 예외 처리 클래스
import java.util.ArrayList;            // 리스트 자료구조
import java.util.List;                 // 리스트 인터페이스

// 메뉴 데이터베이스 접근을 담당하는 클래스
public class MenuDAO {

	private Connection conn = null;        	// DB 연결 객체
	private PreparedStatement pstmt = null; // SQL 실행 객체
	private ResultSet rs = null;            // SELECT 결과 저장 객체

	private final String driver = "com.mysql.cj.jdbc.Driver";   // MySQL JDBC 드라이버
	private final String url = "jdbc:mysql://localhost:3306/pos_db"; // DB URL
	private final String id = "pos";       // DB 아이디
	private final String pw = "pos";       // DB 비밀번호

	public MenuDAO() {} // 기본 생성자

	// DB 연결 메서드
	public void connect() {
		try {
			Class.forName(driver); // 드라이버 로딩
			conn = DriverManager.getConnection(url, id, pw); // DB 연결 시도
		} catch (ClassNotFoundException e) {
			System.out.println("error: 드라이버 로딩 실패 - " + e); // 드라이버 오류
		} catch (SQLException e) {
			System.out.println("error: " + e); // 연결 오류
		}
	}

	// 자원 정리 메서드 (Connection, Statement, ResultSet 정리)
	private void close() {
		try {
			if (rs != null) rs.close();        // 결과셋 닫기
			if (pstmt != null) pstmt.close();  // 쿼리 객체 닫기
			if (conn != null) conn.close();    // 연결 닫기
		} catch (SQLException e) {
			System.out.println("error: " + e); // 정리 중 오류
		}
	}

	// 메뉴 등록
	public int addMenu(int categoryId, String name, int price) {
		int count = -1; // 실행 결과 저장 변수 초기화
		this.connect(); // DB 연결

		try {
			String query = "INSERT INTO menu VALUES (NULL, ?, ?, ?)"; // 메뉴 추가 쿼리
			pstmt = conn.prepareStatement(query); // 쿼리 준비

			pstmt.setString(1, name);      // 메뉴 이름 설정
			pstmt.setInt(2, price);        // 메뉴 가격 설정
			pstmt.setInt(3, categoryId);   // 카테고리 ID 설정

			count = pstmt.executeUpdate(); // INSERT 실행 후 결과 저장
		} catch (SQLException e) {
			System.out.println("error: " + e); // 오류 출력
		} finally {
			this.close(); // 자원 정리
		}
		return count; // 등록된 행 수 반환
	}

	// 메뉴 전체 조회
	public List<MenuVO> getAllMenus() {
		List<MenuVO> list = new ArrayList<>(); // 메뉴 목록 저장 리스트
		this.connect(); // DB 연결

		try {
			String query = "SELECT * FROM menu"; // 모든 메뉴 조회 쿼리
			pstmt = conn.prepareStatement(query); // 쿼리 준비
			rs = pstmt.executeQuery(); // 쿼리 실행

			while (rs.next()) { // 결과 한 줄씩 반복
				list.add(new MenuVO(
					rs.getInt("menu_id"),        // 메뉴 ID
					rs.getInt("category_id"),    // 카테고리 ID
					rs.getString("name"),        // 이름
					rs.getInt("price")           // 가격
				));
			}
		} catch (SQLException e) {
			System.out.println("error: " + e); // 오류 출력
		} finally {
			this.close(); // 자원 정리
		}
		return list; // 전체 메뉴 리스트 반환
	}

	// 메뉴 단일 조회
	public MenuVO getMenu(int id) {
		MenuVO menu = null; // 반환할 메뉴 객체 초기화
		this.connect(); // DB 연결

		try {
			String query = "SELECT * FROM menu WHERE menu_id = ?"; // 특정 메뉴 조회
			pstmt = conn.prepareStatement(query); // 쿼리 준비
			pstmt.setInt(1, id); // 메뉴 ID 세팅
			rs = pstmt.executeQuery(); // 실행

			if (rs.next()) { // 결과 있으면 객체 생성
				menu = new MenuVO(
					rs.getInt("menu_id"),
					rs.getInt("category_id"),
					rs.getString("name"),
					rs.getInt("price")
				);
			}
		} catch (SQLException e) {
			System.out.println("error: " + e); // 오류 출력
		} finally {
			this.close(); // 자원 정리
		}
		return menu; // 조회된 메뉴 반환
	}

	// 메뉴 수정
	public int updateMenu(int id, String name, int price) {
		int count = -1; // 결과 초기화
		this.connect(); // DB 연결

		try {
			String query = "UPDATE menu SET name = ?, price = ? WHERE menu_id = ?"; // 수정 쿼리
			pstmt = conn.prepareStatement(query); // 쿼리 준비
			pstmt.setString(1, name);     // 이름 수정
			pstmt.setInt(2, price);       // 가격 수정
			pstmt.setInt(3, id);          // 대상 메뉴 ID
			count = pstmt.executeUpdate(); // 실행
		} catch (SQLException e) {
			System.out.println("error: " + e); // 오류 출력
		} finally {
			this.close(); // 자원 정리
		}
		return count; // 수정된 행 수 반환
	}

	// 메뉴 삭제
	public int deleteMenu(int id) {
		int count = -1; // 결과 초기화
		this.connect(); // DB 연결

		try {
			String query = "DELETE FROM menu WHERE menu_id = ?"; // 삭제 쿼리
			pstmt = conn.prepareStatement(query); // 쿼리 준비
			pstmt.setInt(1, id); // 삭제할 메뉴 ID
			count = pstmt.executeUpdate(); // 실행
		} catch (SQLException e) {
			System.out.println("error: " + e); // 오류 출력
		} finally {
			this.close(); // 자원 정리
		}
		return count; // 삭제된 행 수 반환
	}
}
