package org.lee.mugen.sprite.background;

public class Info {
	private Stage parent = null;
	public Info(Stage stage) {
		parent = stage;
	}
	
	
	//Name of the stage.
	private String name = "";
	private String author = "";
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	 
}
