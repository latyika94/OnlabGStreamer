package com.gst_sdk_tutorials.onlab;

import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by lacika on 2016.04.14..
 */
public class TcpClient extends Thread implements Runnable {

    protected Messenger messageHandler = null;
    protected Message msg = Message.obtain();
    protected Integer SERVER_PORT;
    protected Socket clientSocket;
    protected Scanner ServerInput;
    protected PrintWriter serverOutput;
    protected String SERVER_IP;

    public TcpClient(Messenger messageHandler, String ip, Integer port) {
        this.messageHandler = messageHandler;
        try {
            this.SERVER_IP = ip;
            this.SERVER_PORT = port;
            this.clientSocket = new Socket(SERVER_IP, SERVER_PORT);
            this.ServerInput = new Scanner(clientSocket.getInputStream());
            this.serverOutput = new PrintWriter(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendData(String data) {
        serverOutput.print(data+ "\r\n");
        serverOutput.flush();
        Log.e("SendData", "Meglett hívva: " + data);
    }

    @Override
    public void run() {
        super.run();

        while (true) {
            if (ServerInput.hasNext()) {
                try {
                    String s = ServerInput.nextLine();
                    Log.e("Server Message", s);
                    msg = Message.obtain();
                    msg.obj = s;
                    messageHandler.send(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
