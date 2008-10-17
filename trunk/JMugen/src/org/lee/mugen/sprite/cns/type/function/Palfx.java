package org.lee.mugen.sprite.cns.type.function;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.parser.type.Valueable;
import org.lee.mugen.sprite.character.Sprite;
import org.lee.mugen.sprite.cns.eval.function.StateCtrlFunction;
import org.lee.mugen.sprite.parser.ExpressionFactory;

public class Palfx extends StateCtrlFunction {

	public Palfx() {
		super("palfx", new String[] { "time", "add", "mul", "sinadd",
				"invertall", "color" });
	}

	@Override
	public Object getValue(String spriteId, Valueable... params) {
		Sprite sprite = StateMachine.getInstance().getSpriteInstance(spriteId);
		sprite.getPalfx().init();
//		PalFxSub fx = new PalFxSub();
		fillBean(spriteId, sprite.getPalfx());
//		sprite.setPalfx(fx);
		return getValue(spriteId, sprite, getFunctionName(), params);
	}
}
