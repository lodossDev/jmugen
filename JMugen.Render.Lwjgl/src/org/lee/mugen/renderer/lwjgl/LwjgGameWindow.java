package org.lee.mugen.renderer.lwjgl;

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
import org.lee.mugen.renderer.MugenTimer;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

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

	private void initDisplay() throws Exception {
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
			
			Mouse.create();
			

		} catch (LWJGLException le) {
		}
	}
	
	boolean isFinishInit = false;
	Thread loadingThread = new Thread() {
		@Override
		public void run() {
			try {
				callback.init(LwjgGameWindow.this);
			} catch (Exception e) {
				e.printStackTrace();
			}

			
			LMugenDrawer.createImageToTextPreparer();
			isFinishInit = true;
		}
	};

	public void start() throws Exception {
		initDisplay();
		loadingThread.start();
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
		
		CmdProcDispatcher cd3 = new CmdProcDispatcher(
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
		
		CmdProcDispatcher.getSpriteDispatcherMap().put("3", cd1);
		
		
		CmdProcDispatcher cd4 = new CmdProcDispatcher(
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
		
		CmdProcDispatcher.getSpriteDispatcherMap().put("4", cd2);
	}
	
	
	private DebugEventManager debugEventManager = new DebugEventManager();
	
	private static class DebugEventManager {
		private Map<DebugAction, int[]> actionKeyMap = new HashMap<DebugAction, int[]>();
		private Map<DebugAction, Boolean> actionPressMap = new HashMap<DebugAction, Boolean>();
		
		public DebugEventManager() {
			addAction(DebugAction.SWICTH_PLAYER_DEBUG_INFO, new int[] {Keyboard.KEY_LCONTROL, Keyboard.KEY_D});
			addAction(DebugAction.EXPLOD_DEBUG_INFO, new int[] {Keyboard.KEY_LCONTROL, Keyboard.KEY_E});
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

	
	public void gameLoop() throws Exception {
		
		while (gameRunning) {
			if (mouse.isLeftClick() && !Mouse.isButtonDown(0)) {
				mouse.setLeftPress(false);
			} else {
				mouse.setLeftPress(true);
			}
			if (!isFinishInit) {
				callback.displayPendingScreeen();
			} else {
				if (callback != null) {
					keyManagementExecute();
					if (getTimer().getFramerate() == 0) {
						getTimer().sleep(1000 / 60);
						callback.render();
						Display.update();
						continue;
					}

					GL11.glClear(GL11.GL_COLOR_BUFFER_BIT
							| GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_ACCUM_BUFFER_BIT);
					GL11.glMatrixMode(GL11.GL_MODELVIEW);
					GL11.glLoadIdentity();

					callback.update(1);
					callback.render();
					callback.renderDebugInfo();
					
				}
			}
			
			
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

	
	MouseCtrl mouse = new MouseCtrl();
	boolean isLeftRelease = false;
	
	@Override
	public MouseCtrl getMouseStatus() {
		Mouse.next();
		mouse.setX(Mouse.getX()/2);
		mouse.setY(240 - Mouse.getY()/2);

		mouse.setLeftPress(Mouse.isButtonDown(0));
		mouse.setLeftRelease(!Mouse.isButtonDown(0));
		return mouse;
	}

}
