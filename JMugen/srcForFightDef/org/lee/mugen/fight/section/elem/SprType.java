package org.lee.mugen.fight.section.elem;

public class SprType extends Type {
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
	
	
}
