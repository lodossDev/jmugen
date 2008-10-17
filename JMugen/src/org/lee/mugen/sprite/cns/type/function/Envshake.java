package org.lee.mugen.sprite.cns.type.function;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.parser.type.Valueable;
import org.lee.mugen.sprite.cns.eval.function.StateCtrlFunction;
import org.lee.mugen.sprite.entity.Shake;
import org.lee.mugen.sprite.parser.ExpressionFactory;

public class Envshake extends StateCtrlFunction {

    public Envshake() {
        super("envshake", new String[] {"time", "freq", "ampl", "phase"});
    }

	@Override
	public Object getValue(String spriteId, Valueable... params) {
		Shake envShake = new Shake();
		getValue(spriteId, envShake, "", params);
		
		StateMachine.getInstance().getInstanceOfStage().getCamera().setEnvShake(envShake);

		return null;
	}
    
}
