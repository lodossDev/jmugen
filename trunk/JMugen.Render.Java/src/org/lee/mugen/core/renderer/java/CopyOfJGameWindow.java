package org.lee.mugen.core.renderer.java;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.lee.framework.swing.WindowsUtils;
import org.lee.mugen.core.Game;
import org.lee.mugen.imageIO.ImageScale2x;
import org.lee.mugen.input.ISpriteCmdProcess;
import org.lee.mugen.renderer.GameWindow;
import org.lee.mugen.renderer.MugenTimer;

public class CopyOfJGameWindow extends Canvas implements GameWindow {

	
	/** The stragey that allows us to use accelerate page flipping */
	protected BufferStrategy strategy;
	/** True if the game is currently "running", i.e. the game loop is looping */
	protected boolean gameRunning = true;
//	private boolean windowFocus = true;
	
	/** The frame in which we'll display our canvas */
	protected JFrame frame;
	/** The width of the display */
	protected int width;
	/** The height of the display */
	protected int height;
	/** The callback which should be notified of events caused by this window */
	protected Game callback;
	/** The current accelerated graphics context */
	protected Graphics2D gStrategy;
	
	protected BufferedImage normalBuffer;
	protected BufferedImage hisResBuffer;
	protected BufferedImage sai2xBuffer;
	protected ImageScale2x imgScale2x;
	
//	protected WaterEffect dampingEffect;
	
	public CopyOfJGameWindow() {
		frame = new JFrame("JMugen");
		setResolution(640, 480);
		WindowsUtils.centerScreen(frame);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
//		frame.addWindowFocusListener(new WindowAdapter() {
//
//			@Override
//			public void windowGainedFocus(WindowEvent e) {
//				windowFocus = true;
//			}
//
//			@Override
//			public void windowLostFocus(WindowEvent e) {
//				windowFocus = false;
//			}
//			
//		});
	}
	public void setTitle(String title) {
		frame.setTitle(title);		
	}
	public void setResolution(int width, int height) {
		this.width = width;
		this.height = height;
		
	}

	public void addSpriteKeyProcessor(final ISpriteCmdProcess scp) {
		addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				scp.keyPressed(e.getKeyCode());
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				scp.keyReleased(e.getKeyCode());
				
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}});
	}

	public void setGameWindowCallback(Game callback) {
		this.callback = callback;		
	}

//	protected SpriteDebugerUI sprDebugerUI;

	public void start() throws Exception  {
		// get hold the content of the frame and set up the resolution of the game
		JPanel panel = (JPanel) frame.getContentPane();
		panel.setPreferredSize(new Dimension(width, height));
		panel.setLayout(null);

		// setup our canvas size and put it into the content of the frame
		setBounds(0, 0, width, height);
		panel.add(this);
		
		// Tell AWT not to bother repainting our canvas since we're
		// going to do that our self in accelerated mode
		setIgnoreRepaint(true);
		
		// finally make the window visible 
		frame.pack();
		frame.setResizable(false);
		WindowsUtils.centerScreen(frame);
		frame.setVisible(true);
		
		// add a listener to respond to the user closing the window. If they
		// do we'd like to exit the game
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// request the focus so key events come to us
		requestFocus();
		
		// create the buffering strategy which will allow AWT
		// to manage our accelerated graphics
		createBufferStrategy(2);
		strategy = getBufferStrategy();
		
		// if we have a callback registered then notify 
		// it that initialisation is taking place
		if (callback != null) {
			callback.init(this);
		}
//		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
//		normalBuffer = gc.createCompatibleImage(320, 240,Transparency.BITMASK);
//		sai2xBuffer = gc.createCompatibleImage(320 * 2, 240 * 2,Transparency.BITMASK);
		
		normalBuffer = new BufferedImage(320, 240, BufferedImage.TYPE_INT_ARGB);
		hisResBuffer = new BufferedImage(320 * 2, 240 * 2, BufferedImage.TYPE_INT_ARGB);
		sai2xBuffer = new BufferedImage(320 * 2, 240 * 2, BufferedImage.TYPE_INT_ARGB);
		imgScale2x = new ImageScale2x(normalBuffer);
		
//		dampingEffect = new WaterEffect(320, 240);
//		sprDebugerUI = new SpriteDebugerUI();
//		sprDebugerUI.setVisible(true);
		// start the game loop
		gameLoop();
	}

	boolean isTransitionToHis = false;
	/**
	 * Retrieve the current accelerated graphics context. Note this
	 * method has been made package scope since only the other 
	 * members of the "java2D" package need to access it.
	 * 
	 * @return The current accelerated graphics context for this window
	 */
	public Graphics2D getDrawGraphics() {

//		Sprite one = StateMachine.getInstance().getSpriteInstance("1");
//		Sprite two = StateMachine.getInstance().getSpriteInstance("2");
		
//		Stage stage = StateMachine.getInstance().getInstanceOfStage();
//		float maxDist = Math.abs(stage.getCamera().getBoundleft()) + Math.abs(stage.getCamera().getBoundright());
//		int minDist = 300;
//		maxDist += minDist/2;
//		float dist = FightEngine.getXDistatnce(one, two);
//		dist = dist > maxDist? maxDist: dist;
//		dist = dist < minDist? minDist: dist;
		
//		float scale = minDist/dist;

		
//		if (scale != 1) {
//			
//			Graphics2D gHisRes = (Graphics2D) hisResBuffer.createGraphics();
//			
//			gHisRes.scale(scale * 2, scale * 2);
//			gHisRes.translate((dist - minDist)/2f, (dist - minDist)/2f);
//			gHisRes.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
//			return gHisRes;
////			
//		} else {
			Graphics2D g = (Graphics2D) normalBuffer.createGraphics();
//			g.scale(scale, scale);
//			g.translate((dist - minDist)/2f, (dist - minDist)/2f);
//			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
			return g;
//		}
		
		

	}
	
	public Graphics2D getDebugDrawGraphics() {
		return gStrategy;
	}
	
//	public float getScale() {
//		Sprite one = StateMachine.getInstance().getSpriteInstance("1");
//		Sprite two = StateMachine.getInstance().getSpriteInstance("2");
//		
//		Stage stage = StateMachine.getInstance().getInstanceOfStage();
//		float maxDist = Math.abs(stage.getCamera().getBoundleft()) + Math.abs(stage.getCamera().getBoundright());
//		int minDist = 300;
//		maxDist += minDist/2;
//		float dist = FightEngine.getXDistatnce(one, two);
//		dist = dist > maxDist? maxDist: dist;
//		dist = dist < minDist? minDist: dist;
//		float scale = minDist/dist;
//		return scale;
//	}
	
	/**
	 * Run the main game loop. This method keeps rendering the scene
	 * and requesting that the callback update its screen.
	 * @throws Exception 
	 */
	protected void gameLoop() throws Exception {
		
		while (gameRunning) {
			

			if (getTimer().getFramerate() == 0) {
				getTimer().sleep(1000 / 60);
				continue;
			}
			
			// Get hold of a graphics context for the accelerated 
			// surface and blank it out
			gStrategy = (Graphics2D) strategy.getDrawGraphics();
			gStrategy.setColor(Color.BLACK);
			gStrategy.fillRect(0, 0, width, height);
			
//			float scale = getScale();

			if (callback != null) {
//				if (windowFocus || true) {
				
				
//				if (scale != 1) {
//					Graphics2D gHisRes = (Graphics2D) hisResBuffer.getGraphics();
//					gHisRes.setColor(Color.BLACK);
//					gHisRes.fillRect(0, 0, width, height);
//					
//					
//					callback.update(1);
//					JGameWindow jgw = (JGameWindow) StateMachine.getInstance().getWindow();
//					Graphics2D g = (Graphics2D) jgw.getDrawGraphics();
//					g.setColor(Color.BLACK);
//					g.fillRect(0, 0, 320, 240);
//					callback.render();
//					
//					gStrategy.drawImage(hisResBuffer, 0, 0, null);
//
//				} else {

					callback.update(1);
					Graphics2D g = (Graphics2D) getDrawGraphics();
					g.setColor(Color.BLACK);
					g.fillRect(0, 0, 320, 240);
					callback.render();

					Image image = normalBuffer;
//					SwimFilter filter = new SwimFilter();
//					filter.setAmount(2);
//					filter.setAngle(45);
//					filter.setTurbulence(45);
//					filter.setTime(StateMachine.getInstance().getGameState().getGameTime());
//					
//					image = filter.filter((BufferedImage) image, null);
//					dampingEffect.initialize(image);
//					dampingEffect.process(image.getGraphics());
					image = imgScale2x.getScaledImage();
//					gStrategy.setColor(Color.BLACK);
//					gStrategy.fillRect(0, 0, width, height);

					gStrategy.drawImage(image, 0, 0, null);
//				}


					
				callback.renderDebugInfo();
					
//				} else {
//					g.drawImage(sai2xBuffer, 0, 0, null);
//				}
			}


			
			// finally, we've completed drawing so clear up the graphics
			// and flip the buffer over
			gStrategy.dispose();
			strategy.show();
		}
	}
//	public WaterEffect getDampingEffect() {
//		return dampingEffect;
//	}
	public BufferedImage getHisResBuffer() {
		return hisResBuffer;
	}
	public BufferedImage getNormalBuffer() {
		return normalBuffer;
	}
//	public SpriteDebugerUI getSprDebugerUI() {
//		return sprDebugerUI;
//	}
	
	private MugenTimer mugenTimer = new MugenTimer() {
		long lasttime = System.currentTimeMillis();

		long ONE = 1000 / 60;
		private long framerate = ONE;
		private long lastTime = 0;//System.currentTimeMillis();
		int frame = 0;
		int fps = 0;
		
		/* (non-Javadoc)
		 * @see org.lee.mugen.core.MugenTimer#getFramerate()
		 */
		public long getFramerate() {
			return framerate;
		}
		/* (non-Javadoc)
		 * @see org.lee.mugen.core.MugenTimer#setFramerate(long)
		 */
		public void setFramerate(long famerate) {
			this.framerate = famerate;
		}
		/* (non-Javadoc)
		 * @see org.lee.mugen.core.MugenTimer#sleep()
		 */
//		public void sleep() {
//			long temp = SystemTimer.getTime();
//			if (temp - lastTime < getFramerate())
//				SystemTimer.sleep(getFramerate() - (temp - lastTime));
//			// work out how long its been since the last update, this
//			// will be used to calculate how far the entities should
//			// move this loop
//			long delta = SystemTimer.getTime() - lastTime;
//			lastTime = SystemTimer.getTime();
//			fps += delta;
//			frame++;
//			
//			// update our FPS counter if a second has passed
//			if (fps >= 1000) {
////				window.setTitle(windowTitle+" (FPS: "+fps+")");
//				fps = 0;
//				fpsToDisplay = frame;
//				frame = 0;
//			}
////			frame++;
////			long temp = System.currentTimeMillis();
////			long diff = temp - lastTime;
////			if (diff >= 1000) {
////				lastTime = System.currentTimeMillis();
////				fps = frame;
////				fpsToDisplay = fps;
////				frame = 0;
////			}
////			
////			if (temp - lasttime < getFramerate()) {
////				synchronized (this) {
////					try {
////						wait(getFramerate() - (temp - lasttime));
////					} catch (InterruptedException e) {
////					}
////				}
////			}
//			lasttime = temp;
//		}
		int fpsToDisplay = 0;
		/* (non-Javadoc)
		 * @see org.lee.mugen.core.MugenTimer#getFps()
		 */
		public int getFps() {
			if (framerate == 0)
				return 0;
			return fpsToDisplay;
		}
		public void sleep(long ms) {
//			SystemTimer.sleep(ms);		
		}
		@Override
		public void sleep() {
			// TODO Auto-generated method stub
			
		}
	};
	public MugenTimer getTimer() {
		return mugenTimer;
	}

}
