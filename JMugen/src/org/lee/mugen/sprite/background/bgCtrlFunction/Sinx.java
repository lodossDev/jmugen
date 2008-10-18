package org.lee.mugen.sprite.background.bgCtrlFunction;

import java.util.ArrayList;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.parser.type.Valueable;
import org.lee.mugen.sprite.background.BG;
import org.lee.mugen.sprite.background.Stage;
import org.lee.mugen.sprite.cns.eval.function.StateCtrlFunction;
import org.lee.mugen.sprite.parser.Parser;

/**
 * 
 * @author Dr Wong
 * Test if the player is alive
 * @category Trigger : Complete
 */
public class Sinx extends StateCtrlFunction {

	public Sinx() {
		super("sinx", new String[] {"value"});
	}
	@Override
	public Object getValue(String bgId, Valueable... params) {
		Stage stage = StateMachine.getInstance().getInstanceOfStage();
		ArrayList<BG> bgs = stage.getBgCtrlDefMap().get(new Integer(bgId)).getBgCopys();

		for (BG bg: bgs) {

			int xIndex = getParamIndex("value");
			if (valueableParams[xIndex] != null) {
				Valueable x = valueableParams[xIndex][0];
				if (x != null) {
					int ix = Parser.getIntValue(x.getValue(bgId));
					bg.setSinXAmp(ix);
				}
				if (valueableParams[xIndex].length > 1) {
					Valueable y = valueableParams[xIndex][1];
					if (y != null) {
						int iy = Parser.getIntValue(y.getValue(bgId));
						bg.setSinXTime(iy);
					}		
				}
			}
			
		}

		return null;
	}
}
