package com.example.spacewar.SpaceWar;

public class Enemy
{
    public static final int WIDTH = 30;
    public static final int HEIGHT = 30;
    public int x = 0;
    public int y = 0;
    public int hp = 0;
    public int direction = 1; // 1: right or -1: left
//    public enum Type {
//        BASIC,
//        SHIELDED,
//        SHOOTING
//    }

    public Enemy(int x, int y, int direction, int hp)
    {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.hp = hp;

    }
}
