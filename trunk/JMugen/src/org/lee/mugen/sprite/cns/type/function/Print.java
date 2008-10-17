package org.lee.mugen.sprite.cns.type.function;

import org.lee.mugen.parser.type.Valueable;
import org.lee.mugen.sprite.cns.eval.function.StateCtrlFunction;
import org.lee.mugen.sprite.parser.ExpressionFactory;

public class Print extends StateCtrlFunction {

	public Print() {
		super("print", new String[] {"value"});
	}

	@Override
	public Object getValue(String spriteId, Valueable... params) {
		int xIndex = getParamIndex("value");
		if (valueableParams[xIndex] != null) {
			Valueable x = valueableParams[xIndex][0];
			System.out.println(" >>> " + x.getValue(spriteId));
//			System.out.println(StateMachine.getInstance().getSpriteInstance(spriteId).getSprAnimMng().getAnimElemNo() - 1);
		}
		return null;
	}
}
