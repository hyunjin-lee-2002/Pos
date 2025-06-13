package pos.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pos.vo.MenuVO;

//메뉴 데이터베이스 접근을 담당하는 클래스
public class MenuDAO {

	// 필드
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	private String driver = "com.mysql.cj.jdbc.Driver";
	private String url = "jdbc:mysql://localhost:3306/pos_db";
	private String id = "pos";
	private String pw = "pos";

	// 생성자
	public MenuDAO() {}

	// DB 연결 메서드 : 데이터베이스에 접속하여 conn(연결 객체)를 생성
	public void connect() {
		try {
			Class.forName(driver); // 드라이버 로딩
			conn = DriverManager.getConnection(url, id, pw); // 실제 연결 시도
		
		} catch (ClassNotFoundException e) {
			System.out.println("error: 드라이버 로딩 실패 - " + e);
		
		} catch (SQLException e) {
			System.out.println("error: " + e);
		}
	}

	// 자원 정리 : 사용한 자원(ResultSet, PreparedStatement, Connection)을 정리
	private void close() {
		try {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			System.out.println("error: " + e);
		}
	}

	// 메뉴 등록 : 새로운 메뉴를 데이터베이스에 추가
	public int addMenu(int categoryId, String name, int price) {
		
		int count = -1;	// 결과값 초기화
		
		this.connect();	// DB 연결
		
		try {
			// SQL 쿼리 작성: menu 테이블에 새로운 메뉴를 추가
			String query = "INSERT INTO menu "
						 + "VALUES (NULL, ?, ?, ?)";
			pstmt = conn.prepareStatement(query);
			
			// 쿼리에 파라미터(값) 입력
			pstmt.setString(1, name);
			pstmt.setInt(2, price);
			pstmt.setInt(3, categoryId);
			
			// 쿼리 실행 (DB에 데이터 저장)
			count = pstmt.executeUpdate();
		
		} catch (SQLException e) {
			System.out.println("error: " + e);
		
		} finally {
			this.close();
		}
		
		return count;	// 등록된 행의 개수 반환
	}

	// 메뉴 전체 조회 : 데이터베이스에서 모든 메뉴를 가져와서 리스트로 반환
	public List<MenuVO> getAllMenus() {
		
		List<MenuVO> list = new ArrayList<>();	// 결과를 저장할 리스트
		
		this.connect();	 // DB 연결
		
		try {
			// SQL 쿼리 작성: menu 테이블의 모든 메뉴 조회
			String query = " SELECT * "
						 + " FROM menu ";
			pstmt = conn.prepareStatement(query);
			// 쿼리 실행 및 결과 받기
			rs = pstmt.executeQuery();
			
			// 결과를 한 줄씩 읽어서 MenuVO 객체로 만들어 리스트에 추가
			while (rs.next()) {
				list.add(new MenuVO(rs.getInt("menu_id")
								   ,rs.getInt("category_id")
								   ,rs.getString("name")
								   ,rs.getInt("price")
								   )
						);
			}
		
		} catch (SQLException e) {
			System.out.println("error: " + e);
		
		} finally {
			this.close();
		}

		return list;
	}

	// 메뉴 단일 조회 : 메뉴 ID로 하나의 메뉴를 조회	
	public MenuVO getMenu(int id) {
		
		MenuVO menu = null;	// 결과값 초기화

		this.connect();		// DB 연결
		
		try {
			// SQL 쿼리 작성: menu 테이블에서 특정 메뉴 조회
			String query = " SELECT * "
						 + " FROM menu "
						 + " WHERE menu_id = ? ";
			pstmt = conn.prepareStatement(query);
			
			// 파라미터 입력
			pstmt.setInt(1, id);
			
			// 쿼리 실행 및 결과 받기
			rs = pstmt.executeQuery();
			
			// 결과가 있으면 MenuVO 객체 생성
			if (rs.next()) {
				menu = new MenuVO(rs.getInt("menu_id")
						  	     ,rs.getInt("category_id")
						  	     ,rs.getString("name")
						  	     ,rs.getInt("price")
						  	     );
			}
			
		} catch (SQLException e) {
			System.out.println("error: " + e);
		
		} finally {
			this.close();
		}

		return menu;	// 조회된 메뉴 객체 반환
	}

	// 메뉴 수정
	public int updateMenu(int id, String name, int price) {
		
		int count = -1;	// 결과값 초기화
		
		this.connect();	// DB 연결
		
		try {
			 // SQL 쿼리 작성: menu 테이블에서 특정 메뉴 수정
			String query = " UPDATE menu "
						 + " SET name = ?, price = ? "
						 + " WHERE menu_id = ? ";
			pstmt = conn.prepareStatement(query);
			
			// 파라미터 입력
			pstmt.setString(1, name);
			pstmt.setInt(2, price);
			pstmt.setInt(3, id);
			
			// 쿼리 실행 (DB 데이터 수정)
			count = pstmt.executeUpdate();
		
		} catch (SQLException e) {
			System.out.println("error: " + e);
	
		} finally {
			this.close();
		}

		// 수정된 행의 개수 반환
		return count;
	}

	// 메뉴 삭제
	public int deleteMenu(int id) {
		
		int count = -1;	// 결과값 초기화
		
		this.connect();	// DB 연결
		
		try {
			// SQL 쿼리 작성: menu 테이블에서 특정 메뉴 삭제
			String query = " DELETE "
						 + " FROM menu "
						 + " WHERE menu_id = ? ";
			pstmt = conn.prepareStatement(query);

			// 파라미터 입력
			pstmt.setInt(1, id);
			
			// 쿼리 실행 (DB 데이터 삭제)
			count = pstmt.executeUpdate();
		
		} catch (SQLException e) {
			System.out.println("error: " + e);
		
		} finally {
			this.close();
		}

		// 삭제된 행의 개수 반환
		return count;
	}
}