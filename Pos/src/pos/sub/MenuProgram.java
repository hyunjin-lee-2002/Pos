package pos.sub;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import pos.dao.CategoryDAO;
import pos.dao.MenuDAO;
import pos.vo.CategoryVO;
import pos.vo.MenuVO;

public class MenuProgram {

    private final MenuDAO menuDAO = new MenuDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();

    public void menuManagementMenu(Scanner sc) throws SQLException {
        while (true) {
            System.out.println("\n메뉴 ---------------------------------------------------------------");
            System.out.println("      1. 등록                   2. 수정                   3. 삭제");
            System.out.println("--------------------------------------------------------------------");

            System.out.println("<메뉴현황>");
            List<MenuVO> menus = menuDAO.getAllMenus();
            for (MenuVO m : menus) {
                CategoryVO c = categoryDAO.getCategory(m.getCategoryId());
                System.out.println(m.getId() + " - 카테고리(" + c.getName() + "), 이름(" + m.getName() + "), 가격(" + m.getPrice() + ")");
            }

            System.out.print("\n[메뉴 번호를 입력해주세요]        * 0번 상위메뉴\n메뉴 번호 : ");
            int sub;
            try {
                sub = sc.nextInt();
                sc.nextLine();
            } catch (java.util.InputMismatchException e) {
                sc.nextLine();
                System.out.println("잘못된 입력입니다. 숫자를 입력해주세요.");
                continue;
            }

            if (sub == 0) break;

            switch (sub) {
                case 1: this.addMenu(sc); break;
                case 2: this.updateMenu(sc); break;
                case 3: this.deleteMenu(sc); break;
                default: System.out.println("잘못된 입력입니다.");
            }
        }
    }

    private void addMenu(Scanner sc) throws SQLException {
        System.out.println("\n등록 ..............................................................");
        System.out.println("위치 : 홈 > 메뉴 > 등록");

        System.out.println("\n<카테고리 현황>");
        List<CategoryVO> categories = categoryDAO.getAllCategories();
        for (CategoryVO c : categories) {
            System.out.println(c.getId() + " - 이모티콘(" + c.getEmoji() + "), 이름(" + c.getName() + "), 설명(" + c.getDescription() + ")");
        }

        int catId;
        System.out.print("\n1. 카테고리 번호 : ");
        try {
            catId = sc.nextInt();
            sc.nextLine();
        } catch (java.util.InputMismatchException e) {
            sc.nextLine();
            System.out.println("잘못된 입력입니다.");
            return;
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
            System.out.println("잘못된 입력입니다.");
            return;
        }

        menuDAO.addMenu(catId, name, price);
        System.out.println("<등록되었습니다.>");
    }

    private void updateMenu(Scanner sc) throws SQLException {
        System.out.println("\n수정 ..............................................................");
        System.out.println("위치 : 홈 > 메뉴 > 수정");

        int menuId;
        System.out.print("1. 메뉴번호 : ");
        try {
            menuId = sc.nextInt();
            sc.nextLine();
        } catch (java.util.InputMismatchException e) {
            sc.nextLine();
            System.out.println("잘못된 입력입니다.");
            return;
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
            System.out.println("잘못된 입력입니다.");
            return;
        }

        menuDAO.updateMenu(menuId, newName, newPrice);
        System.out.println("<수정되었습니다.>");
    }

    private void deleteMenu(Scanner sc) throws SQLException {
        System.out.println("\n삭제 ..............................................................");
        System.out.println("위치 : 홈 > 메뉴 > 삭제");

        int delId;
        System.out.print("1. 메뉴번호 : ");
        try {
            delId = sc.nextInt();
            sc.nextLine();
        } catch (java.util.InputMismatchException e) {
            sc.nextLine();
            System.out.println("잘못된 입력입니다.");
            return;
        }

        menuDAO.deleteMenu(delId);
        System.out.println("<삭제되었습니다.>");
    }
}
