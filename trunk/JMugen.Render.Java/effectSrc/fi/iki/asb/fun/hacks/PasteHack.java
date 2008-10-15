package fi.iki.asb.fun.hacks;


/**
 * Paste one pixel image onto another. The source image must fit inside the
 * target image.
 */
public class PasteHack extends Hack {

    /**
     * The texture.
     */
    private PixelImage texture;

    private int x;
    private int y;

    public PasteHack(PixelImage texture, int x, int y) {
        this.texture = texture;
        this.x = x;
        this.y = y;
    }

    // ==================================================================== //

    /**
     *
     */
    public void tick() {
        int frame_index = y * frame.width + x;
        int texture_index = 0;

        for (int i = 0; i < texture.height; i++) {
            System.arraycopy(texture.pixels, texture_index,
                             frame.pixels, frame_index,
                             texture.width);

            frame_index += frame.width;
            texture_index += texture.width;
        }
    }
}
