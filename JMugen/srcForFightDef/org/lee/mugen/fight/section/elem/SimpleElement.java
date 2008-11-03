package org.lee.mugen.fight.section.elem;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import org.lee.mugen.util.BeanTools;

public class SimpleElement {
	Point pos;
	Map<Integer, Type> bg = new HashMap<Integer, Type>();
	public Point getPos() {
		return pos;
	}
	public void setPos(Point pos) {
		this.pos = pos;
	}
	public Map<Integer, Type> getBg() {
		return bg;
	}
	public void setBg(Map<Integer, Type> bg) {
		this.bg = bg;
	}
	
	public void parse(String name, String value) {
		if (name.equalsIgnoreCase("pos")) {
			pos = (Point) BeanTools.getConvertersMap().get(Point.class).convert(value);
		} else if (name.startsWith("bg")) {
			String sNum = name.substring(2, name.indexOf("."));
			int num = 0;
			if (sNum.length() > 0) {
				num = Integer.parseInt(sNum);
			}
			Type elem = bg.get(num);
			if (elem == null) {
				elem = new Type();
				bg.put(num, elem);
			}
			elem.setType(Type.getNext(name), elem, value);
			elem.parse(Type.getNext(name), value);
			
		}
	}
}
