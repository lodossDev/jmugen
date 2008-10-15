package org.lee.mugen.sprite.background.bgCtrlFunction;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.parser.type.Valueable;
import org.lee.mugen.sprite.background.Stage;
import org.lee.mugen.sprite.cns.eval.function.SpriteCnsTriggerFunction;

public class Time extends SpriteCnsTriggerFunction {

	public Time() {
		super("time", new String[0]);
	}
	@Override
	public Object getValue(String bgId, Valueable... params) {
		Stage stage = StateMachine.getInstance().getInstanceOfStage();
		int time = stage.getBgCtrlDefMap().get(new Integer(bgId)).getCounttime();
		
		return time;
	}
}