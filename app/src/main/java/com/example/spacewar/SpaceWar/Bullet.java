package com.example.spacewar.SpaceWar;

public class Bullet
{
    // dimensions of the bullet asset
    public static final int WIDTH = 5;
    public static final int HEIGHT = 16;
    // coordinates for positioning the asset on the screen
    public int x;
    public int y;

    public Bullet(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
}
