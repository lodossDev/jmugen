package org.lee.mugen.sprite.cns.eval.trigger.function.spriteCns;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.parser.type.Valueable;
import org.lee.mugen.sprite.cns.eval.function.SpriteCnsTriggerFunction;

public class Roundno extends SpriteCnsTriggerFunction {
	public Roundno() {
		super("roundno", new String[] {});
	}

	@Override
	public Object getValue(String spriteId, Valueable... params) {
		return StateMachine.getInstance().getGameState().getRoundno();
	}
}
