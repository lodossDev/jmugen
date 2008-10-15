package org.lee.mugen.test;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.core.sound.SoundSystem;
import org.lee.mugen.renderer.GameWindow;
import org.lee.mugen.renderer.GraphicsWrapper;
import org.lee.mugen.util.debugger.SpriteDebugerUI;

public class TestFrame {

	public static void main(String[] args) throws Exception {

		final StateMachine statemachine = StateMachine.getInstance();
		args = new String[3];
		if (args.length != 3) {
			System.out
				.println("set character like this:\n javaw [all the classpath] org.lee.mugen.test.TestFrame kfm kfm stage0.def");
		}

		args[0] = "ccixiangfei";
		args[1] = "kof_joe";
		args[2] = "stage0" + ".def";
		
		
		statemachine.getGameState().setGameType(1);
		statemachine.preloadSprite(StateMachine.TEAMSIDE_ONE, "1",
				"resource/chars/" + args[0] + "/" + args[0] + ".def", 0);
		statemachine.preloadSprite(StateMachine.TEAMSIDE_TWO, "2",
				"resource/chars/" + args[1] + "/" + args[1] + ".def", 0);
		statemachine.preloadStage("resource/stages/" + args[2]);
 		SoundSystem.SoundBackGround.playMusic("resource/sound/ADX_S060.wav");

		
		GraphicsWrapper.init();
		final GameWindow gameWindow = GraphicsWrapper.getInstance()
				.getInstanceOfGameWindow();
		gameWindow.setGameWindowCallback(statemachine);
		SpriteDebugerUI debugerUI = new SpriteDebugerUI();
		debugerUI.setVisible(true);
		gameWindow.start();
		


	}

	public static void main2(String[] args) throws Exception {
		final StateMachine statemachine = StateMachine.getInstance();
		if (args.length < 5) {
			System.out
					.println("set character like this:\n javaw [all the classpath] [kind => java ou lwgl] org.lee.mugen.test.TestFrame kfm 0 kfm 1 stage.def [music in sound dir]");
			return;
		}
		statemachine.getGameState().setGameType(1);
		statemachine.preloadSprite(StateMachine.TEAMSIDE_ONE, "1",
				"resource/chars/" + args[0] + "/" + args[1] + ".def", Integer
						.parseInt(args[1]));
		statemachine.preloadSprite(StateMachine.TEAMSIDE_TWO, "2",
				"resource/chars/" + args[2] + "/" + args[3] + ".def", Integer
						.parseInt(args[3]));
		statemachine.preloadStage("resource/stages/" + args[3]);
		if (args.length > 5)
			SoundSystem.SoundBackGround.playMusic("resource/sound/ " + args[5]);

		GameWindow gameWindow = GraphicsWrapper.getInstance()
				.getInstanceOfGameWindow();

		gameWindow.setGameWindowCallback(statemachine);
		gameWindow.start();

	}

}