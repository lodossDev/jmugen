package org.lee.mugen.sprite.cns.eval.trigger.function.spriteCns;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.parser.type.Valueable;
import org.lee.mugen.sprite.cns.eval.function.SpriteCnsTriggerFunction;

public class Ishometeam extends SpriteCnsTriggerFunction {
	// TODO Dans les modes Arcade, l'ordinateur est toujours considéré comme l'équipe maison
	public Ishometeam() {
		super("ishometeam", new String[] {});
	}
	@Override
	public Object getValue(String spriteId, Valueable... params) {
		return StateMachine.getInstance().isHomeTeam(spriteId)? 1: 0;
	}

}
