package org.lee.mugen.sprite.cns.type.function;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.parser.type.Valueable;
import org.lee.mugen.sprite.cns.eval.function.StateCtrlFunction;
import org.lee.mugen.sprite.parser.ExpressionFactory;

public class Envcolor extends StateCtrlFunction {

    public Envcolor() {
        super("envcolor", new String[] {"value", "time", "under"});
    }
	public static Valueable[] parse(String name, String value) {
		String[] tokens = ExpressionFactory.expression2Tokens(value);
		Valueable[] vals = ExpressionFactory.evalExpression(tokens);
		return vals;
	}


	@Override
	public Object getValue(String spriteId, Valueable... params) {
		StateMachine.getInstance().getGlobalEvents().getEnvcolor().init();
		fillBean(spriteId, StateMachine.getInstance().getGlobalEvents().getEnvcolor());
		
		return null;
	}
}
