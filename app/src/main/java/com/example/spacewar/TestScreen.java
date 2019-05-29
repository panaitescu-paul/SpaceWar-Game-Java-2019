package com.example.spacewar;

import android.graphics.Bitmap;
import android.graphics.Color;

public class TestScreen extends Screen
{
    float x = 0;
    float y = 240;
    Bitmap bitmap;
    Sound sound;
    Music backgroundMusic;
    boolean isPlaying = false;

    public TestScreen(GameEngine gameEngine)
    {
        super(gameEngine);
        bitmap = gameEngine.loadBitmap("breakout/bob.png");
        sound = gameEngine.loadSound("breakout/blocksplosion.wav");
        backgroundMusic = gameEngine.loadMusic("breakout/music.ogg");
        isPlaying = true;
    }

    @Override
    public void update(float deltaTime)
    {
        //Log.d("TestScreen", "FPS: " + gameEngine.getFramesPerSecond());
        gameEngine.clearFrameBuffer(Color.GREEN);

        x = x + 50 * deltaTime;
        if (x > 320 + bitmap.getWidth()) x = 0 - bitmap.getWidth();

        if(gameEngine.isTouchDown(0))
        {
            x = gameEngine.getTouchX(0);
            y = gameEngine.getTouchY(0);
            sound.play(1);

            if(backgroundMusic.isPlaying())
            {
                backgroundMusic.pause();
                isPlaying = false;
            }
            else
            {
                backgroundMusic.play();
                isPlaying = true;
            }
        }

        /*float x = gameEngine.getAccelerometer()[0];
        float y = -1 * gameEngine.getAccelerometer()[1];
        x = gameEngine.getFrameBufferWidth()/2 + (x/10 * gameEngine.getFrameBufferWidth()/2);
        y = gameEngine.getFrameBufferHeight()/2 + (y/10 * gameEngine.getFrameBufferHeight()/2);*/

        gameEngine.drawBitmap(bitmap, (int)x-64, (int)y-64);
        //gameEngine.drawBitmap(bitmap, 200, 300, 64, 64, 64, 64);
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
