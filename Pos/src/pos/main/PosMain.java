package pos.main;

import java.sql.SQLException;
import java.util.Scanner;

import pos.sub.MenuProgram;



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
	                
//					case 1:	// 주문/결재 호출
//						OrderProgram orderProgram = new OrderProgram();
//						orderProgram.orderPaymentMenu(sc);
//	                    break;
//	                
//					case 2:	// 매출 호출
//						SaleProgram saleProgram = new SaleProgram();
//						saleProgram.showSalesMenu(sc);
//	                    break;
					
					case 3: // 메뉴관리 호출
						MenuProgram menuProgram = new MenuProgram();
						menuProgram.menuManagementMenu(sc);
						break;
					
//					case 4: // 카테고리 호출
//						CategoryProgram categoryProgram= new CategoryProgram();
//						categoryProgram.categoryManagementMenu(sc);
//						break;
					
					default:
						System.out.println("다시 입력해주세요.");
				}
			
			} catch (SQLException e) { // 데이터베이스 관련 예외 처리
				e.printStackTrace();
			}
		}
	}

}