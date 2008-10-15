package org.lee.mugen.sprite.cns.eval.trigger.function.spriteCns;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.parser.type.Valueable;
import org.lee.mugen.sprite.cns.eval.function.SpriteCnsTriggerFunction;
/**
 * 
 * @author Dr Wong
 * @category Trigger : Complete
 */
public class Gametime extends SpriteCnsTriggerFunction {

	public Gametime() {
		super("gametime", new String[0]);
	}
	@Override
	public Object getValue(String spriteId, Valueable... params) {
		return StateMachine.getInstance().getGameState().getGameTime();
	}
}
