package org.lee.mugen.fight.section.elem;

import org.lee.mugen.util.BeanTools;

public class AnimType extends CommonType {
	int action;

	public AnimType(int action) {
		this.action = action;
	}
	public AnimType() {
	}
	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}
	@Override
	public void parse(String name, String value) {
		if (name.equals("anim")) {
			action = Integer.parseInt(value);
			
		}
		
	}
	
}
