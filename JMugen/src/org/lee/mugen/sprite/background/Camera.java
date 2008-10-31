package org.lee.mugen.sprite.background;

import java.awt.Rectangle;
import java.util.Random;

import org.lee.mugen.sprite.entity.Shake;


public class Camera {
	private int width = 320;
	private int height = 240;
	private Stage parent = null;
	public Camera(Stage stage) {
		parent = stage;
	}

	private int x;
	private int y;
	
	//Camera starting position: Usually 0 for both
	private int startx = 0;
	private int starty = 0;

	 //Left and right bound of camera
	 //You may want to fiddle a bit with these values to make sure the
	 //background doesn't move too far or too little
	 //***
	 private int boundleft = -95;
	 private int boundright = 95;

	 //High and low bound of camera
	 //High is a negative number. Make is more negative if you want to
	 //camera to be able to move higher.
	 //Low should usually be 0.
	 //If omitted, defaults to -25 and 0 respectively
	 //***
	 private int boundhigh = -25;
	 private int boundlow = 0;

	 //This is how much the camera will move vertically towards the
	 //highest player. Valid values are from 0 to 1. A value of 0 will mean
	 //the camera does not move up at all. A value of 1 will makes the camera
	 //follow the highest player.
	 //Typically .2 for normal-sized backgrounds. You may need to pull this
	 //value up for taller backgrounds.
	 private float verticalfollow = .2f;

	 //Minimum vertical distance the highest player has to be from the floor,
	 //before the camera starts to move up to follow him. For extremely
	 //tall stages, a floor tension of about 20-30 coupled with a
	 //vertical-follow of .8 allows the camera to follow the action.
	 private int floortension = 0;

	 //Horizontal distance player is from edge before camera starts to move
	 //left or right. Typically 50 or 60.
	 private int tension = 50;

	public int getBoundhigh() {
		return boundhigh;
	}

	public void setBoundhigh(int boundhigh) {
		this.boundhigh = boundhigh;
	}

	public int getBoundleft() {
		return boundleft;
	}

	public void setBoundleft(int boundleft) {
		this.boundleft = boundleft;
	}

	public int getBoundlow() {
		return boundlow;
	}

	public void setBoundlow(int boundlow) {
		this.boundlow = boundlow;
	}

	public int getBoundright() {
		return boundright;
	}

	public void setBoundright(int boundright) {
		this.boundright = boundright;
	}

	public int getFloortension() {
		return floortension;
	}

	public void setFloortension(int floortension) {
		this.floortension = floortension;
	}

	public int getStartx() {
		return startx;
	}

	public void setStartx(int startx) {
		this.startx = startx;
	}

	public int getStarty() {
		return starty;
	}

	public void setStarty(int starty) {
		this.starty = starty;
	}

	public int getTension() {
		return tension;
	}

	public void setTension(int tension) {
		this.tension = tension;
	}

	public float getVerticalfollow() {
		return verticalfollow;
	}

	public void setVerticalfollow(float verticalfollow) {
		this.verticalfollow = verticalfollow;
	}

	private Random r = new Random();
	private Shake envShake = new Shake();
	public Rectangle getCameraView() {
		return new Rectangle(x, y, width, height);
	}
	
	public int getX() {
		return getXNoShaKe() + (envShake.getTime() > 0? r.nextInt(envShake.getAmpl() <= 0? 1: 2): 0);
	}
	
	public int getXNoShaKe() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y + (envShake.getTime() > 0? r.nextInt(envShake.getAmpl() <= 0? 5: 5): 0);
	}
	
	public int getYNoShake() {
		return y;
	}

	
	public void setY(int y) {
		this.y = y;
	
	}

	public void addX(int i) {
		x += i;
	}
	 
	public void addY(int i) {
		y += i;
	}

	public Shake getEnvShake() {
		return envShake;
	}

	public void setEnvShake(Shake envShake) {
		this.envShake = envShake;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void init() {
		x = getStartx();
		y = getStarty();
		
	}

}
