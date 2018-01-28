package org.tensorflow.demo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    SurfaceView cameraView;
    StringBuilder stringBuilder;
    TextView textView;
    CameraSource cameraSource;
    final int RequestCameraPermissionID = 1001;
    boolean flag=false;
    boolean cnt=false;
    TextToSpeech t1;
    boolean times=false;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    try {
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
            break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flag=false;
        cnt=false;
        times=false;
        cameraView = (SurfaceView) findViewById(R.id.surface_view);
        textView = (TextView) findViewById(R.id.text_view);

        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if (!textRecognizer.isOperational()) {
            Log.w("MainActivity", "Detector dependencies are not yet available");
        } else {

            cameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setRequestedFps(2.0f)
                    .setAutoFocusEnabled(true)
                    .build();
            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder) {

                    try {
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    RequestCameraPermissionID);
                            return;
                        }
                        if(!flag){
                            cameraSource.start(cameraView.getHolder());
                            flag=true;
                        }
                        else{
                            cameraSource.stop();
                        }

                        ////////////////////////////////////////////////////////////
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                    cameraSource.stop();
                }
            });

            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {

                }

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                   // Toast.makeText(MainActivity.this,"1",Toast.LENGTH_SHORT).show();
                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if(items.size() != 0)
                    {
                        textView.post(new Runnable() {
                            @Override
                            public void run() {
                                stringBuilder = new StringBuilder();
                                for(int i =0;i<items.size();++i)
                                {
                                    TextBlock item = items.valueAt(i);
                                    stringBuilder.append(item.getValue());
                                    stringBuilder.append("\n");
                                }
                                textView.setText(stringBuilder.toString());
                            }
                        });
                    }
                   // Toast.makeText(MainActivity.this,"2",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    int press_cnt=0;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // Your code here
        press_cnt++;
        if(press_cnt==1)
        {
            cameraSource.stop();
            t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if(status != TextToSpeech.ERROR) {
                        t1.setLanguage(Locale.UK);
                    }
                }
            });
            t1.setSpeechRate(2.00f);
            if((stringBuilder==null))
            {
                press_cnt=0;
                t1.stop();
                t1.shutdown();
                startActivity((new Intent(MainActivity.this,MainActivity.class)).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();

            }
        }
        else if(press_cnt<5)
        {
            if(press_cnt!=2)
            t1.speak(stringBuilder.toString(),TextToSpeech.QUEUE_FLUSH,null,null);
            Toast.makeText(MainActivity.this,stringBuilder.toString(),Toast.LENGTH_SHORT).show();

        }
        else
        {
            press_cnt=0;
            t1.stop();
            t1.shutdown();
            startActivity((new Intent(MainActivity.this,MainActivity.class)).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
           // cameraSource.start(cameraView.getHolder());
            //flag=true;

        }
        /*if(!cnt  && !times){
            cameraSource.stop();
            cnt=true;
            times=false;


            Toast.makeText(Ma)
        }

        else if(cnt && !times)
        {
            times=true;
            cnt=true;
            t1.speak(stringBuilder.toString(),TextToSpeech.QUEUE_ADD,null,null);
            Toast.makeText(MainActivity.this,stringBuilder.toString(),Toast.LENGTH_SHORT).show();
        }
        else {
            cnt=false;
            times=false;
            t1.stop();
            startActivity(new Intent(MainActivity.this,MainActivity.class));
        }*/
        return super.dispatchTouchEvent(ev);
    }
    @Override
    public void onBackPressed() {
        if(t1!=null) {
            t1.stop();
            t1.shutdown();
        }
        Intent intent = new Intent(MainActivity.this, DetectorActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        if(t1!=null) {
            t1.stop();
            t1.shutdown();

        }
        super.onDestroy();
    }
    protected void onRestart() {
        super.onRestart();
        Intent i=new Intent(MainActivity.this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

}
