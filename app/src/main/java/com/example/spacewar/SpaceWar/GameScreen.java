package com.example.spacewar.SpaceWar;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
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

    int bgImgHeight = 4000; //asset size
    // Assets
    Bitmap backgroundLayer1;
    Bitmap backgroundLayer2;
    Bitmap backgroundLayer3;
    Bitmap resume;
    Bitmap gameOver;
    Bitmap pause;
    Bitmap start;
    Sound gameOverSound;
    Sound bulletSound;
    Sound bulletSound2;
    //    used to move the background
    float backgroundY = bgImgHeight - 480; // img size - screen size
    float backgroundY2 = bgImgHeight - 480;
    float backgroundY3 = bgImgHeight - 480;
    //  3 different background speeds for the parallax effect
    int backgroundSpeed = 100;
    int backgroundSpeed2 = 150;
    int backgroundSpeed3 = 80;
    Typeface font;
    String showText = "dummy";
    World world;                    // for creating the world
    WorldRenderer renderer;         // for drawing the world
    State state = State.Running;

    public GameScreen(GameEngine gameEngine)
    {
        super(gameEngine); // call the constructor from Screen
        Log.d("Spacewar", "Starting the GameScreen");
        // load the resources
        backgroundLayer1 = gameEngine.loadBitmap("spacewar/images/maps/space_map1.png");
        backgroundLayer2 = gameEngine.loadBitmap("spacewar/images/maps/star_map1.png");
        backgroundLayer3 = gameEngine.loadBitmap("spacewar/images/maps/star_map2.png");
        resume = gameEngine.loadBitmap("spacewar/images/resume.png");
        pause = gameEngine.loadBitmap("spacewar/images/ui/btn_pause.png");
        start = gameEngine.loadBitmap("spacewar/images/ui/btn_start.png");
        gameOver = gameEngine.loadBitmap("spacewar/images/gameover.png");
        gameOverSound = gameEngine.loadSound("spacewar/music/gameover.wav");
        bulletSound = gameEngine.loadSound("spacewar/music/laser1-2.wav");
        bulletSound2 = gameEngine.loadSound("spacewar/music/laser4-2.wav");
        font = gameEngine.loadFont("spacewar/images/ui/font.ttf");


        world = new World(gameEngine, new CollisionListener()
        {
            // implement methods from the interface
            // add sound effects to different actions in the game
            @Override
            public void generateBullet()
            {
                bulletSound2.play(1);
            }
            @Override
            public void collideBulletEnemy()
            {
                bulletSound.play(1);
            }
            @Override
            public void collideShipItem(){}
            @Override
            public void collideShipEnemy()
            {
                bulletSound2.play(1);
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
    public void update(float deltaTime) // deltaTime is the time between updates
    {
        if (world.gameOver) // gameOver boolean is true when the ship looses all lives
        {
            state = State.GameOver;
        }
        if (state == State.Paused && gameEngine.getTouchEvents().size() > 0) // if state is paused and there is a touch event
        {
            Log.d("GameScreen", "Starting the game again");
            state = State.Running;
            resume(); // continue background music
        }
        if (state == State.GameOver) // if game is over
        {
            Log.d("GameScreen", "Game is Over.");
            List<TouchEvent> events = gameEngine.getTouchEvents();
            for (int i=0; i < events.size(); i++)
            {
                if (events.get(i).type == TouchEvent.TouchEventType.Down) // look for touch down event
                {
                    gameEngine.setScreen(new MainMenuScreen(gameEngine)); // change screen to main menu
                    gameEngine.music.play();  // play music
                    return;
                }
            }
        }
        // press pause button
        if (state == State.Running && gameEngine.getTouchY(0) < 50 && gameEngine.getTouchX(0) > 320-50)
        {
            Log.d("GameScreen", "Pausing the game.");
            state = State.Paused;
            pause(); // pause music
        }
        if (state == State.Running)
        {
            // makes the background layers move on each update
            backgroundY = backgroundY - backgroundSpeed * deltaTime; // background moves with medium speed - aprox. 1 pixel per update
            backgroundY2 = backgroundY2 - backgroundSpeed2 * deltaTime; // background moves faster
            backgroundY3 = backgroundY3 - backgroundSpeed3 * deltaTime; // background moves slower
            if (backgroundY < 0)
            {
                backgroundY = bgImgHeight - 480; //reset background image
            }
            if (backgroundY2 < 0)
            {
                backgroundY2 = bgImgHeight - 480; //reset background image
            }
            if (backgroundY3 < 0)
            {
                backgroundY3 = bgImgHeight - 480; //reset background image
            }
            // update the objects
            world.update(deltaTime);
        }
        // draw the background, no matter what state (paused, running)
        gameEngine.drawBitmap(backgroundLayer1, 0,0, 0, (int)backgroundY, 320, 480);
        gameEngine.drawBitmap(backgroundLayer2, 0,0, 0, (int)backgroundY2, 320, 480);
        gameEngine.drawBitmap(backgroundLayer3, 0,0, 0, (int)backgroundY3, 320, 480);
        gameEngine.drawBitmap(pause, 320-50, 10, 0, 0, 80 , 80);
        // draw the game objects, no matter what state (paused, running)
        renderer.render();
        // write on screen
        showText = "Score: " + world.scorePoints + "    Lives: " + world.ship.lives ;
        gameEngine.drawText(font, showText, 22, 22, Color.GREEN, 12);

        if (state == State.Paused)
        {
            pause();
            //draw images
            gameEngine.drawBitmap(resume, 160 - resume.getWidth()/2, 240 - resume.getHeight()/2);
            gameEngine.drawBitmap(start, 320-50, 10, 0, 0, 80 , 80);
        }
        if (state == State.GameOver)
        {
            pause();
            //draw image
            gameEngine.drawBitmap(gameOver, 320/2 - gameOver.getWidth()/2, 480/2 - gameOver.getHeight()/2);
        }
    }

    @Override
    public void pause()
    {
        gameEngine.music.pause(); // pause the background music
        if (state == State.Running) state = State.Paused;
    }
    @Override
    public void resume()
    {
        gameEngine.music.play(); // play the background music
    }
    @Override
    public void dispose()
    {
        gameEngine.music.pause(); // pause the background music
//        gameEngine.music.stop();
//        gameEngine.music.dispose();
    }
}
