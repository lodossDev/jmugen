package org.lee.mugen.sprite.cns.eval.trigger.function.spriteCns;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.parser.type.Valueable;
import org.lee.mugen.sprite.cns.eval.function.SpriteCnsTriggerFunction;

public class Authorname extends SpriteCnsTriggerFunction {

	public Authorname() {
		super("authorname", new String[] {});
	}
	@Override
	public Object getValue(String spriteId, Valueable... params) {
		return StateMachine.getInstance().getSpriteDef(spriteId).getInfo().getAuthor();
	}
}
