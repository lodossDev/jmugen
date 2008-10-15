package fi.iki.asb.fun.hacks;

import java.util.ArrayList;
import java.util.Random;

/**
 * A base class for graphics hacks. All hack objects draw their pixels
 * to a PixelImage.
 */
public class ExplodeHack extends Hack {

    public static final int FIXEDP = 8;

    private Random rand = new Random();

    private Particle[] particles;

    public int particleCount = 0;

    private PixelImage texture, background;

    public ExplodeHack(PixelImage texture, PixelImage background) {
        this.texture = texture;
        this.background = background;
    }

    /**
     * Called automatically by <code>init(PixelImage)</code>. Subclasses
     * should implement this and do their initialization here.
     */
    public void init() {

        ArrayList list = new ArrayList(texture.width * texture.height);

        int xm = frame.width >> 1;
        int ym = frame.height >> 1;

        for (int y = 0; y < texture.height; y++) {
            for (int x = 0; x < texture.width; x++) {
                int index = x + y * texture.width;
                int color = texture.pixels[index];

                if ((color & 0xFF000000) == 0xFF000000) {
                    Particle p = new Particle(x << FIXEDP, y << FIXEDP);
                    p.color = color;

                    int xd = x - xm;
                    int yd = y - ym;
                    if (xd == 0 && yd == 0) {
                        continue;
                    }

                    double hyp = Math.sqrt((xd * xd) + (yd * yd));
                    double factor = 1.0 / hyp;

                    p.xdir = (int)(((rand.nextDouble() - 0.5) + xd * factor) * (2 << FIXEDP));
                    p.ydir = (int)(((rand.nextDouble() - 0.5) + yd * factor) * (2 << FIXEDP));

                    //p.xdir= (int)((rand.nextDouble() + 0.1) * (2 << FIXEDP));
                    //p.ydir= (int)((rand.nextDouble() + 0.1) * (2 << FIXEDP));
                    //if (rand.nextBoolean()) p.xdir = -p.xdir;
                    //if (rand.nextBoolean()) p.ydir = -p.ydir;

                    list.add(p);
                }
            }
        }

        particles = new Particle[list.size()];
        particles = (Particle[])list.toArray(particles);
        particleCount = particles.length;

        System.arraycopy(texture.pixels, 0,
                         frame.pixels, 0,
                         texture.pixels.length);
    }

    /**
     * Generate the next frame.
     */
    public void tick() {

        System.arraycopy(background.pixels, 0,
                         frame.pixels, 0,
                         background.pixels.length);

        particleCount = 0;

        int index;
        Particle p;
        for (int i = 0; i < particles.length; i++) {
            p = particles[i];
            if (p.dead == false) {
                int x = p.x >> FIXEDP;
                int y = p.y >> FIXEDP;
                p.x += p.xdir;
                p.y += p.ydir;

                if (x < 0 || y < 0 || x >= frame.width || y >= frame.height) {
                    p.dead = true;
                } else {
                    index = x + y * frame.width;
                    frame.pixels[index] = p.color;
                    particleCount++;
                }
            }
        }
    }

}

/**
 * A container class for a single particle.
 */
class Particle {

    public int x;
    public int y;
    public int color;
    public boolean dead = false;
    public int xdir;
    public int ydir;

    public Particle(int x, int y) {
        this.x = x;
        this.y = y;
    }

}
