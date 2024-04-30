package com.main.utils;

/**
 * The Button class is a new addition to the project for assessment 2. It represents a graphical button
 * with a specified position and size. The button can be initialised with x and y coordinates, width, and height.
 * It also provides functionality to check if the button has been clicked based on given screen coordinates.
 */
public class Button {
    private float xCoord;
    private float yCoord;
    private float width;
    private float height;

    /**
     * Constructs a Button at coordinates (0, 0) with a width and height of 0.
     */
    public Button() {
        this.xCoord = 0;
        this.yCoord = 0;
        this.width = 0;
        this.height = 0;
    }

    /**
     * Initialises the Button with specified coordinates and size (width and height)
     *
     * @param xCoord The x-coordinate of the button's position.
     * @param yCoord The y-coordinate of the button's position.
     * @param width  The width of the button.
     * @param height The height of the button.
     */
    public void init(float xCoord, float yCoord, float width, float height) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.width = width;
        this.height = height;
    }

    /**
     * Checks if button is clicked based on the given screen coordinates.
     *
     * @param x The x-coordinate of the click.
     * @param y The y-coordinate of the click.
     * @return True if the click is within the button's bounds, otherwise False.
     */
    public boolean isClicked(int x, int y) {
        return  x >= this.xCoord &&
                y >= this.yCoord &&
                x <= this.xCoord + this.width &&
                y <= this.yCoord + this.height;
    }

    /**
     * Returns the x-coordinate of the button.
     * @return The button's x-coordinate.
     */
    public float x() {
        return xCoord;
    }

    /**
     * Returns the y-coordinate of the button.
     * @return The button's y-coordinate.
     */
    public float y() {
        return yCoord;
    }

    /**
     * Returns the width of the button.
     * @return The button's width
     */
    public float width() {
        return width;
    }

    /**
     * Returns the height of the button.
     * @return The button's height.
     */
    public float height() {
        return height;
    }
}
