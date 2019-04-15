package com.example.survivingmotorcycle;

public class TestGame extends GameEngine
{
    @Override
    public Screen createStartScreen()
    {
        return new TestScreen(this);
    }

}
