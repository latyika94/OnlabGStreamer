package onlab;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class FileServerTCP extends Thread implements Runnable {
	protected Socket client;
	protected PrintWriter client_writer;
	protected Scanner client_reader;
	protected Boolean flip=false;
	protected String message,webCamMessage;
	protected String[] args1, args2;
	protected String outputPipeline1, outputPipeline2;
	protected Integer port1, port2;
	protected Boolean futhat=true;
	protected Thread ListenThread;
	protected String StreamType="";
	protected String filePath1,filePath2,webcamSource;
	protected String output;
	protected TextArea textArea;
	protected Integer ServerPORT;

	public FileServerTCP(){

	}
	public void InitServer(Socket client)
	{
		this.client=client;
		try {
			if(client_reader!=null)
			{
				client_reader.reset();
			}
			this.client_reader = new Scanner(client.getInputStream());
			this.client_writer = new PrintWriter(client.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void killGstreamerServer()
	{
		String[] exit = new String[] {"/bin/bash", "-c", "killall -9 gst-launch-1.0"};
		try {
			@SuppressWarnings("unused")
			Process proc2 = new ProcessBuilder(exit).start();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void multiFile()
	{
		killGstreamerServer();

		String pipeline1="gst-launch-1.0 -v filesrc location="+filePath1+" ! decodebin name=dec !  x264enc pass=qual quantizer=20 tune=zerolatency ! rtph264pay ! udpsink host="+client.getInetAddress().getHostAddress()+" port="+port1;
		String pipeline2="gst-launch-1.0 -v filesrc location="+filePath2+" ! decodebin name=dec !  x264enc pass=qual quantizer=20 tune=zerolatency ! rtph264pay ! udpsink host="+client.getInetAddress().getHostAddress()+" port="+port2;
		System.out.println("Pipeline1: " +pipeline1);
		System.out.println("Pipeline2: " +pipeline2);
		String caps="udpsink0";
		args1 = new String[] {"/bin/bash", "-c", pipeline1+">src/output1.txt"};
		args2 = new String[] {"/bin/bash", "-c", pipeline2+">src/output2.txt"};

		try {
			@SuppressWarnings("unused")
			Process proc = new ProcessBuilder(args1).start();
			@SuppressWarnings("unused")
			Process proc2 = new ProcessBuilder(args2).start();
			Thread.sleep(3000);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};

		String file1="src/output1.txt";
		String file2="src/output2.txt";

		try(
				FileReader file=new FileReader(file1);
				BufferedReader br = new BufferedReader(file)
			) {
			output="";
			for(String line; (line = br.readLine()) != null; )
			{
			     output+=line+"\n";
			}
			System.out.println(output);
			runOnUI(output);
			Pattern pattern=Pattern.compile(caps+".GstPad(.*) caps = (.*)");
		    Matcher m=pattern.matcher(output);
		    if(m.find())
		    {
                String change1 = "\\";
                String guess="\\";
                String str=m.group(2);
                int index = str.indexOf(guess);
                while (index >= 0) {
                    str = new StringBuilder(str).insert(index, change1).toString();
                    index = str.indexOf(guess, index + 2);
                }
                String guess2="\"";
                int index2 = str.indexOf(guess2);
                while (index2 >= 0) {
                    str = new StringBuilder(str).insert(index2, change1).toString();
                    index2 = str.indexOf(guess2, index2 + 2);
                }
		    	outputPipeline1="C1F"+"udpsrc port="+port1+" caps = \""+str+"\" ! rtph264depay ! avdec_h264 ! decodebin ! autovideosink sync=false";
		    	file.close();
		    	br.close();
		    }

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		 try(FileReader file=new FileReader(file2);
					BufferedReader br = new BufferedReader(file)) {
				output="";
			    for(String line; (line = br.readLine()) != null; ) {
			        output+=line+"\n";
			    }
			    System.out.println(output);
			    runOnUI(output);
			    Pattern pattern=Pattern.compile(caps+".GstPad(.*) caps = (.*)");
		        Matcher m=pattern.matcher(output);
		        	if(m.find())
		        	{
		        		String change1 = "\\";
		                String guess="\\";
		                String str= m.group(2);
		                int index = str.indexOf(guess);
		                while (index >= 0) {
		                    str = new StringBuilder(str).insert(index, change1).toString();
		                    index = str.indexOf(guess, index + 2);
		                }
		                String guess2="\"";
		                int index2 = str.indexOf(guess2);
		                while (index2 >= 0) {
		                    str = new StringBuilder(str).insert(index2, change1).toString();
		                    index2 = str.indexOf(guess2, index2 + 2);
		                }
				    	outputPipeline2="C2F"+"udpsrc port="+port2+" caps = \""+str+"\" ! rtph264depay ! avdec_h264 ! decodebin ! autovideosink sync=false";
				    	file.close();
				    	br.close();
		        	}
			    } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		 	client_writer.print(outputPipeline1+"\r\n");
			client_writer.flush();

			client_writer.print(outputPipeline2+"\r\n");
			client_writer.flush();

	    	System.out.println(outputPipeline1);
	    	System.out.println(outputPipeline2);
	    	runOnUI(outputPipeline1);
	    	runOnUI(outputPipeline2);

	}
	public void runOnUI(String s)
	{
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				textArea.setText(textArea.getText()+"\n"+s);
			}
		});
	}
	public void runMultiWebcam()
	{
		//String pipeline="gst-launch-1.0 -v v4l2src device=/dev/video0 ! 'video/x-raw,width=(int)640,height=(int)480' ! x264enc pass=qual quantizer=20 tune=zerolatency  ! rtph264pay ! multiudpsink clients="+client.getInetAddress().getHostAddress()+":"+port1+","+client.getInetAddress().getHostAddress()+":"+port2;
		String pipeline="gst-launch-1.0 -v v4l2src device="+webcamSource+" ! 'video/x-raw,width=640,height=480' ! x264enc pass=qual quantizer=20 tune=zerolatency ! rtph264pay ! multiudpsink clients="+client.getInetAddress().getHostAddress()+":"+port1+","+client.getInetAddress().getHostAddress()+":"+port2;

		String[] args1 = new String[] {"/bin/bash", "-c", pipeline+">src/output.txt"};
		try {
			@SuppressWarnings("unused")
			Process proc = new ProcessBuilder(args1).start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	public void multiWebcam()
	{
		killGstreamerServer();
		runMultiWebcam();
		String caps="multiudpsink0";
		String file="src/output.txt";
	    try(FileReader file1=new FileReader(file);
				BufferedReader br = new BufferedReader(file1)) {
		String output="";

	    for(String line; (line = br.readLine()) != null; ) {
	        output+=line+"\n";
	    }
	    System.out.println(output);
	    runOnUI(output);
	    Pattern pattern=Pattern.compile(caps+".GstPad(.*) caps = (.*)");
        Matcher m=pattern.matcher(output);

        if(m.find())
        {
        	if(caps.equals("multiudpsink0"))
        	{
        		String change1 = "\\";
                String guess="\\";
                String str= m.group(2);
                int index = str.indexOf(guess);
                while (index >= 0) {
                    str = new StringBuilder(str).insert(index, change1).toString();
                    index = str.indexOf(guess, index + 2);
                }
                String guess2="\"";
                int index2 = str.indexOf(guess2);
                while (index2 >= 0) {
                    str = new StringBuilder(str).insert(index2, change1).toString();
                    index2 = str.indexOf(guess2, index2 + 2);
                }

		    	outputPipeline2="C2M"+"udpsrc port="+port2+" caps = \""+str+"\" ! rtph264depay ! avdec_h264 ! autovideosink sync=false";
		    	outputPipeline1="C1M"+"udpsrc port="+port1+" caps = \""+str+"\" ! rtph264depay ! avdec_h264 ! autovideosink sync=false";
		    	client_writer.print(outputPipeline1+"\r\n");
				client_writer.flush();

				client_writer.print(outputPipeline2+"\r\n");
				client_writer.flush();
				runOnUI(outputPipeline1);
				runOnUI(outputPipeline2);
				file1.close();
				br.close();
        	}

	    }

	    } catch (IOException e) {
	    	// TODO Auto-generated catch block
	    	e.printStackTrace();
	    }
}
	public void runSingleWebcam()
	{
		String pipeline="";

		if(flip)
			pipeline="gst-launch-1.0 -v v4l2src device="+webcamSource+" ! 'video/x-raw,width=(int)800,height=(int)600' ! videoflip method=rotate-180 ! x264enc pass=qual quantizer=20 tune=zerolatency  ! rtph264pay ! udpsink host="+client.getInetAddress().getHostAddress()+" port="+port1;
		else
			pipeline="gst-launch-1.0 -v v4l2src device="+webcamSource+" ! 'video/x-raw,width=(int)800,height=(int)600' ! x264enc pass=qual quantizer=20 tune=zerolatency  ! rtph264pay ! udpsink host="+client.getInetAddress().getHostAddress()+" port="+port1;

		String[] args1 = new String[] {"/bin/bash", "-c", pipeline+">src/output"+ServerPORT+".txt"};
		try {
			@SuppressWarnings("unused")
			Process proc = new ProcessBuilder(args1).start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Thread.sleep(5000);
			System.out.println("Ez a pipeline: "+pipeline);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void singleWebcam()
	{
		killGstreamerServer();
		runSingleWebcam();
		String caps="udpsink0";
		String file="src/output"+ServerPORT+".txt";
	    try(FileReader file1=new FileReader(file);
				BufferedReader br = new BufferedReader(file1)) {
		String output="";

	    for(String line; (line = br.readLine()) != null; ) {
	        output+=line+"\n";
	    }
	    System.out.println(output);
	    runOnUI(output);
	    Pattern pattern=Pattern.compile(caps+".GstPad(.*) caps = (.*)");
        Matcher m=pattern.matcher(output);

        if(m.find())
        {
        	if(caps.equals("udpsink0"))
        	{
        		String change1 = "\\";
                String guess="\\";
                String str= m.group(2);
                int index = str.indexOf(guess);
                while (index >= 0) {
                    str = new StringBuilder(str).insert(index, change1).toString();
                    index = str.indexOf(guess, index + 2);
                }
                String guess2="\"";
                int index2 = str.indexOf(guess2);
                while (index2 >= 0) {
                    str = new StringBuilder(str).insert(index2, change1).toString();
                    index2 = str.indexOf(guess2, index2 + 2);
                }

		    	outputPipeline1="C1S"+"udpsrc port="+port1+" caps = \""+str+"\" ! rtph264depay ! avdec_h264 ! autovideosink sync=false";
		    	client_writer.print(outputPipeline1+"\r\n");
				client_writer.flush();

				runOnUI(outputPipeline1);
				file1.close();
				br.close();
        	}

	    }

	    } catch (IOException e) {
	    	// TODO Auto-generated catch block
	    	e.printStackTrace();
	    }
}

	public void listenServer()
	{
		Runnable listen=new Runnable(){
			@Override
			public void run() {
				while(!interrupted())
				{
					if(client_reader.hasNext("I_AM_READY_FOR_STREAM"))
					{
						String s = client_reader.nextLine();
						System.out.println(s);
						runOnUI(s);
						if(s.equals("I_AM_READY_FOR_STREAM"))
							{
								switch (StreamType) {
									case "SINGLEWEBCAM":
										singleWebcam();
										break;
									case "MULTIWEBCAM":
										multiWebcam();
										break;
									case "SINGLEFILE":

										break;
									case "MULTIFILE":
										multiFile();
										break;
									case "DESKTOP":

										break;
									default:
										System.out.println("No streaming source");
										break;
								}
							}
						else
							System.out.println("no input");
					}

				}
				System.out.println("SERVER KILLED");
			}};
			ListenThread = new Thread(listen);
			ListenThread.start();

	}

	@Override
	public void run() {
		listenServer();
		while (futhat) {
			if(message!=null)
			{
				client_writer.print(message+"\r\n");
				client_writer.flush();
				System.out.println(message);
				runOnUI(message);
				message=null;
			}
					};
					ListenThread.interrupt();


	}
	public synchronized void sendMessage(String message) {
		this.message = message;
	}

}