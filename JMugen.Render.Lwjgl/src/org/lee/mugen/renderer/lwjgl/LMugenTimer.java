package org.lee.mugen.renderer.lwjgl;

import org.lee.mugen.renderer.MugenTimer;

/**
 * Timer class Wrapper
 * @author Dr Wong
 *
 */
public final class LMugenTimer implements MugenTimer {
	long lasttime = System.currentTimeMillis();

	long ONE = 60;
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
	public void sleep() {
		double check = 8d;
		/// Check speed every 250 ms
		long temp = SystemTimer.getTime();
		if (temp - lastTime < (1000f/check)/(getFramerate()/check))
			SystemTimer.sleep((long) ((1000f/check)/(getFramerate()/check) - (temp - lastTime)));

		long delta = SystemTimer.getTime() - lastTime;
		lastTime = SystemTimer.getTime();
		fps += delta;
		frame++;
		
		// update our FPS counter if a second has passed
		if (fps >= 1000f/check) {
//			window.setTitle(windowTitle+" (FPS: "+fps+")");
			fps = 0;
			fpsToDisplay = (int) (frame * check);
			frame = 0;
		}

		lasttime = temp;
	}
	int fpsToDisplay = 0;

	
	
	public int getFps() {
		if (framerate == 0)
			return 0;
		return fpsToDisplay;
	}
	public void sleep(long ms) {
		SystemTimer.sleep(ms);		
	}

}