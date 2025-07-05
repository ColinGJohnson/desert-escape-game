package dev.cgj.games.old;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Handles keyboard input from the user.
 */
class KeyInputHandler extends KeyAdapter {
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean upPressed = false;
    private boolean ePressed = false;
    private boolean rPressed = false;
    private boolean enterPressed = false;
    private boolean anyPressed = false;
    private boolean showDebug = false;

    @Override
    public void keyPressed(KeyEvent e) {

        // respond to move left, right and up keys being pressed
        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
            leftPressed = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
            rightPressed = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
            upPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        // any key pressed
        anyPressed = !anyPressed;

        // enter key to restart game
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }

        // left arrow or a to turn car left
        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
            leftPressed = false;
        }

        // right arrow or d to turn car right
        if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
            rightPressed = false;
        }

        // up arrow or w to move car forward
        if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
            upPressed = false;
        }

        // r key to use nitro
        if (e.getKeyCode() == KeyEvent.VK_R) {
            rPressed = true;
        }

        // if ']' is pressed, show debug
        if (e.getKeyChar() == KeyEvent.VK_CLOSE_BRACKET) {
            showDebug = !showDebug;
        }

        // e key to use a rocket
        if (e.getKeyCode() == KeyEvent.VK_E) {
            ePressed = !ePressed;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

        // if escape is pressed, end game
        if (e.getKeyChar() == 27) {
            System.exit(0);
        }
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public boolean isUpPressed() {
        return upPressed;
    }

    public boolean isePressed() {
        return ePressed;
    }

    public boolean isrPressed() {
        return rPressed;
    }

    public boolean isEnterPressed() {
        return enterPressed;
    }

    public boolean isAnyPressed() {
        return anyPressed;
    }

    public boolean isShowDebug() {
        return showDebug;
    }

    public void setLeftPressed(boolean leftPressed) {
        this.leftPressed = leftPressed;
    }

    public void setRightPressed(boolean rightPressed) {
        this.rightPressed = rightPressed;
    }

    public void setUpPressed(boolean upPressed) {
        this.upPressed = upPressed;
    }

    public void setePressed(boolean ePressed) {
        this.ePressed = ePressed;
    }

    public void setrPressed(boolean rPressed) {
        this.rPressed = rPressed;
    }

    public void setEnterPressed(boolean enterPressed) {
        this.enterPressed = enterPressed;
    }

    public void setAnyPressed(boolean anyPressed) {
        this.anyPressed = anyPressed;
    }

    public void setShowDebug(boolean showDebug) {
        this.showDebug = showDebug;
    }
}
