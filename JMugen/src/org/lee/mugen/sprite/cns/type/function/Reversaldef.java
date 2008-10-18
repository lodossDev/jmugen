package org.lee.mugen.sprite.cns.type.function;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.parser.type.Valueable;
import org.lee.mugen.sprite.character.Sprite;
import org.lee.mugen.sprite.character.spiteCnsSubClass.ReversaldefSub;
import org.lee.mugen.sprite.character.spiteCnsSubClass.ReversaldefSub.ReversalAttrClass;
import org.lee.mugen.sprite.cns.eval.function.StateCtrlFunction;
import org.lee.mugen.util.BeanTools;

public class Reversaldef extends StateCtrlFunction {

	// TODO : reversaldef
	public Reversaldef() {
		super("reversaldef", new String[] { "pausetime", "sparkno", "sparkxy",
				"hitsound", "p1stateno", "p2stateno", "reversal.attr"});
	}


	public static Valueable[] parseForReversal$attr(String name, final String value) {
		final Object v = BeanTools.getConvertersMap().get(ReversalAttrClass.class).convert(value);
		Valueable[] vals = new Valueable[1];
		vals = new Valueable[1];
		vals[0] = new Valueable() {
			public Object getValue(String spriteId, Valueable... params) {
				return v;
			}
		};
		return vals;
	}

	@Override
	public Object getValue(String spriteId, Valueable... params) {		
		Sprite sprOne = StateMachine.getInstance().getSpriteInstance(spriteId);
		ReversaldefSub hitDef = new ReversaldefSub();
		hitDef.setSpriteHitter(sprOne);
		hitDef.setSpriteId(spriteId);
		fillBean(spriteId, hitDef);
		
		StateMachine.getInstance().getFightEngine().add(hitDef);
		StateMachine.getInstance().getFightEngine().process();
		return null;
	}

}
