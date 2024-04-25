package com.main.utils;

public class Button
{
    private float xCoord;
    private float yCoord;
    private float width;
    private float height;

    public Button()
    {
        this.xCoord = 0;
        this.yCoord = 0;
        this.width = 0;
        this.height = 0;
    }
    public void init(float x_coord, float y_coord, float width, float height)
    {
        this.xCoord = x_coord;
        this.yCoord = y_coord;
        this.width = width;
        this.height = height;
    }
    public boolean isClicked(int x, int y)
    {
        return  x >= this.xCoord &&
                y >= this.yCoord &&
                x <= this.xCoord + this.width &&
                y <= this.yCoord + this.height;
    }

    public float x() {
        return xCoord;
    }

    public float y() {
        return yCoord;
    }

    public float width() {
        return width;
    }

    public float height() {
        return height;
    }
}
