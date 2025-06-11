package Pos;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class PosMain {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
        	System.out.println("\n********************************************************************");
            System.out.println("                           프로그램 시작                        ");
            System.out.println("********************************************************************");
            System.out.println("\n시작메뉴 ===========================================================");
            System.out.println("      1. 주문 / 결제      2. 매출      3. 메뉴      4. 카테고리");
            System.out.println("====================================================================");
            System.out.print("[시작메뉴 번호를 입력해주세요]        * 0번 프로그램 종료\n시작메뉴 번호 : ");
            int choice =sc.nextInt(); 
            try {
                switch (choice) {
                    case 0:
                        System.out.println("\n********************************************************************");
                        System.out.println("                           프로그램 종료                        ");
                        System.out.println("********************************************************************");
                        return;
                  
                    case 3:
                        menuManagementMenu(sc);
                        break;
                  
                    default:
                        System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

     
 // 메뉴 관리 메뉴
    static void menuManagementMenu(Scanner sc) throws SQLException {
        // DAO 객체 생성
        MenuDAO menuDAO = new MenuDAO();
        CategoryDAO categoryDAO = new CategoryDAO();

        // 무한 반복을 통해 메뉴 관리 화면 유지
        while (true) {
            // 메인 메뉴 출력
            System.out.println("\n메뉴 ---------------------------------------------------------------");
            System.out.println("      1. 등록                   2. 수정                   3. 삭제");
            System.out.println("--------------------------------------------------------------------");

            // 현재 메뉴 목록 출력
            System.out.println("<메뉴현황>");
            List<MenuVO> menus = menuDAO.getAllMenus();  // 전체 메뉴 가져오기
            for (MenuVO m : menus) {
                CategoryVO c = categoryDAO.getCategory(m.getCategoryId()); // 해당 메뉴의 카테고리 가져오기
                System.out.println(m.getId() + " - 카테고리(" + m.getCategoryId() + "), 이름(" + m.getName() + "), 가격(" + m.getPrice() + ")");
            }

            // 사용자에게 메뉴 선택 요청
            System.out.print("\n[메뉴 번호를 입력해주세요]        * 0번 상위메뉴\n메뉴 번호 : ");
            int sub;
            try {
                sub = sc.nextInt(); // 메뉴 입력 받기
                sc.nextLine(); // 개행 문자 제거
            } catch (java.util.InputMismatchException e) {
                sc.nextLine(); // 잘못된 입력 시 버퍼 정리
                System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
                continue; // 메뉴 다시 선택
            }

            // 0 입력 시 상위 메뉴로 나가기
            if (sub == 0) break;

            // 입력된 메뉴 번호에 따라 분기
            switch (sub) {

                // 메뉴 등록
                case 1:
                    System.out.println("\n등록 ..............................................................");
                    System.out.println("위치 : 홈 > 메뉴 > 등록");

                    // 카테고리 현황 출력
                    System.out.println("<카테고리 현황>");
                    List<CategoryVO> categories = categoryDAO.getAllCategories();
                    for (int i = 0; i < categories.size(); i++) {
                        System.out.println(categories.get(i).getId() + " - 이모티콘(" + categories.get(i).getEmoji() + "), 이름(" + categories.get(i).getName() + "), 설명(" + categories.get(i).getDescription() + ")");
                    }

                    // 사용자로부터 카테고리, 이름, 가격 입력 받기
                    int catId;
                    System.out.print("1. 카테고리 번호 : ");
                    try {
                        catId = sc.nextInt();
                        sc.nextLine();
                    } catch (java.util.InputMismatchException e) {
                        sc.nextLine();
                        System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
                        continue;
                    }

                    System.out.print("2. 이 름 : ");
                    String name = sc.nextLine();

                    int price;
                    System.out.print("3. 가 격 : ");
                    try {
                        price = sc.nextInt();
                        sc.nextLine();
                    } catch (java.util.InputMismatchException e) {
                        sc.nextLine();
                        System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
                        continue;
                    }

                    // 입력 받은 값으로 메뉴 등록
                    menuDAO.addMenu(catId, name, price);
                    System.out.println("<등록되었습니다.>");
                    System.out.println("");
                    break;

                // 메뉴 수정
                case 2:
                    System.out.println("\n수정 ..............................................................");
                    System.out.println("위치 : 홈 > 메뉴 > 수정");

                    // 사용자로부터 메뉴 번호, 새 이름, 새 가격 입력 받기
                    int menuId;
                    System.out.print("1. 메뉴번호 : ");
                    try {
                        menuId = sc.nextInt();
                        sc.nextLine();
                    } catch (java.util.InputMismatchException e) {
                        sc.nextLine();
                        System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
                        continue;
                    }

                    System.out.print("2. 이 름 : ");
                    String newName = sc.nextLine();

                    int newPrice;
                    System.out.print("3. 가 격 : ");
                    try {
                        newPrice = sc.nextInt();
                        sc.nextLine();
                    } catch (java.util.InputMismatchException e) {
                        sc.nextLine();
                        System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
                        continue;
                    }

                    // 메뉴 수정 수행
                    menuDAO.updateMenu(menuId, newName, newPrice);
                    System.out.println("<수정되었습니다.>");
                    System.out.println("");
                    break;

                // 메뉴 삭제
                case 3:
                    System.out.println("\n삭제 ..............................................................");
                    System.out.println("위치 : 홈 > 메뉴 > 삭제");

                    // 삭제할 메뉴 번호 입력
                    int delId;
                    System.out.print("1. 메뉴번호 : ");
                    try {
                        delId = sc.nextInt();
                        sc.nextLine();
                    } catch (java.util.InputMismatchException e) {
                        sc.nextLine();
                        System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
                        continue;
                    }

                    // 삭제 수행
                    menuDAO.deleteMenu(delId);
                    System.out.println("<삭제되었습니다.>");
                    System.out.println("");
                    break;

                // 그 외 번호 입력 시
                default:
                    System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
            }
        }
    }

}