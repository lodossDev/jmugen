package fi.iki.asb.fun.hacks;

import java.util.Hashtable;

public class PixelFont {

    /**
     * The fonts.
     */
    public Hashtable fonts = new Hashtable();

    /**
     * The width of a character.
     */
    public int width;

    /**
     * The height of a character.
     */
    public int height;

    // ==================================================================== //

    /**
     * Create a font object from the given pixel image.
     *
     * @param chars the characters
     * @param image the font pixmap
     */
    public PixelFont(String chars, PixelImage image) {

        this.width = image.width / chars.length();
        this.height = image.height;

        for (int i = 0; i < chars.length(); i++) {
            fonts.put(new Character(chars.charAt(i)),
                      new PixelImage(image, i * width, 0, width, height));
        }
    }

    // ==================================================================== //

    /**
     * Get the character.
     */
    public PixelImage getCharacter(char ch) {
        return (PixelImage)fonts.get(new Character(ch));
    }

}
