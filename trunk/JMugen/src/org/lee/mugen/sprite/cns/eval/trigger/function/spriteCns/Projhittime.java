package org.lee.mugen.sprite.cns.eval.trigger.function.spriteCns;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lee.mugen.core.FightEngine;
import org.lee.mugen.core.StateMachine;
import org.lee.mugen.lang.Wrap;
import org.lee.mugen.lang.Wrapper;
import org.lee.mugen.parser.type.Valueable;
import org.lee.mugen.sprite.cns.eval.function.MathFunction;
import org.lee.mugen.sprite.cns.eval.function.SpriteCnsTriggerFunction;
import org.lee.mugen.sprite.entity.ProjectileSub;
import org.lee.mugen.sprite.parser.Parser;

public class Projhittime extends SpriteCnsTriggerFunction {

	// TODO : to test projhittime and add like projhit
	public Projhittime() {
		super("projhittime", new String[] {});
	}
	@Override
	public Object getValue(String spriteId, Valueable...params) {
		return params[0].getValue(spriteId);
	};
	/*
	Projhit[ID] = valeur
	Projhit[ID] = valeur, [oper] valeur2
	 */
	public static Pattern PROJ_HIT_TIME_SPEC_REG = Pattern.compile(
			"(" + "projhittime *\\((\\d+)\\)" + ")" +
			"|" +
			"\\b(" + "projhittime(\\d*)" + ")\\b");
	@Override
	public int parseValue(String[] tokens, int pos, List<Valueable> result) {
		String keyStr = tokens[pos];

		Matcher m = PROJ_HIT_TIME_SPEC_REG.matcher(keyStr);
		final Integer projid;
		String grp = null;
		if (m.find()) {
			if (m.group(2) != null) {
				grp = m.group(2);
			}
			if (m.group(4) != null) {
				grp = m.group(4);
			}
		}
			
		if (grp != null && grp.length() > 0) {
			projid = Integer.parseInt(grp);
		} else {
			projid = null;
		}


		Valueable value = new Valueable() {

			public Object getValue(String spriteId, Valueable... params) {
				List<ProjectileSub> projectiles;
				FightEngine fightEngine = StateMachine.getInstance().getFightEngine();
				projectiles = fightEngine.getProjectiles(projid);
				long currentTime = StateMachine.getInstance().getGameState().getGameTime();
				for (ProjectileSub projectile: projectiles) {
					if (projectile.getLastTimeHitSomething() == -1)
						continue;

					if (projectile.getLastTimeHitSomething() == projectile.getLastTimeBlockBySomething())
						return -1;
					
					long deltaContact = currentTime - projectile.getLastTimeHitSomething();
					return deltaContact;
				}
				return -1;
			}
		};
		
		result.add(value);
		return pos;
		
	
	
	}
	
	@Override
	public String getRegex() {
		return PROJ_HIT_TIME_SPEC_REG.pattern();
	}
}
