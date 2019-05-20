package com.example.spacewar.SpaceWar;

import android.util.Log;

import com.example.spacewar.GameEngine;

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
    List<Item> itemList = new ArrayList<>();
    public int maxMonsters = 5;
    public int maxBullets = 10;
    public int maxItems = 1;

    GameEngine gameEngine;
    CollisionListener listener;
//    private final CollisionListener collisionListener;

    boolean gameOver = false;
    int points = 0;
    int backgroundSpeed = 0;
    int backgroundSpeed2 = 0;
    int backgroundSpeed3 = 0;
    double ShootingTimer = 0.5;
    float passedTime = 0;
    long startTime;
    long updateCounter = 0;
    int movementAngle = 1;
    int bulletSet = 5;
    int bulletsOnCounter = 0;
    int bulletsOffCounter = 0;
    boolean bulletsOn = true;
    int scorePoints = 0;
//    bulletSound3 = gameEngine.loadSound("spacewar/music/laser1.mp3");

    public World(GameEngine gameEngine, CollisionListener listener, int backgroundSpeed, int backgroundSpeed2, int backgroundSpeed3)
    {
        this.backgroundSpeed = backgroundSpeed;
        this.backgroundSpeed2 = backgroundSpeed2;
        this.backgroundSpeed3 = backgroundSpeed3;
        this.gameEngine = gameEngine;
        this.listener = listener;
//        initializeBullets();
        initializeMonsters();
        initializeItems();


    }

    public void update(float deltaTime, float accelX, float accelY)
    {

//        vehicle.x = (int)(vehicle.x - accelX * 50 * deltaTime); // accelerometer control
//        vehicle.y = (int)(vehicle.y - accelY * 40 * deltaTime); // accelerometer control


        updateCounter++;


        if (updateCounter % 10 ==0)
        {
            scorePoints++; // add 1 point for every
        }

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

        /*if(vehicle.y < 480/2)
        {
            vehicle.y = 480 / 2 - Vehicle.HEIGHT / 2;
        }
        else
        {
            vehicle.y = gameEngine.getTouchY(0) - Vehicle.HEIGHT/2;
        }
        if(vehicle.y > 480 - Vehicle.HEIGHT) vehicle.y = 480 - Vehicle.HEIGHT - 10;*/


        // check left screen boundary
        if (vehicle.x < MIN_X) vehicle.x = (int) MIN_X + 1;
        // check right screen boundary
        if (vehicle.x + vehicle.WIDTH > MAX_X) vehicle.x = (int)(MAX_X - vehicle.WIDTH - 1);

        Monster monster = null;
        for (int i = 0; i < monsterList.size(); i++)
        {
            monster = monsterList.get(i);
            // make monster move
            monster.y = (int)(monster.y + backgroundSpeed * deltaTime);
            Random random2 = new Random();
            int randDirectionChangeRate = 100 + random2.nextInt(500); // between 100 and 600
            if (updateCounter % randDirectionChangeRate ==0)
            {
                monster.direction = -monster.direction; // individual enemy direction
            }
            if (monster.x < 0 || monster.x > 320-monster.WIDTH)
            {
                monster.direction = -monster.direction; // individual enemy direction
                monster.x = (int)(monster.x + backgroundSpeed * deltaTime * monster.direction);
            }
            monster.x = (int)(monster.x + backgroundSpeed * deltaTime * monster.direction);
            if (monster.y > 480 - Monster.HEIGHT) // if monster disappears off screen
            {
                Random random = new Random();
                monster.hp = 3;
                int randX = random.nextInt(320-30); // between 0 and 50
                int randY = random.nextInt(20);
                monster.x = (randX);
                monster.y = ((-450 + randY) + i*100);
                Log.d("World", "Just recycled a monster.");
            }
        }

        Item item = null;
        for (int i = 0; i < maxItems; i++)
        {
            item = itemList.get(i);
            // make item move
            item.y = (int)(item.y + backgroundSpeed * deltaTime);
            Random random3 = new Random();
            int randDirectionChangeRate = 100 + random3.nextInt(500); // between 100 and 600
            if (updateCounter % randDirectionChangeRate ==0)
            {
                item.direction = -item.direction; // individual item direction
            }
            if (item.x < 0 || item.x > 320-item.WIDTH)
            {
                item.direction = -item.direction; // individual item direction
                item.x = (int)(item.x + backgroundSpeed * deltaTime * item.direction);
            }
            item.x = (int)(item.x + backgroundSpeed * deltaTime * item.direction);
            if (item.y > 480 - Item.HEIGHT) // if item disappears off screen
            {
                Random random4 = new Random();
                int randX = random4.nextInt(320-30); // between 0 and 50
                int randY = random4.nextInt(20);
                item.x = (randX);
                item.y = ((-450 + randY) + i*100);
                Log.d("World", "Just recycled a monster.");
            }
        }

        Bullet bullet;
        if (updateCounter % 10 ==0)
        {
            checkBulletShooting();
            if (bulletList.size() < 300 && bulletsOn)
            {
                bullet = new Bullet(vehicle.x+vehicle.WIDTH/2-2, vehicle.y-15); // middle vehicle coordonates
                bulletList.add(bullet);

                /*bullet = new Bullet(vehicle.x+vehicle.WIDTH/2-2-20-20, vehicle.y+8); // middle vehicle coordonates
                bulletList.add(bullet);
                bullet = new Bullet(vehicle.x+vehicle.WIDTH/2-2-20, vehicle.y); // middle vehicle coordonates
                bulletList.add(bullet);
                bullet = new Bullet(vehicle.x+vehicle.WIDTH/2-2, vehicle.y-8); // middle vehicle coordonates
                bulletList.add(bullet);*/
                /*bullet = new Bullet(vehicle.x+vehicle.WIDTH/2-2+20, vehicle.y); // middle vehicle coordonates
                bulletList.add(bullet);
                bullet = new Bullet(vehicle.x+vehicle.WIDTH/2-2+20+20, vehicle.y+8); // middle vehicle coordonates
                bulletList.add(bullet);*/
//                maxBullets ++;
                bulletsOnCounter ++;
                listener.generateBullet();
            }

        }

//        Bullet bullet = null;

        for (int i = 0; i < bulletList.size(); i++)
        {
            bullet = bulletList.get(i);
            // move bullet
            bullet.y = (int)(bullet.y - backgroundSpeed * deltaTime);
//                recycling a bullet
//                if (bullet.y < 0 - Bullet.HEIGHT-300)// - 30 for testing purposes
//                {
//                    bullet.x = vehicle.x+vehicle.WIDTH/2;
//                    bullet.y = vehicle.y;
//                    Log.d("World", "Just recycled a bullet.");
//                }
        }

        // check if the car collides with a monster
        collideShipEnemy();
        collideShipItem();
        collideBulletEnemy();

    }

    private void collideShipEnemy()
    {
        Monster monster = null;
        for (int i=0; i < maxMonsters; i++)
        {
            monster = monsterList.get(i);
            if (collideRects(vehicle.x, vehicle.y, Vehicle.WIDTH, Vehicle.HEIGHT,
                    monster.x, monster.y, Monster.WIDTH, Monster.HEIGHT))
            {
                monster.y = 500; // move monster off screen for recycling
                vehicle.lives = vehicle.lives - 1;
                listener.collideShipEnemy();
                Log.d("World", "The ship just hit an enemy");
            }
            if(vehicle.lives==0) gameOver = true;
        }
    }

    private void collideShipItem()
    {
        Item item = null;
        for (int i=0; i < maxItems; i++)
        {
            item = itemList.get(i);
            if (collideRects(vehicle.x, vehicle.y, Vehicle.WIDTH, Vehicle.HEIGHT,
                    item.x, item.y, Item.WIDTH, Item.HEIGHT))
            {
                if(vehicle.lives < 3)
                {
                    vehicle.lives = vehicle.lives + 1;
                }

                item.y = 500; // move item off screen for recycling
                listener.collideShipItem();
                Log.d("World", "The ship just collected an item.");
            }
        }
    }

    public void collideBulletEnemy()
    {
        Bullet bullet = null;
        for (int i=0; i < bulletList.size(); i++)
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

                    monster.hp -=1;
                    Log.d("World", "The enemy was hit: Enemy HP -1" + monster.hp);
                    // bullet dissapears
                    bullet.y = -500; // move bullet off screen for recycling
                    Log.d("World", "The bullet just hit an enemy");

                    if (monster.hp <= 0)
                    {
                        // enemy dissapears
                        monster.y = 500; // move monster off screen for recycling
                    }
                    // add points
                    scorePoints+=10;
                    listener.collideBulletEnemy();
//                    collideBulletEnemySound();
//                    bulletSound3.play(1);
                }
            }

        }
    }
//    public void collideBulletEnemySound()
//    {
//
//    }

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
//            Monster monster = new Monster(randX, ((-450 + randY) + i*100));
            Monster monster = new Monster(randX, ((-450 + randY) + i*100), 1, 3);
            monsterList.add(monster);
        }
    }

    private void initializeItems()
    {
        Random random = new Random();
        for(int i=0; i< maxItems; i++)
        {
            int randX = random.nextInt(320-30); // between 0 and 50
            int randY = random.nextInt(20);
            Item item = new Item(randX, ((-450 + randY) + i*100), 1);
            itemList.add(item);
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

    private void checkBulletShooting()
    {
        if (bulletsOnCounter >= 1)
        {
            bulletsOn = false;      // pause the bullet generation
        }
        if (!bulletsOn)
        {
            bulletsOffCounter++;   // count when the bullet is not generated
        }
        if (bulletsOffCounter > 2)
        {
            bulletsOn = true;       // start the bullet generation
            bulletsOnCounter = 0;   // reset counter
            bulletsOffCounter = 0;  // reset counter
        }
    }

}
