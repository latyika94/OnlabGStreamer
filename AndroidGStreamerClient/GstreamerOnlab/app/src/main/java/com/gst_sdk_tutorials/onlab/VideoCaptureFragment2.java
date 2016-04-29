package com.gst_sdk_tutorials.onlab;

import android.app.Activity;
import android.app.Fragment;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.freedesktop.gstreamer.GStreamer;

/**
 * Created by László on 2016. 03. 17..
 */
public class VideoCaptureFragment2 extends Fragment implements SurfaceHolder.Callback//, SensorEventListener
{

    protected native void nativeInit();     // Initialize native code, build pipeline, etc

    protected native void nativeFinalize(); // Destroy pipeline and shutdown native code

    protected native void nativePlay();     // Set pipeline to PLAYING

    protected native void nativePause();    // Set pipeline to PAUSED

    protected static native boolean nativeClassInit(); // Initialize native class: cache Method IDs for callbacks

    protected native void nativeSurfaceInit(Object surface);

    protected native void nativeSurfaceFinalize();

    private long native_custom_data;      // Native code will use this to keep private data

    protected native void nativeSetPipeline(String url);

    private boolean is_playing_desired;   // Whether the user asked to go to PLAYING
    public static String myurl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_2, container, false);

        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        try {
            GStreamer.init(getActivity());
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            getActivity().finish();
            return;
        }


        ImageButton pause = (ImageButton) getActivity().findViewById(R.id.button_stop);
        pause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                is_playing_desired = false;
                nativePause();
            }
        });

        SurfaceView sv2 = (SurfaceView) getActivity().findViewById(R.id.surface_video2);
        SurfaceHolder sh1 = sv2.getHolder();
        sh1.addCallback(this);

        if (savedInstanceState != null) {
            is_playing_desired = savedInstanceState.getBoolean("playing");
            Log.i("GStreamer", "Activity created. Saved state is playing:" + is_playing_desired);
        } else {
            is_playing_desired = false;
            Log.i("GStreamer", "Activity created. There is no saved state, playing: false");
        }

        // Start with disabled buttons, until native code is initialized
        getActivity().findViewById(R.id.button_p).setEnabled(false);
        getActivity().findViewById(R.id.button_stop).setEnabled(false);

        nativeSetPipeline(myurl);

        nativeInit();
        //nativeSetPipeline(myurl);
        //((VideoCaptureActivity)getActivity()).getMonitor(1);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("GStreamer", "Surface created: " + holder.getSurface());
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d("GStreamer", "Surface changed to format " + format + " width "
                + width + " height " + height);
        nativeSurfaceInit(holder.getSurface());
        letsPlay();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d("GStreamer", "Surface destroyed");
        nativeSurfaceFinalize();
    }

    public void letsPlay() {
        is_playing_desired = true;
        nativePlay();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("GStreamer", "Saving state, playing:" + is_playing_desired);
        outState.putBoolean("playing", is_playing_desired);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        nativeFinalize();
    }

    private void setMessage(final String message) {
        final TextView tv = (TextView) getActivity().findViewById(R.id.textview_message);
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                tv.setText(message);
            }
        });
    }

    private void onGStreamerInitialized() {
        Log.i("GStreamer", "Gst initialized. Restoring state, playing:" + is_playing_desired);
        // Restore previous playing state
        if (is_playing_desired) {
            nativePlay();
        } else {
            nativePause();
        }

        // Re-enable buttons, now that GStreamer is initialized
        final Activity activity = getActivity();
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                activity.findViewById(R.id.button_p).setEnabled(true);
                activity.findViewById(R.id.button_stop).setEnabled(true);
            }
        });
    }

    static {
        System.loadLibrary("gstreamer_android");
        System.loadLibrary("VideoCapture2");
        nativeClassInit();
    }

    @Override
    public void onResume() {
        super.onResume();
        //sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), sensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onStop() {
        super.onStop();
        //sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION));
    }

}
