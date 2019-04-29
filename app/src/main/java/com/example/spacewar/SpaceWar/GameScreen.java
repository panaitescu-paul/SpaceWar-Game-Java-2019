package com.example.spacewar.SpaceWar;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.spacewar.GameEngine;
import com.example.spacewar.Screen;
import com.example.spacewar.Sound;
import com.example.spacewar.TouchEvent;

import java.util.List;


public class GameScreen extends Screen
{
    enum State
    {
        Paused,
        Running,
        GameOver
    }

    int bgImgHeight = 4096;
    Bitmap background = null;
    Bitmap background2 = null;
    Bitmap background3 = null;
    float backgroundX = 100;
    float backgroundY = bgImgHeight - 480;
    float backgroundY2 = bgImgHeight - 480;
    float backgroundY3 = bgImgHeight - 480;
    Bitmap resume = null;
    Bitmap gameOver = null;
    Bitmap pause = null;
    Bitmap start = null;
    Sound bounce = null;
    Sound crash = null;
    Sound gameOverSound = null;
    Sound bulletSound = null;
    Sound bulletSound2 = null;
    int backgroundSpeed = 100;
    int backgroundSpeed2 = 150;
    int backgroundSpeed3 = 80;
//    boolean bgMusicOn = false;


    World world = null; // creating the world
    WorldRenderer renderer = null; // drawing the world
    State state = State.Running;

    public GameScreen(GameEngine gameEngine)
    {
        super(gameEngine);
        Log.d("Carscroller", "Starting the GameScreen");

        background = gameEngine.loadBitmap("spacewar/images/maps/space_map1.png");
        background2 = gameEngine.loadBitmap("spacewar/images/maps/star_map1.png");
        background3 = gameEngine.loadBitmap("spacewar/images/maps/star_map2.png");
        resume = gameEngine.loadBitmap("spacewar/images/resume.png");
        pause = gameEngine.loadBitmap("spacewar/images/ui/btn_pause.png");
        start = gameEngine.loadBitmap("spacewar/images/ui/btn_start.png");
        gameOver = gameEngine.loadBitmap("spacewar/images/gameover.png");
        bounce = gameEngine.loadSound("spacewar/music/bounce.wav");
        crash = gameEngine.loadSound("spacewar/music/blocksplosion.wav");
        gameOverSound = gameEngine.loadSound("spacewar/music/gameover.wav");
        bulletSound = gameEngine.loadSound("spacewar/music/laser1.mp3");
        bulletSound2 = gameEngine.loadSound("spacewar/music/laser2.mp3");

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
        }, backgroundSpeed, backgroundSpeed2, backgroundSpeed3);
        renderer = new WorldRenderer(gameEngine, world);
    }

    @Override
    public void update(float deltaTime)
    {
        // add bg music

//        if (!bgMusicOn)
//        {
//            gameEngine.music.play();
//            bgMusicOn = true;
//        }

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

//        if (state == State.GameOver && gameEngine.getTouchEvents().size() > 0)
//        {
//            Log.d("GameScreen", "Starting the game again");
//            state = State.Running;
//            resume();
//        }

        if (state == State.GameOver)
        {
            Log.d("GameScreen", "Game is Over.");
            List<TouchEvent> events = gameEngine.getTouchEvents();
            for (int i=0; i < events.size(); i++)
            {
                if (events.get(i).type == TouchEvent.TouchEventType.Down)
                {
                    gameEngine.setScreen(new MainMenuScreen(gameEngine));
                    gameEngine.music.play();
                    return;
                }
            }
        }

        // press invisible pause button
        if (state == State.Running && gameEngine.getTouchY(0) < 50 && gameEngine.getTouchX(0) > 320-50)
        {
            Log.d("GameScreen", "Pausing the game.");
            state = State.Paused;
            pause();
//            return;  //maybe don;t draw anything more?
        }

        if (state == State.Running)
        {
//            backgroundY = backgroundY + backgroundSpeed * deltaTime;
            // makes the background move
            backgroundY = backgroundY - backgroundSpeed * deltaTime;
            backgroundY2 = backgroundY2 - backgroundSpeed2 * deltaTime;
            backgroundY3 = backgroundY3 - backgroundSpeed3 * deltaTime;
//            if (backgroundY > 2000 - 320) // img size - screen size
//            {
//                backgroundY = 0;
//            }
            if (backgroundY < 0) // img size - screen size
            {
                backgroundY = bgImgHeight - 480;
            }

            if (backgroundY2 < 0) // img size - screen size
            {
                backgroundY2 = bgImgHeight - 480;
            }
            if (backgroundY3 < 0) // img size - screen size
            {
                backgroundY3 = bgImgHeight - 480;
            }
            // update the objects
            world.update(deltaTime, gameEngine.getAccelerometer()[1]);
//            world.update(deltaTime, gameEngine.getTouchY(0));
        }

        // draw the backgraound, no matter what state (paused, running)
//        gameEngine.drawBitmap(background, 0,0, (int)backgroundY, 0, 480, 320);
//        gameEngine.drawBitmap(background, 0,0, 0, (int)backgroundY, 480, 320);
        gameEngine.drawBitmap(background, 0,0, 0, (int)backgroundY, 320, 480);
        gameEngine.drawBitmap(background2, 0,0, 0, (int)backgroundY2, 320, 480);
        gameEngine.drawBitmap(background3, 0,0, 0, (int)backgroundY3, 320, 480);
        gameEngine.drawBitmap(pause, 320-50, 10, 0, 0, 80 , 80);

        // draw the game object, no matter what state (paused, running)
        renderer.render();

        if (state == State.Paused)
        {
            pause();

//            gameEngine.drawBitmap(resume, 240 - resume.getWidth()/2, 160 - resume.getHeight()/2);
            gameEngine.drawBitmap(resume, 160 - resume.getWidth()/2, 240 - resume.getHeight()/2);
            gameEngine.drawBitmap(start, 320-50, 10, 0, 0, 80 , 80);


        }
        if (state == State.GameOver)
        {
            pause();
            gameEngine.drawBitmap(gameOver, 320/2 - gameOver.getWidth()/2, 480/2 - gameOver.getHeight()/2);

        }
    }

    @Override
    public void pause()
    {
        if (state == State.Running) state = State.Paused;
        gameEngine.music.pause();
//        bgMusicOn = false;
    }

    @Override
    public void resume()
    {
        gameEngine.music.play();
//        bgMusicOn = true;
    }

    @Override
    public void dispose()
    {
        gameEngine.music.pause();
//        gameEngine.music.stop();
//        gameEngine.music.dispose();
    }
}
