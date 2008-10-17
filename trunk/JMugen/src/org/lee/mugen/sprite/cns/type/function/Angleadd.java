package org.lee.mugen.sprite.cns.type.function;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.parser.type.Valueable;
import org.lee.mugen.sprite.character.Sprite;
import org.lee.mugen.sprite.cns.eval.function.StateCtrlFunction;
import org.lee.mugen.sprite.parser.ExpressionFactory;
import org.lee.mugen.sprite.parser.Parser;

/**
 * OK
 * 
 * @author Dr Wong
 *
 */
public class Angleadd extends StateCtrlFunction {

	public Angleadd() {
		super("angleadd", new String[] {"value"});
	}


	@Override
	public Object getValue(String spriteId, Valueable... params) {
		int valueIndex = getParamIndex("value");
		if (valueableParams[valueIndex] != null) {
			Valueable value = valueableParams[valueIndex][0];
			int iValue = Parser.getIntValue(value.getValue(spriteId, params));
			Sprite sprite = StateMachine.getInstance().getSpriteInstance(
					spriteId);
			sprite.getSprAnimMng().getSpriteDrawProperties().setAngleadd(iValue);
		}
		return null;
	}
}
