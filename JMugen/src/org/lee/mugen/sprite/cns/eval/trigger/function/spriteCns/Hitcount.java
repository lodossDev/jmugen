package org.lee.mugen.sprite.cns.eval.trigger.function.spriteCns;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.parser.type.Valueable;
import org.lee.mugen.sprite.character.Sprite;
import org.lee.mugen.sprite.cns.eval.function.SpriteCnsTriggerFunction;

public class Hitcount extends SpriteCnsTriggerFunction {

	public Hitcount() {
		super("hitcount", new String[] {});
	}
	@Override
	public Object getValue(String spriteId, Valueable... params) {
		Sprite sprite = StateMachine.getInstance().getFightEngine().getTarget(StateMachine.getInstance().getSpriteInstance(spriteId), -1);
		if (sprite == null)
			return 0;
		return sprite.getInfo().getHitcount();
	}

}
