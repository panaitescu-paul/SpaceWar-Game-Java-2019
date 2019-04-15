package com.example.survivingmotorcycle.SurvivingMotorcycle;

import android.graphics.Bitmap;

import com.example.survivingmotorcycle.GameEngine;
import com.example.survivingmotorcycle.Screen;

public class MainMenuScreen extends Screen
{
    Bitmap background;
    Bitmap startGame;
    float passedTime = 0;
    long startTime;

    public MainMenuScreen(GameEngine gameEngine)
    {
        super(gameEngine);
        background = gameEngine.loadBitmap("survivingmotorcycle/images/maps/map2.png");
        startGame = gameEngine.loadBitmap("survivingmotorcycle/images/xstartgame.png");
        startTime = System.nanoTime();
    }

    @Override
    public void update(float deltaTime)
    {
        if (gameEngine.isTouchDown(0) && (passedTime) > 0.5f) // wait half of second after the main menu shows, then interact
        {
            gameEngine.setScreen(new GameScreen(gameEngine));
            return;
        }
        gameEngine.drawBitmap(background, 0, 0);
        passedTime = passedTime + deltaTime;
        if ((passedTime - (int)passedTime) > 0.5f )  // 1,55 - 1 > 0.5  blink every half seconds
        {
            gameEngine.drawBitmap(startGame, 240 - startGame.getWidth()/2, 160);
        }

    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void dispose()
    {

    }
}
