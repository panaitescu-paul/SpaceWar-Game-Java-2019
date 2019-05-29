package com.example.spacewar.SpaceWar;

public class Enemy
{
    // dimensions of the enemy asset
    public static final int WIDTH = 30;
    public static final int HEIGHT = 30;
    // coordinates for positioning the asset on the screen
    public int x;
    public int y;
    //direction of the enemy
    public int direction; // 1: right or -1: left
    public int health;
    public boolean shooting; // shooting enemy type
    public boolean shield; // shield enemy type

    public Enemy(int x, int y, int direction, int health, boolean shooting, boolean shield)
    {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.health = health;
        this.shooting = shooting;
        this.shield = shield;
    }
}
