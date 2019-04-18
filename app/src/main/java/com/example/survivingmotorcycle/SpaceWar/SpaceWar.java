package com.example.survivingmotorcycle.SpaceWar;

import com.example.survivingmotorcycle.GameEngine;
import com.example.survivingmotorcycle.Screen;

public class SpaceWar extends GameEngine
{
    @Override
    public Screen createStartScreen()
    {
        music = this.loadMusic("survivingmotorcycle/music/bg_music.mp3");
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
