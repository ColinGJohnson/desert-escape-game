package dev.cgj.games.old;

import javax.imageio.ImageIO;
import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;

public class DigitRenderer {
    private static final int DIGIT_COUNT = 10;
    private static final int DIGIT_WIDTH = 16; // Width of each digit
    private static final String SPRITE_PATH = "/sprites/";
    private final Image[] digitImages = new Image[DIGIT_COUNT];

    public DigitRenderer() {
        loadDigitImages();
    }

    /**
     * Loads digit images (0-9) into the digitImages array.
     */
    private void loadDigitImages() {
        for (int i = 0; i < DIGIT_COUNT; i++) {
            try {
                URL resource = EscapeGame.class.getResource("%s%d.png".formatted(SPRITE_PATH, i));
                digitImages[i] = ImageIO.read(resource);
            } catch (IOException e) {
                throw new RuntimeException("Failed to load digit image: " + i, e);
            }
        }
    }

    /**
     * Draws a number made of images for each digit, starting at given (x, y) coordinates.
     *
     * @param g      The Graphics2D instance for drawing.
     * @param c      The Canvas instance for the drawing context.
     * @param number The number to draw.
     * @param x      The x-coordinate for the drawing starting point.
     * @param y      The y-coordinate for the drawing starting point.
     */
    public void drawImageNumber(Graphics2D g, Canvas c, int number, int x, int y) {
        int offset = 0;
        for (char digitChar : Integer.toString(number).toCharArray()) {
            int digit = Character.getNumericValue(digitChar);
            g.drawImage(digitImages[digit], x + offset, y, c);
            offset += DIGIT_WIDTH;
        }
    }
}
