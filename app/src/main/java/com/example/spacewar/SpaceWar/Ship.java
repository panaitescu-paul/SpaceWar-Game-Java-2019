package com.example.spacewar.SpaceWar;

public class Ship
{
    // dimensions of the ship asset
    public static final int WIDTH = 100;
    public static final int HEIGHT = 76;
    // coordinates for positioning the asset on the screen
    public int x = 0;
    public int y = 0;
    // power ups
    public int lives = 1;
    public int multipleBullets = 1;
    public boolean shield = false;


    public Ship()
    {
        x = 0;
        y = 480-76-10;
    }
}
