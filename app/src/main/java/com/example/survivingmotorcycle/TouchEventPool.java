package com.example.survivingmotorcycle;

public class TouchEventPool extends Pool<TouchEvent>
{

    @Override
    protected TouchEvent newItem()
    {
        return new TouchEvent();
    }
}
