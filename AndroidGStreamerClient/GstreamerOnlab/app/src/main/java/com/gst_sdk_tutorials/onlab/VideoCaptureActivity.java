package com.gst_sdk_tutorials.onlab;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


public class VideoCaptureActivity extends Activity {


    protected boolean touch = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Log.e("Most fut a 2. ", "activity onCreate-je omnom");

        setContentView(R.layout.main);

        /*Intent intent=new Intent(getBaseContext(),UdpServer.class);
        startService(intent);*/

        LinearLayout ll = (LinearLayout) findViewById(R.id.touchLin);
        ll.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (touch) {
                    LinearLayout ll = (LinearLayout) findViewById(R.id.touchLin);
                    ll.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                    touch = false;
                }
                if (!touch) {
                    touch = true;
                }

            }
        });


        ///Remélem ez lefut most
        FragmentManager fm = getFragmentManager();
        final VideoCaptureFragment1 fragment1 = (VideoCaptureFragment1) fm.findFragmentById(R.id.frag1);
        final VideoCaptureFragment2 fragment2 = (VideoCaptureFragment2) fm.findFragmentById(R.id.frag2);
        fragment1.letsPlay();
        fragment2.letsPlay();


        /*ImageButton play = (ImageButton) findViewById(R.id.button_p);
        play.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                final VideoCaptureFragment1 fragment1 = (VideoCaptureFragment1) fm.findFragmentById(R.id.frag1);
                final VideoCaptureFragment2 fragment2 = (VideoCaptureFragment2) fm.findFragmentById(R.id.frag2);
                fragment1.letsPlay();
                fragment2.letsPlay();
            }
        });*/

    }

    protected void onDestroy() {
        // nativeFinalize();
        super.onDestroy();
    }

    // Called from native code. This sets the content of the TextView from the UI thread.
    private void setMessage(final String message) {
        final TextView tv = (TextView) this.findViewById(R.id.textview_message);
        runOnUiThread(new Runnable() {
            public void run() {
                tv.setText(message);
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.e("START onPasue", "");
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getFragmentManager();
        final VideoCaptureFragment1 fragment1 = (VideoCaptureFragment1) fm.findFragmentById(R.id.frag1);
        final VideoCaptureFragment2 fragment2 = (VideoCaptureFragment2) fm.findFragmentById(R.id.frag2);
        fragment1.nativePause();
        fragment2.nativePause();
        fragment1.nativeFinalize();
        fragment2.nativeFinalize();
        super.onBackPressed();

    }
}
