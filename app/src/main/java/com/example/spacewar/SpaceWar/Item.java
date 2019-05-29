package com.example.spacewar.SpaceWar;

public class Item
{
    // dimensions of the item asset
    public static final int WIDTH = 30;
    public static final int HEIGHT = 30;
    // coordinates for positioning the asset on the screen
    public int x;
    public int y;
    public int direction; // 1: right or -1: left

    public Item(int x, int y, int direction)
    {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }
}