package Pos;

// 메뉴 정보를 담는 VO (Value Object) 클래스
// DB의 menu 테이블 구조에 대응되는 자바 객체
public class MenuVO {

	// 필드 선언 (DB의 컬럼과 1:1 매칭됨)
	private int id;           // 메뉴 고유 번호 (menu_id)
	private int categoryId;   // 해당 메뉴가 속한 카테고리 번호 (category_id)
	private String name;      // 메뉴 이름
	private int price;        // 메뉴 가격

	// 기본 생성자 (비어 있는 상태로 객체 생성 가능)
	public MenuVO() {
	// 아무 값도 없이 초기화됨
	}

	// 필드 전체를 초기화하는 생성자 (필수 데이터가 있을 때 사용)
	public MenuVO(int id, int categoryId, String name, int price) {
		this.id = id;                   // menu_id
		this.categoryId = categoryId;   // category_id
		this.name = name;               // 메뉴 이름
		this.price = price;             // 메뉴 가격
	}

	// getter: 외부에서 필드 값을 가져올 때 사용
	public int getId() {
		return id;
	}

	// setter: 외부에서 필드 값을 설정할 때 사용
	public void setId(int id) {
		this.id = id;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}
}
