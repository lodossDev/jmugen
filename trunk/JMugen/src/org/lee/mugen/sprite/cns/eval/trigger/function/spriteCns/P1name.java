package org.lee.mugen.sprite.cns.eval.trigger.function.spriteCns;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.parser.type.Valueable;
import org.lee.mugen.sprite.cns.eval.function.SpriteCnsTriggerFunction;

public class P1name extends SpriteCnsTriggerFunction {

	public P1name() {
		super("p1name", new String[] {});
	}
	@Override
	public Object getValue(String spriteId, Valueable... params) {
		return StateMachine.getInstance().getSpriteDef("1").getInfo().getDisplayname();
	}
	

}
