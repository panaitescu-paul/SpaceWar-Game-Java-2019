package com.example.spacewar.SpaceWar;

import android.graphics.Bitmap;

import com.example.spacewar.GameEngine;


public class WorldRenderer
{
    GameEngine gameEngine;
    World world;
    Bitmap carImage;
    Bitmap monsterImage;
    Bitmap bulletImage;
    Bitmap itemImage;

    public WorldRenderer(GameEngine ge, World w)
    {
        gameEngine = ge;
        world = w;
        carImage = gameEngine.loadBitmap("spacewar/images/vehicles/ship2.png");
        monsterImage = gameEngine.loadBitmap("spacewar/images/vehicles/e3.png");
        bulletImage = gameEngine.loadBitmap("spacewar/images/vehicles/bullet1.png");
        itemImage = gameEngine.loadBitmap("spacewar/images/vehicles/item1.png");
    }

    public void render()
    {
        gameEngine.drawBitmap(carImage, world.vehicle.x, world.vehicle.y);
//        gameEngine.drawBitmap(bulletImage, world.bullet.x, world.bullet.y);
        //
        for (int i=0; i< world.monsterList.size(); i++)
        {
            gameEngine.drawBitmap(monsterImage, world.monsterList.get(i).x, world.monsterList.get(i).y);
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
