package onlab;

import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;


public class TCPServer extends Thread implements Runnable {

	protected Socket client;
	protected ServerSocket serverSocket;
	protected String name;
	protected Integer PORT_NUMBER;


	protected boolean connectable=true;
	protected FileServerTCP fileServerTCP;
	protected Boolean futhat=true;

	public TCPServer(String name)
	{
		this.name=name;
		this.fileServerTCP=new FileServerTCP();
	}
	public void setPORT_NUMBER(Integer pORT_NUMBER) {
		PORT_NUMBER = pORT_NUMBER;
	}
	@Override
	public void run()
	{
		System.out.println("SERVER STARTED");
		fileServerTCP.runOnUI("SERVER STARTED");
		fileServerTCP.ServerPORT=PORT_NUMBER;
		try {
			serverSocket = new ServerSocket(PORT_NUMBER);
			while(futhat){
					if(client==null){
					client = serverSocket.accept();
					fileServerTCP.InitServer(client);
					fileServerTCP.start();
					}
					else
					{
						client = serverSocket.accept();
						//fileServerTCP.futhat=false;
						//String string=fileServerTCP.StreamType;
						//fileServerTCP=new FileServerTCP();
						fileServerTCP.InitServer(client);
						//fileServerTCP.StreamType=string;
						//fileServerTCP.start();
					}
				}
			}

		catch (IOException e) {

			e.printStackTrace();
			try {
				serverSocket.close();
				serverSocket=null;
				client.close();
				client=null;
				fileServerTCP.client.close();
				fileServerTCP.client=null;
			} catch (IOException e1) {

				e1.printStackTrace();
			}
		}
	}

	/*public static void main(String[] args) {
		new TCPServer("Labyrinth_Game_Server").start();


	}*/

}