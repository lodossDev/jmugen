package org.lee.mugen.sprite.cns.eval.trigger.function.spriteCns;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.parser.type.Valueable;
import org.lee.mugen.sprite.character.Sprite;
import org.lee.mugen.sprite.character.SpriteHelper;
import org.lee.mugen.sprite.cns.eval.function.SpriteCnsTriggerFunction;
import org.lee.mugen.sprite.parser.Parser;

public class Numhelper extends SpriteCnsTriggerFunction {
	public Numhelper() {
		super("numhelper", new String[] {});
	}

	@Override
	public Object getValue(String spriteId, Valueable... params) {
		int val = 0;
		int count = 0;
		if (params.length > 0) {
			val = Parser.getIntValue(params[0].getValue(spriteId));
		}
		for (Sprite s: StateMachine.getInstance().getPartners(StateMachine.getInstance().getSpriteInstance(spriteId))) {
			if (s instanceof SpriteHelper) {
				if (((SpriteHelper)s).getHelperSub().getId() == val)
					count++;
			}
		}
		return count;
	}
}
