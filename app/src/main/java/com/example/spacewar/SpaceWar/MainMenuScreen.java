package com.example.spacewar.SpaceWar;

import android.graphics.Bitmap;

import com.example.spacewar.GameEngine;
import com.example.spacewar.Screen;

public class MainMenuScreen extends Screen
{
    // Assets
    Bitmap background;
    Bitmap startGame;
    Bitmap info;
    Bitmap exit;
    float passedTime = 0; // time passed before an interaction with the screen

    public MainMenuScreen(GameEngine gameEngine)
    {
        super(gameEngine); // call the constructor from Screen
        // load the resources
        background = gameEngine.loadBitmap("spacewar/images/maps/bg_start.png");
        startGame = gameEngine.loadBitmap("spacewar/images/ui/2/start_btn_200.png");
        info = gameEngine.loadBitmap("spacewar/images/ui/2/info_btn_60.png");
        exit = gameEngine.loadBitmap("spacewar/images/ui/2/exit_btn_200.png");
    }

    @Override
    public void update(float deltaTime)
    {
        // draw images
        gameEngine.drawBitmap(background, 0, 0);
        gameEngine.drawBitmap(startGame, 320/2 - startGame.getWidth()/2, 480/2 - startGame.getHeight()/2 - 90);
        gameEngine.drawBitmap(exit, 320/2 - exit.getWidth()/2, 480/2 - exit.getHeight()/2 + 10);
        gameEngine.drawBitmap(info, 320/2 - info.getWidth()/2, 480/2 - info.getHeight()/2 + 110);

        passedTime = passedTime + deltaTime; // add time between updates
        if (gameEngine.isTouchDown(0) && (passedTime) > 0.5f) // wait half a second after the main menu shows, then interact
        {
            // calculate area where the button is situated, so that only in that area the action happens
            // calculate the starting and ending points of the button for the X and Y axis, to place the button area on the center of the screen
            if(gameEngine.getTouchY(0) > 150-30 && gameEngine.getTouchY(0) < 150+30
                    && gameEngine.getTouchX(0) > 160-100 && gameEngine.getTouchX(0) < 160+100)
            {
                gameEngine.setScreen(new GameScreen(gameEngine)); // change to Game screen
            }
            if(gameEngine.getTouchY(0) > 250-30 && gameEngine.getTouchY(0) < 250+30
                    && gameEngine.getTouchX(0) > 160-100 && gameEngine.getTouchX(0) < 160+100)
            {
                System.exit(0); // close the game
            }
            if(gameEngine.getTouchY(0) > 350-30 && gameEngine.getTouchY(0) < 350+30
                    && gameEngine.getTouchX(0) > 160-30 && gameEngine.getTouchX(0) < 160+30)
            {
                gameEngine.setScreen(new InfoScreen(gameEngine)); // change to Info screen
            }
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
