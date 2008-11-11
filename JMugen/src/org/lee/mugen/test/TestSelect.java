package org.lee.mugen.test;

import org.lee.mugen.core.GameSelect;
import org.lee.mugen.renderer.GameWindow;
import org.lee.mugen.renderer.GraphicsWrapper;

public class TestSelect {
	public static void main(String[] args) throws Exception {
		GameSelect gameSelect = GameSelect.getInstance();
		
		GraphicsWrapper.init();
		final GameWindow gameWindow = GraphicsWrapper.getInstance().getInstanceOfGameWindow();
		gameWindow.setGameWindowCallback(gameSelect);
		gameWindow.start();

		
		
	}
}
