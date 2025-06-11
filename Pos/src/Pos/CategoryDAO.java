package Pos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    private Connection conn;
    private PreparedStatement pstmt;
    private ResultSet rs;

    private final String driver = "com.mysql.cj.jdbc.Driver";
    private final String url = "jdbc:mysql://localhost:3306/pos_db";
    private final String id = "pos";
    private final String pw = "pos";

    public CategoryDAO() {}

    // DB 연결
    private void connect() {
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, id, pw);
        } catch (Exception e) {
            System.out.println("DB 연결 실패: " + e.getMessage());
        }
    }

    // 자원 해제
    private void close() {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.out.println("자원 해제 실패: " + e.getMessage());
        }
    }

    // 전체 카테고리 조회
    public List<CategoryVO> getAllCategories() {
        List<CategoryVO> list = new ArrayList<>();
        connect();
        try {
            String sql = "SELECT * FROM category";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new CategoryVO(
                    rs.getInt("category_id"),
                    rs.getString("emoji"),
                    rs.getString("name"),
                    rs.getString("explanation")
                ));
            }
        } catch (SQLException e) {
            System.out.println("카테고리 전체 조회 실패: " + e.getMessage());
        } finally {
            close();
        }
        return list;
    }

    // 단일 카테고리 조회

	    public CategoryVO getCategory(int categoryId) {
	        CategoryVO vo = null;
	        connect();
	        try {
	            String sql = "SELECT * FROM category WHERE category_id = ?";
	            pstmt = conn.prepareStatement(sql);
	            pstmt.setInt(1, categoryId);
	            rs = pstmt.executeQuery();
	            if (rs.next()) {
	                vo = new CategoryVO(
	                    rs.getInt("category_id"),
	                    rs.getString("emoji"),
	                    rs.getString("name"),
	                    rs.getString("explanation")
	                );
	            }
	        } catch (SQLException e) {
	            System.out.println("카테고리 단일 조회 실패: " + e.getMessage());
	        } finally {
	            close();
	        }
	        return vo;
	    }


    // 카테고리 등록
    public void addCategory(String emoji, String name, String explanation) {
        connect();
        try {
            String sql = "INSERT INTO category (emoji, name, explanation) VALUES (?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, emoji);
            pstmt.setString(2, name);
            pstmt.setString(3, explanation);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("카테고리 등록 실패: " + e.getMessage());
        } finally {
            close();
        }
    }

    // 카테고리 수정
    public void updateCategory(int categoryId, String emoji, String name, String explanation) {
        connect();
        try {
            String sql = "UPDATE category SET emoji = ?, name = ?, explanation = ? WHERE category_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, emoji);
            pstmt.setString(2, name);
            pstmt.setString(3, explanation);
            pstmt.setInt(4, categoryId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("카테고리 수정 실패: " + e.getMessage());
        } finally {
            close();
        }
    }

    // 카테고리 삭제
    public void deleteCategory(int categoryId) {
        connect();
        try {
            String sql = "DELETE FROM category WHERE category_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, categoryId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("카테고리 삭제 실패: " + e.getMessage());
        } finally {
            close();
        }
    }
}
