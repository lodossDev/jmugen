package org.lee.mugen.sprite.cns.type.function;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.parser.type.Valueable;
import org.lee.mugen.sprite.cns.eval.function.StateCtrlFunction;

public class Hitadd extends StateCtrlFunction {

    public Hitadd() {
        super("hitadd", new String[] {"value"});
    }
    

	@Override
	public Object getValue(String spriteId, Valueable... params) {

		Object[] vals = getValueFromName(spriteId, "value");
		Integer val = ((Number) vals[0]).intValue();
		
		StateMachine.getInstance().getSpriteInstance(spriteId).getInfo().addHitCount(val);
		
		return null;
	}
}
