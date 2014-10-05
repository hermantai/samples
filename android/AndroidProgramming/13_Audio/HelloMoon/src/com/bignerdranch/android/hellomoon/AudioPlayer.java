package com.bignerdranch.android.hellomoon;

import android.content.Context;
import android.media.MediaPlayer;

public class AudioPlayer {

    private MediaPlayer mPlayer;
    
    public void stop() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    public void play(Context c) {
        
        stop();

        mPlayer = MediaPlayer.create(c, R.raw.one_small_step);
        
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                stop();
            }
        });

        mPlayer.start();
    }
    
    public boolean isPlaying() {
        return mPlayer != null;
    }
    
}
