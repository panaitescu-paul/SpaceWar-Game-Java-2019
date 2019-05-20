package com.example.spacewar.SpaceWar;

import android.graphics.Bitmap;

import com.example.spacewar.GameEngine;


public class WorldRenderer
{
    GameEngine gameEngine;
    World world;
    Bitmap carImage;
    Bitmap monsterImage;
    Bitmap monsterHealthImage;
    Bitmap bulletImage;
    Bitmap itemImage;

    public WorldRenderer(GameEngine ge, World w)
    {
        gameEngine = ge;
        world = w;
        carImage = gameEngine.loadBitmap("spacewar/images/vehicles/ship2.png");
        monsterImage = gameEngine.loadBitmap("spacewar/images/vehicles/e3.png");
        monsterHealthImage = gameEngine.loadBitmap("spacewar/images/health/health_bar3.png");
        bulletImage = gameEngine.loadBitmap("spacewar/images/vehicles/bullet1.png");
        itemImage = gameEngine.loadBitmap("spacewar/images/items/item-health30.png");
    }

    public void render()
    {
        gameEngine.drawBitmap(carImage, world.vehicle.x, world.vehicle.y);

        for (int i=0; i< world.monsterList.size(); i++)
        {
            gameEngine.drawBitmap(monsterImage, world.monsterList.get(i).x, world.monsterList.get(i).y);

            if (world.monsterList.get(i).hp == 3) // render image for enemy with HP 3
            {
                gameEngine.drawBitmap(monsterHealthImage, world.monsterList.get(i).x, world.monsterList.get(i).y-10,
                        4, 5,
                        37, 5);
            }
            else if (world.monsterList.get(i).hp == 2) // render image for enemy with HP 2
            {
                gameEngine.drawBitmap(monsterHealthImage, world.monsterList.get(i).x, world.monsterList.get(i).y-10,
                        4, 15,
                        37, 5);
            }
            else if (world.monsterList.get(i).hp == 1) // render image for enemy with HP 1
            {
                gameEngine.drawBitmap(monsterHealthImage, world.monsterList.get(i).x, world.monsterList.get(i).y-10,
                        4, 34,
                        37, 5);
            }
        }
        for (int i=0; i< world.bulletList.size(); i++)
        {
            gameEngine.drawBitmap(bulletImage, world.bulletList.get(i).x, world.bulletList.get(i).y);
        }
        for (int i=0; i< world.itemList.size(); i++)
        {
            gameEngine.drawBitmap(itemImage, world.itemList.get(i).x, world.itemList.get(i).y);
        }
    }
}
