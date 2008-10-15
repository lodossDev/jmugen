package org.lee.mugen.sprite.background;

public class Scaling {
	private float xscale = 1;
	private float yscale = 1;
	
	private Stage parent = null;
	
	public Scaling(Stage stage) {
		parent = stage;
	}
	 //No need to change these values
	 private int topz     = 0;       //Top z-coordinate for scaling
	 private int botz     = 50;      //Bottom z-coordinate for scaling
	 private int topscale = 1;       //Scale to use at top
	 private float botscale = 1.2f;     //Scale to use at bottom
	 
	 ////////////////////////////////
	public float getBotscale() {
		return botscale;
	}
	public void setBotscale(float botscale) {
		this.botscale = botscale;
	}
	public int getBotz() {
		return botz;
	}
	public void setBotz(int botz) {
		this.botz = botz;
	}
	public int getTopscale() {
		return topscale;
	}
	public void setTopscale(int topscale) {
		this.topscale = topscale;
	}
	public int getTopz() {
		return topz;
	}
	public void setTopz(int topz) {
		this.topz = topz;
	}
	public float getXscale() {
		return xscale;
	}
	public void setXscale(float xscale) {
		this.xscale = xscale;
	}
	public float getYscale() {
		return yscale;
	}
	public void setYscale(float yscale) {
		this.yscale = yscale;
	}
}
