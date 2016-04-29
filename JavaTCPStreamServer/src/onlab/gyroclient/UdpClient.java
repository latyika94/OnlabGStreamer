package onlab.gyroclient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class UdpClient extends Thread implements Runnable{

	private String hostIP;
	private byte[] buffer;
	private DatagramSocket sock = null;
	private TextArea coords;
	public UdpClient(String ip,TextArea coords)
	{
		this.hostIP=ip;
		this.coords=coords;
		System.out.println("udpclient lefutott");
	}
	@Override
	public void run()
	{
	    int port = 6500;
	    String s;
	    try
	    {
	    	sock=new DatagramSocket();
	        InetAddress host = InetAddress.getByName(hostIP);
	        s = "SENDMEXYZ";
            byte[] b = s.getBytes();

            DatagramPacket  dp = new DatagramPacket(b , b.length , host , port);
            sock.send(dp);
            runOnUI(s);
	    	/*Long out=System.currentTimeMillis();
        	s=out.toString();
        	 byte[] b = s.getBytes();
        	 InetAddress host = InetAddress.getByName(hostIP);
                DatagramPacket  dp = new DatagramPacket(b , b.length , host , port);
                sock.send(dp);
                out=System.currentTimeMillis();
	        	s=out.toString();
	        	b = s.getBytes();
	        	  host = InetAddress.getByName(hostIP);
	                dp = new DatagramPacket(b , b.length , host , port);
	                sock.send(dp);
	                out=System.currentTimeMillis();
		        	s=out.toString();
		         b = s.getBytes();
		        	 host = InetAddress.getByName(hostIP);
		                dp = new DatagramPacket(b , b.length , host , port);
		                sock.send(dp);*/
	        while(true)
	        {
	        	/*Long out=System.currentTimeMillis();
	        	s=out.toString();
	        	 byte[] b = s.getBytes();
	        	 InetAddress host = InetAddress.getByName(hostIP);
	                DatagramPacket  dp = new DatagramPacket(b , b.length , host , port);
	                sock.send(dp);*/

	            buffer = new byte[65536];
	            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
	            sock.receive(reply);

	            byte[] data = reply.getData();
	            final String msg = new String(data, 0, reply.getLength());
	            System.out.println(msg);
	            runOnUI(msg);
	            //echo the details of incoming data - client ip : client port - client message
	           // echo(reply.getAddress().getHostAddress() + " : " + reply.getPort() + " - " + s);
	        }
	    }

	    catch(IOException e)
	    {
	        System.err.println("IOException " + e);
	    }
	}

	public void runOnUI(String s)
	{
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				coords.setText(coords.getText()+"\n"+s);
				coords.appendText("");
			}
		});
	}
}
