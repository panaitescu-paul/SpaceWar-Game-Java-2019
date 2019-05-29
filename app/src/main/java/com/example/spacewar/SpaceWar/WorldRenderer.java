package com.example.spacewar.SpaceWar;

import android.graphics.Bitmap;

import com.example.spacewar.GameEngine;


public class WorldRenderer
{
    GameEngine gameEngine;
    World world;
    Bitmap shipImage;
    Bitmap enemy1Image;
    Bitmap enemy2Image;
    Bitmap enemy3Image;
    Bitmap enemyHealthImage;
    Bitmap bullet1Image;
    Bitmap shipShieldImage;
    Bitmap enemyShieldImage;
    Bitmap healthItemImage;
    Bitmap bulletItemImage;
    Bitmap shieldItemImage;
    Bitmap enemyBulletImage;

    public WorldRenderer(GameEngine ge, World w)
    {
        gameEngine = ge;
        world = w;
        shipImage = gameEngine.loadBitmap("spacewar/images/vehicles/ship2.png");
//        enemy1Image = gameEngine.loadBitmap("spacewar/images/vehicles/1/enemy2-8.png");
//        enemy1Image = gameEngine.loadBitmap("spacewar/images/vehicles/1/enemy1-4.png");
//        enemy1Image = gameEngine.loadBitmap("spacewar/images/vehicles/1/enemy2-8.png");
        enemy1Image = gameEngine.loadBitmap("spacewar/images/vehicles/1/enemy1-30.png");
        enemy2Image = gameEngine.loadBitmap("spacewar/images/vehicles/1/enemy2-30.png");
        enemy3Image = gameEngine.loadBitmap("spacewar/images/vehicles/1/enemy3-30.png");
//        enemy1Image = gameEngine.loadBitmap("spacewar/images/vehicles/1/enemy1-9.png");
//        enemy1Image = gameEngine.loadBitmap("spacewar/images/vehicles/1/enemy1-9.png");
        enemyHealthImage = gameEngine.loadBitmap("spacewar/images/health/health_bar3.png");
        bullet1Image = gameEngine.loadBitmap("spacewar/images/vehicles/1/bullet3-1.png");
        shipShieldImage = gameEngine.loadBitmap("spacewar/images/items/item-shield-on110.png");
        enemyShieldImage = gameEngine.loadBitmap("spacewar/images/items/item-shield-on50.png");
        healthItemImage = gameEngine.loadBitmap("spacewar/images/items/item-health30.png");
        bulletItemImage = gameEngine.loadBitmap("spacewar/images/items/item-bullet30.png");
        shieldItemImage = gameEngine.loadBitmap("spacewar/images/items/item-shield30.png");
//        enemyBulletImage = gameEngine.loadBitmap("spacewar/images/vehicles/bullet1.png");
        enemyBulletImage = gameEngine.loadBitmap("spacewar/images/vehicles/1/bullet2-2.png");

    }

    public void render()
    {
        gameEngine.drawBitmap(shipImage, world.ship.x, world.ship.y);
        if(world.ship.shield)
        {
            gameEngine.drawBitmap(shipShieldImage, world.ship.x-10, world.ship.y-18);
        }

        for (int i = 0; i< world.enemyList.size(); i++)
        {
            // Render Basic enemy
            // Render Shooting enemy
            // Render Shield enemy

            if (!world.enemyList.get(i).shooting && !world.enemyList.get(i).shield) // Render Basic enemy
            {
                gameEngine.drawBitmap(enemy1Image, world.enemyList.get(i).x+3, world.enemyList.get(i).y);
            }
            else if (world.enemyList.get(i).shooting) // Render Shooting enemy
            {
                gameEngine.drawBitmap(enemy2Image, world.enemyList.get(i).x-1, world.enemyList.get(i).y);
            }
            else if (world.enemyList.get(i).shield) // Render Shield enemy
            {
                gameEngine.drawBitmap(enemy3Image, world.enemyList.get(i).x, world.enemyList.get(i).y);
            }

            if (world.enemyList.get(i).health == 3 && !world.enemyList.get(i).shield) // render image for enemy with HP 3, if it is not a shielded enemy
            {
                gameEngine.drawBitmap(enemyHealthImage, world.enemyList.get(i).x, world.enemyList.get(i).y-10,
                        4, 5,
                        37, 5);
            }
            else if (world.enemyList.get(i).health == 2 && !world.enemyList.get(i).shield) // render image for enemy with HP 2
            {
                gameEngine.drawBitmap(enemyHealthImage, world.enemyList.get(i).x, world.enemyList.get(i).y-10,
                        4, 15,
                        37, 5);
            }
            else if (world.enemyList.get(i).health == 1 && !world.enemyList.get(i).shield) // render image for enemy with HP 1
            {
                gameEngine.drawBitmap(enemyHealthImage, world.enemyList.get(i).x, world.enemyList.get(i).y-10,
                        4, 34,
                        37, 5);
            }
            if (world.enemyList.get(i).shield) // render image for enemy with shield
            {
                gameEngine.drawBitmap(enemyShieldImage, world.enemyList.get(i).x-10, world.enemyList.get(i).y-10);
            }
        }
        for (int i=0; i< world.bulletList.size(); i++)
        {
            gameEngine.drawBitmap(bullet1Image, world.bulletList.get(i).x, world.bulletList.get(i).y);
        }
        for (int i = 0; i< world.itemHealthList.size(); i++)
        {
            gameEngine.drawBitmap(healthItemImage, world.itemHealthList.get(i).x, world.itemHealthList.get(i).y);
        }
        for (int i = 0; i< world.itemBulletList.size(); i++)
        {
            gameEngine.drawBitmap(bulletItemImage, world.itemBulletList.get(i).x, world.itemBulletList.get(i).y);
        }
        for (int i=0; i< world.enemyBulletList.size(); i++)
        {
            gameEngine.drawBitmap(enemyBulletImage, world.enemyBulletList.get(i).x, world.enemyBulletList.get(i).y);
        }
        for (int i = 0; i< world.itemShieldList.size(); i++)
        {
            gameEngine.drawBitmap(shieldItemImage, world.itemShieldList.get(i).x, world.itemShieldList.get(i).y);
        }
    }
}
