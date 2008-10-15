package fi.iki.asb.fun.hacks;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * An abstract class that provides the basic hack functions.
 */
public abstract class HackApplet2 extends Applet {

    private HackPanel panel;

    /**
     * Get the panel.
     */
    public abstract HackPanel createPanel();

    public void init() {
        try {
            Properties props = new Properties();
            props.load((new URL(getParameter("properties"))).openStream());
            props.put("width", String.valueOf(getSize().width));
            props.put("height", String.valueOf(getSize().height));

            panel = createPanel();
            panel.init(props);

            setLayout(new BorderLayout());
            add(panel, BorderLayout.CENTER);
        } catch (IOException ex) {
            ex.printStackTrace();
            panel = null;
        }
    }

    public void start() {
        panel.start();
    }

    public void stop() {
        panel.stop();
    }
}
