package org.lee.mugen.sprite.cns.type.function;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.parser.type.Valueable;

public class Parentvarset extends Varset {

    public Parentvarset() {
    }
    @Override
    public Object getValue(String spriteId, Valueable... params) {
    	return super.getValue(StateMachine.getInstance().getParentId(spriteId), params);
    }
    
    @Override
    public Valueable[] parse(String name, String value) {
    	// TODO Auto-generated method stub
    	return super.parse(name, value);
    }
    

}
