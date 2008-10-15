package org.lee.mugen.sprite.cns.type.function;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.parser.type.Valueable;

public class Parentvaradd extends Varadd {

    public Parentvaradd() {
    }

    @Override
    public Object getValue(String spriteId, Valueable... params) {
    	return super.getValue(StateMachine.getInstance().getParentId(spriteId), params);
    }
}
