package org.lee.mugen.sprite.cns.type.function;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.parser.type.Valueable;
import org.lee.mugen.sprite.cns.eval.function.StateCtrlFunction;

public class Envcolor extends StateCtrlFunction {

    public Envcolor() {
        super("envcolor", new String[] {"value", "time", "under"});
    }

	@Override
	public Object getValue(String spriteId, Valueable... params) {
		StateMachine.getInstance().getGlobalEvents().getEnvcolor().init();
		fillBean(spriteId, StateMachine.getInstance().getGlobalEvents().getEnvcolor());
		
		return null;
	}
}
