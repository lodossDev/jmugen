package org.lee.mugen.sprite.cns.type.function;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.parser.type.Valueable;
import org.lee.mugen.sprite.cns.eval.function.StateCtrlFunction;
import org.lee.mugen.sprite.parser.Parser;

public class Removeexplod extends StateCtrlFunction {

    public Removeexplod() {
        super("removeexplod", new String[] {"id"});
    }
	@Override
	public Object getValue(String spriteId, Valueable... params) {
		int idIndex = getParamIndex("id");
		if (valueableParams[idIndex] != null) {
			Valueable id = valueableParams[idIndex][0];

			StateMachine.getInstance().removeExplod(spriteId, Parser.getIntValue(id.getValue(spriteId)));
		} else {
			StateMachine.getInstance().removeExplod(spriteId);
		}
		return null;
	}
}
