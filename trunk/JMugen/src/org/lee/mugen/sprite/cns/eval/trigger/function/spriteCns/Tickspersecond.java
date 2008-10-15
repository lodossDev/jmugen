package org.lee.mugen.sprite.cns.eval.trigger.function.spriteCns;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.parser.type.Valueable;
import org.lee.mugen.sprite.cns.eval.function.SpriteCnsTriggerFunction;

public class Tickspersecond extends SpriteCnsTriggerFunction {

	public Tickspersecond() {
		super("tickspersecond", new String[] {});
	}
	@Override
	public Object getValue(String spriteId, Valueable... params) {
		return (int)StateMachine.getInstance().getWindow().getTimer().getFps();
	}

}
