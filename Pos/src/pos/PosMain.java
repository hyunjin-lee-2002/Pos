package pos;    

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class PosMain {
	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		while (true) { // 프로그램 종료시까지 무한반복
			System.out.println("\n********************************************************************");
			System.out.println("                           프로그램 시작                        ");
			System.out.println("********************************************************************");
			
			System.out.println("\n시작메뉴 ===========================================================");
			System.out.println("      1. 주문 / 결제      2. 매출      3. 메뉴      4. 카테고리");
			System.out.println("====================================================================");
			
			System.out.print("[시작메뉴 번호를 입력해주세요]        * 0번 프로그램 종료\n시작메뉴 번호 : ");
			
			int choice = sc.nextInt();
			
			try {
				switch (choice) {
					case 0: // 프로그램 종료
						System.out.println("\n********************************************************************");
						System.out.println("                           프로그램 종료                        ");
						System.out.println("********************************************************************");
						return;
	                
					case 1:	// 주문/결재 호출
	                    orderPaymentMenu(sc);
	                    break;
	                
					case 2:	// 매출 호출
	                    showSalesMenu(sc);
	                    break;
					
					case 3: // 메뉴관리 호출
						menuManagementMenu(sc);
						break;
					
					case 4: // 카테고리 호출
						categoryManagementMenu(sc);
						break;
					
					default:
						System.out.println("다시 입력해주세요.");
				}
			
			} catch (SQLException e) { // 데이터베이스 관련 예외 처리
				e.printStackTrace();
			}
		}
	}

	  // 주문/결제 메뉴 
	  public static void orderPaymentMenu(Scanner sc) throws SQLException {
		  
		  OrderDAO orderDAO = new OrderDAO();
		  MenuDAO menuDAO = new MenuDAO();
		  
	        while (true) {	// 주문/결제 메뉴 출력
	            System.out.println("\n주문 / 결제 --------------------------------------------------------");
	            System.out.println("      1. 등록                   2. 삭제                   3. 결제");
	            System.out.println("--------------------------------------------------------------------");
	            
	            System.out.println("<주문현황>");	// 현재 주문 현황 출력
	            List<OrderVO> orders = orderDAO.selectAllOrders();
	            
	            for (OrderVO o : orders) {
	                System.out.println(o.getId() + " - 메뉴번호(" + o.getMenuName() + ") / 메뉴수량(" + o.getQuantity() + ") / 테이블번호(" + o.getTableNo() + ")");
	            }	
	            
	            System.out.print("\n[주문 / 결제 번호를 입력해주세요]        * 0번 상위메뉴\n주문 / 결제 번호 : ");
	            int sub = sc.nextInt();
	            
	            if (sub == 0) break;
	            sc.nextLine();
	            
	            switch (sub) {
	                case 1:
	                    System.out.println("\n등록 ..............................................................");
	                    System.out.println("위치 : 홈 > 주문 / 결제 > 등록");
	                    
	                    // 현재 등록된 메뉴 목록 출력
	                    List<MenuVO> menus = menuDAO.getAllMenus();
	                    System.out.println("\n<메뉴>");
	                    for (MenuVO m : menus) {
	                        System.out.println(m.getId() + " - " + m.getName() + " (" + m.getPrice() + "원)");
	                    }
	                    System.out.println("");
	                    
	                    System.out.print("1. 메뉴번호 : ");
	                    int menuId = sc.nextInt();
	                    System.out.print("2. 메뉴수량 : ");
	                    int quantity = sc.nextInt();
	                    System.out.print("3. 테이블번호 : ");
	                    int tableNo = sc.nextInt();
	                    sc.nextLine();
	                    
	                    orderDAO.addOrder(menuId, quantity, tableNo);
	                    
	                    System.out.println("<주문되었습니다.>");
	                    
	                    break;
	                
	                case 2:
	                    System.out.println("\n삭제 ..............................................................");
	                    System.out.println("위치 : 홈 > 주문 / 결제 > 삭제");
	                    System.out.println("");
	                    
	                    System.out.print("1. 주문번호 : ");
	                    int delId = sc.nextInt();
	                    sc.nextLine();
	                    
	                    orderDAO.deleteOrder(delId);
	                    
	                    System.out.println("<주문 취소되었습니다.>");
	                    
	                    break;
	                
	                case 3:
	    			    System.out.println("\n결제 ................................................................");
	    			    System.out.println("      1. 전체결제                    2. 개별결제");
	    			    System.out.println(".....................................................................");
	    			    System.out.print("[결제 번호를 입력해주세요]        * 0번 상위메뉴\n위치 : 홈 > 주문 / 결제 > 결제\n결제 번호 : ");
	    			    int pay = sc.nextInt();
	    			    
	    			    if (pay == 0) break;
	    			    
	    			    if (pay == 1) {
	    			        System.out.print("1. 테이블번호 : ");
	    			        int table = sc.nextInt();
	    			        int result = orderDAO.payTable(table); 
	    			        
	    			        if (result > 0) {
	    			            System.out.println("<결제되었습니다.>");
	    			        
	    			        } else {
	    			            System.out.println("<결제할 주문이 존재하지 않습니다.>");
	    			        }
	    			    
	    			    } else if (pay == 2) {
	    			        System.out.print("1. 주문번호 : ");
	    			        int orderId = sc.nextInt();
	    			        int result = orderDAO.payOrder(orderId); 
	    			        
	    			        if (result > 0) {
	    			            System.out.println("<결제되었습니다.>");
	    			        
	    			        } else {
	    			            System.out.println("<존재하지 않는 주문번호입니다.>");
	    			        }
	    			        
	    			    } else {
	    			        System.out.println("<잘못된 입력입니다.>");
	    			    }
	    			    
	    			    break;
	            }
	        }
	    }

	// 매출 메뉴
		public static void showSalesMenu(Scanner sc) throws SQLException {
		    CategoryDAO categoryDAO = new CategoryDAO();
		    OrderDAO orderDAO = new OrderDAO();

	        while (true) {
	            System.out.println("\n매출 ---------------------------------------------------------------");
	            System.out.println("      1. 일일                     2. 분야                ");
	            System.out.println("--------------------------------------------------------------------");
	            System.out.print("[매출 번호를 입력해주세요]        * 0번 상위메뉴\n매출 번호 : ");
	            int sub = sc.nextInt();
	            sc.nextLine();
	            
	            if (sub == 0) break;
	            
	            switch (sub) {
	                case 1:
	                    System.out.println("\n일일 ..............................................................");
	                    System.out.println("위치 : 홈 > 매출 > 날짜");
	                    int total = orderDAO.selectTotalSales();
	                    System.out.println("\n매출액 : " + total + "원");
	                    
	                    break;
	                
	                case 2:
	                    System.out.println("\n분야 ..............................................................");
	                    System.out.println("위치 : 홈 > 매출 > 분야");
                        System.out.println("");
	                    List<CategoryVO> categories = categoryDAO.getAllCategories();
	                    
	                    for (CategoryVO c : categories) {
	                        int sales = orderDAO.selectSalesByCategory(c.getId());
	                        System.out.println(c.getName() + " : " + sales + "원");
	                    }
	                    
	                    break;
	                
	                default:
	                    System.out.println("잘못된 입력입니다.");
	            }
	        }
	    }

		// 메뉴 관리 메뉴
		public static void menuManagementMenu(Scanner sc) throws SQLException {
			MenuDAO menuDAO = new MenuDAO(); // 메뉴 정보를 다루는 DAO 객체 생성
			CategoryDAO categoryDAO = new CategoryDAO(); // 카테고리 정보를 다루는 DAO 객체 생성

			while (true) { // 메뉴 관리 메뉴 반복 출력
				System.out.println("\n메뉴 ---------------------------------------------------------------");
				System.out.println("      1. 등록                   2. 수정                   3. 삭제");
				System.out.println("--------------------------------------------------------------------");
				System.out.println("<메뉴현황>");

				List<MenuVO> menus = menuDAO.getAllMenus(); // 모든 메뉴 리스트 가져오기

				for (MenuVO m : menus) { // 메뉴 목록 출력 반복문
					CategoryVO c = categoryDAO.getCategory(m.getCategoryId()); // 해당 메뉴의 카테고리 정보 조회
					System.out.println(m.getId() + " - 카테고리(" + m.getCategoryId() + "), 이름(" + m.getName() + "), 가격(" + m.getPrice() + ")");
				}

				System.out.print("\n[메뉴 번호를 입력해주세요]        * 0번 상위메뉴\n메뉴 번호 : ");

				int sub;
				try {
					sub = sc.nextInt(); // 사용자 메뉴 번호 입력 받기
					sc.nextLine(); // 버퍼 비우기
				} catch (java.util.InputMismatchException e) {
					sc.nextLine(); // 잘못된 입력 제거
					System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
					continue; // 반복문 처음으로 돌아감
				}

				if (sub == 0) break; // 0 입력 시 상위 메뉴로 돌아감

				switch (sub) {
					case 1: // 메뉴 등록
						System.out.println("\n등록 ..............................................................");
						System.out.println("위치 : 홈 > 메뉴 > 등록");
						System.out.println("\n<카테고리 현황>");
						List<CategoryVO> categories = categoryDAO.getAllCategories(); // 모든 카테고리 불러오기

						for (int i = 0; i < categories.size(); i++) { // 카테고리 리스트 출력
							System.out.println(categories.get(i).getId() + " - 이모티콘(" + categories.get(i).getEmoji() + "), 이름(" + categories.get(i).getName() + "), 설명(" + categories.get(i).getDescription() + ")");
						}

						int catId; // 카테고리 번호
						System.out.print("\n1. 카테고리 번호 : ");
						try {
							catId = sc.nextInt(); // 입력 받기
							sc.nextLine(); // 버퍼 비우기
						} catch (java.util.InputMismatchException e) {
							sc.nextLine();
							System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
							continue; // 처음으로 돌아감
						}

						System.out.print("2. 이 름 : ");
						String name = sc.nextLine(); // 메뉴 이름 입력

						int price; // 메뉴 가격
						System.out.print("3. 가 격 : ");
						try {
							price = sc.nextInt(); // 가격 입력
							sc.nextLine(); // 버퍼 비우기
						} catch (java.util.InputMismatchException e) {
							sc.nextLine();
							System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
							continue; // 처음으로 돌아감
						}

						menuDAO.addMenu(catId, name, price); // 메뉴 DB에 등록
						System.out.println("<등록되었습니다.>");
						System.out.println("");
						break;

					case 2: // 메뉴 수정
						System.out.println("\n수정 ..............................................................");
						System.out.println("위치 : 홈 > 메뉴 > 수정");

						int menuId; // 수정할 메뉴 번호
						System.out.print("1. 메뉴번호 : ");
						try {
							menuId = sc.nextInt(); // 입력
							sc.nextLine();
						} catch (java.util.InputMismatchException e) {
							sc.nextLine();
							System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
							continue; // 처음으로
						}

						System.out.print("2. 이 름 : ");
						String newName = sc.nextLine(); // 수정할 이름 입력

						int newPrice; // 수정할 가격 입력
						System.out.print("3. 가 격 : ");
						try {
							newPrice = sc.nextInt();
							sc.nextLine();
						} catch (java.util.InputMismatchException e) {
							sc.nextLine();
							System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
							continue;
						}

						menuDAO.updateMenu(menuId, newName, newPrice); // 메뉴 정보 수정
						System.out.println("<수정되었습니다.>");
						System.out.println("");
						break;

					case 3: // 메뉴 삭제
						System.out.println("\n삭제 ..............................................................");
						System.out.println("위치 : 홈 > 메뉴 > 삭제");

						int delId; // 삭제할 메뉴 번호
						System.out.print("1. 메뉴번호 : ");
						try {
							delId = sc.nextInt(); // 입력
							sc.nextLine();
						} catch (java.util.InputMismatchException e) {
							sc.nextLine();
							System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
							continue;
						}

						menuDAO.deleteMenu(delId); // 메뉴 삭제
						System.out.println("<삭제되었습니다.>");
						System.out.println("");
						break;

					default: // 잘못된 메뉴 번호 입력 시
						System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
				}
			}
		}


	// 카테고리 관리 메뉴
		public static void categoryManagementMenu(Scanner sc) throws SQLException {
		    	
		    	CategoryDAO categoryDao = new CategoryDAO();
		        
		        while (true) {
		            System.out.println("\n카테고리 -----------------------------------------------------------");
		            System.out.println("      1. 등록                   2. 수정                   3. 삭제");
		            System.out.println("--------------------------------------------------------------------");
		            System.out.println("<카테고리>");
		            
		            List<CategoryVO> categories = categoryDao.getAllCategories();
		            
		            for (int i = 0; i < categories.size(); i++) {
		                System.out.println(categories.get(i).getId() + " - 이모티콘(" + categories.get(i).getEmoji() + "), "
		                    + "이름(" + categories.get(i).getName() + "), 설명(" + categories.get(i).getDescription() + ")");
		            }
		            
		            int sub;
		            
		            try{
		                System.out.print("\n[카테고리 번호를 입력해주세요]        * 0번 상위메뉴\n카테고리 번호 : ");      
		                sub = sc.nextInt();
		            
		        	}catch(InputMismatchException e) {
			            System.out.println("잘못된 입력입니다. 숫자를 입력해주세요.");
			            sc.nextLine(); // 잘못된 입력 버리기
			            
		            continue; // 다시 입력받기
		            }
		            
		            switch (sub) {
		                case 1:
		                    System.out.println("\n등록 ..............................................................");
		                    System.out.println("위치 : 홈 > 카테고리 > 등록");
		                    sc.nextLine(); 
		                    System.out.print("1. 이모티콘 : ");
		                    String emoji = sc.nextLine();
		                    System.out.print("2. 이 름 : ");
		                    String name = sc.nextLine();
		                    System.out.print("3. 설 명 : ");
		                    String desc = sc.nextLine();
		                    categoryDao.categoryInsert(emoji, name, desc);
		                    System.out.println("<등록되었습니다.>");
		                    System.out.println("");
		                    
		                    break;

		                case 2:
		                    System.out.println("\n수정 ..............................................................");
		                    System.out.println("위치 : 홈 > 카테고리 > 수정");
		                    
		                    int catId;
		                    
		                    while (true) {
		                        try {
		                            System.out.print("카테고리번호 : ");
		                            catId = sc.nextInt();
		                            sc.nextLine(); // 입력 버퍼 비우기
		                            
		                            break;
		                        
		                        } catch (InputMismatchException e) {
		                            System.out.println("잘못된 입력입니다. 숫자를 입력해주세요.");
		                            sc.nextLine(); // 잘못된 입력 버리기
		                        }
		                    }
		                    
		                    System.out.print("1. 이모티콘 : ");
		                    String newEmoji = sc.nextLine();
		                    System.out.print("2. 이 름 : ");
		                    String newName = sc.nextLine();
		                    System.out.print("3. 설 명 : ");
		                    String newDesc = sc.nextLine();
		                    categoryDao.updateCategory(catId, newEmoji, newName, newDesc);
		                    System.out.println("<수정되었습니다.>");
		                    System.out.println("");
		                    break;
		                    
		                case 3:
		                    System.out.println("\n삭제 ..............................................................");
		                    System.out.println("위치 : 홈 > 카테고리 > 삭제");
		                    
		                    int delId ;
		                    
		                    while (true) {
		                        try {
		                            System.out.print("카테고리번호 : ");
		                            delId = sc.nextInt();
		                            sc.nextLine(); // 입력 버퍼 비우기
		                           
		                            break;
		                        
		                        } catch (InputMismatchException e) {
		                            System.out.println("잘못된 입력입니다. 숫자를 입력해주세요.");
		                            sc.nextLine(); // 잘못된 입력 버리기
		                        }
		                    }
		                    
		                    categoryDao.deleteCategory(delId);
		                    System.out.println("<삭제되었습니다.>");
		                    System.out.println("");
		                    
		                    break;
		                    
						case 0:
							
							return;
							
		                default:
		                    System.out.println("잘못된 입력입니다.");
			}
		}
	}
}
