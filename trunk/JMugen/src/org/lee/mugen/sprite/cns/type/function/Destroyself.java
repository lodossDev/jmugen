package org.lee.mugen.sprite.cns.type.function;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.parser.type.Valueable;
import org.lee.mugen.sprite.cns.eval.function.StateCtrlFunction;

public class Destroyself extends StateCtrlFunction {

    public Destroyself() {
        super("destroyself", new String[] {});
    }
    @Override
    public Object getValue(String spriteId, Valueable... params) {
    	StateMachine.getInstance().removeSpriteHelper(spriteId);
    	return null;
    }
}
