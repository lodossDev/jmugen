package org.lee.mugen.sprite.background.bgCtrlFunction;

import java.util.ArrayList;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.parser.type.Valueable;
import org.lee.mugen.sprite.background.BG;
import org.lee.mugen.sprite.background.Stage;
import org.lee.mugen.sprite.cns.eval.function.StateCtrlFunction;
import org.lee.mugen.sprite.parser.Parser;

public class Enable extends StateCtrlFunction {

	public Enable() {
		super("enable", new String[] { "value" });
	}

	@Override
	public Object getValue(String bgId, Valueable... params) {
		Stage stage = StateMachine.getInstance().getInstanceOfStage();
		ArrayList<BG> bgs = stage.getBgCtrlDefMap().get(new Integer(bgId))
				.getBgCopys();

		for (BG bg : bgs) {
			int xValue = getParamIndex("value");
			if (valueableParams[xValue] != null) {
				Valueable value = valueableParams[xValue][0];
				if (value != null) {
					int ix = Parser.getIntValue(value.getValue(bgId));
					bg.setEnable(ix != 0);
				}
			}
		}
		return null;
	}
	
}