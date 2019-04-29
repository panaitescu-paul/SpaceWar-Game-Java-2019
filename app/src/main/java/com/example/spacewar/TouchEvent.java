package com.example.spacewar;

public class TouchEvent
{
    public enum TouchEventType
    {
        Down,
        Up,
        Dragged
    }

    public TouchEventType type; // the type of the event
    public int x;               // the x-coordinate of the event
    public int y;               // the y-coordinate of the event
    public int pointer;         // the pointer id (from the Android system)
}
