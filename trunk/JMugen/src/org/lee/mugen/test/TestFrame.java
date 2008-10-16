package org.lee.mugen.test;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.core.sound.SoundSystem;
import org.lee.mugen.renderer.GameWindow;
import org.lee.mugen.renderer.GraphicsWrapper;
import org.lee.mugen.util.debugger.SpriteDebugerUI;

public class TestFrame {

	public static void main(String[] args) throws Exception {

		final StateMachine statemachine = StateMachine.getInstance();
		args = new String[6];
		if (args.length < 5) {
			System.out
				.println("set character like this:\n javaw [all the classpath] org.lee.mugen.test.TestFrame kfm 0 kfm 1 stage0.def music");
			System.exit(1);
		}

		args[0] = "gokuHR";
		args[1] = "0";
		args[2] = "hotaru";
		args[3] = "0";
		args[4] = "d4_ggxxac_China" + ".def";
		args[5] = "resource/sound/ADX_S060.wav";
		
		
		
		args[0] = "kfm";
		args[1] = "0";
		args[2] = "hotaru";
		args[3] = "0";
		args[4] = "stage0" + ".def";
		args[5] = "resource/sound/ADX_S060.wav";
		
		
		
		statemachine.getGameState().setGameType(1);
		statemachine.preloadSprite(StateMachine.TEAMSIDE_ONE, "1",
				"resource/chars/" + args[0] + "/" + args[0] + ".def", Integer.parseInt(args[1]));
		statemachine.preloadSprite(StateMachine.TEAMSIDE_TWO, "2",
				"resource/chars/" + args[2] + "/" + args[2] + ".def", Integer.parseInt(args[3]));
		statemachine.preloadStage("resource/stages/" + args[4]);
 		SoundSystem.SoundBackGround.playMusic(args[5]);

		
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