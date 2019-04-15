package com.example.survivingmotorcycle.SurvivingMotorcycle;

import android.graphics.Bitmap;

import com.example.survivingmotorcycle.GameEngine;


public class WorldRenderer
{
    GameEngine gameEngine;
    World world;
    Bitmap carImage;
    Bitmap monsterImage;

    public WorldRenderer(GameEngine ge, World w)
    {
        gameEngine = ge;
        world = w;
        carImage = gameEngine.loadBitmap("survivingmotorcycle/images/vehicles/ship2.png");
        monsterImage = gameEngine.loadBitmap("survivingmotorcycle/images/enemy1.png");
    }

    public void render()
    {
        gameEngine.drawBitmap(carImage, world.vehicle.x, world.vehicle.y);
        for (int i=0; i< world.monsterList.size(); i++)
        {
            gameEngine.drawBitmap(monsterImage, world.monsterList.get(i).x, world.monsterList.get(i).y);
        }
    }
}
