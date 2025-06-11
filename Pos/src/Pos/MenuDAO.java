package Pos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

	// DB 연결
	public void connect() {
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, id, pw);
		} catch (ClassNotFoundException e) {
			System.out.println("error: 드라이버 로딩 실패 - " + e);
		} catch (SQLException e) {
			System.out.println("error: " + e);
		}
	}

	// 자원 정리
	private void close() {
		try {
			if (rs != null) rs.close();
			if (pstmt != null) pstmt.close();
			if (conn != null) conn.close();
		} catch (SQLException e) {
			System.out.println("error: " + e);
		}
	}

	// 메뉴 등록
	public int addMenu(int categoryId, String name, int price) {
		int count = -1;
		this.connect();
		try {
			String query = "INSERT INTO menu VALUES (NULL, ?, ?, ?)";
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, name);
			pstmt.setInt(2, price);
			pstmt.setInt(3, categoryId);
			count = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("error: " + e);
		} finally {
			this.close();
		}
		return count;
	}

	// 메뉴 전체 조회
	public List<MenuVO> getAllMenus() {
		List<MenuVO> list = new ArrayList<>();
		this.connect();
		try {
			String query = "SELECT * FROM menu";
			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(new MenuVO(rs.getInt("menu_id"), rs.getInt("category_id"), rs.getString("name"), rs.getInt("price")));
			}
		} catch (SQLException e) {
			System.out.println("error: " + e);
		} finally {
			this.close();
		}
		return list;
	}

	// 메뉴 단일 조회
	public MenuVO getMenu(int id) {
		MenuVO menu = null;
		this.connect();
		try {
			String query = "SELECT * FROM menu WHERE menu_id = ?";
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				menu = new MenuVO(rs.getInt("menu_id"), rs.getInt("category_id"), rs.getString("name"), rs.getInt("price"));
			}
		} catch (SQLException e) {
			System.out.println("error: " + e);
		} finally {
			this.close();
		}
		return menu;
	}

	// 메뉴 수정
	public int updateMenu(int id, String name, int price) {
		int count = -1;
		this.connect();
		try {
			String query = "UPDATE menu SET name = ?, price = ? WHERE menu_id = ?";
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, name);
			pstmt.setInt(2, price);
			pstmt.setInt(3, id);
			count = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("error: " + e);
		} finally {
			this.close();
		}
		return count;
	}

	// 메뉴 삭제
	public int deleteMenu(int id) {
		int count = -1;
		this.connect();
		try {
			String query = "DELETE FROM menu WHERE menu_id = ?";
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, id);
			count = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("error: " + e);
		} finally {
			this.close();
		}
		return count;
	}
}
