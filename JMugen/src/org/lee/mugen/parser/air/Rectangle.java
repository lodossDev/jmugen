package org.lee.mugen.parser.air;

public class Rectangle {
	public Rectangle(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	private int x1;
	private int y1;
	private int x2;
	private int y2;
	public int getX1() {
		return x1;
	}
	public void setX1(int x1) {
		this.x1 = x1;
	}
	public int getX2() {
		return x2;
	}
	public void setX2(int x2) {
		this.x2 = x2;
	}
	public int getY1() {
		return y1;
	}
	public void setY1(int y1) {
		this.y1 = y1;
	}
	public int getY2() {
		return y2;
	}
	public void setY2(int y2) {
		this.y2 = y2;
	}
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof Rectangle))
			return false;
		Rectangle r = (Rectangle) obj;
		return this.x1 == r.x1 &&
		this.y1 == r.y1 &&
		this.x2 == r.x2 &&
		this.y2 == r.y2;
	}
	
}
