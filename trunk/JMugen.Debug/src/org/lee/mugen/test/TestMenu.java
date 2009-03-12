package org.lee.mugen.test;

import org.lee.mugen.core.GameMenu;
import org.lee.mugen.core.JMugenConstant;
import org.lee.mugen.core.sound.SoundSystem;
import org.lee.mugen.renderer.GameWindow;
import org.lee.mugen.renderer.GraphicsWrapper;

public class TestMenu {
	public static void main(String[] args) throws Exception {
		GameMenu gameMenu = GameMenu.getInstance();
		
		GraphicsWrapper.init();
		final GameWindow gameWindow = GraphicsWrapper.getInstance().getInstanceOfGameWindow();
		gameWindow.setGameWindowCallback(gameMenu);
		SoundSystem.SoundBackGround.playMusic(JMugenConstant.RESOURCE + "sound/" + "mvc2/ADX_S060.BIN");
		gameWindow.start();

		
		
	}
}
