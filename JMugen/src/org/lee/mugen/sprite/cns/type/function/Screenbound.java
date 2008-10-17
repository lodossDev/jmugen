package org.lee.mugen.sprite.cns.type.function;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.parser.type.Valueable;
import org.lee.mugen.sprite.cns.eval.function.StateCtrlFunction;
import org.lee.mugen.sprite.parser.ExpressionFactory;
import org.lee.mugen.sprite.parser.Parser;

public class Screenbound extends StateCtrlFunction {

    public Screenbound() {
        super("screenbound", new String[] {"value", "movecamera"});
    }

    
    @Override
    public Object getValue(String spriteId, Valueable... params) {
    	Object[] v = getValueFromName(spriteId, "value");
    	int value;
    	if (v.length == 0) {
    		value = 0;
    	} else {
    		value = Parser.getIntValue(v[0]);
        		
    	}
    	if (value == 0) {
        	StateMachine.getInstance().getSpriteInstance(spriteId).getInfo().activateScreenboundDef();
    	} else {
        	StateMachine.getInstance().getSpriteInstance(spriteId).getInfo().deactivateScreenbound();
    		
    	}
    	return null;
    }
}
