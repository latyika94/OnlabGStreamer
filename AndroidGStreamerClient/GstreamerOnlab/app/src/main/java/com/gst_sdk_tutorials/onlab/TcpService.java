package com.gst_sdk_tutorials.onlab;

/**
 * Created by lacika on 2016.04.05..
 */

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class TcpService extends Service {


    protected IBinder mBinder = new LocalBinder();
    protected Messenger messageHandler = null;
    protected Message msg = Message.obtain();
    protected Integer SERVER_PORT, SERVER_PORT2;
    protected String SERVER_IP, SERVER_IP2;
    protected TcpClient client, client2;

    public class LocalBinder extends Binder {
        TcpService getService() {
            return TcpService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

            Bundle extras = intent.getExtras();
            messageHandler = (Messenger) extras.get("MESSENGER");
            SERVER_IP = (String) extras.get("SERVERIP");
            SERVER_PORT = Integer.parseInt((String) extras.get("SERVERPORT"));
            String thereis = (String) extras.get("SECOND");
            if (thereis.equals("THERE_IS_SECOND_SERVER")) {
                SERVER_IP2 = (String) extras.get("SERVERIP2");
                SERVER_PORT2 = Integer.parseInt((String) extras.get("SERVERPORT2"));
            }
            Runnable startClient = new Runnable() {
                @Override
                public void run() {
                    if (SERVER_IP2 != null && SERVER_PORT2 != null) {
                        client = new TcpClient(messageHandler, SERVER_IP, SERVER_PORT);
                        client2 = new TcpClient(messageHandler, SERVER_IP2, SERVER_PORT2);
                        client.start();
                        client2.start();
                        client.sendData("I_AM_READY_FOR_STREAM");
                        client2.sendData("I_AM_READY_FOR_STREAM");

                    } else {
                        if (SERVER_IP != null && SERVER_PORT != null) {
                            client = new TcpClient(messageHandler, SERVER_IP, SERVER_PORT);
                            client.start();
                            client.sendData("I_AM_READY_FOR_STREAM");

                        }
                    }
                }
            };
            Thread t = new Thread(startClient);
            t.start();
            return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

   /*@Override
    public boolean onUnbind(Intent intent) {
            Log.e("onUnbind", "Meghívva");
        return super.onUnbind(intent);
    }*/

    @Override
    public void onRebind(Intent intent) {
        Log.e("onRebind", "Meghívva");
        super.onRebind(intent);
    }


}
