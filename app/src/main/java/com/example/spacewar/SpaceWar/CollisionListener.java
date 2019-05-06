package com.example.spacewar.SpaceWar;

public interface CollisionListener
{
    public void generateBullet();
    public void collideBulletEnemy();
    public void collisionRoadSide();
    public void collisionMonster();
    public void gameOver();
}
