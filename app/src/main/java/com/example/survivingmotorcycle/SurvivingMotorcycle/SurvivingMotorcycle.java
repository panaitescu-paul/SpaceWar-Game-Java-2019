package com.example.survivingmotorcycle.SurvivingMotorcycle;

import com.example.survivingmotorcycle.GameEngine;
import com.example.survivingmotorcycle.Screen;

public class SurvivingMotorcycle extends GameEngine
{
    @Override
    public Screen createStartScreen()
    {
        //music = this.loadMusic("carscroller/music.ogg");
        return new MainMenuScreen(this);
    }

    public void onResume()
    {
        super.onResume();
        //music.play();
    }

    public void onPause()
    {
        super.onPause();;
        //music.pause();
    }

}
