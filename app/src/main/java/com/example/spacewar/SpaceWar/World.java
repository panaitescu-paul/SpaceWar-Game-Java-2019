package com.example.spacewar.SpaceWar;

import android.util.Log;

import com.example.spacewar.GameEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class World
{

    public static final float MIN_X = -30;
    public static final float MAX_X = 320 + 30; // logical screen + ship size

    // making a new instance of the Ship object
    Ship ship = new Ship();
    // array lists of enemies, power ups, ship and enemy bullets
    List<Enemy> enemyList = new ArrayList<>();
    List<Bullet> bulletList = new ArrayList<>();
    List<Item> itemHealthList = new ArrayList<>();
    List<Item> itemBulletList = new ArrayList<>();
    List<Item> itemShieldList = new ArrayList<>();
    List<Bullet> enemyBulletList = new ArrayList<>();
    GameEngine gameEngine;
    CollisionListener listener;

    boolean gameOver = false;
    int backgroundSpeed = 0;
    int backgroundSpeed2 = 0;
    int backgroundSpeed3 = 0;
    long updateCounter = 0;
    int bulletsOnCounter = 0;
    int bulletsOffCounter = 0;
    boolean bulletsOn = true;
    int bulletsOnCounter2 = 0;
    int bulletsOffCounter2 = 0;
    boolean bulletsOn2 = true;
    int scorePoints = 0;

    public World(GameEngine gameEngine, CollisionListener listener, int backgroundSpeed, int backgroundSpeed2, int backgroundSpeed3)
    {
        this.backgroundSpeed = backgroundSpeed;
        this.backgroundSpeed2 = backgroundSpeed2;
        this.backgroundSpeed3 = backgroundSpeed3;
        this.gameEngine = gameEngine;
        this.listener = listener;
    }

    public void update(float deltaTime)
    {
        Log.d("World", "deltaTime" + deltaTime);
        updateCounter++;
        configureEnemyWaves(updateCounter);
        configureItemWaves(updateCounter);
        scoreCounter();
        moveShip();
        moveEnemies(deltaTime);
        moveItems(deltaTime);
        spawnShipBullets();
        spawnEnemyBullets();
        moveShipBullets(deltaTime);
        moveEnemyBullets(deltaTime);

        // check for collisions
        collideShipEnemy();
        collideShipHealthItem();
        collideShipBulletsItem();
        collideShipShieldItem();
        collideShipBulletWithEnemy();
        collideEnemyBulletWithShip();
    }

    private void moveEnemyBullets(float deltaTime)
    {
        Bullet enemyBullet2;
        for (int i = 0; i < enemyBulletList.size(); i++)
        {
            enemyBullet2 = enemyBulletList.get(i);
            // move enemy bullet
            enemyBullet2.y = (int) (enemyBullet2.y + 2 + backgroundSpeed * deltaTime);
            if (enemyBullet2.y > 480 - Bullet.HEIGHT) // if enemy bullet disappears off screen
            {
                enemyBulletList.remove(i);
            }
        }
    }

    private void moveShipBullets(float deltaTime)
    {
        Bullet bullet;
        for (int i = 0; i < bulletList.size(); i++)
        {
            bullet = bulletList.get(i);
            // move bullet
            bullet.y = (int)(bullet.y - backgroundSpeed * deltaTime);
            if (bullet.y < 0 - Bullet.HEIGHT)
            {
                bulletList.remove(i);
            }
        }
    }

    private void spawnEnemyBullets()
    {
        Bullet enemyBullet;
        if (updateCounter % 10 == 0)
        {
            checkEnemyBulletShooting(1, 12);
            if (enemyBulletList.size() < 500 && bulletsOn2)
            {
                Enemy enemy2 = null;
                for (int i = 0; i < enemyList.size(); i++)
                {
                    enemy2 = enemyList.get(i);
                    if (enemy2.shooting && enemy2.y > 0) // shoot only if it is a shooting enemy type
                    {
                        enemyBullet = new Bullet(enemy2.x + enemy2.WIDTH / 2 - 2, enemy2.y + 30); // middle enemy coordonates
                        enemyBulletList.add(enemyBullet);
                        bulletsOnCounter2++;
                        listener.generateBullet();
                    }
                }
            }
        }
    }

    private void spawnShipBullets()
    {
        Bullet bullet;
        if (updateCounter % 10 ==0)
        {
            // when the ship shoots 1 bulelt at a time, it will
            if(ship.multipleBullets == 1)
            {
                checkBulletShooting(1, 1);
            }
            else if (ship.multipleBullets == 2)
            {
                checkBulletShooting(1, 2);
            }
            else if (ship.multipleBullets == 3)
            {
                checkBulletShooting(1, 3);
            }
            else if (ship.multipleBullets == 4)
            {
                checkBulletShooting(2, 4);
            }
            else if (ship.multipleBullets == 5)
            {
                checkBulletShooting(2, 5);
            }
            if (bulletsOn)
            {
                if(ship.multipleBullets == 1)
                {
                    bullet = new Bullet(ship.x+ship.WIDTH/2-2, ship.y-15); // middle ship coordonates
                    bulletList.add(bullet);
                }
                else if(ship.multipleBullets == 2)
                {
                    bullet = new Bullet(ship.x+ship.WIDTH/2-2-20, ship.y);
                    bulletList.add(bullet);
                    bullet = new Bullet(ship.x+ship.WIDTH/2-2+20, ship.y);
                    bulletList.add(bullet);
                }
                else if(ship.multipleBullets == 3)
                {
                    bullet = new Bullet(ship.x+ship.WIDTH/2-2-20, ship.y);
                    bulletList.add(bullet);
                    bullet = new Bullet(ship.x+ship.WIDTH/2-2, ship.y-8); // middle ship coordonates
                    bulletList.add(bullet);
                    bullet = new Bullet(ship.x+ship.WIDTH/2-2+20, ship.y);
                    bulletList.add(bullet);
                }
                else if(ship.multipleBullets == 4)
                {
                    bullet = new Bullet(ship.x+ship.WIDTH/2-2-20-20, ship.y+8);
                    bulletList.add(bullet);
                    bullet = new Bullet(ship.x+ship.WIDTH/2-2-20, ship.y);
                    bulletList.add(bullet);
                    bullet = new Bullet(ship.x+ship.WIDTH/2-2+20, ship.y);
                    bulletList.add(bullet);
                    bullet = new Bullet(ship.x+ship.WIDTH/2-2+20+20, ship.y+8);
                    bulletList.add(bullet);
                }
                else if(ship.multipleBullets == 5)
                {
                    bullet = new Bullet(ship.x+ship.WIDTH/2-2-20-20, ship.y+8);
                    bulletList.add(bullet);
                    bullet = new Bullet(ship.x+ship.WIDTH/2-2-20, ship.y);
                    bulletList.add(bullet);
                    bullet = new Bullet(ship.x+ship.WIDTH/2-2, ship.y-8); // middle vehicle coordonates
                    bulletList.add(bullet);
                    bullet = new Bullet(ship.x+ship.WIDTH/2-2+20, ship.y);
                    bulletList.add(bullet);
                    bullet = new Bullet(ship.x+ship.WIDTH/2-2+20+20, ship.y+8);
                    bulletList.add(bullet);
                }
                bulletsOnCounter ++;
                listener.generateBullet();
            }
        }
    }

    // making the health item power up
    private void moveItems(float deltaTime)
    {
        Item healthItem = null;
        for (int i = 0; i < itemHealthList.size(); i++)
        {
            healthItem = itemHealthList.get(i);
            // make shieldItem move
            healthItem.y = (int)(healthItem.y + backgroundSpeed * deltaTime);
            Random random3 = new Random();
            int randDirectionChangeRate = 100 + random3.nextInt(500); // between 100 and 600
            if (updateCounter % randDirectionChangeRate ==0)
            {
                healthItem.direction = -healthItem.direction; // individual shieldItem direction
            }
            if (healthItem.x < 0 || healthItem.x > 320-healthItem.WIDTH)
            {
                healthItem.direction = -healthItem.direction; // individual shieldItem direction
                healthItem.x = (int)(healthItem.x + backgroundSpeed * deltaTime * healthItem.direction);
            }
            healthItem.x = (int)(healthItem.x + backgroundSpeed * deltaTime * healthItem.direction);
            if (healthItem.y > 480 - Item.HEIGHT) // if shieldItem disappears off screen
            {
                itemHealthList.remove(i);
            }
        }

        // making the bullets item power up
        Item bulletsItem = null;
        for (int i = 0; i < itemBulletList.size(); i++)
        {
            bulletsItem = itemBulletList.get(i);
            // make shieldItem move
            bulletsItem.y = (int)(bulletsItem.y + backgroundSpeed * deltaTime);
            Random random3 = new Random();
            int randDirectionChangeRate = 100 + random3.nextInt(500); // between 100 and 600
            if (updateCounter % randDirectionChangeRate ==0)
            {
                bulletsItem.direction = -bulletsItem.direction; // individual bulletsItem direction
            }
            if (bulletsItem.x < 0 || bulletsItem.x > 320-bulletsItem.WIDTH)
            {
                bulletsItem.direction = -bulletsItem.direction; // individual bulletsItem direction
                bulletsItem.x = (int)(bulletsItem.x + backgroundSpeed * deltaTime * bulletsItem.direction);
            }
            bulletsItem.x = (int)(bulletsItem.x + backgroundSpeed * deltaTime * bulletsItem.direction);
            if (bulletsItem.y > 480 - Item.HEIGHT) // if bulletsItem disappears off screen
            {
                itemBulletList.remove(i);
            }
        }

        // making the shield item power up
        Item shieldItem = null;
        for (int i = 0; i < itemShieldList.size(); i++)
        {
            shieldItem = itemShieldList.get(i);
            // make shieldItem move
            shieldItem.y = (int)(shieldItem.y + backgroundSpeed * deltaTime);
            Random random3 = new Random();
            int randDirectionChangeRate = 100 + random3.nextInt(500); // between 100 and 600
            if (updateCounter % randDirectionChangeRate ==0)
            {
                shieldItem.direction = -shieldItem.direction; // individual shieldItem direction
            }
            if (shieldItem.x < 0 || shieldItem.x > 320- shieldItem.WIDTH)
            {
                shieldItem.direction = -shieldItem.direction; // individual shieldItem direction
                shieldItem.x = (int)(shieldItem.x + backgroundSpeed * deltaTime * shieldItem.direction);
            }
            shieldItem.x = (int)(shieldItem.x + backgroundSpeed * deltaTime * shieldItem.direction);
            if (shieldItem.y > 480 - Item.HEIGHT) // if shieldItem disappears off screen
            {
                itemShieldList.remove(i);
            }
        }
    }

    private void moveEnemies(float deltaTime)
    {
        Enemy enemy = null;
        for (int i = 0; i < enemyList.size(); i++)
        {
            enemy = enemyList.get(i);
            // make enemy move
            enemy.y = (int)(enemy.y + backgroundSpeed * deltaTime);
            Random random2 = new Random();
            int randDirectionChangeRate = 100 + random2.nextInt(500); // between 100 and 600
            if (updateCounter % randDirectionChangeRate ==0)
            {
                enemy.direction = -enemy.direction; // individual enemy direction
            }
            if (enemy.x < 0 || enemy.x > 320- enemy.WIDTH)
            {
                enemy.direction = -enemy.direction; // individual enemy direction
                enemy.x = (int)(enemy.x + backgroundSpeed * deltaTime * enemy.direction);
            }
            enemy.x = (int)(enemy.x + backgroundSpeed * deltaTime * enemy.direction);
            if (enemy.y > 480 - Enemy.HEIGHT) // if enemy disappears off screen
            {
                enemyList.remove(i);
            }
        }
    }

    private void scoreCounter()
    {
        if (updateCounter % 10 ==0)
        {
            scorePoints++; // add 1 point for every 1/6 seconds
        }
    }

    private void moveShip()
    {
        // move the ship based on user touch.
        if (gameEngine.isTouchDown(0));
        {
            ship.x = gameEngine.getTouchX(0) - Ship.WIDTH/2; // position ship after touch x
            // check for ship to be in the lower part of the screen
            if (gameEngine.getTouchY(0) < 480/2) // check if touch is on the upper part
            {
                ship.y = 480/2 - Ship.HEIGHT/2; // reposition ship on the lower part of the screen
            }else
            {
                ship.y = gameEngine.getTouchY(0) - Ship.HEIGHT/2; // position ship after touch y
            }
            if (gameEngine.getTouchY(0) > 480 - Ship.HEIGHT)  // check if ship is out of screen (lower)
            {
                ship.y = 480 - Ship.HEIGHT - 10; // reposition ship on the lower part of the screen
            }
        }
        // check left screen boundary
        if (ship.x < MIN_X) ship.x = (int) MIN_X + 1;
        // check right screen boundary
        if (ship.x + ship.WIDTH > MAX_X) ship.x = (int)(MAX_X - Ship.WIDTH - 1);
    }

    private void collideShipEnemy()
    {
        Enemy enemy = null;
        for (int i=0; i < enemyList.size(); i++)
        {
            enemy = enemyList.get(i);
            // if shield is off then check collide rectangles of the ship with enemy
            if (!ship.shield && collideRects(ship.x, ship.y, Ship.WIDTH, Ship.HEIGHT,
                    enemy.x, enemy.y, Enemy.WIDTH, Enemy.HEIGHT))
            {
                ship.lives = ship.lives - 1; // lives decrease when collide with enemy
//                if (!enemy.shield) // if enemy has shield, then it will stay alive, even after ship collision
//                {
                    //delete enemy from list
                    enemyList.remove(i);
//                }
                if(ship.multipleBullets > 1)
                {
                    ship.multipleBullets = ship.multipleBullets - 1; // multipleBullets decrease when collide with enemy
                }
                listener.collideShipEnemy();
                Log.d("World", "The ship just hit an enemy");
            }
            // if shield is on then check collide rectangles of the shield on the ship with enemy
            else if(ship.shield && collideRects(ship.x, ship.y, Ship.WIDTH, Ship.HEIGHT,
                    enemy.x, enemy.y, Enemy.WIDTH, Enemy.HEIGHT))
            {
                //delete enemy from list
                enemyList.remove(i);
                ship.shield = false; // shield goes off after colliding an enemy
            }
            // if there are no lives left then it's game over
            if(ship.lives==0)
            {
                gameOver = true;
                listener.gameOver();
            }
        }
    }

    private void collideShipHealthItem()
    {
        Item healthItem = null;
        for (int i = 0; i < itemHealthList.size(); i++)
        {
            healthItem = itemHealthList.get(i);
            if (collideRects(ship.x, ship.y, Ship.WIDTH, Ship.HEIGHT,
                    healthItem.x, healthItem.y, Item.WIDTH, Item.HEIGHT))
            {
                // if there are less then 3 lives (which is the maximum)
                if(ship.lives < 3)
                {
                    ship.lives = ship.lives + 1; // increase the lives
                }
                itemHealthList.remove(i);
                listener.collideShipItem();
                Log.d("World", "The ship just collected an healthItem.");
                // add points
                scorePoints+=10;
            }
        }
    }

    private void collideShipBulletsItem()
    {
        Item bulletsItem = null;
        for (int i = 0; i < itemBulletList.size(); i++)
        {
            bulletsItem = itemBulletList.get(i);
            if (collideRects(ship.x, ship.y, Ship.WIDTH, Ship.HEIGHT,
                    bulletsItem.x, bulletsItem.y, Item.WIDTH, Item.HEIGHT))
            {
                // if there are less then 5 multiple bullest (which is the maximum)
                if(ship.multipleBullets < 5)
                {
                    ship.multipleBullets = ship.multipleBullets + 1; // increase the multiple bullets
                }
                itemBulletList.remove(i);
                listener.collideShipItem();
                Log.d("World", "The ship just collected an bulletsItem.");
                // add points
                scorePoints+=10;
            }
        }
    }

    private void collideShipShieldItem()
    {
        Item shieldItem = null;
        for (int i = 0; i < itemShieldList.size(); i++)
        {
            shieldItem = itemShieldList.get(i);
            if (collideRects(ship.x, ship.y, Ship.WIDTH, Ship.HEIGHT,
                    shieldItem.x, shieldItem.y, Item.WIDTH, Item.HEIGHT))
            {
                // put the shield on
                ship.shield = true; // shield is on
                itemShieldList.remove(i);
                listener.collideShipItem();
                Log.d("World", "The ship just collected an shieldItem.");
                // add points
                scorePoints+=10;
            }
        }
    }

    public void collideShipBulletWithEnemy()
    {
        Bullet bullet = null;
        for (int i=0; i < bulletList.size(); i++)
        {
            bullet = bulletList.get(i);
            Enemy enemy = null;
            for (int j=0; j < enemyList.size(); j++)
            {
                enemy = enemyList.get(j);
                // check collision of a bullet with a enemy
                if (collideRects(bullet.x, bullet.y, Bullet.WIDTH, Bullet.HEIGHT,
                        enemy.x, enemy.y, Enemy.WIDTH, Enemy.HEIGHT) &&
                        bullet.y > 0)
                {
                    enemy.health -=1;
                    Log.d("World", "The enemy was hit: Enemy HP -1" + enemy.health);
                    // bullet dissapears
                    bulletList.remove(i);
                    Log.d("World", "The bullet just hit an enemy");

                    if (enemy.health <= 0 && !enemy.shield) // shielded enemy is immune
                    {
                        //delete enemy from list
                        enemyList.remove(j);
                    }
                    // add points
                    if (!enemy.shooting && !enemy.shield) // basic enemy
                    {
                        scorePoints+=10;
                    }
                    else if (enemy.shooting) // shooting enemy
                    {
                        scorePoints+=25;
                    }
                    else if (enemy.shield) // shield enemy
                    {
                        scorePoints+=1;
                    }
                    listener.collideBulletEnemy();
//                    collideBulletEnemySound();
                }
            }

        }
    }

    public void collideEnemyBulletWithShip()
    {
        Bullet enemyBullet = null;
        for (int i=0; i < enemyBulletList.size(); i++)
        {
            enemyBullet = enemyBulletList.get(i);
            // check collision of a bullet with a enemy
            // if shield is off then check collide rectangles of the ship with bullet of enemy
            if (!ship.shield && collideRects(enemyBullet.x, enemyBullet.y, Bullet.WIDTH, Bullet.HEIGHT,
                    ship.x, ship.y, Ship.WIDTH, Ship.HEIGHT))
            {
                enemyBulletList.remove(i);
                ship.lives = ship.lives - 1; // lives decrease when collide with enemy
                // if there at at least 2 multiple bullets
                if(ship.multipleBullets > 1)
                {
                    ship.multipleBullets = ship.multipleBullets - 1; // multipleBullets decrease when collide with enemy bullet
                }
                Log.d("World", "The bullet just hit an enemy");
                listener.collideBulletEnemy();
//                    collideBulletEnemySound();
            }
            // if shield is on then check collide rectangles of the shield on the ship with bullet of enemy
            else if(ship.shield && collideRects(enemyBullet.x, enemyBullet.y, Bullet.WIDTH, Bullet.HEIGHT,
                    ship.x, ship.y, Ship.WIDTH, Ship.HEIGHT))
            {
                enemyBulletList.remove(i);
                ship.shield = false; // shield goes off after colliding an enemy
            }
            // if there are no lives left then it's game over
            if(ship.lives==0)
            {
                gameOver = true;
                listener.gameOver();
            }
        }
    }

    // check the collide rectangles
    private boolean collideRects(float x, float y, float width, float height,
                                 float x2, float y2, float width2, float height2)
    {
        if(x < x2+width2 && x+width > x2 && y < y2+height2 && y+height > y2)
        {
            return true;
        }
        return false;
    }

    private void checkBulletShooting(int onUnits, int offUnits)
    {
        if (bulletsOnCounter >= onUnits)
        {
            bulletsOn = false;      // pause the bullet generation
        }
        if (!bulletsOn)
        {
            bulletsOffCounter++;   // count when the bullet is not generated
        }
        if (bulletsOffCounter > offUnits)
        {
            bulletsOn = true;       // start the bullet generation
            bulletsOnCounter = 0;   // reset counter
            bulletsOffCounter = 0;  // reset counter
        }
    }

    private void checkEnemyBulletShooting(int onUnits, int offUnits)
    {
        if (bulletsOnCounter2 >= onUnits)
        {
            bulletsOn2 = false;      // pause the bullet generation
        }
        if (!bulletsOn2)
        {
            bulletsOffCounter2++;   // count when the bullet is not generated
        }
        if (bulletsOffCounter2 > offUnits)
        {
            bulletsOn2 = true;       // start the bullet generation
            bulletsOnCounter2 = 0;   // reset counter
            bulletsOffCounter2 = 0;  // reset counter
        }
    }
    private void configureEnemyWaves(long updateCounter)
    {
        if (updateCounter == 50)
        {
            spawnEnemyWave(5, 0, 0);
        }
        if (updateCounter == 700)
        {
            spawnEnemyWave(0, 5, 0);
        }
        if (updateCounter == 1400)
        {
            spawnEnemyWave(0, 0, 4);
        }
        if (updateCounter >= 2400 && updateCounter % 750 ==0)
        {
            spawnEnemyWave(5, 3, 2);
        }
    }
    private void spawnEnemyWave(int enemyBasic, int enemyShooting, int enemyShielded)
    {
        Random random = new Random();
        for (int i=0; i<enemyBasic; i++)
        {
            int randX = random.nextInt(320-30); // between 0 and 50
            int randY = random.nextInt(20);
//            Enemy enemy = new Enemy(randX, ((-450 + randY) + i*100), 1, 3, false, false);
            Enemy enemy = new Enemy(randX, ((-450 + randY) + i*150)-200, 1, 3, false, false);
            enemyList.add(enemy);
        }
        for (int i=0; i<enemyShooting; i++)
        {
            int randX = random.nextInt(320-30); // between 0 and 50
            int randY = random.nextInt(20);
            Enemy enemy2 = new Enemy(randX, ((-450 + randY) + i*150)-200, 1, 3, true, false);
            enemyList.add(enemy2);
        }
        for (int i=0; i<enemyShielded; i++)
        {
            int randX = random.nextInt(320-30); // between 0 and 50
            int randY = random.nextInt(20);
            Enemy enemy3 = new Enemy(randX, ((-450 + randY) + i*150)-200, 1, 3, false, true);
            enemyList.add(enemy3);
        }
    }

    //     Items
    private void configureItemWaves(long updateCounter)
    {
        if (updateCounter == 400)
        {
            spawnItemWave(1, 1, 1);
        }
        if (updateCounter == 1100)
        {
            spawnItemWave(1, 1, 1);
        }
        if (updateCounter >= 2000 && updateCounter % 750 ==0)
        {
            spawnItemWave(1, 1, 1);
        }
    }
    private void spawnItemWave(int itemHealth, int itemBullet, int itemShield)
    {
        Random random = new Random();
        for (int i=0; i<itemHealth; i++)
        {
            int randX = random.nextInt(320-30); // between 0 and 50
            int randY = random.nextInt(20);
//            Item item = new Item(randX, ((-450 + randY) + i*100), 1);
            Item item = new Item((randX + (i+1)*50), (randY + (i+1)*150)-300, 1);
            itemHealthList.add(item);
        }
        for (int i=0; i<itemBullet; i++)
        {
            int randX = random.nextInt(320-30); // between 0 and 50
            int randY = random.nextInt(70);
            Item item = new Item((randX + (i+1)*50), (randY + (i+1)*150)-300, 1);
            itemBulletList.add(item);
        }
        for (int i=0; i<itemShield; i++)
        {
            int randX = random.nextInt(320-30); // between 0 and 50
            int randY = random.nextInt(140);
            Item item = new Item((randX + (i+1)*50), (randY + (i+1)*150)-300, 1);
            itemShieldList.add(item);
        }
    }
}
