package org.lee.mugen.sprite.background;

public class StageInfo {
	private Stage parent = null;
	public StageInfo(Stage stage) {
		parent = stage;
	}
	 //Z offset for drawing
	//Adjust this value to move the ground level up/down in the screen.
	//It's the position where the players stand at.
	//Up - smaller, Down - larger
	private int zoffset = 190;

	//Leave this at 1. It makes the players face each other
	private int autoturn = 1;

	//Set the following to 1 to have the background be reset between
	//rounds.
	private int resetbg = 1;
	
	private int hires = 0;

	public int getHires() {
		return hires;
	}

	public void setHires(int hires) {
		this.hires = hires;
		if (this.hires == 1) {
			parent.getScaling().setXscale(0.5f);
			parent.getScaling().setYscale(0.5f);
			
		}
	}

	public int getAutoturn() {
		return autoturn;
	}

	public void setAutoturn(int autoturn) {
		this.autoturn = autoturn;
	}

	public int getResetbg() {
		return resetbg;
	}

	public void setResetbg(int resetbg) {
		this.resetbg = resetbg;
	}

	public int getZoffset() {
		return zoffset;
	}

	public void setZoffset(int zoffset) {
		this.zoffset = zoffset;
	}
}
