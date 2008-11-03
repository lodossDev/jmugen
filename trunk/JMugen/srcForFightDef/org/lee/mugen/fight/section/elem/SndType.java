package org.lee.mugen.fight.section.elem;

import org.lee.mugen.util.BeanTools;

public class SndType extends CommonType implements Cloneable {
	int grp;
	int num;
	public int getGrp() {
		return grp;
	}
	public void setGrp(int grp) {
		this.grp = grp;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	@Override
	public void parse(String name, String value) {
		if (name.equals("snd")) {
			int[] res = (int[]) BeanTools.getConvertersMap().get(int[].class).convert(value);
			grp = res[0];
			num = res[1];
			
		}
		
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
