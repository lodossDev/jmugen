package org.lee.mugen.sprite.cns.type.function;

import java.util.List;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.parser.type.Valueable;
import org.lee.mugen.renderer.Renderable;
import org.lee.mugen.sprite.cns.eval.function.StateCtrlFunction;
import org.lee.mugen.sprite.parser.ExpressionFactory;
import org.lee.mugen.sprite.parser.Parser;

/**
 * Seems ok
 * @author Dr Wong
 *
 */
public class Afterimagetime extends StateCtrlFunction {

	public Afterimagetime() {
		super("afterimagetime", new String[] {"time"});
	}
	public static Valueable[] parse(String name, String value) {
		String[] tokens = ExpressionFactory.expression2Tokens(value);
		Valueable[] vals = ExpressionFactory.evalExpression(tokens);
		return vals;
	}
	@Override
	public Object getValue(String spriteId, Valueable... params) {
		List<Renderable> list = StateMachine.getInstance().getRenderables();
		int timeIndex = getParamIndex("time");
		Valueable time = valueableParams[timeIndex][0];
		int itime = Parser.getIntValue(time.getValue(spriteId, params));
		
		StateMachine.getInstance().setAfterImageTime(spriteId, itime);
		return null;
	}
}