package com.example.spacewar.SpaceWar;

public class Item
{
    public static final int WIDTH = 30;
    public static final int HEIGHT = 30;
    public int x = 0;
    public int y = 0;
    public int direction = 1; // 1: right or -1: left

    public Item(int x, int y, int direction)
    {
        this.x = x;
        this.y = y;
        this.direction = direction;

    }
}