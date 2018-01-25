package com.versatile.fastclas.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.versatile.fastclas.adapters.SessionsInnerAdapter;
import com.versatile.fastclas.utils.Constants;
import com.versatilemobitech.fastclas.R;

public class YoutubeVideoPlayer extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, YouTubePlayer.PlayerStateChangeListener,
        View.OnClickListener {
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    YouTubePlayerView youtubePlayer;
    String video_id, from;
    private YouTubePlayer mPlayer;
    private View mPlayButtonLayout;
    private TextView mPlayTimeTextView;

    private Handler mHandler = null;
    private SeekBar mSeekBar;
    ImageView imgPause;
    ImageButton play_video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_video_player);
        youtubePlayer = (YouTubePlayerView) findViewById(R.id.youtubePlayer);
        imgPause = (ImageView) findViewById(R.id.imgPause);

        Intent intent = getIntent();
        if (intent.hasExtra("video_id_1")) {
            video_id = intent.getStringExtra("video_id_1");
            from = "first";
        } else if (intent.hasExtra("video_id_2")) {
            video_id = intent.getStringExtra("video_id_2");
            from = "second";
        }
        youtubePlayer.initialize(Constants.YOUTUBE_API_KEY, this);

        //Add play button to explicitly play video in YouTubePlayerView
        mPlayButtonLayout = findViewById(R.id.video_control);
        play_video = (ImageButton) findViewById(R.id.play_video);
        play_video.setOnClickListener(this);
        findViewById(R.id.pause_video).setOnClickListener(this);
        imgPause.setOnClickListener(this);

        mPlayTimeTextView = (TextView) findViewById(R.id.play_time);
        mSeekBar = (SeekBar) findViewById(R.id.video_seekbar);
        mSeekBar.setOnSeekBarChangeListener(mVideoSeekBarChangeListener);

        mHandler = new Handler();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
//        if (!wasRestored) { imp
            // loadVideo() will auto play video
            // Use cueVideo() method, if you don't want to play it automatically
//            youTubePlayer.loadVideo("HWrNzUCjbkk",20000);
//            youTubePlayer.loadVideo(video_id);imp

            // Hiding player controls
//            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);imp
//            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);imp
//            youTubePlayer.setPlayerStateChangeListener(this);imp
//        }imp

        if (null == youTubePlayer) return;
        mPlayer = youTubePlayer;

        displayCurrentTime();

        // Start buffering
        if (!wasRestored) {
            youTubePlayer.loadVideo(video_id);
        }

        youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
        mPlayButtonLayout.setVisibility(View.VISIBLE);

        // Add listeners to YouTubePlayer instance
//        youTubePlayer.setPlayerStateChangeListener(mPlayerStateChangeListener);
        youTubePlayer.setPlaybackEventListener(mPlaybackEventListener);
        youTubePlayer.setPlayerStateChangeListener(this);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(getString(R.string.error_player), errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    YouTubePlayer.PlaybackEventListener mPlaybackEventListener = new YouTubePlayer.PlaybackEventListener() {
        @Override
        public void onBuffering(boolean arg0) {
        }

        @Override
        public void onPaused() {
            mHandler.removeCallbacks(runnable);
        }

        @Override
        public void onPlaying() {
            mHandler.postDelayed(runnable, 10);
            displayCurrentTime();
        }

        @Override
        public void onSeekTo(int arg0) {
            mHandler.postDelayed(runnable, 100);
        }

        @Override
        public void onStopped() {
            mHandler.removeCallbacks(runnable);
        }
    };

    SeekBar.OnSeekBarChangeListener mVideoSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            long lengthPlayed = (mPlayer.getDurationMillis() * progress) / 100;
            mPlayer.seekToMillis((int) lengthPlayed);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, SessionsInnerActivity.class);
        intent.putExtra("isCompleted", "false");
        intent.putExtra("from", from);
        setResult(2, intent);
        finish();
    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onLoaded(String s) {

    }

    @Override
    public void onAdStarted() {

    }

    @Override
    public void onVideoStarted() {

    }

    @Override
    public void onVideoEnded() {
        if (from.equals("first")) {
            Intent intent = new Intent(this, SessionsInnerActivity.class);
            intent.putExtra("isCompleted", "true");
            intent.putExtra("from", from);
            setResult(2, intent);
            finish();
        } else if (from.equals("second")) {
            Intent intent = new Intent(this, SessionsInnerActivity.class);
            intent.putExtra("isCompleted", "true");
            intent.putExtra("from", from);
            setResult(2, intent);
            finish();
        }

    }

    @Override
    public void onError(YouTubePlayer.ErrorReason errorReason) {
        Intent intent = new Intent(this, SessionsInnerActivity.class);
        intent.putExtra("isCompleted", "false");
        intent.putExtra("from", from);
        setResult(2, intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_video:
                if (null != mPlayer && !mPlayer.isPlaying())
                    mPlayer.play();
                imgPause.setVisibility(View.GONE);
                break;
            case R.id.pause_video:
                if (null != mPlayer && mPlayer.isPlaying())
                    mPlayer.pause();
                imgPause.setVisibility(View.VISIBLE);
                break;
            case R.id.imgPause:
                if (null != mPlayer && !mPlayer.isPlaying())
                    mPlayer.play();
                imgPause.setVisibility(View.GONE);
                break;

        }
    }

    private void displayCurrentTime() {
        if (null == mPlayer) return;

        float percentagePlayed = ((float) mPlayer.getCurrentTimeMillis() / (float) mPlayer.getDurationMillis()) * 100;
        mSeekBar.setProgress((int) percentagePlayed);

        String formattedTime = formatTime(mPlayer.getDurationMillis() - mPlayer.getCurrentTimeMillis());
        mPlayTimeTextView.setText(formattedTime);
    }

    private String formatTime(int millis) {
        int seconds = millis / 1000;
        int minutes = seconds / 60;
        int hours = minutes / 60;

        return (hours == 0 ? "--:" : hours + ":") + String.format("%02d:%02d", minutes % 60, seconds % 60);
    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            displayCurrentTime();
            mHandler.postDelayed(this, 100);
        }
    };
}
