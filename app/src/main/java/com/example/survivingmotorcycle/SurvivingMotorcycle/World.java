package com.example.survivingmotorcycle.SurvivingMotorcycle;

import android.util.Log;

import com.example.survivingmotorcycle.GameEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class World
{

    public static final float MIN_X = -30;
    public static final float MAX_X = 320 + 30; // logical screen + vehicle size
//    public static final float MAX_X = 1000;
    public static final float MIN_Y = 37;
    public static final float MAX_Y = 285;


    Vehicle vehicle = new Vehicle();
    List<Monster> monsterList = new ArrayList<>();
    List<Bullet> bulletList = new ArrayList<>();
    public int maxMonsters = 5;
    public int maxBullets = 10;

    GameEngine gameEngine;
    CollisionListener listener;

    boolean gameOver = false;
    int points = 0;
    int lives = 3;
    int backgroundSpeed = 0;
    int backgroundSpeed2 = 0;
    int backgroundSpeed3 = 0;
    double ShootingTimer = 0.5;
    float passedTime = 0;
    long startTime;

    public World(GameEngine gameEngine, CollisionListener listener, int backgroundSpeed, int backgroundSpeed2, int backgroundSpeed3)
    {
        this.backgroundSpeed = backgroundSpeed;
        this.backgroundSpeed2 = backgroundSpeed2;
        this.backgroundSpeed3 = backgroundSpeed3;
        this.gameEngine = gameEngine;
        this.listener = listener;
        initializeBullets();
        initializeMonsters();


    }

    public void update(float deltaTime, float accelY)
    {


        // move the Vehicle based on the phone accelerometer. For finished game.
//        vehicle.y = (int)(vehicle.y + accelY * deltaTime * 40);
//        vehicle.y = (int)(vehicle.y - accelY * deltaTime * 40);
        // move the vehicle based on user touch. Only for testing. Remove before publishing.
        if (gameEngine.isTouchDown(0));
        {
            vehicle.x = gameEngine.getTouchX(0) - Vehicle.WIDTH/2; // position vehicle after touch x
            // check for vehicle to be in the lower part of the screen
            if (gameEngine.getTouchY(0) < 480/2) // check if touch is on the upper part
            {
                vehicle.y = 480/2 - Vehicle.HEIGHT/2; // reposition vehicle on the lower part of the screen
            }else
            {
                vehicle.y = gameEngine.getTouchY(0) - Vehicle.HEIGHT/2; // position vehicle after touch y
            }

            if (gameEngine.getTouchY(0) > 480 - Vehicle.HEIGHT)  // check if vehicle is out of screen (lower)
            {
                vehicle.y = 480 - Vehicle.HEIGHT - 10; // reposition vehicle on the lower part of the screen
            }
        }

        // check left screen boundary
        if (vehicle.x < MIN_X) vehicle.x = (int) MIN_X + 1;
        // check right screen boundary
        if (vehicle.x + vehicle.WIDTH > MAX_X) vehicle.x = (int)(MAX_X - vehicle.WIDTH - 1);

        Monster monster = null;
        for (int i = 0; i < maxMonsters; i++)
        {
            monster = monsterList.get(i);
            // make monster move
//            monster.x = (int)(monster.x - backgroundSpeed * deltaTime);
            monster.y = (int)(monster.y + backgroundSpeed * deltaTime);
            if (monster.y > 480 - Monster.HEIGHT) // if monster disappears off screen
            {
                Random random = new Random();
                int randX = random.nextInt(320-30); // between 0 and 50
                int randY = random.nextInt(20);
                monster.x = (randX);
                monster.y = ((-450 + randY) + i*100);
                Log.d("World", "Just recycled a monster.");
            }
        }
//        Bullet bullet = null;
        Bullet bullet;
        for (int i = 0; i < maxBullets; i++)
        {
            bullet = bulletList.get(i);
            // move bullet
            bullet.y = (int)(bullet.y - backgroundSpeed * deltaTime);
//                 recycling a bullet
                if (bullet.y < 0 - Bullet.HEIGHT-300)// - 30 for testing purposes
                {
                    bullet.x = vehicle.x+vehicle.WIDTH/2;
                    bullet.y = vehicle.y;
                    Log.d("World", "Just recycled a bullet.");
                }
        }




        // check if the car collides with a monster
        collideCarMonster();
        collideBulletEnemy();

    }

    private void collideCarMonster()
    {
        Monster monster = null;
        for (int i=0; i < maxMonsters; i++)
        {
            monster = monsterList.get(i);
            if (collideRects(vehicle.x, vehicle.y, Vehicle.WIDTH, Vehicle.HEIGHT,
                    monster.x, monster.y, Monster.WIDTH, Monster.HEIGHT))
            {
                gameOver = true;
                Log.d("World", "The car just hit a monster");
            }
        }
    }

    private void collideBulletEnemy()
    {
        Bullet bullet = null;
        for (int i=0; i < maxBullets; i++)
        {
            bullet = bulletList.get(i);

            Monster monster = null;
            for (int j=0; j < maxMonsters; j++)
            {
                monster = monsterList.get(j);
                // check collision of a bullet with a enemy
                if (collideRects(bullet.x, bullet.y, Bullet.WIDTH, Bullet.HEIGHT,
                        monster.x, monster.y, Monster.WIDTH, Monster.HEIGHT))
                {
                    // bullet dissapears
                    // enemy dissapears
                    bullet.y = -500; // move bullet off screen for recycling
                    monster.y = 500; // move monster off screen for recycling
                    Log.d("World", "The bullet just hit an enemy");
                }
            }

        }
    }

    private boolean collideRects(float x, float y, float width, float height,
                                 float x2, float y2, float width2, float height2)
    {
        if(x < x2+width2 && x+width > x2 && y < y2+height2 && y+height > y2)
        {
            return true;
        }
        return false;
    }

    private void initializeMonsters()
    {
        Random random = new Random();
        for(int i=0; i< maxMonsters; i++)
        {
            int randX = random.nextInt(320-30); // between 0 and 50
            int randY = random.nextInt(20);
//            Monster monster = new Monster(((500 + randX) + i*50), 30 + randY);
            Monster monster = new Monster(randX, ((-450 + randY) + i*100));
            monsterList.add(monster);
        }
    }

    private void initializeBullets()
    {
        for(int i=0; i< maxBullets; i++)
        {
//            Bullet bullet = new Bullet(vehicle.x+vehicle.WIDTH/2, 480-vehicle.HEIGHT-10+(i*20)); // middle vehicle coordonates
            Bullet bullet = new Bullet(100, -200+(i*20)); // middle vehicle coordonates
            bulletList.add(bullet);
        }
    }

}
