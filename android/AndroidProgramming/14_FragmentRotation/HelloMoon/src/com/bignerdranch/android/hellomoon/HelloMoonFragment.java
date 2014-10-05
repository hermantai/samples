package com.bignerdranch.android.hellomoon;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class HelloMoonFragment extends Fragment {
    private AudioPlayer mPlayer = new AudioPlayer();
    
    private Button mPlayButton;
    private Button mStopButton;
      
    void updateButtons() {
        boolean isEnabled = !mPlayer.isPlaying();
        mPlayButton.setEnabled(isEnabled);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlayer.stop();
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_hello_moon, parent, false);

        mPlayButton = (Button)v.findViewById(R.id.hellomoon_playButton);
        mStopButton = (Button)v.findViewById(R.id.hellomoon_stopButton);
        
        updateButtons();
        
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mPlayer.play(getActivity());
                updateButtons();
            }
        });
        
        mStopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {                
                mPlayer.stop();
                updateButtons();
            }
        });

        return v;
    }
}
