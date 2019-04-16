package com.example.survivingmotorcycle.SurvivingMotorcycle;

import android.util.Log;

import com.example.survivingmotorcycle.GameEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class World
{

    public static final float MIN_X = 1;
    public static final float MAX_X = 320-1;
//    public static final float MAX_X = 1000;
    public static final float MIN_Y = 37;
    public static final float MAX_Y = 285;


    Vehicle vehicle = new Vehicle();
    List<Monster> monsterList = new ArrayList<>();
    public int maxMonsters = 3;

    GameEngine gameEngine;
    CollisionListener listener;

    boolean gameOver = false;
    int points = 0;
    int lives = 3;
    int backgroundSpeed = 0;

    public World(GameEngine gameEngine, CollisionListener listener, int backgroundSpeed)
    {
        this.backgroundSpeed = backgroundSpeed;
        this.gameEngine = gameEngine;
        this.listener = listener;
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
//            vehicle.y = gameEngine.getTouchY(0) - Vehicle.HEIGHT;
            vehicle.x = gameEngine.getTouchX(0) - Vehicle.WIDTH;
        }

        // check left screen boundary
        if (vehicle.x < MIN_X) vehicle.x = (int) MIN_X + 1;
        // check right screen boundary
        if (vehicle.x + vehicle.WIDTH > MAX_X) vehicle.x = (int)(MAX_X - vehicle.WIDTH - 1);

        Monster monster = null;
        for (int i=1; i<maxMonsters; i++)
        {
            monster = monsterList.get(i);
//            monster.x = (int)(monster.x - backgroundSpeed * deltaTime);
            monster.y = (int)(monster.y - backgroundSpeed * deltaTime);
            if (monster.x < 0 - Monster.WIDTH)
            {
                Random random = new Random();
                monster.x = 500 + random.nextInt(300);
                monster.y = 30 + random.nextInt(230);
                Log.d("World", "Just recycled a monster.");
            }
        }
        // check if the car collides with a monster
        collideCarMonster();
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
            int randX = random.nextInt(50); // between 0 and 50
            int randY = random.nextInt(255);
            Monster monster = new Monster(((500 + randX) + i*50), 30 + randY);
            monsterList.add(monster);
        }
    }
}
