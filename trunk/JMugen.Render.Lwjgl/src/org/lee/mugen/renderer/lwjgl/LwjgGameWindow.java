package org.lee.mugen.renderer.lwjgl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.lee.mugen.core.Game;
import org.lee.mugen.core.Game.DebugAction;
import org.lee.mugen.input.CmdProcDispatcher;
import org.lee.mugen.input.ISpriteCmdProcess;
import org.lee.mugen.renderer.GameWindow;
import org.lee.mugen.renderer.ImageContainer;
import org.lee.mugen.renderer.MugenTimer;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

public class LwjgGameWindow implements GameWindow {

	public LwjgGameWindow() {
		setResolution(320, 240);
		setResolution(640, 480);
	}

	private Game callback;
	/** The width of the game display area */
	private int width;

	/** The height of the game display area */
	private int height;


	/**
	 * Title of window, we get it before our window is ready, so store it till
	 * needed
	 */
	private String title;
	private boolean gameRunning = true;

	public void setResolution(int x, int y) {
		width = x;
		height = y;
	}

	private boolean setDisplayMode() {
		try {
			// get modes
			DisplayMode[] dm = org.lwjgl.util.Display.getAvailableDisplayModes(
					width, height, -1, -1, -1, -1, 60, 60);

			org.lwjgl.util.Display.setDisplayMode(dm, new String[] {
					"width=" + width,
					"height=" + height,
					"freq=" + 60,
					"bpp="
							+ org.lwjgl.opengl.Display.getDisplayMode()
									.getBitsPerPixel() });

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out
					.println("Unable to enter fullscreen, continuing in windowed mode");
		}

		return false;
	}



	private List<SprCmdProcessListenerAction> spriteCmdProcess = new LinkedList<SprCmdProcessListenerAction>();

	private static class SprCmdProcessListenerAction {
		private ISpriteCmdProcess scp;
		private boolean[] areKeysPress;
		private int[] keys;

		public SprCmdProcessListenerAction(ISpriteCmdProcess scp) {
			this.scp = scp;
			keys = scp.getKeys();
			areKeysPress = new boolean[keys.length];
		}

		public boolean[] getAreKeysPress() {
			return areKeysPress;
		}

		public void setAreKeysPress(boolean[] areKeysPress) {
			this.areKeysPress = areKeysPress;
		}

		public int[] getKeys() {
			return keys;
		}

		public void setKeys(int[] keys) {
			this.keys = keys;
		}

		public ISpriteCmdProcess getScp() {
			return scp;
		}
	}

	public void addSpriteKeyProcessor(ISpriteCmdProcess scp) {
		spriteCmdProcess.add(new SprCmdProcessListenerAction(scp));
	}

	public void removeSpriteKeyProcessor(ISpriteCmdProcess scp) {
		for (Iterator<SprCmdProcessListenerAction> iter = spriteCmdProcess
				.iterator(); iter.hasNext();) {
			SprCmdProcessListenerAction sa = iter.next();
			if (sa.getScp().equals(scp))
				iter.remove();
		}
	}

	public void setGameWindowCallback(Game callback) {
		this.callback = callback;
	}

	public void setTitle(String title) {
		this.title = title;
		if (Display.isCreated()) {
			Display.setTitle(title);
		}
	}

	public Game getCallback() {
		return callback;
	}

	public void setCallback(Game callback) {
		this.callback = callback;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getTitle() {
		return title;
	}

	public void start() throws Exception {
		try {
			setDisplayMode();
			Display.create();

			setTitle("JMugen");
			// grab the mouse, dont want that hideous cursor when we're playing!
			// Mouse.setGrabbed(true);

			// enable textures since we're going to use these for our sprites
			GL11.glEnable(GL11.GL_TEXTURE_2D);

			// disable the OpenGL depth test since we're rendering 2D graphics
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glEnable(GL11.GL_BLEND);

			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();

			GL11.glOrtho(0, width, height, 0, -1, 1);

			GL11.glScaled((float) width / 320, (float) height / 240, 0);
			initKeys();
			
			
			if (callback != null) {
				callback.init(this);
			}
		} catch (LWJGLException le) {
		}

		gameLoop();
	}

	private void initKeys() throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {
		ResourceBundle bundle = ResourceBundle.getBundle("keys");
		
		// P1

		
		CmdProcDispatcher cd1 = new CmdProcDispatcher(
				Keyboard.class.getDeclaredField("KEY_" + bundle.getString("P1.UP").toUpperCase()).getInt(null), 
				Keyboard.class.getDeclaredField("KEY_" + bundle.getString("P1.DOWN").toUpperCase()).getInt(null),
				Keyboard.class.getDeclaredField("KEY_" + bundle.getString("P1.LEFT").toUpperCase()).getInt(null),
				Keyboard.class.getDeclaredField("KEY_" + bundle.getString("P1.RIGHT").toUpperCase()).getInt(null),
				Keyboard.class.getDeclaredField("KEY_" + bundle.getString("P1.A").toUpperCase()).getInt(null),
				Keyboard.class.getDeclaredField("KEY_" + bundle.getString("P1.B").toUpperCase()).getInt(null),
				Keyboard.class.getDeclaredField("KEY_" + bundle.getString("P1.C").toUpperCase()).getInt(null),
				Keyboard.class.getDeclaredField("KEY_" + bundle.getString("P1.X").toUpperCase()).getInt(null),
				Keyboard.class.getDeclaredField("KEY_" + bundle.getString("P1.Y").toUpperCase()).getInt(null),
				Keyboard.class.getDeclaredField("KEY_" + bundle.getString("P1.Z").toUpperCase()).getInt(null),
				Keyboard.class.getDeclaredField("KEY_" + bundle.getString("P1.ABC").toUpperCase()).getInt(null),
				Keyboard.class.getDeclaredField("KEY_" + bundle.getString("P1.XYZ").toUpperCase()).getInt(null));	
		
		CmdProcDispatcher.getSpriteDispatcherMap().put("1", cd1);
		
		
		CmdProcDispatcher cd2 = new CmdProcDispatcher(
				Keyboard.class.getDeclaredField("KEY_" + bundle.getString("P2.UP").toUpperCase()).getInt(null), 
				Keyboard.class.getDeclaredField("KEY_" + bundle.getString("P2.DOWN").toUpperCase()).getInt(null),
				Keyboard.class.getDeclaredField("KEY_" + bundle.getString("P2.LEFT").toUpperCase()).getInt(null),
				Keyboard.class.getDeclaredField("KEY_" + bundle.getString("P2.RIGHT").toUpperCase()).getInt(null),
				Keyboard.class.getDeclaredField("KEY_" + bundle.getString("P2.A").toUpperCase()).getInt(null),
				Keyboard.class.getDeclaredField("KEY_" + bundle.getString("P2.B").toUpperCase()).getInt(null),
				Keyboard.class.getDeclaredField("KEY_" + bundle.getString("P2.C").toUpperCase()).getInt(null),
				Keyboard.class.getDeclaredField("KEY_" + bundle.getString("P2.X").toUpperCase()).getInt(null),
				Keyboard.class.getDeclaredField("KEY_" + bundle.getString("P2.Y").toUpperCase()).getInt(null),
				Keyboard.class.getDeclaredField("KEY_" + bundle.getString("P2.Z").toUpperCase()).getInt(null),
				Keyboard.class.getDeclaredField("KEY_" + bundle.getString("P2.ABC").toUpperCase()).getInt(null),
				Keyboard.class.getDeclaredField("KEY_" + bundle.getString("P2.XYZ").toUpperCase()).getInt(null));	
		
		CmdProcDispatcher.getSpriteDispatcherMap().put("2", cd2);
	}
	
	
	private DebugEventManager debugEventManager = new DebugEventManager();
	
	private static class DebugEventManager {
		private Map<DebugAction, int[]> actionKeyMap = new HashMap<DebugAction, int[]>();
		private Map<DebugAction, Boolean> actionPressMap = new HashMap<DebugAction, Boolean>();
		
		public DebugEventManager() {
			addAction(DebugAction.SWICTH_PLAYER_DEBUG_INFO, new int[] {Keyboard.KEY_LCONTROL, Keyboard.KEY_D});
			addAction(DebugAction.INIT_PLAYER, new int[] {Keyboard.KEY_SPACE});
			addAction(DebugAction.SHOW_HIDE_CNS, new int[] {Keyboard.KEY_LCONTROL, Keyboard.KEY_C});
			addAction(DebugAction.SHOW_HIDE_ATTACK_CNS, new int[] {Keyboard.KEY_LCONTROL, Keyboard.KEY_X});
			addAction(DebugAction.INCREASE_FPS, new int[] {Keyboard.KEY_LCONTROL, Keyboard.KEY_ADD}, true);
			addAction(DebugAction.DECREASE_FPS, new int[] {Keyboard.KEY_LCONTROL, Keyboard.KEY_SUBTRACT}, true);
			addAction(DebugAction.RESET_FPS, new int[] {Keyboard.KEY_LCONTROL, Keyboard.KEY_MULTIPLY});

			addAction(DebugAction.DEBUG_PAUSE, new int[] {Keyboard.KEY_LCONTROL, Keyboard.KEY_P});
			addAction(DebugAction.PAUSE_PLUS_ONE_FRAME, new int[] {Keyboard.KEY_LCONTROL, Keyboard.KEY_A});

			addAction(DebugAction.DISPLAY_HELP, new int[] {Keyboard.KEY_F1});

			
		}
		private void addAction(DebugAction action, int[] keys) {
			addAction(action, keys, false);
		}
		private void addAction(DebugAction action, int[] keys, boolean isAllowKeyRepeat) {
			actionKeyMap.put(action, keys);
			if (!isAllowKeyRepeat)
				actionPressMap.put(action, false);
		}
		
		public void process(Game callback) {
			for (DebugAction action: actionKeyMap.keySet()) {
				boolean isAllKeyOk = true;
				for (int key: actionKeyMap.get(action)) {
					isAllKeyOk = isAllKeyOk && Keyboard.isKeyDown(key);
				}
				if (isAllKeyOk && actionPressMap.get(action) == null) {
					callback.onDebugAction(action);
					continue;
				}
				if (isAllKeyOk) { // on Press
					actionPressMap.put(action, true);
				} else if (actionPressMap.get(action) != null && actionPressMap.get(action)) { // on release
					actionPressMap.put(action, false);
					callback.onDebugAction(action);
				}
			}
		}
		
	}
	
	
	private void keyManagementExecute() {

		debugEventManager.process(callback);
		
		for (SprCmdProcessListenerAction sa : spriteCmdProcess) {
			boolean[] areKeysPress = sa.getAreKeysPress();
			int[] keys = sa.getKeys();
			ISpriteCmdProcess scp = sa.getScp();

			for (int i = 0; i < keys.length; ++i) {
				if (!areKeysPress[i] && Keyboard.isKeyDown(keys[i])) {
					areKeysPress[i] = true;
					scp.keyPressed(keys[i]);

				} else if (areKeysPress[i] && !Keyboard.isKeyDown(keys[i])) {
					areKeysPress[i] = false;
					scp.keyReleased(keys[i]);
				}

			}
		}

	}
	int totalTexture = 0;
	boolean isFinishLoadText = false;
	public void gameLoop() throws Exception {
//		SpriteDebugerUI sprDebugerUI = new SpriteDebugerUI();
//		sprDebugerUI.setVisible(true);
		Collections.sort(LMugenDrawer.list, new Comparator<ImageContainer>() {

			@Override
			public int compare(ImageContainer o1, ImageContainer o2) {
				// TODO Auto-generated method stub
				return o1.getWidth() * o1.getWidth() - o2.getWidth() * o2.getWidth();
			}});
//		Collections.shuffle(LMugenDrawer.list);

		boolean isThreadStart = false;
		final Object thisContext = Display.getDrawable().getContext();
		Thread textureLoaderThread = new Thread() {
			@Override
			public void run() {
				try {
					for (ImageContainer ic: LMugenDrawer.list) {
						GLContext.useContext(thisContext);
//						Display.makeCurrent();
//						log("Start Loading => Texture width=" + ic.getWidth() + " - height=" + ic.getHeight());

						ic.getImg();
						totalTexture++;
//						log("End loading Texture " + (LMugenDrawer.list.size() - totalTexture) + " remainings");
						System.out.print(".");
						if (totalTexture%80 == 0)
							System.out.println();		
					}

				} catch (LWJGLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
//		IL.create();
//		ILU.create();
		List<ImageContainer> tempList = new ArrayList<ImageContainer>();
		tempList.addAll(LMugenDrawer.list);
		Iterator<ImageContainer> iteratorIC = tempList.iterator();
		while (gameRunning) {

			if (callback != null) {
				keyManagementExecute();
				if (getTimer().getFramerate() == 0) {
					getTimer().sleep(1000 / 60);
					Display.update();
					continue;
				}

				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT
						| GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_ACCUM_BUFFER_BIT);
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glLoadIdentity();

//				if (!isThreadStart) {
//					isThreadStart = true;
//					textureLoaderThread.start();
//				}

				callback.update(1);
				callback.render();
				callback.renderDebugInfo();
			}
			
//			if (iteratorIC.hasNext() && getTimer().getFps() > 60) {
//				iteratorIC.next().getImg();
////				System.out.println("Loading Texture");
//			}
			getTimer().sleep();
			Display.update();
			if (Display.isCloseRequested()
					|| Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				gameRunning = false;
				Display.destroy();
				System.exit(0);
			}

		}
		
	}

	private MugenTimer timer = new LMugenTimer();

	public MugenTimer getTimer() {
		return timer;
	}

}
