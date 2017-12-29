package com.versatile.fastclas.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.versatile.fastclas.utils.Constants;
import com.versatilemobitech.fastclas.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by USER on 28-12-2017.
 */

public class YouTubeParser extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, YouTubePlayer.PlayerStateChangeListener, YouTubePlayer.OnFullscreenListener {

    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private String youtubeUrl = "HWrNzUCjbkk";
    private com.google.android.youtube.player.YouTubePlayerView youtubePlayer;
    private YouTubePlayer player;
    private Context mContext;
    private boolean fullscreen;

    private static final int PORTRAIT_ORIENTATION = Build.VERSION.SDK_INT < 9
            ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            : ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;

//    public YouTubeParser(View v, Context context, Post post, String lastUrl) {
//        this.mContext = context;
//        youtubeUrl = post.getYoutubeUrl();
//        TextView username = (TextView) v.findViewById(R.id.youtube_username_tv);
//        TextView when = (TextView) v.findViewById(R.id.youtube_when_tv);
//        TextView content = (TextView) v.findViewById(R.id.youtube_content_tv);
//
//
//        if (username != null)
//            username.setText(post.getUsername());
//        if (when != null)
//            when.setText(post.getWhen());
//        if (content != null) {
//            content.setText(Html.fromHtml(post.getContent()));
//            content.setMovementMethod(LinkMovementMethod.getInstance());
//        }
//        if (lastUrl != youtubeUrl) {
//            youtube = new com.google.android.youtube.player.YouTubePlayerView(context);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) LinearLayout.LayoutParams.MATCH_PARENT, (int) LinearLayout.LayoutParams.WRAP_CONTENT);
//            youtube.setLayoutParams(params);
//
//            LinearLayout ll = (LinearLayout) v.findViewById(R.id.youtube_ll);
//            if (ll.getChildCount() == 4)
//                ll.removeViewAt(2);
//            ll.addView(youtube, 2);
//        }
//        if (youtube != null) {
//            youtube.initialize(Constants.YOUTUBE_API_KEY, this);
//        }
//    }


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_youtube_video_player);

        youtubePlayer = (YouTubePlayerView)findViewById(R.id.youtubePlayer);
        youtubePlayer.initialize(Constants.YOUTUBE_API_KEY, this);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(getString(0), errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(Constants.YOUTUBE_API_KEY, this);
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        this.player = player;
        player.setPlayerStateChangeListener(this);
        if (!wasRestored) {
            player.cueVideo(youtubeUrl);
            Log.i("Position", "video cued: " + youtubeUrl);
        }
    }


    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return youtubePlayer;
    }

    public void setNoLandscape() {
        if (player != null) {
            int controlFlags = player.getFullscreenControlFlags();
            controlFlags &= ~YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE;
            player.setFullscreenControlFlags(controlFlags);
            if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                player.pause();
        }
    }

    public void setToLandscape() {
        if (player != null) {
            int controlFlags = player.getFullscreenControlFlags();
            controlFlags |= YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE;
            player.setFullscreenControlFlags(controlFlags);
        }
    }

    @Override
    public void onAdStarted() {
    }


    @Override
    public void onLoaded(String arg0) {
    }

    @Override
    public void onLoading() {
    }

    @Override
    public void onVideoEnded() {
        setNoLandscape();
    }

    @Override
    public void onError(YouTubePlayer.ErrorReason errorReason) {

    }

    @Override
    public void onVideoStarted() {
        int controlFlags = player.getFullscreenControlFlags();
        controlFlags |= YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE;
        player.setFullscreenControlFlags(controlFlags);
    }

    @Override
    public void onFullscreen(boolean isFullscreen) {
        fullscreen = isFullscreen;
        doLayout();

    }

    private void doLayout() {
        LinearLayout.LayoutParams playerParams =
                (LinearLayout.LayoutParams) youtubePlayer.getLayoutParams();
        if (fullscreen) {
            // When in fullscreen, the visibility of all other views than the player should be set to
            // GONE and the player should be laid out across the whole screen.
            playerParams.width =  MATCH_PARENT;
            playerParams.height =  MATCH_PARENT;

        } else {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                playerParams.width = 0;
                playerParams.height = WRAP_CONTENT;
                playerParams.weight = 1;
            } else {
                playerParams.width = MATCH_PARENT;
                playerParams.height = WRAP_CONTENT;
                playerParams.weight = 0;
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        doLayout();
    }


}