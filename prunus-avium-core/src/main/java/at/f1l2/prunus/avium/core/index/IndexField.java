package at.f1l2.prunus.avium.core.index;

public enum IndexField {

	ID("id"),

	TITLE("title"),

	PROGRAM_TITLE("programTitle"),

	SUB_TITLE("subTitle"),

	HREF("href"),

	BEGIN("begin"),

	END("end");

	private String field;

	IndexField(String field) {
		this.field = field;
	}

	public String getField() {
		return field;
	}

}
