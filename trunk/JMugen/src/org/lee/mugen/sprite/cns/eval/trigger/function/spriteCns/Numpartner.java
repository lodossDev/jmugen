package org.lee.mugen.sprite.cns.eval.trigger.function.spriteCns;

import java.util.Collection;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.parser.type.Valueable;
import org.lee.mugen.sprite.character.Sprite;
import org.lee.mugen.sprite.cns.eval.function.SpriteCnsTriggerFunction;

public class Numpartner extends SpriteCnsTriggerFunction {

	public Numpartner() {
		super("numpartner", new String[] {});
	}

	@Override
	public Object getValue(String spriteId, Valueable... params) {
		Sprite sprite = StateMachine.getInstance().getSpriteInstance(spriteId);
		Collection<Sprite> sprites = StateMachine.getInstance().getPartners(sprite);
		return StateMachine.countNormalPlayer(sprites);
	}
}
