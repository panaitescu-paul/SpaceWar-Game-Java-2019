package com.example.survivingmotorcycle.SurvivingMotorcycle;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.survivingmotorcycle.GameEngine;
import com.example.survivingmotorcycle.Screen;
import com.example.survivingmotorcycle.Sound;
import com.example.survivingmotorcycle.TouchEvent;

import java.util.List;


public class GameScreen extends Screen
{
    enum State
    {
        Paused,
        Running,
        GameOver
    }

    Bitmap background = null;
    float backgroundX = 0;
    float backgroundY = 2000 - 320;
    Bitmap resume = null;
    Bitmap gameOver = null;
    Sound bounce = null;
    Sound crash = null;
    Sound gameOverSound = null;
    int backgroundSpeed = 100;


    World world = null; // creating the world
    WorldRenderer renderer = null; // drawing the world
    State state = State.Running;

    public GameScreen(GameEngine gameEngine)
    {
        super(gameEngine);
        Log.d("Carscroller", "Starting the GameScreen");

        background = gameEngine.loadBitmap("survivingmotorcycle/images/map1.png");
        resume = gameEngine.loadBitmap("survivingmotorcycle/images/resume.png");
        gameOver = gameEngine.loadBitmap("survivingmotorcycle/images/gameover.png");
        bounce = gameEngine.loadSound("survivingmotorcycle/music/bounce.wav");
        crash = gameEngine.loadSound("survivingmotorcycle/music/blocksplosion.wav");
        gameOverSound = gameEngine.loadSound("survivingmotorcycle/music/gameover.wav");

        world = new World(gameEngine, new CollisionListener()
        {
            @Override
            public void collisionRoadSide()
            {
                bounce.play(1);
            }

            @Override
            public void collisionMonster()
            {
                crash.play(1);
            }

            @Override
            public void gameOver()
            {
                gameOverSound.play(1);
            }
        }, backgroundSpeed);
        renderer = new WorldRenderer(gameEngine, world);
    }

    @Override
    public void update(float deltaTime)
    {
        if (world.gameOver)
        {
            state = State.GameOver;
        }

        Log.d("GameScreen", "size: " + gameEngine.getTouchEvents().size());
        Log.d("GameScreen", "not empty size: " + !gameEngine.getTouchEvents().isEmpty());

        if (state == State.Paused && gameEngine.getTouchEvents().size() > 0)
        {
            Log.d("GameScreen", "Starting the game again");
            state = State.Running;
            resume();
        }

        if (state == State.GameOver)
        {
            Log.d("GameScreen", "Game is Over.");
            List<TouchEvent> events = gameEngine.getTouchEvents();
            for (int i=0; i < events.size(); i++)
            {
                if (events.get(i).type == TouchEvent.TouchEventType.Up)
                {
                    gameEngine.setScreen(new MainMenuScreen(gameEngine));
                    return;
                }
            }
        }

        if (state == State.Running && gameEngine.getTouchY(0) < 40 && gameEngine.getTouchX(0) > 320-40)
        {
            Log.d("GameScreen", "Pausing the game.");
            state = State.Paused;
            pause();
//            return;  //maybe don;t draw anything more?
        }

        if (state == State.Running)
        {
//            backgroundY = backgroundY + backgroundSpeed * deltaTime;
            backgroundY = backgroundY - backgroundSpeed * deltaTime;
//            if (backgroundY > 2000 - 320) // img size - screen size
//            {
//                backgroundY = 0;
//            }
            if (backgroundY < 0) // img size - screen size
            {
                backgroundY = 2000 - 320;
            }
            // update the objects
            world.update(deltaTime, gameEngine.getAccelerometer()[1]);
        }

        // draw the backgraound, no matter what state (paused, running)
//        gameEngine.drawBitmap(background, 0,0, (int)backgroundY, 0, 480, 320);
        gameEngine.drawBitmap(background, 0,0, 0, (int)backgroundY, 480, 320);
        // draw the game object, no matter what state (paused, running)
        renderer.render();

        if (state == State.Paused)
        {
            gameEngine.drawBitmap(resume, 240 - resume.getWidth()/2, 160 - resume.getHeight()/2);

        }
        if (state == State.GameOver)
        {
            gameEngine.drawBitmap(gameOver, 240 - gameOver.getWidth()/2, 160 - gameOver.getHeight()/2);
        }
    }

    @Override
    public void pause()
    {

        if (state == State.Running) state = State.Paused;
        gameEngine.music.pause();
    }

    @Override
    public void resume()
    {
        gameEngine.music.play();

    }

    @Override
    public void dispose()
    {

    }
}
