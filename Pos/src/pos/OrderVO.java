package pos;

//주문 정보 저장
public class OrderVO {
	private int id;
	private int menuId;
	private int quantity;
	private int tableNo;
	private boolean isPaid;
    private String menuName;

	// 생성자
	public OrderVO() {
	}

	public OrderVO(int id, int menuId, int quantity, int tableNo, boolean isPaid) {
		this.id = id;
		this.menuId = menuId;
		this.quantity = quantity;
		this.tableNo = tableNo;
		this.isPaid = isPaid;
	}

	// getter/setter
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMenuId() {
		return menuId;
	}

	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getTableNo() {
		return tableNo;
	}

	public void setTableNo(int tableNo) {
		this.tableNo = tableNo;
	}

	public boolean isPaid() {
		return isPaid;
	}

	public void setPaid(boolean isPaid) {
		this.isPaid = isPaid;
	}
    
	public String getMenuName() {
        return menuName;
    }
    
	public void setMenuName(String menuName) {
        this.menuName = menuName;
    }
}