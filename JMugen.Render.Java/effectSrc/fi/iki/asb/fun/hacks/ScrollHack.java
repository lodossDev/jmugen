package fi.iki.asb.fun.hacks;

/**
 *
 */
public class ScrollHack extends Hack {

    private PixelFont font;
    private String text;
    private int x;
    private int y;

    // ==================================================================== //

    /**
     *
     */
    public ScrollHack(String text, PixelFont font, int y) {
        this.text = text;
        this.font = font;
        this.y = y;
    }

    // ==================================================================== //

    public void init() {
        x = frame.width;
    }

    /**
     * Generate the next frame.
     */
    public void tick() {
        int x1 = x;

        for (int i = 0; i < text.length(); i++) {

            PixelImage ch = font.getCharacter(text.charAt(i));

            if (ch != null) {
                ch.drawTo(frame, x1, y);
            }

            x1 += font.width;
        }

        if (x1 < 0) {
            x = frame.width;
        } else {
            x -= 2;
        }
    }
}
