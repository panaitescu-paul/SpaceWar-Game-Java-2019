package com.example.spacewar.SpaceWar;

public interface CollisionListener
{
//    interface for sound effects
    public void generateBullet();
    public void collideBulletEnemy();
    public void collideShipItem();
    public void collideShipEnemy();
    public void gameOver();
}
