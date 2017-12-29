package com.versatile.fastclas.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.versatile.fastclas.adapters.SessionsInnerAdapter;
import com.versatile.fastclas.utils.Constants;
import com.versatilemobitech.fastclas.R;

public class YoutubeVideoPlayer extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, YouTubePlayer.PlayerStateChangeListener {
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    YouTubePlayerView youtubePlayer;
    String video_id,from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_video_player);
        youtubePlayer = (YouTubePlayerView) findViewById(R.id.youtubePlayer);

        Intent intent = getIntent();
        if (intent.hasExtra("video_id_1")) {
            video_id = intent.getStringExtra("video_id_1");
            from = "first";
        }else if(intent.hasExtra("video_id_2")){
            video_id = intent.getStringExtra("video_id_2");
            from = "second";
        } else {
            video_id = "vdNCQ6yg9h4";

        }
        youtubePlayer.initialize(Constants.YOUTUBE_API_KEY, this);


    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        if (!wasRestored) {

            // loadVideo() will auto play video
            // Use cueVideo() method, if you don't want to play it automatically
//            youTubePlayer.loadVideo("HWrNzUCjbkk",20000);
            youTubePlayer.loadVideo(video_id);

            // Hiding player controls
            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
            youTubePlayer.setPlayerStateChangeListener(this);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(
                    getString(R.string.error_player), errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        youtubePlayer.getM
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
        if(from.equals("first")) {
            Intent intent = new Intent(this, SessionsInnerActivity.class);
            intent.putExtra("completed_1", "true");
            setResult(2,intent);
            finish();
        }else if(from.equals("second")){
            Intent intent = new Intent(this, SessionsInnerActivity.class);
            intent.putExtra("completed_2", "true");
            setResult(4,intent);
            finish();
        }

    }

    @Override
    public void onError(YouTubePlayer.ErrorReason errorReason) {
        this.finish();
    }
}
