package pos;
    
//카테고리 정보 저장
public class CategoryVO {
	private int categoryId;
	private String emoji;
	private String name;
	private String explanation;

	// 생성자
	public CategoryVO() {
	}

	public CategoryVO(int categoryId, String emoji, String name, String explanation) {
		this.categoryId = categoryId;
		this.emoji = emoji;
		this.name = name;
		this.explanation = explanation;
	}

	// getter/setter
	public int getId() {
		return categoryId;
	}

	public void setId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getEmoji() {
		return emoji;
	}

	public void setEmoji(String emoji) {
		this.emoji = emoji;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return explanation;
	}

	public void setDescription(String description) {
		this.explanation = description;
	}
}