package org.lee.mugen.fight.section.elem;

import java.awt.Point;

import org.lee.mugen.fight.section.elem.FontType.ALIGNMT;
import org.lee.mugen.snd.Snd;
import org.lee.mugen.sprite.entity.PointF;
import org.lee.mugen.util.BeanTools;

public class Type {
	
	public static Type buildType(String type) {
		Type result = null;
		if (type.equalsIgnoreCase("anim")) {
			result = new AnimType();
		} else if (type.equalsIgnoreCase("spr")) {
			result = new SprType();
		} else if (type.equalsIgnoreCase("font")) {
			result = new FontType();
		}
		return result;
	}
	public static String getNext(String name) {
		return name.substring(name.indexOf(".") + 1);
	}
	
	Point offset = new Point();
	int displaytime = -1;
	int facing = 1;
	int vfacing = 1;
	Snd snd;
	int sndtime;
	int layerno = 0;
	PointF scale = new PointF(1,1);
	
	public Point getOffset() {
		return offset;
	}

	public void setOffset(Point offset) {
		this.offset = offset;
	}

	public int getFacing() {
		return facing;
	}

	public void setFacing(int facing) {
		this.facing = facing;
	}

	public int getVfacing() {
		return vfacing;
	}

	public void setVfacing(int vfacing) {
		this.vfacing = vfacing;
	}

	public int getLayerno() {
		return layerno;
	}

	public void setLayerno(int layerno) {
		this.layerno = layerno;
	}

	public PointF getScale() {
		return scale;
	}

	public void setScale(PointF scale) {
		this.scale = scale;
	}

	public int getSndtime() {
		return sndtime;
	}

	public void setSndtime(int sndtime) {
		this.sndtime = sndtime;
	}

	public Snd getSnd() {
		return snd;
	}

	public void setSnd(Snd snd) {
		this.snd = snd;
	}



	public int getDisplaytime() {
		return displaytime;
	}

	public void setDisplaytime(int displaytime) {
		this.displaytime = displaytime;
	}

	public void parse(String name, String value) {

		if (name.equalsIgnoreCase("offset")) {
			offset = (Point) BeanTools.getConvertersMap().get(Point.class).convert(value);
		} else if (name.equalsIgnoreCase("displaytime")) {
			displaytime = (Integer) BeanTools.getConvertersMap().get(Integer.class).convert(value);
		} else if (name.equalsIgnoreCase("facing")) {
			facing = (Integer) BeanTools.getConvertersMap().get(Integer.class).convert(value);
		} else if (name.equalsIgnoreCase("vfacing")) {
			vfacing = (Integer) BeanTools.getConvertersMap().get(Integer.class).convert(value);
		} else if (name.equalsIgnoreCase("snd")) {
			snd = (Snd) BeanTools.getConvertersMap().get(Snd.class).convert(value);
		} else if (name.equalsIgnoreCase("sndtime")) {
			sndtime = (Integer) BeanTools.getConvertersMap().get(Integer.class).convert(value);
		} else if (name.equalsIgnoreCase("layerno")) {
			layerno = (Integer) BeanTools.getConvertersMap().get(Integer.class).convert(value);
		} else if (name.equalsIgnoreCase("scale")) {
			scale = (PointF) BeanTools.getConvertersMap().get(PointF.class).convert(value);
		}

	}

	public static void setValue(String name, Type elem, String value) {
		if (name.equalsIgnoreCase("anim") 
				&& name.equalsIgnoreCase("spr")
						&& name.equalsIgnoreCase("font")) {
			if (elem instanceof AnimType) {
				AnimType e = (AnimType) elem;
				e.setAction(Integer.parseInt(value));
			} else if (elem instanceof SprType) {
				SprType e = (SprType) elem;
				int[] grpNo = (int[]) BeanTools.getConvertersMap().get(int[].class).convert(value);
				e.setSpritegrp(grpNo[0]);
				e.setSpriteno(grpNo[1]);
				
			} else if (elem instanceof FontType) {
				FontType e = (FontType) elem;
				int[] nba = (int[]) BeanTools.getConvertersMap().get(int[].class).convert(value);
				int fontno = nba[0];
				e.setFontno(fontno);
				if (nba.length > 1) {
					int fontbank = nba[1];
					e.setFontbank(fontbank);
				}
				if (nba.length > 2) {
					int alignmt = nba[2];
					e.setAlignmt(ALIGNMT.getValue(alignmt));
				}
			}		
							
						}
	}
}
