package org.lee.mugen.sprite.cns.type.function;

import org.lee.mugen.parser.type.Valueable;
import org.lee.mugen.sprite.cns.eval.function.StateCtrlFunction;

public class Gamemakeanim extends StateCtrlFunction {

	// TODO : gamemakeanim
	public Gamemakeanim() {
		super("gamemakeanim", new String[] {"value", "under", "pos", "random"});
	}
	
	@Override
	public Valueable[] parse(String name, String value) {
		return null;
	}
	public void addParam(String name, Valueable[] param) {

	}
}
