package fi.iki.asb.fun.hacks;

import java.applet.Applet;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;

/**
 * An abstract class that provides the basic hack functions.
 */
public abstract class HackApplet extends Applet implements Runnable {

    /**
     * The image producer that converts the pixels into an Image object.
     */
    private PixelImageProducer producer;

    /**
     * The thread which runs this applet.
     */
    private Thread thread = null;

    /**
     * The final image that is drawn to the applet.
     */
    private Image image;

    /**
     * Delay between updates.
     */
    private long delay;

    /**
     * The size of this applet.
     */
    protected Dimension size = null;

    /**
     * The frame which is displayed.
     */
    protected PixelImage frame;

    /**
     * The media tracker for loading images.
     */
    protected MediaTracker mt = new MediaTracker(this);

    // ==================================================================== //

    /**
     * Initialize the hack.
     */
    public abstract void initHack();

    /**
     * Run the hack.
     */
    public abstract void main();

    /**
     * Initialize this applet.
     */
    public void init() {

        // Get applet size.
        size = this.getSize();

        // Create the pixel image.
        frame = new PixelImage(size.width, size.height);

        // Create a producer with the pixel image.
        producer = new PixelImageProducer(frame);

        // Create the image with the default toolkit.
        image = Toolkit.getDefaultToolkit().createImage(producer);

        // Get the delay parameter.
        try {
            delay = Long.parseLong(getParameter("delay", "40"));
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            delay = 40;
        }

        // Initialize the hack.
        initHack();
    }

    /**
     * Paint the component.
     */
    public void paint() {

        // Update the pixels in the producer.
        producer.update();

        // Get component graphics object.
        Graphics graphics = getGraphics();

        // Draw image to graphics context.
        graphics.drawImage(image, 0, 0,
                           frame.width, frame.height,
                           null);
    }

    /**
     * Atart animation.
     */
    public void start() {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    /**
     * Animate.
     */
    public void run() {

        int fps_count = 0;
        long fps_start, fps_stop;
        long start, stop;

        fps_start = System.currentTimeMillis();
        start = System.currentTimeMillis();
        while (Thread.currentThread() == thread) {

            main();
            paint();
            stop = System.currentTimeMillis();

            try {
                Thread.sleep(Math.max(0, delay - (stop - start)));
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (++fps_count == 48) {
                fps_stop = System.currentTimeMillis();
                showStatus("FPS: " + 48000 / (fps_stop - fps_start));
                fps_start = fps_stop;
                fps_count = 0;
            }

            start = stop;
        }
    }

    /**
     * Stop animation.
     */
    public void stop() {
        thread = null;
    }

    /**
     * Convenience method.
     */
    public String getParameter(String name, String def) {
        String value = getParameter(name);
        return (value == null) ? def : value;
    }

    /**
     * Convenience method.
     */
    public int getIntParameter(String name, String def) {
        return Integer.parseInt(getParameter(name, def));
    }

    /**
     * Convenience method.
     */
    public PixelImage getImageParameter(String name) {
        Image image = getImage(getDocumentBase(), getParameter(name));

        try {
            mt.addImage(image, 0);
            mt.waitForAll();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return new PixelImage(image);
    }

}
