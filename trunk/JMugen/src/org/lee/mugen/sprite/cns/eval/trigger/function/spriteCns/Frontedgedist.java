package org.lee.mugen.sprite.cns.eval.trigger.function.spriteCns;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.parser.type.Valueable;
import org.lee.mugen.sprite.background.Stage;
import org.lee.mugen.sprite.character.Sprite;
import org.lee.mugen.sprite.cns.eval.function.SpriteCnsTriggerFunction;

public class Frontedgedist extends SpriteCnsTriggerFunction {

	public Frontedgedist() {
		super("frontedgedist", new String[] {});
	}

	public static float compute(Sprite sprite) {
		Stage stage = StateMachine.getInstance().getInstanceOfStage();
		int _mvX = stage.getCamera().getX();
		if (sprite.isFlip()) {
			float x = _mvX + stage.getCamera().getWidth()/2f + (sprite.getInfo().getXPos() + sprite.getInfo().getWidth().getFront());
			return x;
		} else {
			float x = -_mvX + stage.getCamera().getWidth()/2f - (sprite.getInfo().getXPos() + sprite.getInfo().getWidth().getFront());
			return x;
		}
	}
	@Override
	public Object getValue(String spriteId, Valueable... params) {
		Sprite sprite = StateMachine.getInstance().getSpriteInstance(spriteId);
		return compute(sprite);
	}
}
