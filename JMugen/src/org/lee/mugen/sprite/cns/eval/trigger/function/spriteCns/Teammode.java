package org.lee.mugen.sprite.cns.eval.trigger.function.spriteCns;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.parser.type.Valueable;
import org.lee.mugen.sprite.character.Sprite;
import org.lee.mugen.sprite.cns.eval.function.SpriteCnsTriggerFunction;

public class Teammode extends SpriteCnsTriggerFunction {
	public static enum TeamMode {
		SINGLE (1, "One vs One"), 
		SIMUL (2, "2 Vs 2"), 
		TURNS (3, "3 Vs 3 Turn"),  
		MVC(4, "Marvels vs Capcom Style");
		
		TeamMode(int bits, String desc) {
			this.bits = bits;
			this.desc = desc;
		}
		private int bits;
		private String desc;
		public int getBits() {
			return bits;
		}
		public String getDesc() {
			return desc;
		}
		
	}
	public Teammode() {
		super("teammode", new String[] {});
	}
	
	@Override
	public Object getValue(String spriteId, Valueable... params) {
		Sprite one = StateMachine.getInstance().getRoot(spriteId);
		if (StateMachine.getInstance().getTeamOne().get(one.getSpriteId()) != null) {
			return StateMachine.getInstance().getTeamOneMode().toString().toLowerCase();
			
		} else {
			return StateMachine.getInstance().getTeamTwoMode().toString().toLowerCase();
		}
	}
	
	@Override
	public Valueable[] parseValue(String name, String value) {
		final Valueable[] results = new Valueable[1];
		final TeamMode mode = TeamMode.valueOf(value.toUpperCase());
		results[0] = new Valueable() {
			@Override
			public Object getValue(String spriteId, Valueable... params) {
				return mode;
			}};
		return results;
	}
	
}
