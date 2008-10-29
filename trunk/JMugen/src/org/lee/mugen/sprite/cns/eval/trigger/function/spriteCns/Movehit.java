package org.lee.mugen.sprite.cns.eval.trigger.function.spriteCns;

import java.util.LinkedList;

import org.lee.mugen.core.FightEngine;
import org.lee.mugen.core.StateMachine;
import org.lee.mugen.parser.type.Valueable;
import org.lee.mugen.sprite.character.Sprite;
import org.lee.mugen.sprite.character.SpriteHelper;
import org.lee.mugen.sprite.character.SpriteCns.MoveType;
import org.lee.mugen.sprite.character.spiteCnsSubClass.HitDefSub;
import org.lee.mugen.sprite.character.spiteCnsSubClass.ReversaldefSub;
import org.lee.mugen.sprite.cns.eval.function.SpriteCnsTriggerFunction;

public class Movehit extends SpriteCnsTriggerFunction {

	public Movehit() {
		super("movehit", new String[] {});
	}

	@Override
	public Object getValue(String spriteId, Valueable... params) {
		Sprite one = StateMachine.getInstance().getSpriteInstance(spriteId);
		if (one.getInfo().getMovetype() == MoveType.A) {
			LinkedList<HitDefSub> hitdefs = StateMachine.getInstance().getFightEngine().getHitdefBySpriteHitter(spriteId);

			HitDefSub strictHitdef = null;
			// I want the last not reversal
			for (HitDefSub h: hitdefs) {
				if (h.getSpriteId().equals(spriteId))
					strictHitdef = h;
			
				
			}

			for (Sprite s: StateMachine.getInstance().getSprites()) {
				
				if (s == one || (s instanceof SpriteHelper))
					continue;
				if (s.getInfo().getLastHitdef() == null || !spriteId.equals(s.getInfo().getLastHitdef().getSpriteId())) {
					continue;
					
				} else if (FightEngine.isBlockState(s) 
						&& spriteId.equals(s.getInfo().getLastHitdef().getSpriteId())) {
					continue;
				} else if (!FightEngine.isBlockState(s) 
						&& spriteId.equals(s.getInfo().getLastHitdef().getSpriteId()) 
//						&& s.getInfo().getLastHitdef().getHittime() > 0
//						&& s.getInfo().getLastHitdef().getLastTimeHitSomething() < StateMachine.getInstance().getGameState().getGameTime()
						&& s.getInfo().getLastHitdef().getLastTimeHitSomething() != -1
						&& s.getInfo().getLastHitdef().getLastTimeBlockBySomething() == -1
						&& s.getInfo().getLastHitdef().getTimeCreated() + 1 < StateMachine.getInstance().getGameState().getGameTime()
				) {
					if ((hitdefs.size() > 0 && (strictHitdef != null && strictHitdef.getTargetId() == null)
							&& s.getInfo().getLastHitdef().getHittime() <= 0
					)) {
						return 0;
					}
					if (s.getInfo().getLastHitdef().getHittime() <= 0)
						return 0;
					return 1;
				}
			}
		}
		return 0;
	}

	

}
