package me.nucha.uhcc.language;

public enum Languages {
	ENGLISH("English"), JAPANESE("Japanese");

	private String name;

	private Languages(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
