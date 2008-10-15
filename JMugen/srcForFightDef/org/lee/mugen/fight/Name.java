package org.lee.mugen.fight;

import java.awt.Point;

import org.lee.mugen.parser.type.Valueable;
import org.lee.mugen.sprite.parser.ExpressionFactory;
import org.lee.mugen.sprite.parser.Parser.AccessorParser;
import org.lee.mugen.sprite.parser.Parser.GroupText;
import org.lee.mugen.util.BeanTools;

public class Name implements AccessorParser {
	private PlayerName p1 = new PlayerName();
	private PlayerName p2 = new PlayerName();
	
	public static class PlayerName {
		private Point pos = new Point();
		private NameInner name = new NameInner();
		public NameInner getName() {
			return name;
		}

		public void setName(NameInner name) {
			this.name = name;
		}

		public Point getPos() {
			return pos;
		}

		public void setPos(Point pos) {
			this.pos = pos;
		}

		public void process() {
			name.process();
			
		}
		
	}
	public static class NameInner {
		private int[] font = new int[3];

		public int[] getFont() {
			return font;
		}

		public void process() {
			
		}

		public void setFont(int[] font) {
			this.font = font;
		}
	}
	public PlayerName getP1() {
		return p1;
	}
	public void setP1(PlayerName p1) {
		this.p1 = p1;
	}
	public PlayerName getP2() {
		return p2;
	}
	public void setP2(PlayerName p2) {
		this.p2 = p2;
	}
	public boolean isParse() {
		return true;
	}
	public void parse(GroupText grp) throws Exception {
		for (String key : grp.getKeyValues().keySet()) {
			String accessor = key;
			Object[] objectValues = null;

			Valueable[] values = ExpressionFactory.evalExpression(grp
					.getKeyValues().get(key));

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
	public void process() {
		p1.process();
		p2.process();
	}
}
