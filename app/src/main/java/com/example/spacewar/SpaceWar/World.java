package com.example.spacewar.SpaceWar;

import android.util.Log;

import com.example.spacewar.GameEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class World
{
    // making a new instance of the Ship object
    Ship ship = new Ship();
    // array lists of enemies, items, ship bullets and enemy bullets
    List<Enemy> enemyList = new ArrayList<>();
    List<Item> itemHealthList = new ArrayList<>();
    List<Item> itemBulletList = new ArrayList<>();
    List<Item> itemShieldList = new ArrayList<>();
    List<Bullet> bulletList = new ArrayList<>();
    List<Bullet> enemyBulletList = new ArrayList<>();

    GameEngine gameEngine;
    CollisionListener listener;

    // used to calculate a global speed of movement for objects
    int backgroundSpeed;
    // used to variate the speed for the parallax effect on backgrounds
    int backgroundSpeed2;
    int backgroundSpeed3;

    // keeps track of the number of updates that passed
    long updateCounter = 0;

    // used for creating bullet waves from ship
    int bulletsOnCounter = 0;
    int bulletsOffCounter = 0;
    boolean bulletsOn = true;

    // used for creating bullet waves from enemies
    int bulletsOnCounter2 = 0;
    int bulletsOffCounter2 = 0;
    boolean bulletsOn2 = true;

    int scorePoints = 0;
    boolean gameOver = false;

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
            // move enemy bullet downwards from enemy coordinates to the bottom of the screen
            enemyBullet2.y = (int) (enemyBullet2.y + 2 + backgroundSpeed * deltaTime);
            if (enemyBullet2.y > 480) // if enemy bullet disappears off screen
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
            // move ship bullet upwards from ship coordinates to the top of the screen
            bullet.y = (int)(bullet.y - backgroundSpeed * deltaTime);

            if (bullet.y < 0 - Bullet.HEIGHT) // if ship bullet disappears off screen
            {
                bulletList.remove(i);
            }
        }
    }

    private void spawnEnemyBullets()
    {
        Bullet enemyBullet;
        if (updateCounter % 10 == 0) // every 10 updates
        {
            checkEnemyBulletShooting(1, 12); // check if the enemy should shoot or not
            if (bulletsOn2)
            {
                Enemy enemy2 = null;
                for (int i = 0; i < enemyList.size(); i++)
                {
                    enemy2 = enemyList.get(i);
                    if (enemy2.shooting && enemy2.y > 0) // shoot only if it is a shooting enemy type
                    {
                        // create a new bullet based on the enemy coordinates
                        enemyBullet = new Bullet(enemy2.x + enemy2.WIDTH / 2 - 2, enemy2.y + 30); // middle enemy coordonates
                        enemyBulletList.add(enemyBullet);
                        bulletsOnCounter2++; // count as bullet shot, up until the value of onUnits
                        listener.generateBullet(); // generate specific sound
                    }
                }
            }
        }
    }

    private void spawnShipBullets()
    {
        Bullet bullet;
        if (updateCounter % 10 ==0) // every 10 updates
        {
            // check what pace to use on shooting, based on the amount of items picked (Multiple Bullets items)
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
            if (bulletsOn) // check if we can shoot in this update
            {
                if(ship.multipleBullets == 1) // check how many Multiple Bullets items are picked
                {
                    bullet = new Bullet(ship.x+ship.WIDTH/2-2, ship.y-15); // middle ship coordinates
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
                    bullet = new Bullet(ship.x+ship.WIDTH/2-2, ship.y-8); // middle ship coordinates
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
                    bullet = new Bullet(ship.x+ship.WIDTH/2-2, ship.y-8); // middle vehicle coordinates
                    bulletList.add(bullet);
                    bullet = new Bullet(ship.x+ship.WIDTH/2-2+20, ship.y);
                    bulletList.add(bullet);
                    bullet = new Bullet(ship.x+ship.WIDTH/2-2+20+20, ship.y+8);
                    bulletList.add(bullet);
                }
                bulletsOnCounter ++; // count as bullet shot, up until the value of onUnits
                listener.generateBullet(); // generate specific sound
            }
        }
    }


    private void moveItems(float deltaTime)
    {
        // Health item
        Item healthItem = null;
        for (int i = 0; i < itemHealthList.size(); i++)
        {
            healthItem = itemHealthList.get(i);
            // move Health item downwards from the top, until the bottom of the screen
            healthItem.y = (int)(healthItem.y + backgroundSpeed * deltaTime);
            Random random3 = new Random();
            int randDirectionChangeRate = 100 + random3.nextInt(500); // between 100 and 600
            if (updateCounter % randDirectionChangeRate == 0) // between 1 and 10 seconds
            {
                healthItem.direction = -healthItem.direction; // change direction
            }
            if (healthItem.x < 0 || healthItem.x > 320-healthItem.WIDTH)
            {
                healthItem.direction = -healthItem.direction; // // change direction
            }
            // move on the X axis: left or right, depending on the item direction
            healthItem.x = (int)(healthItem.x + backgroundSpeed * deltaTime * healthItem.direction);
            if (healthItem.y > 480) // if item disappears off screen
            {
                itemHealthList.remove(i);
            }
        }

        // Multiple Bullet item
        Item bulletsItem = null;
        for (int i = 0; i < itemBulletList.size(); i++)
        {
            bulletsItem = itemBulletList.get(i);
            // move Multiple Bullet item downwards from the top, until the bottom of the screen
            bulletsItem.y = (int)(bulletsItem.y + backgroundSpeed * deltaTime);
            Random random3 = new Random();
            int randDirectionChangeRate = 100 + random3.nextInt(500); // between 100 and 600
            if (updateCounter % randDirectionChangeRate == 0) // between 1 and 10 seconds
            {
                bulletsItem.direction = -bulletsItem.direction; // change direction
            }
            if (bulletsItem.x < 0 || bulletsItem.x > 320-bulletsItem.WIDTH)
            {
                bulletsItem.direction = -bulletsItem.direction; // change direction
            }
            // move on the X axis: left or right, depending on the item direction
            bulletsItem.x = (int)(bulletsItem.x + backgroundSpeed * deltaTime * bulletsItem.direction);
            if (bulletsItem.y > 480) // if item disappears off screen
            {
                itemBulletList.remove(i);
            }
        }

        // Shield item
        Item shieldItem = null;
        for (int i = 0; i < itemShieldList.size(); i++)
        {
            shieldItem = itemShieldList.get(i);
            // move Shield item downwards from the top, until the bottom of the screen
            shieldItem.y = (int)(shieldItem.y + backgroundSpeed * deltaTime);
            Random random3 = new Random();
            int randDirectionChangeRate = 100 + random3.nextInt(500); // between 100 and 600
            if (updateCounter % randDirectionChangeRate == 0)
            {
                shieldItem.direction = -shieldItem.direction; // change direction
            }
            if (shieldItem.x < 0 || shieldItem.x > 320- shieldItem.WIDTH)
            {
                shieldItem.direction = -shieldItem.direction; // change direction
            }
            // move on the X axis: left or right, depending on the item direction
            shieldItem.x = (int)(shieldItem.x + backgroundSpeed * deltaTime * shieldItem.direction);
            if (shieldItem.y > 480) // if item disappears off screen
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
            // move enemy downwards from the top, until the bottom of the screen
            enemy.y = (int)(enemy.y + backgroundSpeed * deltaTime);
            Random random2 = new Random();
            int randDirectionChangeRate = 100 + random2.nextInt(500); // between 100 and 600
            if (updateCounter % randDirectionChangeRate ==0)
            {
                enemy.direction = -enemy.direction; // change direction
            }
            if (enemy.x < 0 || enemy.x > 320- enemy.WIDTH)
            {
                enemy.direction = -enemy.direction; // change direction
            }
            // move on the X axis: left or right, depending on the enemy direction
            enemy.x = (int)(enemy.x + backgroundSpeed * deltaTime * enemy.direction);
            if (enemy.y > 480) // if enemy disappears off screen
            {
                enemyList.remove(i);
            }
        }
    }

    private void scoreCounter()
    {
        if (updateCounter % 10 ==0) // every 10 updates
        {
            scorePoints++; // add 1 point for every 1/6 seconds
        }
    }

    private void moveShip()
    {
        // move the ship based on user touch
        if (gameEngine.isTouchDown(0));
        {
            // position the middle of the ship on touch coordinates
            ship.x = gameEngine.getTouchX(0) - Ship.WIDTH/2;
            // check for ship to be in the lower part of the screen
            if (gameEngine.getTouchY(0) < 480/2) // check if touch is on the upper part
            {
                ship.y = 480/2 - Ship.HEIGHT/2; // reposition ship on the lower part of the screen
            }else
            {
                ship.y = gameEngine.getTouchY(0) - Ship.HEIGHT/2; // position ship after touch y
            }
            if (gameEngine.getTouchY(0) > 480 - Ship.HEIGHT/2)  // check if ship is out of screen (lower)
            {
                ship.y = 480 - Ship.HEIGHT/2 - 40 ; // reposition ship on the lower part of the screen
            }
        }
        // check left screen boundary
        if (ship.x < -30) ship.x = -30 + 1;
        // check right screen boundary
        if (ship.x + ship.WIDTH > 320 + 30) ship.x = 320 + 30 - Ship.WIDTH - 1;  //  320 + 30 = logical screen + ship wing size
    }

    private void collideShipEnemy()
    {
        Enemy enemy = null;
        for (int i=0; i < enemyList.size(); i++)
        {
            enemy = enemyList.get(i);
            // if shield is off then check collision between ship and enemy
            if (!ship.shield && collideRects(ship.x, ship.y, Ship.WIDTH, Ship.HEIGHT,
                    enemy.x, enemy.y, Enemy.WIDTH, Enemy.HEIGHT))
            {
                ship.lives = ship.lives - 1; // loose 1 live when the ship hits an enemy
                //delete enemy from list
                enemyList.remove(i);
                if(ship.multipleBullets > 1)
                {
                    // loose 1 multiple bullets, when the ship hits an enemy
                    ship.multipleBullets = ship.multipleBullets - 1;
                }
                listener.collideShipEnemy(); // generate specific sound
            }
            // if shield is on then check collision between ship shield and enemy
            else if(ship.shield && collideRects(ship.x, ship.y, Ship.WIDTH+10, Ship.HEIGHT+24,
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
                listener.gameOver(); // generate specific sound
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
                listener.collideShipItem(); // generate specific sound
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
                // if there are less then 5 multiple bullets (which is the maximum)
                if(ship.multipleBullets < 5)
                {
                    ship.multipleBullets = ship.multipleBullets + 1; // increase the multiple bullets
                }
                itemBulletList.remove(i);
                listener.collideShipItem(); // generate specific sound
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
                listener.collideShipItem(); // generate specific sound
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
                    // bullet disappears
                    bulletList.remove(i);
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
                    listener.collideBulletEnemy(); // generate specific sound
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
            // check collision of a enemy bullet with the ship
            // if shield is off then check collision between enemy bullet and ship
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
                listener.collideBulletEnemy(); // generate specific sound
            }
            // if shield is on then check collision between enemy bullet and ship
            else if(ship.shield && collideRects(enemyBullet.x, enemyBullet.y, Bullet.WIDTH, Bullet.HEIGHT,
                    ship.x, ship.y, Ship.WIDTH+10, Ship.HEIGHT+24))
            {
                enemyBulletList.remove(i);
                ship.shield = false; // shield goes off after colliding an enemy
            }
            // if there are no lives left then it's game over
            if(ship.lives==0)
            {
                gameOver = true;
                listener.gameOver(); // generate specific sound
            }
        }
    }

    // check the collision between 2 rectangles
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
    private void configureEnemyWaves(long updateCounter) // used updateCounter to spawn waves at a specific time
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
        if (updateCounter >= 2400 && updateCounter % 750 ==0) // repeat enemy waves spawn every 12-13 seconds, after 2400 updates
        {
            spawnEnemyWave(5, 3, 2);
        }
    }

    private void spawnEnemyWave(int enemyBasic, int enemyShooting, int enemyShielded)
    {
        Random random = new Random();
        for (int i=0; i<enemyBasic; i++)
        {
            int randX = random.nextInt(320-30); // between 0 and 290
            int randY = random.nextInt(20);
            Enemy enemy = new Enemy(randX, ((-450 + randY) + i*150)-200, 1, 3, false, false);
            enemyList.add(enemy);
        }
        for (int i=0; i<enemyShooting; i++)
        {
            int randX = random.nextInt(320-30); // between 0 and 290
            int randY = random.nextInt(20);
            Enemy enemy2 = new Enemy(randX, ((-450 + randY) + i*150)-200, 1, 3, true, false);
            enemyList.add(enemy2);
        }
        for (int i=0; i<enemyShielded; i++)
        {
            int randX = random.nextInt(320-30); // between 0 and 290
            int randY = random.nextInt(20);
            Enemy enemy3 = new Enemy(randX, ((-450 + randY) + i*150)-200, 1, 1, false, true);
            enemyList.add(enemy3);
        }
    }

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
        if (updateCounter >= 2000 && updateCounter % 750 ==0) // repeat item waves spawn every 12-13 seconds, after 2000 updates
        {
            spawnItemWave(1, 1, 1);
        }
    }
    private void spawnItemWave(int itemHealth, int itemBullet, int itemShield)
    {
        Random random = new Random();
        for (int i=0; i<itemHealth; i++)
        {
            int randX = random.nextInt(320-30); // between 0 and 290
            int randY = random.nextInt(20);
            Item item = new Item((randX + (i+1)*50), (randY + (i+1)*150)-300, 1);
            itemHealthList.add(item);
        }
        for (int i=0; i<itemBullet; i++)
        {
            int randX = random.nextInt(320-30); // between 0 and 290
            int randY = random.nextInt(20) + 50;
            Item item = new Item((randX + (i+1)*50), (randY + (i+1)*150)-300, 1);
            itemBulletList.add(item);
        }
        for (int i=0; i<itemShield; i++)
        {
            int randX = random.nextInt(320-30); // between 0 and 290
            int randY = random.nextInt(20) + 100;
            Item item = new Item((randX + (i+1)*50), (randY + (i+1)*150)-300, 1);
            itemShieldList.add(item);
        }
    }
}
