package org.lee.mugen.sprite.background.bgCtrlFunction;

import java.util.ArrayList;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.parser.type.Valueable;
import org.lee.mugen.sprite.background.BG;
import org.lee.mugen.sprite.background.Stage;
import org.lee.mugen.sprite.cns.eval.function.StateCtrlFunction;
import org.lee.mugen.sprite.parser.ExpressionFactory;
import org.lee.mugen.sprite.parser.Parser;

/**
 * 
 * @author Dr Wong
 * Test if the player is alive
 * @category Trigger : Complete
 */
public class Posset extends StateCtrlFunction {

	public Posset() {
		super("posset", new String[] {"x", "y"});
	}
	@Override
	public Object getValue(String bgId, Valueable... params) {
		Stage stage = StateMachine.getInstance().getInstanceOfStage();
		ArrayList<BG> bgs = stage.getBgCtrlDefMap().get(new Integer(bgId)).getBgCopys();

		for (BG bg: bgs) {

			int xIndex = getParamIndex("x");
			if (valueableParams[xIndex] != null) {
				Valueable x = valueableParams[xIndex][0];
				if (x != null) {
					float fx = Parser.getFloatValue(x.getValue(bgId));
					bg.getPos().addX(fx);
				}
			}
			int yIndex = getParamIndex("y");
			if (valueableParams[yIndex] != null) {
				Valueable y = valueableParams[yIndex][0];
				if (y != null) {
					float fy = Parser.getFloatValue(y.getValue(bgId));
					bg.getPos().addY(fy);
				}		
			}
		}

		return null;
	}
}
