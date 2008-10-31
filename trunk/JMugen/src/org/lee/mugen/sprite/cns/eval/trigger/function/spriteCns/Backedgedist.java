package org.lee.mugen.sprite.cns.eval.trigger.function.spriteCns;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.parser.type.Valueable;
import org.lee.mugen.sprite.background.Stage;
import org.lee.mugen.sprite.character.Sprite;
import org.lee.mugen.sprite.cns.eval.function.SpriteCnsTriggerFunction;

public class Backedgedist extends SpriteCnsTriggerFunction {

	public Backedgedist() {
		super("backedgedist", new String[] {});
	}
	public static float compute(Sprite sprite) {
		Stage stage = StateMachine.getInstance().getInstanceOfStage();
		int _mvX = stage.getCamera().getX();
		if (sprite.isFlip()) {
			float x = -_mvX + stage.getCamera().getWidth()/2 - (sprite.getInfo().getXPos() - sprite.getInfo().getWidth().getBack());
			return x;
			
		} else {
			float x = _mvX + stage.getCamera().getWidth()/2 + (sprite.getInfo().getXPos() - sprite.getInfo().getWidth().getBack());
			return x;
			
		}
	}
	@Override
	public Object getValue(String spriteId, Valueable... params) {
		Sprite sprite = StateMachine.getInstance().getSpriteInstance(spriteId);
		return compute(sprite);
	}


}
