package com.example.spacewar.SpaceWar;

public class Ship
{
    // dimensions of the ship asset
    public static final int WIDTH = 100;
    public static final int HEIGHT = 76;
    // coordinates for positioning the asset on the screen
    public int x;
    public int y;
    // power ups
    public int lives = 1;
    public int multipleBullets = 1;
    public boolean shield = false;

    public Ship()
    {
        this.x = 320/2;
        this.y = 480/2;
    }
}
