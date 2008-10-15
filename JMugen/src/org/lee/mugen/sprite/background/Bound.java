package org.lee.mugen.sprite.background;

public class Bound {
	private Stage parent = null;
	public Bound(Stage stage) {
		parent = stage;
	}
	
	 //Distance from left/right edge of screen that player can move to
	 //Typically 15
	private int screenleft = 15; //Dist from left of screen that player can move to
	private int screenright = 15;   //Right edge
	
	
	
	
	public int getScreenright() {
		return screenright;
	}
	public void setScreenright(int screenright) {
		this.screenright = screenright;
	}
	public int getScreenleft() {
		return screenleft;
	}
	public void setScreenleft(int screenleft) {
		this.screenleft = screenleft;
	}
}
