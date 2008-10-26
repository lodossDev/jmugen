package org.lee.mugen.sprite.cns.eval.trigger.function.spriteCns;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.parser.type.Valueable;
import org.lee.mugen.sprite.character.Sprite;
import org.lee.mugen.sprite.character.SpriteHelper;
import org.lee.mugen.sprite.cns.eval.function.SpriteCnsTriggerFunction;

public class Win extends SpriteCnsTriggerFunction {

	// TODO : Win
	public Win() {
		super("win", new String[] {});
	}

	@Override
	public Object getValue(String spriteId, Valueable... params) {
		return isWin(spriteId)? 1: 0;
	}
	
	public static boolean isWin(String spriteId) {
		boolean allLoose = true;
		for (Sprite s: StateMachine.getInstance().getSprites()) {
			if (s instanceof SpriteHelper)
				continue;
			if (s.getSpriteId().equals(spriteId))
				continue;
			allLoose = allLoose && Lose.isLose(s.getSpriteId());
		}
		
		
		return allLoose;
	}

}
