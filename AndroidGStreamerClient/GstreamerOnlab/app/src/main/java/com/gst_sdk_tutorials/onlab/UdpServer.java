package com.gst_sdk_tutorials.onlab;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lacika on 2016.04.13..
 */
public class UdpServer extends Service implements SensorEventListener {

    public static final int port_num = 6500;
    protected DatagramSocket sock = null;
    protected byte[] buffer = null;

    protected InetAddress clientIP;
    protected Integer clientPort;
    protected boolean fut = true;
    SensorManager sensorManager = null;
    protected Run sender;
    protected Queue<String> queue = new ConcurrentLinkedQueue<String>();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        sensorManager = (SensorManager) getApplicationContext().getSystemService(getApplicationContext().SENSOR_SERVICE);
        Sensor accel = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_NORMAL);

        try {
            this.sock = new DatagramSocket(port_num);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        sender = new Run(sock);
        startSend();
        return START_NOT_STICKY;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class Run {
        DatagramSocket sock;

        public Run(DatagramSocket sock) {
            this.sock = sock;

        }

        private void consume() {
            while (!queue.isEmpty()) {
                String s = queue.poll();
                if (s != null) {
                    {
                        final DatagramPacket dp = new DatagramPacket(s.getBytes(), s.getBytes().length, clientIP, clientPort);
                        try {
                            sock.send(dp);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    try {
                        synchronized (queue) {
                            queue.wait();
                        }
                    } catch (InterruptedException ex) {
                    }

                }
            }
        }

        public void startThread() {
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    consume();
                }
            };
            Thread t = new Thread(run);
            t.start();

        }

      /*  public void sendCoords(String s) throws IOException {
            Log.e("Kiküldeni", s);
            final DatagramPacket dp = new DatagramPacket(s.getBytes(), s.getBytes().length, clientIP, clientPort);
            Runnable run=new Runnable() {
                @Override
                public void run() {
                    try {
                        sock.send(dp);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            Thread t=new Thread(run);
            t.start();

        }*/

    }
   /* public void sendData(String s) {

        Log.e("send message", s);

            Runnable run=new Runnable() {
                @Override
                public void run() {
                    DatagramPacket dp = new DatagramPacket(s.getBytes(), s.getBytes().length, clientIP, clientPort);
                    sock.send(dp);
                }
            };
    }*/

    /* public void Listening() {
         Runnable run = new Runnable() {
             @Override
             public void run() {
                 while (fut) {
                     try {
                         sock.receive(incoming);
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                     byte[] data = incoming.getData();
                     String message = new String(data, 0, incoming.getLength());
                     if (message.equals("STOP")) {

                         fut = false;
                     }
                 }
             }
         };
         Thread t = new Thread(run);
         t.start();
     }*/
    Boolean egyszerfut = true;

    public void startSend() {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                byte[] buffer = new byte[65536];
                DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);

                while (true) {
                    try {
                        sock.receive(incoming);
                        clientIP = incoming.getAddress();
                        clientPort = incoming.getPort();
                       /* if(egyszerfut)
                        {
                            sender.startThread();
                            egyszerfut=false;
                        }*/
                        //sendData("LALALALA");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    byte[] data = incoming.getData();
                    String s = new String(data, 0, incoming.getLength());
                    Log.e("Incoming", s);
                }
            }
        };
        Thread t = new Thread(run);
        t.start();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        synchronized (this) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ORIENTATION:
                    if (clientIP != null && clientPort != null) {   //try {
                        String output = "x:" + Float.toString(event.values[0]) + " y:" + Float.toString(event.values[1]) + " z:" + Float.toString(event.values[2]);
                        //  sender.sendCoords(output);
                        queue.offer(output);
                        Log.e("Que mérete:", String.valueOf(queue.size()));
                       /* synchronized (queue) {
                            queue.notifyAll();
                        }*/
                        //} catch (IOException e) {
                        //    e.printStackTrace();
                        // }
                        Log.e("MEGHÍVJUK EZT IS", "");
                        break;

                    }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}