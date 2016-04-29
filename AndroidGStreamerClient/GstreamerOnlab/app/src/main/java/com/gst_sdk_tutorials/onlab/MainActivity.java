package com.gst_sdk_tutorials.onlab;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by László on 2016. 03. 17..
 */
public class MainActivity extends Activity {

    protected TextView clientIP1, clientIP2, clientPort1, clientPort2;
    protected AutoCompleteTextView streamServerIp1,streamServerIP2,streamServerPort1,streamServerPort2;
    protected TcpClient client = null;
    protected String url1, url2;
    protected TcpService mBoundService;
    protected Boolean mBound;
    protected Messenger messenger;
    protected String AppPath, IpPath,PortPath;
    protected File IpCacheFile,PortCacheFile;
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  Because we have bound to a explicit
            // service that we know is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            TcpService.LocalBinder binder = (TcpService.LocalBinder) service;
            mBoundService = binder.getService();
            mBound = true;
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            // Because it is running in our same process, we should never
            // see this happen.
            mBound = null;
        }
    };

    protected Handler mHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Pattern fileSouce1_p = Pattern.compile("C1F(.*)");
            Matcher fileSource1_m = fileSouce1_p.matcher((String) msg.obj);
            if (fileSource1_m.matches()) {
                Log.e("C1F", fileSource1_m.group(1));

                url1 = fileSource1_m.group(1);
                VideoCaptureFragment1.myurl = url1;
                if (url2 != null) {
                    Intent intent = new Intent(getBaseContext(), VideoCaptureActivity.class);
                    intent.putExtra("URL1", url1);
                    intent.putExtra("URL2", url2);
                    startActivity(intent);
                }

            }
            Pattern fileSouce2_p = Pattern.compile("C2F(.*)");
            Matcher fileSource2_m = fileSouce2_p.matcher((String) msg.obj);
            if (fileSource2_m.matches()) {
                Log.e("C2F", fileSource2_m.group(1));

                url2 = fileSource2_m.group(1);
                VideoCaptureFragment2.myurl = url2;
                if (url1 != null) {
                    Intent intent = new Intent(getBaseContext(), VideoCaptureActivity.class);
                    intent.putExtra("URL1", url1);
                    intent.putExtra("URL2", url2);
                    startActivity(intent);
                }
            }


            Pattern webcamSouce1_p = Pattern.compile("C1M(.*)");
            Matcher webcamSource1_m = webcamSouce1_p.matcher((String) msg.obj);
            if (webcamSource1_m.matches()) {

                url1 = webcamSource1_m.group(1);
                VideoCaptureFragment1.myurl = url1;
                if (url2 != null) {
                    Intent intent = new Intent(getBaseContext(), VideoCaptureActivity.class);
                    intent.putExtra("URL1", url1);
                    intent.putExtra("URL2", url2);
                    startActivity(intent);
                }
            }
            Pattern webcamSouce2_p = Pattern.compile("C2M(.*)");
            Matcher webcamSource2_m = webcamSouce2_p.matcher((String) msg.obj);
            if (webcamSource2_m.matches()) {
                Log.e("C2M", webcamSource2_m.group(1));

                url2 = webcamSource2_m.group(1);
                VideoCaptureFragment2.myurl = url2;
                if (url1 != null) {
                    Intent intent = new Intent(getBaseContext(), VideoCaptureActivity.class);
                    intent.putExtra("URL1", url1);
                    intent.putExtra("URL2", url2);
                    startActivity(intent);
                }
            }

            Pattern webcamSouce1Single_p = Pattern.compile("C1S(.*)");
            Matcher webcamSource1Single_m = webcamSouce1Single_p.matcher((String) msg.obj);
            if (webcamSource1Single_m.matches()) {

                String temp = webcamSource1Single_m.group(1);
                if (url1 == null) {
                    url1 = temp;
                    VideoCaptureFragment1.myurl = url1;
                } else {
                    if (url2 == null) {
                        url2 = temp;
                        VideoCaptureFragment2.myurl = url2;
                        Intent intent = new Intent(getBaseContext(), VideoCaptureActivity.class);
                        intent.putExtra("URL1", url1);
                        intent.putExtra("URL2", url2);
                        startActivity(intent);
                    }
                }
            }


        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, TcpService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        streamServerIp1= ((AutoCompleteTextView) findViewById(R.id.streamServerIp1));
        streamServerIP2= ((AutoCompleteTextView) findViewById(R.id.streamServerIP2));
        streamServerPort1= ((AutoCompleteTextView) findViewById(R.id.streamServerPort1));
        streamServerPort2= ((AutoCompleteTextView) findViewById(R.id.streamServerPort2));
        streamServerIp1.setText("192.168.0.101");
        streamServerIP2.setText("192.168.0.101");
        streamServerPort1.setText("7777");
        streamServerPort2.setText("7778");

        clientIP1 = ((TextView) findViewById(R.id.server1IP));
        clientIP2 = ((TextView) findViewById(R.id.server2ip));
        clientPort1 = ((TextView) findViewById(R.id.server1port));
        clientPort2 = ((TextView) findViewById(R.id.server2port));

        PackageManager m = getPackageManager();
        String s = getPackageName();
        PackageInfo p = null;
        try {
            p = m.getPackageInfo(s, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        AppPath=p.applicationInfo.dataDir;
        IpPath="IpCache";
        PortPath="PortCache";
       /* loadCacheIP(IpPath);
        loadCachePort(PortPath);*/
        Log.e("elérés",s);

        WifiManager wifiMgr = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        final String ipAddress = Formatter.formatIpAddress(ip);
        ((TextView) findViewById(R.id.wifiIP)).setText(((TextView) findViewById(R.id.wifiIP)).getText() + ipAddress);

        final Messenger mActivityMessenger;
        mActivityMessenger = new Messenger(mHandler);
        messenger = mActivityMessenger;


        Button play = (Button) findViewById(R.id.myStartButton);
        play.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (mBoundService.client2 != null && mBoundService.client != null) {
                    url1 = null;
                    url2 = null;
                    VideoCaptureFragment1.myurl = null;
                    VideoCaptureFragment2.myurl = null;
                    mBoundService.client.sendData("I_AM_READY_FOR_STREAM");
                    mBoundService.client2.sendData("I_AM_READY_FOR_STREAM");
                } else {
                    if (mBoundService.client != null) {
                        url1 = null;
                        url2 = null;
                        VideoCaptureFragment1.myurl = null;
                        VideoCaptureFragment2.myurl = null;
                        mBoundService.client.sendData("I_AM_READY_FOR_STREAM");
                    } else {
                        Intent intent = new Intent(getBaseContext(), TcpService.class);
                        intent.putExtra("MESSENGER", mActivityMessenger);
                        intent.putExtra("SERVERIP", streamServerIp1.getText().toString());
                        intent.putExtra("SERVERPORT", streamServerPort1.getText().toString());

                        if (!streamServerIP2.getText().toString().equals("") && !streamServerPort2.getText().toString().equals("")) {
                            intent.putExtra("SERVERIP2", streamServerIP2.getText().toString());
                            intent.putExtra("SERVERPORT2", streamServerPort2.getText().toString());
                            intent.putExtra("SECOND", "THERE_IS_SECOND_SERVER");

                        } else {
                            intent.putExtra("SECOND", "THERE_IS_NOT_SECOND_SERVER");
                        }
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startService(intent);
                        }
                    }
                }
            }
        });
    }
    public void loadCacheIP(String path){
        File urltest=new File(AppPath+"/"+path);
        if(urltest.exists()){
           IpCacheFile=urltest;

            try {
                FileReader file = new FileReader(IpCacheFile.getAbsolutePath());
                BufferedReader br = new BufferedReader(file);
                String output="";
                for(String line; (line = br.readLine()) != null; )
                {
                    output+=line+"\n";
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }

        }
        else{
            // create an new file
            File urlconfig = new File(AppPath+"/", path+".txt");

            IpCacheFile=urlconfig;
            try {
                FileOutputStream out = new FileOutputStream(IpCacheFile);
                out.flush();
                out.close();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }


    };
    public void loadCachePort(String path){
        File urltest=new File(AppPath+"/"+path);
        if(urltest.exists()){
            PortCacheFile=urltest;

        }
        else{
            // create an new file
            File urlconfig = new File(AppPath+"/", path+".txt");
            PortCacheFile=urlconfig;
            try {
                FileOutputStream out = new FileOutputStream(PortCacheFile);
                out.flush();
                out.close();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
    };
}
