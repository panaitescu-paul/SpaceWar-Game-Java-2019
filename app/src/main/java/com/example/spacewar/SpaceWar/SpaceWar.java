package com.example.spacewar.SpaceWar;

import com.example.spacewar.GameEngine;
import com.example.spacewar.Screen;

public class SpaceWar extends GameEngine
{
    @Override
    public Screen createStartScreen()
    {
        // load background music
        music = this.loadMusic("spacewar/music/bg_music-2.ogg");
        return new MainMenuScreen(this); // return the main menu screen
    }

    public void onResume()
    {
        super.onResume(); // call onResume() method from GameEngine
        music.setLooping(true); // replay music after ending
        music.play(); // play music
    }

    public void onPause()
    {
        super.onPause(); // call onPause() method from GameEngine
        music.pause(); // pause music
    }
}
