package org.lee.mugen.fight.system.elem;

import java.awt.Point;
import java.awt.Rectangle;

import org.lee.mugen.fight.section.Section;
import org.lee.mugen.sprite.entity.PointF;
import org.lee.mugen.util.BeanTools;

public class StageDisplay implements Section {

	
	boolean enable;
	Point pos;
	java.awt.Rectangle rectangle;
	PointF scale;
	Point camera;
	
	
	public boolean isEnable() {
		return enable;
	}


	public void setEnable(boolean enable) {
		this.enable = enable;
	}


	public Point getPos() {
		return pos;
	}


	public void setPos(Point pos) {
		this.pos = pos;
	}


	public java.awt.Rectangle getRectangle() {
		return rectangle;
	}


	public void setRectangle(java.awt.Rectangle rectangle) {
		this.rectangle = rectangle;
	}


	public PointF getScale() {
		return scale;
	}


	public void setScale(PointF scale) {
		this.scale = scale;
	}


	public Point getCamera() {
		return camera;
	}


	public void setCamera(Point camera) {
		this.camera = camera;
	}


	@Override
	public void parse(Object root, String name, String value) throws Exception {
		if (name.equals("enable")) {
			enable = Integer.parseInt(value) != 0;
		} else if (name.equals("pos")) {
			pos = (Point) BeanTools.getConvertersMap().get(Point.class).convert(value);
		} else if (name.equals("rectangle")) {
			rectangle = (Rectangle) BeanTools.getConvertersMap().get(Rectangle.class).convert(value);
		} else if (name.equals("scale")) {
			scale = (PointF) BeanTools.getConvertersMap().get(PointF.class).convert(value);
		} else if (name.equals("camera")) {
			camera = (Point) BeanTools.getConvertersMap().get(Point.class).convert(value);
		}
	}



}
