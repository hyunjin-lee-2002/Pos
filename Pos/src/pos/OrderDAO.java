package pos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//주문/결제 데이터베이스 접근을 담당하는 클래스
public class OrderDAO {

	// 필드
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	private String driver = "com.mysql.cj.jdbc.Driver";
	private String url = "jdbc:mysql://localhost:3306/pos_db";
	private String id = "pos";
	private String pw = "pos";

	// 생성자
	public OrderDAO() {}

	// DB 연결 메소드
	private void connect() {
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, id, pw);
		
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println("error: DB 연결 실패 - " + e.getMessage());
		}
	}

	// 자원 정리 메소드
	private void close() {
		try {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
			if (conn != null)
				conn.close();
		
		} catch (SQLException e) {
			System.out.println("error: 자원 해제 실패 - " + e.getMessage());
		}
	}

	// 주문 등록
	public int insertOrder(int menuId, int quantity, int tableNum) {
		
		int count = -1;	// 결과값 초기화
		
		this.connect();	// DB 연결

		try {
			// SQL 쿼리 작성: orders 테이블에 새로운 주문 추가
			String query = " INSERT INTO orders(menu_id, quantity, table_num, ispaid) "
					     + " VALUES (?, ?, ?, FALSE)";
			pstmt = conn.prepareStatement(query);

			// 쿼리에 파라미터(값) 입력
			pstmt.setInt(1, menuId);
			pstmt.setInt(2, quantity);
			pstmt.setInt(3, tableNum);
			
			// 쿼리 실행 (DB에 데이터 저장)
			count = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("error: " + e);
		
		} finally {
			this.close();
		}
		
		return count;
	}

	// 주문 전체 조회 : 데이터베이스에서 결제되지 않은 모든 주문을 가져와서 리스트로 반환
	public List<OrderVO> selectAllOrders() {
		
		List<OrderVO> orderList = new ArrayList<>();	 // 결과를 저장할 리스트
		
		this.connect();	// DB 연결

		try {
			// SQL 쿼리 작성: orders 테이블과 menu 테이블을 조인하여 주문 정보와 메뉴 이름을 함께 조회
	        String query = "SELECT o.*, m.name AS menu_name "
                		 + "FROM orders o "
                		 + "JOIN menu m ON o.menu_id = m.menu_id "
                		 + "WHERE o.ispaid = FALSE";
			pstmt = conn.prepareStatement(query);

			// 쿼리 실행 및 결과 받기
			rs = pstmt.executeQuery();

			// 결과를 한 줄씩 읽어서 OrderVO 객체로 만들어 리스트에 추가
			while (rs.next()) {
				OrderVO order = new OrderVO(rs.getInt("order_id")
										   ,rs.getInt("menu_id")
										   ,rs.getInt("quantity")
										   ,rs.getInt("table_num")
										   ,rs.getBoolean("ispaid")
										   );
				// 메뉴 이름 저장
				order.setMenuName(rs.getString("menu_name"));
				orderList.add(order);
			}
		
		} catch (SQLException e) {
			System.out.println("error: " + e.getMessage());
		
		} finally {
			this.close();
		}
		
		return orderList;
	}

	// 테이블별 주문 조회 : 특정 테이블 번호에 해당하는 결제되지 않은 주문을 조회
	public List<OrderVO> selectOrdersByTable(int tableNum) {
		
		List<OrderVO> orderList = new ArrayList<>();	 // 결과를 저장할 리스트
		
		this.connect();	// DB 연결

		try {
			 // SQL 쿼리 작성: orders 테이블에서 특정 테이블 번호의 결제되지 않은 주문 조회
			String query = "SELECT * "
						 + "FROM orders "
						 + "WHERE table_num = ? "
						 + "AND ispaid = FALSE";
			pstmt = conn.prepareStatement(query);
			
			// 파라미터 입력
			pstmt.setInt(1, tableNum);

			// 쿼리 실행 및 결과 받기
			rs = pstmt.executeQuery();

			// 결과를 한 줄씩 읽어서 OrderVO 객체로 만들어 리스트에 추가
			while (rs.next()) {
				OrderVO order = new OrderVO(rs.getInt("order_id")
										   ,rs.getInt("menu_id")
										   ,rs.getInt("quantity")
										   ,rs.getInt("table_num")
										   ,rs.getBoolean("ispaid")
										   );
				orderList.add(order);
			}
			
		} catch (SQLException e) {
			System.out.println("error: " + e.getMessage());
		
		} finally {
			this.close();
		}
		
		return orderList;
	}

	// 결제 여부별 주문 조회 : 결제 여부(ispaid)에 따라 주문을 조회
	public List<OrderVO> selectOrdersByPaid(boolean isPaid) {
		
		List<OrderVO> orderList = new ArrayList<>();	// 결과를 저장할 리스트
		
		this.connect();		// DB 연결

		try {
			 // SQL 쿼리 작성: orders 테이블에서 결제 여부에 따라 주문 조회
			String query = " SELECT * "
						 + " FROM orders "
						 + " WHERE ispaid = ?";
			pstmt = conn.prepareStatement(query);
			pstmt.setBoolean(1, isPaid);
			rs = pstmt.executeQuery();
			
			// 결과를 한 줄씩 읽어서 OrderVO 객체로 만들어 리스트에 추가
			while (rs.next()) {
				OrderVO order = new OrderVO(rs.getInt("order_id")
										   ,rs.getInt("menu_id")
										   ,rs.getInt("quantity")
										   ,rs.getInt("table_num")
										   ,rs.getBoolean("ispaid")
										   );
				orderList.add(order);
			}
			
		} catch (SQLException e) {
			System.out.println("error: " + e.getMessage());
		
		} finally {
			this.close();
		}
		
		return orderList;	// 주문 리스트 반환
	}

	// 주문 1건 조회 : 주문 ID로 하나의 주문을 조회
	public OrderVO selectOrder(int orderId) {
		
		OrderVO order = null;	// 결과값 초기화
		
		this.connect();	// DB 연결

		try {
			 // SQL 쿼리 작성: orders 테이블에서 특정 주문 ID로 조회
			String query = " SELECT * "
						 + " FROM orders "
						 + " WHERE order_id = ? ";
			pstmt = conn.prepareStatement(query);
			
			// 파라미터 입력
			pstmt.setInt(1, orderId);
			
			// 쿼리 실행 및 결과 받기
			rs = pstmt.executeQuery();

			if (rs.next()) {
				order = new OrderVO(rs.getInt("order_id")
								   ,rs.getInt("menu_id")
								   ,rs.getInt("quantity")
								   ,rs.getInt("table_num")
								   ,rs.getBoolean("ispaid")
								   );
			}
			
		} catch (SQLException e) {
			System.out.println("error: " + e.getMessage());
			
		} finally {
			this.close();
		}
		
		return order;	// 조회된 주문 객체 반환
	}

	// 주문 수정 : 주문의 수량과 테이블 번호를 수정
	public int updateOrder(int orderId, int quantity, int tableNum) {
		
		int count = -1;	// 결과값 초기화
		
		this.connect();	// DB 연결

		try {
			// SQL 쿼리 작성: orders 테이블에서 특정 주문 수정
			String query = " UPDATE orders "
						 + " SET quantity = ?, table_num = ? "
						 + " WHERE order_id = ?";
			pstmt = conn.prepareStatement(query);
			
			// 파라미터 입력
			pstmt.setInt(1, quantity);
			pstmt.setInt(2, tableNum);
			pstmt.setInt(3, orderId);
			
			// 쿼리 실행 (DB 데이터 수정)
			count = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("error: " + e.getMessage());
		
		} finally {
			this.close();
		}
		
		return count;	// 수정된 행의 개수 반환
	}

	// 주문 삭제
	public int deleteOrder(int orderId) {
		
		int count = -1;	// 결과값 초기화
		
		this.connect();	// DB 연결

		try {
			// SQL 쿼리 작성: orders 테이블에서 특정 주문 삭제
			String query = " DELETE "
						 + " FROM orders "
						 + " WHERE order_id = ?";
			pstmt = conn.prepareStatement(query);
			
			// 파라미터 입력
			pstmt.setInt(1, orderId);
			
			// 쿼리 실행 (DB 데이터 삭제)
			count = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("error: " + e.getMessage());
		
		} finally {
			this.close();
		}
		
		return count;
	}

	// 주문 결제 처리 (ispaid 업데이트 방식 사용 권장)
	public int payOrder(int orderId) {
		
		int count = -1;	// 결과값 초기화
		
		this.connect();	// DB 연결

		try {
			// SQL 쿼리 작성: orders 테이블에서 특정 주문 결제 처리
			String query = " UPDATE orders "
						 + " SET ispaid = TRUE "
						 + " WHERE order_id = ? ";
			pstmt = conn.prepareStatement(query);

			// 파라미터 입력
			pstmt.setInt(1, orderId);
			
			// 쿼리 실행 (DB 데이터 수정)
			count = pstmt.executeUpdate();
		
		} catch (SQLException e) {
			System.out.println("error: " + e.getMessage());
		
		} finally {
			this.close();
		}
		
		return count;
	}

	// 테이블 전체 결제 처리 : 특정 테이블의 모든 주문을 결제 처리(ispaid를 TRUE로 변경)
	public int payTable(int tableNum) {
		
		int count = -1;	// 결과값 초기화
		
		this.connect();	// DB 연결

		try {
			// SQL 쿼리 작성: orders 테이블에서 특정 테이블의 모든 주문 결제 처리
			String query = " UPDATE orders "
						 + " SET ispaid = TRUE "
						 + " WHERE table_num = ? ";
			pstmt = conn.prepareStatement(query);
			
			// 파라미터 입력
			pstmt.setInt(1, tableNum);
			
			// 쿼리 실행 (DB 데이터 수정)
			count = pstmt.executeUpdate();
		
		} catch (SQLException e) {
			System.out.println("error: " + e.getMessage());
		
		} finally {
			this.close();
		}
		
		return count;	// 수정된 행의 개수 반환
	}

	// 총 매출 조회 : 결제된 모든 주문의 매출 합계를 조회
	public int selectTotalSales() {
		
		int total = 0;	// 결과값 초기화
		
		this.connect();	// DB 연결

		try {
			// SQL 쿼리 작성: orders 테이블과 menu 테이블을 조인하여 결제된 주문의 매출 합계 조회
			String query = " SELECT SUM(o.quantity * m.price) AS total "
					     + " FROM orders o "
					     + " JOIN menu m "
					     + " ON o.menu_id = m.menu_id "
					     + " WHERE o.ispaid = TRUE ";
			pstmt = conn.prepareStatement(query);
			
			// 쿼리 실행 및 결과 받기
			rs = pstmt.executeQuery();

			// 결과가 있으면 매출 합계 저장
			if (rs.next()) {
				total = rs.getInt("total");
	        }
		
		} catch (SQLException e) {
			System.out.println("error: " + e.getMessage());
		
		} finally {
			this.close();
		}
		
		return total;	// 매출 합계 반환
	}

	// 카테고리별 매출 조회 : 특정 카테고리의 결제된 주문 매출 합계를 조회	
	public int selectSalesByCategory(int categoryId) {
		
		int total = 0;	// 결과값 초기화
		
		this.connect();	// DB 연결

		try {
			// SQL 쿼리 작성: orders 테이블과 menu 테이블을 조인하여 특정 카테고리의 결제된 주문 매출 합계 조회
			String query = " SELECT SUM(o.quantity * m.price) AS total "
						 + " FROM orders o "
						 + " JOIN menu m "
						 + " ON o.menu_id = m.menu_id "
						 + " WHERE o.ispaid = TRUE "
						 + " AND m.category_id = ? ";
			pstmt = conn.prepareStatement(query);

			// 파라미터 입력
			pstmt.setInt(1, categoryId);
			
			// 쿼리 실행 및 결과 받기
			rs = pstmt.executeQuery();
			
			// 결과가 있으면 매출 합계 저장
			if (rs.next()) {
				total = rs.getInt("total");
			}
			
		} catch (SQLException e) {
			System.out.println("error: " + e.getMessage());

		} finally {
			this.close();
		}
		
		return total;
	}

    // addOrder는 insertOrder를 호출하는 단축 메서드
    public void addOrder(int menuId, int quantity, int tableNum) {
        insertOrder(menuId, quantity, tableNum);
	}

}