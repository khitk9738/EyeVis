package org.tensorflow.demo;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

public class Splash extends Activity {
    VideoView videoView;

    @Override
    protected void onCreate(Bundle sa){
        super.onCreate(sa);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        try{
            videoView = new VideoView(this);
            setContentView(videoView);
            Uri path = Uri.parse( "android.resource://"+getPackageName()+"/"+ +R.raw.vid1);
            videoView.setVideoURI(path);

            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    jump();
                }


            });
            videoView.start();
        }catch (Exception e){
            jump();
        }



    }
    private void jump() {

        if(isFinishing())
            return;
        startActivity(new Intent(this,DetectorActivity.class));
        finish();
    }


    /*@Override
    protected void onPause() {
        super.onPause();
        if (videoView.isPlaying()) {
            videoView.stopPlayback();
        }
    }*/

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent i=new Intent(Splash.this,DetectorActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }



}

