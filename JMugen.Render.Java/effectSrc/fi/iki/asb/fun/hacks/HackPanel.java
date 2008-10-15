package fi.iki.asb.fun.hacks;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Panel;
import java.awt.Toolkit;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * An abstract class that provides the basic hack functions.
 */
public abstract class HackPanel extends Panel implements Runnable {

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
     * The properties.
     */
    private Properties props;

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
    public abstract void initHack() throws IOException;

    /**
     * Run the hack.
     */
    public abstract void main();

    /**
     * Initialize this applet.
     */
    public void init(Properties props) throws IOException {

        this.props = props;

        // Get applet size.
        size = this.getMinimumSize();

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
        if (image != null && frame != null) {
            graphics.drawImage(image, 0, 0,
                               frame.width, frame.height,
                               null);
        }
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
     * Get the specified parameter.
     */
    public String getParameter(String name) {
        return (String)props.get(name);
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
    public PixelImage getImageParameter(String name) throws IOException {
        Toolkit tk = getToolkit();
        Image image = tk.createImage(new URL(getParameter(name)));

        try {
            mt.addImage(image, 0);
            mt.waitForAll();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return new PixelImage(image);
    }

    public Dimension getMinimumSize() {
        return new Dimension(getIntParameter("width", "0"),
                             getIntParameter("height", "0"));
    }

    public Dimension getPreferredSize() {
        return getMinimumSize();
    }

    public Dimension getMaximumSize() {
        return getMinimumSize();
    }

    // ==================================================================== //

    /**
     * 
     */
    public static void main(String args[]) throws Exception {
        if (args.length == 0) {
            return;
        }

        // Load properties.
        Properties props = new Properties();
        props.load(new FileInputStream(args[0]));

        // Create the hack panel.
        String className = (String)props.get("class");
        Class clazz = Class.forName(className);
        HackPanel panel = (HackPanel)clazz.newInstance();
        panel.init(props);

        // Create the UI.
        Frame frame = new Frame();
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);

        // Start the hack.
        panel.start();
    }

}
