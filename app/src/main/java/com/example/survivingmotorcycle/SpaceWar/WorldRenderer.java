package com.example.survivingmotorcycle.SpaceWar;

import android.graphics.Bitmap;

import com.example.survivingmotorcycle.GameEngine;


public class WorldRenderer
{
    GameEngine gameEngine;
    World world;
    Bitmap carImage;
    Bitmap monsterImage;
    Bitmap bulletImage;

    public WorldRenderer(GameEngine ge, World w)
    {
        gameEngine = ge;
        world = w;
        carImage = gameEngine.loadBitmap("survivingmotorcycle/images/vehicles/ship2.png");
        monsterImage = gameEngine.loadBitmap("survivingmotorcycle/images/vehicles/e3.png");
        bulletImage = gameEngine.loadBitmap("survivingmotorcycle/images/vehicles/bullet1.png");
    }

    public void render()
    {
        gameEngine.drawBitmap(carImage, world.vehicle.x, world.vehicle.y);
//        gameEngine.drawBitmap(bulletImage, world.bullet.x, world.bullet.y);
        for (int i=0; i< world.monsterList.size(); i++)
        {
            gameEngine.drawBitmap(monsterImage, world.monsterList.get(i).x, world.monsterList.get(i).y);
        }
        for (int i=0; i< world.bulletList.size(); i++)
        {
            gameEngine.drawBitmap(bulletImage, world.bulletList.get(i).x, world.bulletList.get(i).y);
        }
    }
}
