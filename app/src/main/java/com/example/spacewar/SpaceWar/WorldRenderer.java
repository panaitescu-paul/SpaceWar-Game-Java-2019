package com.example.spacewar.SpaceWar;

import android.graphics.Bitmap;

import com.example.spacewar.GameEngine;


public class WorldRenderer
{
    GameEngine gameEngine;
    World world;
    Bitmap shipImage;
    Bitmap enemyImage;
    Bitmap enemyHealthImage;
    Bitmap bulletImage;
    Bitmap shieldImage;
    Bitmap healthItemImage;
    Bitmap bulletItemImage;
    Bitmap shieldItemImage;
    Bitmap enemyBulletImage;

    public WorldRenderer(GameEngine ge, World w)
    {
        gameEngine = ge;
        world = w;
        shipImage = gameEngine.loadBitmap("spacewar/images/vehicles/ship2.png");
        enemyImage = gameEngine.loadBitmap("spacewar/images/vehicles/e3.png");
        enemyHealthImage = gameEngine.loadBitmap("spacewar/images/health/health_bar3.png");
        bulletImage = gameEngine.loadBitmap("spacewar/images/vehicles/bullet1.png");
        shieldImage = gameEngine.loadBitmap("spacewar/images/items/item-shield-on110.png");
        healthItemImage = gameEngine.loadBitmap("spacewar/images/items/item-health30.png");
        bulletItemImage = gameEngine.loadBitmap("spacewar/images/items/item-bullet30.png");
        shieldItemImage = gameEngine.loadBitmap("spacewar/images/items/item-shield30.png");
        enemyBulletImage = gameEngine.loadBitmap("spacewar/images/vehicles/bullet1.png");

    }

    public void render()
    {
        gameEngine.drawBitmap(shipImage, world.vehicle.x, world.vehicle.y);
        if(world.vehicle.shield)
        {
            gameEngine.drawBitmap(shieldImage, world.vehicle.x-10, world.vehicle.y-18);
        }

        for (int i = 0; i< world.enemyList.size(); i++)
        {
            gameEngine.drawBitmap(enemyImage, world.enemyList.get(i).x, world.enemyList.get(i).y);

            if (world.enemyList.get(i).hp == 3) // render image for enemy with HP 3
            {
                gameEngine.drawBitmap(enemyHealthImage, world.enemyList.get(i).x, world.enemyList.get(i).y-10,
                        4, 5,
                        37, 5);
            }
            else if (world.enemyList.get(i).hp == 2) // render image for enemy with HP 2
            {
                gameEngine.drawBitmap(enemyHealthImage, world.enemyList.get(i).x, world.enemyList.get(i).y-10,
                        4, 15,
                        37, 5);
            }
            else if (world.enemyList.get(i).hp == 1) // render image for enemy with HP 1
            {
                gameEngine.drawBitmap(enemyHealthImage, world.enemyList.get(i).x, world.enemyList.get(i).y-10,
                        4, 34,
                        37, 5);
            }
        }
        for (int i=0; i< world.bulletList.size(); i++)
        {
            gameEngine.drawBitmap(bulletImage, world.bulletList.get(i).x, world.bulletList.get(i).y);
        }
        for (int i=0; i< world.healthItemList.size(); i++)
        {
            gameEngine.drawBitmap(healthItemImage, world.healthItemList.get(i).x, world.healthItemList.get(i).y);
        }
        for (int i=0; i< world.bulletsItemList.size(); i++)
        {
            gameEngine.drawBitmap(bulletItemImage, world.bulletsItemList.get(i).x, world.bulletsItemList.get(i).y);
        }
        for (int i=0; i< world.enemyBulletList.size(); i++)
        {
            gameEngine.drawBitmap(enemyBulletImage, world.enemyBulletList.get(i).x, world.enemyBulletList.get(i).y);
        }
        for (int i=0; i< world.shieldItemList.size(); i++)
        {
            gameEngine.drawBitmap(shieldItemImage, world.shieldItemList.get(i).x, world.shieldItemList.get(i).y);
        }
    }
}
