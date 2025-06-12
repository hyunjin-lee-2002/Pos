package pos;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import pos.CategoryDAO;
import pos.CategoryVO;
import pos.MenuDAO;
import pos.MenuVO;

public class PosMain {

    // 프로그램의 시작점 (main 메서드)
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in); // 사용자 입력을 받기 위한 Scanner 객체 생성

        // 메인 반복 루프 (0번 입력 전까지 반복 실행)
        while (true) {
            // 메인 화면 출력
            System.out.println("\n********************************************************************");
            System.out.println("                           프로그램 시작                        ");
            System.out.println("********************************************************************");
            System.out.println("\n시작메뉴 ===========================================================");
            System.out.println("      1. 주문 / 결제      2. 매출      3. 메뉴      4. 카테고리");
            System.out.println("====================================================================");
            System.out.print("[시작메뉴 번호를 입력해주세요]        * 0번 프로그램 종료\n시작메뉴 번호 : ");

            int choice = sc.nextInt(); // 사용자 메뉴 선택 입력

            try {
                switch (choice) {
                    case 0: // 프로그램 종료
                        System.out.println("\n********************************************************************");
                        System.out.println("                           프로그램 종료                        ");
                        System.out.println("********************************************************************");
                        return; // main 메서드 종료 → 프로그램 종료

                    case 3: // 메뉴 관리 화면으로 이동
                        menuManagementMenu(sc); // 메뉴 관리 함수 호출
                        break;

                    default: // 1, 2, 4 외 다른 숫자 입력 시
                        System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
                }
            } catch (SQLException e) {
                e.printStackTrace(); // DB 작업 중 발생하는 예외 출력
            }
        }
    }

    // [메뉴 관리] 메뉴
    static void menuManagementMenu(Scanner sc) throws SQLException {
        // DAO 객체 생성 → DB 작업을 위한 객체
        MenuDAO menuDAO = new MenuDAO();
        CategoryDAO categoryDAO = new CategoryDAO();

        // 메뉴 관리 반복 루프
        while (true) {
            System.out.println("\n메뉴 ---------------------------------------------------------------");
            System.out.println("      1. 등록                   2. 수정                   3. 삭제");
            System.out.println("--------------------------------------------------------------------");
            System.out.println("<메뉴현황>");

            // 현재 등록된 모든 메뉴 항목 조회 후 출력
            List<MenuVO> menus = menuDAO.getAllMenus();
            for (MenuVO m : menus) {
                CategoryVO c = categoryDAO.getCategory(m.getCategoryId()); // 메뉴의 카테고리 정보 조회
                System.out.println(m.getId() + " - 카테고리(" + m.getCategoryId() + "), 이름(" + m.getName() + "), 가격(" + m.getPrice() + ")");
            }

            
        	// 하위 메뉴 선택 입력
            
            /*
             ==catch (java.util.InputMismatchException e)
            
            - 사용자가 숫자(int) 대신 문자를 입력하는 등 입력 형식이 맞지 않을 경우
           	- nextInt() 같은 메서드는 InputMismatchException 예외를 발생시킴
            - 이 catch 블록은 그 예외를 잡아서 프로그램이 비정상 종료되지 않도록 막아줌
            - 또한 sc.nextLine()으로 입력 버퍼를 비워줘야 다음 입력이 꼬이지 않음
            */
             

            System.out.print("\n[메뉴 번호를 입력해주세요]        * 0번 상위메뉴\n메뉴 번호 : ");
            int sub;
            try {
                sub = sc.nextInt(); // 정수 입력
                sc.nextLine(); // 개행 문자 처리
            } catch (java.util.InputMismatchException e) {
                sc.nextLine(); // 입력 오류 발생 시 버퍼 비우기
                System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
                continue;
            }

            if (sub == 0) break; // 0번 선택 시 상위 메뉴로 복귀

            switch (sub) {
                case 1: // 메뉴 등록
                    System.out.println("\n등록 ..............................................................");
                    System.out.println("위치 : 홈 > 메뉴 > 등록");

                    // 카테고리 목록 출력
                    System.out.println("\n<카테고리 현황>");
                    List<CategoryVO> categories = categoryDAO.getAllCategories();
                    for (CategoryVO c : categories) {
                        System.out.println(c.getId() + " - 이모티콘(" + c.getEmoji() + "), 이름(" + c.getName() + "), 설명(" + c.getDescription() + ")");
                    }

                    // 사용자에게 입력 받기
                    int catId;
                    System.out.print("\n1. 카테고리 번호 : ");
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

                    // DB에 메뉴 등록 요청
                    menuDAO.addMenu(catId, name, price);
                    System.out.println("<등록되었습니다.>");
                    System.out.println("");
                    break;

                case 2: // 메뉴 수정
                    System.out.println("\n수정 ..............................................................");
                    System.out.println("위치 : 홈 > 메뉴 > 수정");

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

                    // DB에 수정 요청
                    menuDAO.updateMenu(menuId, newName, newPrice);
                    System.out.println("<수정되었습니다.>");
                    System.out.println("");
                    break;

                case 3: // 메뉴 삭제
                    System.out.println("\n삭제 ..............................................................");
                    System.out.println("위치 : 홈 > 메뉴 > 삭제");

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

                    // DB에서 삭제
                    menuDAO.deleteMenu(delId);
                    System.out.println("<삭제되었습니다.>");
                    System.out.println("");
                    break;

                default: // 1~3 외 입력 처리
                    System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
            }
        }
    }
}
