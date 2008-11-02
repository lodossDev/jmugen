package org.lee.mugen.fight.section;

import java.util.HashMap;
import java.util.Map;

import org.lee.mugen.fight.section.elem.PlayerPowerbar;
import org.lee.mugen.fight.section.elem.Type;

public class Powerbar {
	PlayerPowerbar p1 = new PlayerPowerbar();
	PlayerPowerbar p2 = new PlayerPowerbar();
	
	Map<Integer, Type> level = new HashMap<Integer, Type>();

	public void parse(String name, String value) {
		if (name.startsWith("p1.")) {
			p1.parse(Type.getNext(name), value);
		} else if (name.startsWith("p2.")) {
			p2.parse(Type.getNext(name), value);
		}  else if (name.startsWith("level")) {
			String sNum = name.substring(2);
			int num = 0;
			if (sNum.length() > 0) {
				num = Integer.parseInt(sNum);
			}
			Type elem = level.get(num);
			if (elem == null) {
				elem = Type.buildType(Type.getNext(name));
				if (elem == null) {
					throw new IllegalStateException("You Must specifie type anim, font, or spr first");
				}
				level.put(num, elem);
			}
			Type.setValue(Type.getNext(name), elem, value);
			elem.parse(Type.getNext(name), value);
			
		}
	}
	
	public PlayerPowerbar getP1() {
		return p1;
	}

	public void setP1(PlayerPowerbar p1) {
		this.p1 = p1;
	}

	public PlayerPowerbar getP2() {
		return p2;
	}

	public void setP2(PlayerPowerbar p2) {
		this.p2 = p2;
	}

	public Map<Integer, Type> getLevel() {
		return level;
	}

	public void setLevel(Map<Integer, Type> level) {
		this.level = level;
	}


	
}
