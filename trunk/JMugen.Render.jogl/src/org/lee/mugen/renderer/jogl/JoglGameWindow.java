package org.lee.mugen.renderer.jogl;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.lee.framework.swing.WindowsUtils;
import org.lee.mugen.core.Game;
import org.lee.mugen.core.Game.DebugAction;
import org.lee.mugen.input.CmdProcDispatcher;
import org.lee.mugen.input.ISpriteCmdProcess;
import org.lee.mugen.renderer.GameWindow;
import org.lee.mugen.renderer.GraphicsWrapper;
import org.lee.mugen.renderer.MugenTimer;

public class JoglGameWindow implements GameWindow, GLEventListener {
	
	private int width;
	private int height;
	private String title;

	private Game callback;
	private GL _gl;
	private GLCanvas canvas;
	
	private MouseCtrl mouse = new MouseCtrl();
	private boolean isFinishInit = false;
	private Thread loadingInitThread = new Thread() {
		@Override
		public void run() {
			try {
				callback.init(JoglGameWindow.this);
			} catch (Exception e) {
				e.printStackTrace();
			}

			
			JoglMugenDrawer.createImageToTextPreparer();
			isFinishInit = true;
		}
	};
	
	public GL getGl() {
		return _gl;
	}

	public JoglGameWindow() {
		setTitle("JMugen");
		width = 640;
		height = 480;
	}

	/* */

	 /* */
	private Map<Integer, Boolean> keyMapPress = new HashMap<Integer, Boolean>();
	@Override
	public void addSpriteKeyProcessor(final ISpriteCmdProcess scp) {
		canvas.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (scp == null)
					return;
				scp.keyPressed(e.getKeyCode());
				keyMapPress.put(e.getKeyCode(), true);
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (scp == null)
					return;
				scp.keyReleased(e.getKeyCode());
				keyMapPress.put(e.getKeyCode(), false);
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}});
	}
	private DebugEventManager debugEventManager = new DebugEventManager();
	
	private class DebugEventManager implements KeyListener {
		private Map<DebugAction, int[]> actionKeyMap = new HashMap<DebugAction, int[]>();
		private Map<DebugAction, Boolean> actionCtrl = new HashMap<DebugAction, Boolean>();
		
		public DebugEventManager() {
			addAction(DebugAction.SWICTH_PLAYER_DEBUG_INFO, new int[] {KeyEvent.VK_D}, true);
			addAction(DebugAction.EXPLOD_DEBUG_INFO, new int[] {KeyEvent.VK_E}, true);
			addAction(DebugAction.INIT_PLAYER, new int[] {KeyEvent.VK_SPACE});
			addAction(DebugAction.SHOW_HIDE_CNS, new int[] {KeyEvent.VK_C}, true);
			addAction(DebugAction.SHOW_HIDE_ATTACK_CNS, new int[] {KeyEvent.VK_X}, true);
			addAction(DebugAction.INCREASE_FPS, new int[] {KeyEvent.VK_ADD}, true);
			addAction(DebugAction.DECREASE_FPS, new int[] {KeyEvent.VK_SUBTRACT}, true);
			addAction(DebugAction.RESET_FPS, new int[] {KeyEvent.VK_MULTIPLY}, true);
			addAction(DebugAction.DEBUG_PAUSE, new int[] {KeyEvent.VK_P}, true);
			addAction(DebugAction.PAUSE_PLUS_ONE_FRAME, new int[] {KeyEvent.VK_A}, true);
			addAction(DebugAction.DISPLAY_HELP, new int[] {KeyEvent.VK_F1});

			
		}
		private void addAction(DebugAction action, int[] keys) {
			addAction(action, keys, false);
		}
		private void addAction(DebugAction action, int[] keys, boolean ctrl) {
			actionKeyMap.put(action, keys);
			actionCtrl.put(action, ctrl);
		}
		
		@Override
		public void keyPressed(KeyEvent e) {
			
			
		}
		@Override
		public void keyReleased(KeyEvent e) {
			for (DebugAction action : actionKeyMap.keySet()) {
				if (e.getKeyCode() == actionKeyMap.get(action)[0]) {

					if (actionCtrl.get(action).booleanValue()
							&& e.isControlDown()) {
						callback.onDebugAction(action);
					} else if (!actionCtrl.get(action).booleanValue()) {
						callback.onDebugAction(action);
					}
				}
			}
		}
		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	

	private void initKeys() throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {
		ResourceBundle bundle = ResourceBundle.getBundle("keys");
		
		// P1
		String prefix = "VK_";
		
		CmdProcDispatcher cd1 = new CmdProcDispatcher(
				KeyEvent.class.getDeclaredField(prefix + bundle.getString("P1.UP").toUpperCase()).getInt(null), 
				KeyEvent.class.getDeclaredField(prefix + bundle.getString("P1.DOWN").toUpperCase()).getInt(null),
				KeyEvent.class.getDeclaredField(prefix + bundle.getString("P1.LEFT").toUpperCase()).getInt(null),
				KeyEvent.class.getDeclaredField(prefix + bundle.getString("P1.RIGHT").toUpperCase()).getInt(null),
				KeyEvent.class.getDeclaredField(prefix + bundle.getString("P1.A").toUpperCase()).getInt(null),
				KeyEvent.class.getDeclaredField(prefix + bundle.getString("P1.B").toUpperCase()).getInt(null),
				KeyEvent.class.getDeclaredField(prefix + bundle.getString("P1.C").toUpperCase()).getInt(null),
				KeyEvent.class.getDeclaredField(prefix + bundle.getString("P1.X").toUpperCase()).getInt(null),
				KeyEvent.class.getDeclaredField(prefix + bundle.getString("P1.Y").toUpperCase()).getInt(null),
				KeyEvent.class.getDeclaredField(prefix + bundle.getString("P1.Z").toUpperCase()).getInt(null),
				KeyEvent.class.getDeclaredField(prefix + bundle.getString("P1.ABC").toUpperCase()).getInt(null),
				KeyEvent.class.getDeclaredField(prefix + bundle.getString("P1.XYZ").toUpperCase()).getInt(null));	
		
		
		CmdProcDispatcher.getSpriteDispatcherMap().put("1", cd1);
		
		
		CmdProcDispatcher cd2 = new CmdProcDispatcher(
				KeyEvent.class.getDeclaredField(prefix + bundle.getString("P2.UP").toUpperCase()).getInt(null), 
				KeyEvent.class.getDeclaredField(prefix + bundle.getString("P2.DOWN").toUpperCase()).getInt(null),
				KeyEvent.class.getDeclaredField(prefix + bundle.getString("P2.LEFT").toUpperCase()).getInt(null),
				KeyEvent.class.getDeclaredField(prefix + bundle.getString("P2.RIGHT").toUpperCase()).getInt(null),
				KeyEvent.class.getDeclaredField(prefix + bundle.getString("P2.A").toUpperCase()).getInt(null),
				KeyEvent.class.getDeclaredField(prefix + bundle.getString("P2.B").toUpperCase()).getInt(null),
				KeyEvent.class.getDeclaredField(prefix + bundle.getString("P2.C").toUpperCase()).getInt(null),
				KeyEvent.class.getDeclaredField(prefix + bundle.getString("P2.X").toUpperCase()).getInt(null),
				KeyEvent.class.getDeclaredField(prefix + bundle.getString("P2.Y").toUpperCase()).getInt(null),
				KeyEvent.class.getDeclaredField(prefix + bundle.getString("P2.Z").toUpperCase()).getInt(null),
				KeyEvent.class.getDeclaredField(prefix + bundle.getString("P2.ABC").toUpperCase()).getInt(null),
				KeyEvent.class.getDeclaredField(prefix + bundle.getString("P2.XYZ").toUpperCase()).getInt(null));	
		
		CmdProcDispatcher.getSpriteDispatcherMap().put("2", cd2);
		
		CmdProcDispatcher cd3 = new CmdProcDispatcher(
				KeyEvent.class.getDeclaredField(prefix + bundle.getString("P3.UP").toUpperCase()).getInt(null), 
				KeyEvent.class.getDeclaredField(prefix + bundle.getString("P3.DOWN").toUpperCase()).getInt(null),
				KeyEvent.class.getDeclaredField(prefix + bundle.getString("P3.LEFT").toUpperCase()).getInt(null),
				KeyEvent.class.getDeclaredField(prefix + bundle.getString("P3.RIGHT").toUpperCase()).getInt(null),
				KeyEvent.class.getDeclaredField(prefix + bundle.getString("P3.A").toUpperCase()).getInt(null),
				KeyEvent.class.getDeclaredField(prefix + bundle.getString("P3.B").toUpperCase()).getInt(null),
				KeyEvent.class.getDeclaredField(prefix + bundle.getString("P3.C").toUpperCase()).getInt(null),
				KeyEvent.class.getDeclaredField(prefix + bundle.getString("P3.X").toUpperCase()).getInt(null),
				KeyEvent.class.getDeclaredField(prefix + bundle.getString("P3.Y").toUpperCase()).getInt(null),
				KeyEvent.class.getDeclaredField(prefix + bundle.getString("P3.Z").toUpperCase()).getInt(null),
				KeyEvent.class.getDeclaredField(prefix + bundle.getString("P3.ABC").toUpperCase()).getInt(null),
				KeyEvent.class.getDeclaredField(prefix + bundle.getString("P3.XYZ").toUpperCase()).getInt(null));	
		
		
		CmdProcDispatcher.getSpriteDispatcherMap().put("3", cd3);
		
		
		CmdProcDispatcher cd4 = new CmdProcDispatcher(
				KeyEvent.class.getDeclaredField(prefix + bundle.getString("P4.UP").toUpperCase()).getInt(null), 
				KeyEvent.class.getDeclaredField(prefix + bundle.getString("P4.DOWN").toUpperCase()).getInt(null),
				KeyEvent.class.getDeclaredField(prefix + bundle.getString("P4.LEFT").toUpperCase()).getInt(null),
				KeyEvent.class.getDeclaredField(prefix + bundle.getString("P4.RIGHT").toUpperCase()).getInt(null),
				KeyEvent.class.getDeclaredField(prefix + bundle.getString("P4.A").toUpperCase()).getInt(null),
				KeyEvent.class.getDeclaredField(prefix + bundle.getString("P4.B").toUpperCase()).getInt(null),
				KeyEvent.class.getDeclaredField(prefix + bundle.getString("P4.C").toUpperCase()).getInt(null),
				KeyEvent.class.getDeclaredField(prefix + bundle.getString("P4.X").toUpperCase()).getInt(null),
				KeyEvent.class.getDeclaredField(prefix + bundle.getString("P4.Y").toUpperCase()).getInt(null),
				KeyEvent.class.getDeclaredField(prefix + bundle.getString("P4.Z").toUpperCase()).getInt(null),
				KeyEvent.class.getDeclaredField(prefix + bundle.getString("P4.ABC").toUpperCase()).getInt(null),
				KeyEvent.class.getDeclaredField(prefix + bundle.getString("P4.XYZ").toUpperCase()).getInt(null));	
		CmdProcDispatcher.getSpriteDispatcherMap().put("4", cd4);
	}
	

	@Override
	public MouseCtrl getMouseStatus() {
		return mouse;
	}

	MugenTimer joglMugenTimer = new MugenTimer() {

		@Override
		public int getFps() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public long getFramerate() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void setFramerate(long famerate) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void sleep() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void sleep(long ms) {
			// TODO Auto-generated method stub
			
		}};
	
	@Override
	public MugenTimer getTimer() {
		
		return joglMugenTimer;
	}

	
	@Override
	public void setGameWindowCallback(Game callback) {
		this.callback = callback;
		
	}

	@Override
	public void setResolution(int w, int h) {
		width = w;
		height = h;
		
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
		
	}

	@Override
	public void start() throws Exception {
        JFrame frame = new JFrame(title);
        GLCapabilities caps = new GLCapabilities();
        canvas = new GLCanvas(caps);
        canvas.addGLEventListener(this);
        frame.add(canvas);
        canvas.setPreferredSize(new Dimension(width, height));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        final com.sun.opengl.util.Animator animator =
        	new com.sun.opengl.util.Animator(canvas);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
            	System.exit(0);
            }
        });
        frame.pack();
        WindowsUtils.centerScreen(frame);
        frame.setVisible(true);

        animator.start();
        
	}

	
	/*
	 * 
	 * JOGL part
	 * 
	 * 
	 */
	
	@Override
	public void init(GLAutoDrawable drawable) {
		_gl = drawable.getGL();
		_gl.glEnable(GL.GL_TEXTURE_2D);

		// disable the OpenGL depth test since we're rendering 2D graphics
		_gl.glDisable(GL.GL_DEPTH_TEST);
		_gl.glEnable(GL.GL_BLEND);

		_gl.glMatrixMode(GL.GL_PROJECTION);
		_gl.glLoadIdentity();

		_gl.glOrtho(0, width, height, 0, -10000, 10000);

		_gl.glScaled((float) width / 320, (float) height / 240, 0);
		
		
		try {
			initKeys();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		loadingInitThread.start();
		canvas.requestFocus();
		canvas.addKeyListener(debugEventManager);
		canvas.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {

				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				mouse.setLeftPress(SwingUtilities.isLeftMouseButton(e));
				mouse.setRightPress(SwingUtilities.isRightMouseButton(e));
				mouse.setLeftRelease(!SwingUtilities.isLeftMouseButton(e));
				mouse.setRightRelease(!SwingUtilities.isRightMouseButton(e));
				mouse.setX((int) (e.getX()/2f));
				mouse.setY((int) (e.getY()/2f));
				
				if (SwingUtilities.isRightMouseButton(e))
					((JoglMugenDrawer)GraphicsWrapper.getInstance()).setScaleByForMeDebug(!((JoglMugenDrawer)GraphicsWrapper.getInstance()).isScaleByForMeDebug());
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				mouse.setLeftPress(!SwingUtilities.isLeftMouseButton(e));
				mouse.setRightPress(!SwingUtilities.isRightMouseButton(e));
				mouse.setLeftRelease(SwingUtilities.isLeftMouseButton(e));
				mouse.setRightRelease(SwingUtilities.isRightMouseButton(e));

				mouse.setX((int) (e.getX()/2f));
				mouse.setY((int) (e.getY()/2f));
				
			}});
		
		canvas.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
				mouse.setX((int) (e.getX()/2f));
				mouse.setY((int) (e.getY()/2f));
				
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				mouse.setX((int) (e.getX()/2f));
				mouse.setY((int) (e.getY()/2f));
				
			}});
		
	}
	
	@Override
	public void display(GLAutoDrawable drawable) {
		_gl = drawable.getGL();

		if (!isFinishInit) {
			callback.displayPendingScreeen();
		} else {
			try {
				_gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT
						| GL.GL_ACCUM_BUFFER_BIT);
				_gl.glMatrixMode(GL.GL_MODELVIEW);
				_gl.glLoadIdentity();
				callback.update(1);
				
				_gl.glPushMatrix();
				callback.render();
				
				_gl.glPopMatrix();
				
				_gl.glPushMatrix();
				callback.renderDebugInfo();
				_gl.glPopMatrix();
				mouse.setLeftPress(false);
				mouse.setRightPress(false);
				mouse.setLeftRelease(false);
				mouse.setRightRelease(false);
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

	}
	float rot = 0;
	@Override
	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
			boolean deviceChanged) {

		
	}


	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}
	
	

}
