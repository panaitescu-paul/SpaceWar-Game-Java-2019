package com.example.spacewar.SpaceWar;

import com.example.spacewar.GameEngine;
import com.example.spacewar.Screen;

public class SpaceWar extends GameEngine
{
    @Override
    public Screen createStartScreen()
    {
        music = this.loadMusic("spacewar/music/bg_music-2.wav");
        return new MainMenuScreen(this);
    }

    public void onResume()
    {
        super.onResume();
        music.play();
    }

    public void onPause()
    {
        super.onPause();;
        music.pause();
    }

}
