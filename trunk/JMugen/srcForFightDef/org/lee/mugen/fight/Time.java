package org.lee.mugen.fight;

import java.awt.Point;

import org.lee.mugen.parser.type.Valueable;
import org.lee.mugen.sprite.parser.ExpressionFactory;
import org.lee.mugen.sprite.parser.Parser.AccessorParser;
import org.lee.mugen.sprite.parser.Parser.GroupText;
import org.lee.mugen.util.BeanTools;

public class Time implements AccessorParser {
/*
pos = 160,23
; Uncomment the following line to use a bg component
;bg.spr = 
; Time count display
counter.offset = 0,0
counter.font = 2,0, 0
; Ticks for each count
framespercount = 60
 */

	public static class Counter {
		private Point offset = new Point();
		private int[] font = new int[3];
		public Point getOffset() {
			return offset;
		}
		public void setOffset(Point offset) {
			this.offset = offset;
		}
		public int[] getFont() {
			return font;
		}
		public void setFont(int[] font) {
			this.font = font;
		}
		
	}
	
	private Point pos = new Point();
	private Counter counter = new Counter();
	private int framespercount = 60;
	public Point getPos() {
		return pos;
	}
	public void setPos(Point pos) {
		this.pos = pos;
	}
	public Counter getCounter() {
		return counter;
	}
	public void setCounter(Counter counter) {
		this.counter = counter;
	}
	public int getFramespercount() {
		return framespercount;
	}
	public void setFramespercount(int framespercount) {
		this.framespercount = framespercount;
	}
	@Override
	public boolean isParse() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void parse(GroupText grp) throws Exception {
		for (String key : grp.getKeyValues().keySet()) {
			String accessor = key;
			Object[] objectValues = null;

			Valueable[] values = ExpressionFactory.evalExpression(grp.getKeyValues().get(key));

			objectValues = new Object[values.length];
			for (int i = 0; i < objectValues.length; ++i) {
				objectValues[i] = values[i].getValue(null);
			}

			try {
				if (objectValues.length == 1) {
					BeanTools.setObject(this, accessor, objectValues[0]);
				} else if (objectValues.length > 1) {
					BeanTools.setObject(this, accessor, objectValues);

				}
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println(this.getClass().getName()
						+ " setProperties cause problem ! >" + accessor + " = "
						+ objectValues);
			}
		}
	}
}
