package com.example.spacewar;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import java.io.IOException;

public class Music implements MediaPlayer.OnCompletionListener
{

    private MediaPlayer mediaPlayer;  // MediaPlayer doing the music playback
    private boolean isPrepared = false; // is the Mediaplayer ready?

    public Music(AssetFileDescriptor assetFileDescriptor)
    {
        mediaPlayer = new MediaPlayer();
        try
        {
            mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(),
                    assetFileDescriptor.getStartOffset(),
                    assetFileDescriptor.getLength());
            mediaPlayer.prepare();
            isPrepared = true;
            mediaPlayer.setOnCompletionListener(this);
        }
        catch(IOException e)
        {
            throw new RuntimeException("Could not open the music fileDescriptor: " + assetFileDescriptor);
        }
    }

    public void dispose()
    {
        if(mediaPlayer.isPlaying())
        {
            mediaPlayer.start();
            //mediaPlayer.stop();
        }
        mediaPlayer.release();
    }

    public boolean isLooping()
    {
        return mediaPlayer.isLooping();
    }

    public boolean isPlaying()
    {
        return mediaPlayer.isPlaying();
    }

    public boolean isStopped()
    {
        return !isPrepared;
    }

    public void pause()
    {
        if(mediaPlayer.isPlaying()) mediaPlayer.pause();
    }

    public void play()
    {
        if(mediaPlayer.isPlaying()) return;
        try
        {
            synchronized (this)
            {
                if (!isPrepared) mediaPlayer.prepare();
                mediaPlayer.start();
            }
        }
        catch(IllegalStateException e)
        {
            e.printStackTrace();
            throw new RuntimeException("Music class: You are trying to play from a wrong MediaPlayer!");
        }
        catch(IOException e)
        {
            e.printStackTrace();
            throw new RuntimeException("MediaPlayer play error.");
        }
    }

    public void setLooping(boolean loop)
    {
        mediaPlayer.setLooping(loop);
    }

    public void setVolume(float volume)
    {
        mediaPlayer.setVolume(volume, volume);
    }

    public void stop()
    {
        synchronized (this)
        {
            if(!isPrepared) return;
            mediaPlayer.stop();
            isPrepared = false;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp)
    {
        synchronized (this)
        {
            isPrepared = false;
        }
    }

}
