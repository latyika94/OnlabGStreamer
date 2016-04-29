package onlab;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileServer extends Thread implements Runnable {
	protected DatagramSocket sock = null;
	protected int port;
	protected InetAddress host;
	protected String message = null;
	protected byte[] b = null;
	protected String[] args1, args2;
	protected String outputPipeline1, outputPipeline2, found1, found2;
	protected Integer port1, port2;

	FileServer(int port_num, String host_ip, Integer port1, Integer port2)
			throws SocketException, UnknownHostException {
		this.port = port_num;
		this.sock = new DatagramSocket();
		this.host = InetAddress.getByName(host_ip);
		this.port1 = port1;
		this.port2 = port2;
	}

	public void listenServer()
	{
		Runnable listen=new Runnable(){
			String output;
			@Override
			public void run() {
				byte[] buffer = new byte[65536];
		        DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
		        try {
					sock.receive(reply);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		        byte[] data = reply.getData();
		        String s = new String(data, 0, reply.getLength());
		        System.out.println(s);
		        if(s.equals("I_AM_READY"))
		        {
		        	if(args2!=null)
		    		{
							try {
								@SuppressWarnings("unused")
								Process proc = new ProcessBuilder(args1).start();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							try {
								@SuppressWarnings("unused")
								Process proc2 = new ProcessBuilder(args2).start();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							String file1="src/output1.txt";
						    try(BufferedReader br = new BufferedReader(new FileReader(file1))) {
							output="";
						    for(String line; (line = br.readLine()) != null; ) {
						        output+=line+"\n";
						    }
						    System.out.println(output);
						    String caps="udpsink0";
						    Pattern pattern=Pattern.compile(caps+".GstPad(.*) caps = (.*)");
					        Matcher m=pattern.matcher(output);
					        	if(m.find())
					        	{
					        		found1="CLIENT1 PORT1:"+port1+"CAPS:"+m.group(2);
					        		sendMessage(found1);
					        	}
						    } catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}


						    String file2="src/output2.txt";
						    try(BufferedReader br = new BufferedReader(new FileReader(file2))) {
							output="";
						    for(String line; (line = br.readLine()) != null; ) {
						        output+=line+"\n";
						    }
						    System.out.println(output);
						    String caps="udpsink0";
						    Pattern pattern=Pattern.compile(caps+".GstPad(.*) caps = (.*)");
					        Matcher m=pattern.matcher(output);
					        	if(m.find())
					        	{
					        		found2="CLIENT2 PORT2:"+port2+"CAPS:"+m.group(2);
					        	}
						    } catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						    	sendMessage(found2);
		    		}

		        		if(args2==null)
		        		{

								try {
									@SuppressWarnings("unused")
									Process proc = new ProcessBuilder(args1).start();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

									try {
										Thread.sleep(700);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
		        		}
		        }

		}
		};

	Thread t = new Thread(listen);t.start();
	}

	@Override
	public void run() {
		listenServer();
		while (!interrupted()) {
			sendToPhone();
		}
	}

	public synchronized void sendMessage(String message) {
		this.message = message;
	}

	public synchronized void sendToPhone() {
		if (message != null) {
			b = message.getBytes();
			DatagramPacket dp = new DatagramPacket(b, b.length, host, port);
			try {
				sock.send(dp);
				System.out.println(message);
				message = null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}