package com.example.spacewar;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public abstract class GameEngine extends Activity implements Runnable, TouchHandler, SensorEventListener
{
    private Thread mainLoopThread;
    private State state = State.Paused;
    private List<State> stateChanges = new ArrayList<>();
    private SurfaceView surfaceView; // takes care of placing the surface at the correct location on the screen
    private SurfaceHolder surfaceHolder; // control the surface size and format, edit the pixels in the surface, and monitor changes to the surface
    private Canvas canvas = null; // for drawing
    private Screen screen = null;
    private Bitmap offscreenSurface;
    private MultiTouchHandler touchHandler;
    private TouchEventPool touchEventPool = new TouchEventPool();
    private List<TouchEvent> touchEventsBuffer = new ArrayList<>();
    private List<TouchEvent> touchEventsCopied = new ArrayList<>();
    private float[] accelerometer = new float[3];
    private SoundPool soundPool = new SoundPool.Builder().setMaxStreams(20).build();
    private int framesPerSecond = 0;
    long currentTime = 0;
    long lastTime = 0;
    Paint paint = new Paint();
    public Music music;

    public abstract Screen createStartScreen();

    public void setScreen(Screen screen)
    {
        if (this.screen != null)  this.screen.dispose();
        this.screen = screen;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        surfaceView = new SurfaceView(this);
        setContentView(surfaceView);
        surfaceHolder = surfaceView.getHolder();
        screen = createStartScreen();

        touchHandler = new MultiTouchHandler(surfaceView, touchEventsBuffer, touchEventPool);
        SensorManager manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if(manager.getSensorList(Sensor.TYPE_ACCELEROMETER).size()!=0)
        {
            Sensor accelerometer = manager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
            manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        }
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        //soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);

    }

    public void setOffscreenSurface(int width, int height)
    {
        if(offscreenSurface != null) offscreenSurface.recycle();
        offscreenSurface = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        canvas = new Canvas(offscreenSurface); // global canvas for the logical screen
    }

    public int getFrameBufferWidth()
    {
        return offscreenSurface.getWidth();
    }

    public int getFrameBufferHeight()
    {
        return offscreenSurface.getHeight();
    }

    public Bitmap loadBitmap(String fileName) // open and read from file
    {
        InputStream in = null;
        Bitmap bitmap = null;
        try
        {
            in = getAssets().open(fileName);
            bitmap = BitmapFactory.decodeStream(in);
            if (bitmap == null)
            {
                throw new RuntimeException("Could not load bitmap from file " + fileName + " Crap!");
            }
            return bitmap;
        }
        catch (IOException ioe)
        {
            throw new RuntimeException("Could not load bitmap from assets " + fileName);
        }
        finally
        {
            if(in != null)
            {
                try
                {
                    in.close();
                }
                catch(IOException ioe)
                {
                    throw new RuntimeException("Could not close the file");
                }
            }
        }
    }

    public void clearFrameBuffer(int color)
    {
        canvas.drawColor(color);
    }

    public void drawBitmap(Bitmap bitmap, int x, int y)
    {
        if (canvas != null) canvas.drawBitmap(bitmap, x, y, null);
    }

    Rect src = new Rect();
    Rect dst = new Rect();
    public void drawBitmap(Bitmap bitmap, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight)
    {
        if(canvas == null) return;

        src.left = srcX;
        src.top = srcY;
        src.right = srcX + srcWidth;
        src.bottom = srcY + srcHeight;

        dst.left = x;
        dst.top = y;
        dst.right = x + srcWidth;
        dst.bottom = y + srcHeight;

        canvas.drawBitmap(bitmap, src, dst, null);
    }

    public Typeface loadFont(String fileName)
    {
        Typeface font = Typeface.createFromAsset(getAssets(), fileName);
        if(font == null)
        {
            throw new RuntimeException("Oh shit, I could not load the font from file: " + fileName);
        }
        return font;
    }

    public void drawText(Typeface font, String text, int x, int y, int colour, int size)
    {
        paint.setTypeface(font);
        paint.setTextSize(size);
        paint.setColor(colour);
        canvas.drawText(text, x, y, paint);
    }

    public Sound loadSound(String fileName)
    {
        try
        {
            AssetFileDescriptor assetFileDescriptor = getAssets().openFd(fileName);
            if(soundPool == null) throw new RuntimeException("Soundpool object except!");
            int soundId = soundPool.load(assetFileDescriptor, 0); // soundPool is for keeping track of loaded audio files in his memory
            return new Sound(soundPool, soundId);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Could not load sound file: " + fileName);
        }
    }

    public Music loadMusic(String fileName)
    {
        try
        {
            AssetFileDescriptor assetFileDescriptor = getAssets().openFd(fileName);
            return new Music(assetFileDescriptor);
        }
        catch (IOException e)
        {
            throw new RuntimeException("GameEngine: Could not load music file: " + fileName);
        }
    }

    public boolean isTouchDown(int pointer)
    {
        return touchHandler.isTouchDown(pointer);
    }

    public List<TouchEvent> getTouchEvents()
    {
        return touchEventsCopied;
    }

    public int getTouchX(int pointer)
    {
        int scaledX = 0;
        scaledX = (int) ((float)touchHandler.getTouchX(pointer) * (float)offscreenSurface.getWidth() / (float)surfaceView.getWidth());
        return scaledX;
    }

    public int getTouchY(int pointer)
    {
        int scaledY = 0;
        scaledY = (int) ((float)touchHandler.getTouchY(pointer) * (float)offscreenSurface.getHeight() / (float)surfaceView.getHeight());
        return scaledY;
    }

    public float[] getAccelerometer()
    {
        return accelerometer;
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }

    public void onSensorChanged(SensorEvent event)
    {
        System.arraycopy(event.values, 0, accelerometer, 0, 3);
    }

    private void fillEvents()
    {
        synchronized (touchEventsBuffer)
        {
            int stop = touchEventsBuffer.size();
            for(int i=0; i<stop; i++)
            {
                touchEventsCopied.add(touchEventsBuffer.get(i));
            }
            touchEventsBuffer.clear();
        }
    }

    private void freeEvents()
    {
        synchronized (touchEventsCopied)
        {
            int stop = touchEventsCopied.size();
            for(int i=0; i<stop; i++)
            {
                touchEventPool.free(touchEventsCopied.get(i));
            }
            touchEventsCopied.clear();
        }
    }

    public int getFramesPerSecond()
    {
        return framesPerSecond;
    }

    public void run()
    {
        int frames = 0;
        long startTime = System.nanoTime();

        while(true)
        {
            synchronized (stateChanges)
            {
                for(int i=0; i < stateChanges.size(); i++)
                {
                    state = stateChanges.get(i);
                    if(state == State.Disposed)
                    {
                        Log.d("GameEngine", "State changed to Disposed");
                        return;
                    }
                    if(state == State.Paused)
                    {
                        Log.d("GameEngine", "State changed to Paused");
                        return;
                    }
                    if(state == State.Resumed)
                    {
                        Log.d("GameEngine", "State changed to Resumed");
                        state = State.Running;
                    }

                } // end of for loop
                stateChanges.clear();

                if(state == State.Running)
                {

                    if (!surfaceHolder.getSurface().isValid())
                    {
                        continue;
                    }
                    Canvas canvas = surfaceHolder.lockCanvas(); // local canvas for physical screen
                    // all the drawing code should happen here
                    // canvas.drawColor(Color.BLUE);
                    fillEvents();
                    currentTime = System.nanoTime();
                    if(screen != null) screen.update((currentTime - lastTime)/1000000000.0f);
                    lastTime = currentTime;
                    freeEvents();
                    src.left = 0;
                    src.top = 0;
                    src.right = offscreenSurface.getWidth(); // or maybe with minus 1
                    src.bottom = offscreenSurface.getHeight();
                    dst.left = 0;
                    dst.top = 0;
                    dst.right = surfaceView.getWidth();
                    dst.bottom = surfaceView.getHeight();
                    canvas.drawBitmap(offscreenSurface, src, dst, null);
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
                /*frames++; // I have just drawn a new frame
                if(System.nanoTime() - startTime > 1000000000)
                {
                    framesPerSecond = frames;
                    frames = 0;
                    startTime = System.nanoTime();
                }*/
            } // end of synchronize
        }// end of while(true) loop
    }

    public void onPause()
    {
        super.onPause();
        synchronized (stateChanges)
        {
            if (isFinishing())  // Check to see whether this activity is in the process of finishing, either because you called finish() on it or someone else has requested that it finished.
            {
                stateChanges.add(stateChanges.size(), State.Disposed);
            }
            else
            {
                stateChanges.add(stateChanges.size(), State.Paused);
            }
        }
        if(isFinishing())
        {
            ((SensorManager)getSystemService(Context.SENSOR_SERVICE)).unregisterListener(this);
            soundPool.release();
        }
    }

    public void onResume()
    {
        super.onResume();
        if(surfaceView.getWidth() > surfaceView.getHeight())
        {
            setOffscreenSurface(480, 320);
        }
        else
        {
            setOffscreenSurface(320, 480);
        }
//        setOffscreenSurface(480, 320); // landscape screen
        setOffscreenSurface(320, 480); // portrait screen

        mainLoopThread = new Thread(this);
        mainLoopThread.start();
        synchronized (stateChanges)
        {
            stateChanges.add(stateChanges.size(), State.Resumed);
        }
    }

}
