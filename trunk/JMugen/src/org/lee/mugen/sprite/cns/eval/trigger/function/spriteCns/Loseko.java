package org.lee.mugen.sprite.cns.eval.trigger.function.spriteCns;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.parser.type.Valueable;
import org.lee.mugen.sprite.character.Sprite;
import org.lee.mugen.sprite.cns.eval.function.SpriteCnsTriggerFunction;

/**
 * 
 * @author Dr Wong
 * Test if the player is alive
 * @category Trigger : Complete
 */
public class Loseko extends SpriteCnsTriggerFunction {

	public Loseko() {
		super("loseko", new String[] {});
	}
	@Override
	public Object getValue(String spriteId, Valueable... params) {
		return isLoseKo(spriteId)? 1: 0;
			
	}
	
	public static boolean isLoseKo(String spriteId) {
		StateMachine stateMachine = StateMachine.getInstance();
		Sprite sprite = stateMachine.getSpriteInstance(spriteId);
		return sprite.getInfo().getLife() <= 0;	
	}
}
