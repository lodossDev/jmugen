package org.lee.mugen.fight.section.elem;

import org.lee.mugen.util.BeanTools;

public class SprType extends CommonType {
	int spritegrp;
	int spriteno;
	
	public SprType(int spritegrp, int spriteno) {
		this.spritegrp = spritegrp;
		this.spriteno = spriteno;
	}

	public SprType() {
	}

	public int getSpritegrp() {
		return spritegrp;
	}

	public void setSpritegrp(int spritegrp) {
		this.spritegrp = spritegrp;
	}

	public int getSpriteno() {
		return spriteno;
	}

	public void setSpriteno(int spriteno) {
		this.spriteno = spriteno;
	}

	@Override
	public void parse(String name, String value) {
		if (name.equals("spr")) {
			int[] res = (int[]) BeanTools.getConvertersMap().get(int[].class).convert(value);
			spritegrp = res[0];
			spriteno = res[1];
			
		}
		
	}
	
	
}
