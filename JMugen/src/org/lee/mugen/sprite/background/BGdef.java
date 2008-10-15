package org.lee.mugen.sprite.background;

public class BGdef {
	private Stage parent = null;

	public BGdef(Stage stage) {
		parent = stage;
	}

	// Filename of sprite data
	private String spr;

	// Set to 1 if you want to clear the screen to magenta before
	// drawing layer 0 (the default background). Good for spotting "holes"
	// in your background.
	// Remember to turn this off when you are done debugging the background,
	// because it slows down performance.
	private int debugbg = 0;

	public int getDebugbg() {
		return debugbg;
	}

	public void setDebugbg(int debugbg) {
		this.debugbg = debugbg;
	}

	public String getSpr() {
		return spr;
	}

	public void setSpr(String spr) {
		this.spr = spr;
	}
}
