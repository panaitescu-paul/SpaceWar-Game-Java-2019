package com.example.spacewar.SpaceWar;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;

import com.example.spacewar.GameEngine;
import com.example.spacewar.Screen;

public class InfoScreen extends Screen
{
    // Assets
    Bitmap background;
    Bitmap close;
    Bitmap healthItemImage;
    Bitmap bulletItemImage;
    Bitmap shieldItemImage;
    Bitmap enemy1Image;
    Bitmap enemy2Image;
    Bitmap enemy3Image;
    // Text to write on the screen
    String title = "title";
    String text1 = "text1";
    String text2 = "text2";
    String text3 = "text3";
    String text4 = "text4";
    float passedTime = 0; // time passed before an interaction with the screen
    Typeface font; // the text font

    public InfoScreen(GameEngine gameEngine)
    {
        super(gameEngine);  // call the constructor from Screen
        // load the resources
        background = gameEngine.loadBitmap("spacewar/images/ui/2/window_320.png");
        close = gameEngine.loadBitmap("spacewar/images/ui/btn_close.png");
        healthItemImage = gameEngine.loadBitmap("spacewar/images/items/item-health30.png");
        bulletItemImage = gameEngine.loadBitmap("spacewar/images/items/item-bullet30.png");
        shieldItemImage = gameEngine.loadBitmap("spacewar/images/items/item-shield30.png");
        enemy1Image = gameEngine.loadBitmap("spacewar/images/vehicles/1/enemy1-30.png");
        enemy2Image = gameEngine.loadBitmap("spacewar/images/vehicles/1/enemy2-30.png");
        enemy3Image = gameEngine.loadBitmap("spacewar/images/vehicles/1/enemy3-30.png");
    }

    @Override
    public void update(float deltaTime)
    {
        // draw images and text
        gameEngine.drawBitmap(background, 0, 55);
        gameEngine.drawBitmap(close, 320/2 - close.getWidth()/2, 480/2 - close.getHeight()/2 + 130);
        title = "Guidelines" ;
        gameEngine.drawText(font, title, 90, 90, Color.WHITE, 30);
        text1 = "Stay alive and shoot enemies to" ;
        gameEngine.drawText(font, text1, 20, 130, Color.WHITE, 18);
        text2 = "get a higher score" ;
        gameEngine.drawText(font, text2, 20, 150, Color.WHITE, 18);
        text3 = "Pick Up power ups:" ;
        gameEngine.drawText(font, text3, 20, 200, Color.WHITE, 18);
        gameEngine.drawBitmap(healthItemImage, 80, 210);
        gameEngine.drawBitmap(bulletItemImage, 140, 210);
        gameEngine.drawBitmap(shieldItemImage, 200, 210);
        text4 = "Avoid enemies:" ;
        gameEngine.drawText(font, text4, 20, 280, Color.WHITE, 18);
        gameEngine.drawBitmap(enemy1Image, 80, 290);
        gameEngine.drawBitmap(enemy2Image, 140, 295);
        gameEngine.drawBitmap(enemy3Image, 200, 295);
        passedTime = passedTime + deltaTime; // add time between updates

        // 123,4
        // 0,4 > 0,5  false
        // 123,8
        // 0,8 > 0,5  true
        if (gameEngine.isTouchDown(0) && (passedTime) > 0.5f) // wait half a second after the info menu shows, then interact
        {
            // check if x button from info screen is pressed
            if (gameEngine.getTouchY(0) > 370 - 30 && gameEngine.getTouchY(0) < 370 + 30
                    && gameEngine.getTouchX(0) > 160 - 30 && gameEngine.getTouchX(0) < 160 + 30)
            {
                gameEngine.setScreen(new MainMenuScreen(gameEngine)); // change to main menu screen
            }
        }
    }
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void dispose() {}
}
