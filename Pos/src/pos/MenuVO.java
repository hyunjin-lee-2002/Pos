package pos; // 이 클래스가 포함된 패키지명

/**
 * MenuVO 클래스는 '메뉴(menu)' 정보를 담는 데이터 객체입니다.
 * 이 클래스는 데이터 전송, 조회, 수정 시 각 메뉴 항목의 정보를 안전하게 담기 위한 VO(Value Object)입니다.
 */
public class MenuVO {
	
	// 필드(속성): 메뉴 정보를 구성하는 요소들

	private int id;           // 메뉴 ID (고유 식별자, PK 역할)
	private int categoryId;   // 메뉴가 속한 카테고리의 ID (FK 역할)
	private String name;      // 메뉴 이름
	private int price;        // 메뉴 가격

	// 기본 생성자 (매개변수가 없는 생성자)
	// 객체 생성 후 set 메서드를 통해 값을 개별적으로 설정할 때 사용
	public MenuVO() {
	}

	// 모든 필드를 초기화하는 생성자
	// DB 조회 결과나 입력값을 한 번에 담아서 객체를 만들 때 사용
	public MenuVO(int id, int categoryId, String name, int price) {
		this.id = id;
		this.categoryId = categoryId;
		this.name = name;
		this.price = price;
	}

	// 아래부터는 각 필드에 접근하거나 수정할 수 있도록 도와주는 getter/setter 메서드

	// 메뉴 ID를 반환
	public int getId() {
		return id;
	}

	// 메뉴 ID를 설정
	public void setId(int id) {
		this.id = id;
	}

	// 카테고리 ID를 반환
	public int getCategoryId() {
		return categoryId;
	}

	// 카테고리 ID를 설정
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	// 메뉴 이름을 반환
	public String getName() {
		return name;
	}

	// 메뉴 이름을 설정
	public void setName(String name) {
		this.name = name;
	}

	// 메뉴 가격을 반환
	public int getPrice() {
		return price;
	}

	// 메뉴 가격을 설정
	public void setPrice(int price) {
		this.price = price;
	}
}
