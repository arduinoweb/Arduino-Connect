package ucc.arduino.main;

//import ucc.arduino.Serial;
import ucc.arduino.net.ClientConnection;
import ucc.arduino.serial.SerialComm;
import ucc.arduino.net.ClientHandler;
import ucc.arduino.main.Pin;

import java.net.ServerSocket;
import java.net.Socket;

import java.io.IOException;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Arduino{

    private static final HashMap< Integer, Integer> PINS = new HashMap<Integer, Integer>();
	private static final ClientHandler clientHandler = new ClientHandler();
	private static final ConcurrentLinkedQueue<Pin> WRITE_QUEUE = new ConcurrentLinkedQueue<Pin>();
	
    private static SerialComm serialComm;
	
   // private static Serial serial;
    private Socket client;

	
	//private ClientConnection testClientConnection;
    private ServerSocket serverSocket;
	
	public Arduino() throws IOException {
	
	  setPin( 9, 0);
	  setPin( 10, 255);
	  setPin( 4, 23 );
	   
	 serverSocket = new ServerSocket( 10002);


	   new Thread( clientHandler).start();
	   
	   accept();
	  
	   
	   
	}
	
	private void accept()
	{
	    serialComm = new SerialComm(  );
		  try{
	   
	    serialComm.connect( "/dev/ttyUSB0");
		new Thread( serialComm).start();
	   }catch( Exception e ){
	     System.err.println( e );
		 System.err.println("Exception in thread creation");
		 System.exit( 1 );
		 
	  }
	 
		
		int count = 0;
		while( true )
		{
		
		  try{
		   
		    client = serverSocket.accept();
		    System.out.println("Client Connected");
			clientHandler.add( new ClientConnection( client ) );
			
			}catch( IOException ioe ){
			   System.err.println( ioe );
			   shutdown();
			  }
			
          		
	
	}
	
	
  }
	
	private void shutdown()
	{
	   try{
	    clientHandler.stop();
		serialComm.stop();
		serverSocket.close();
		
		}catch( Exception e ){
		 
		}
		
		
	}
	
	
	public synchronized static void setPin( Integer pin, Integer value )
	{
	      PINS.put( pin, value );
	
	}
	
	public synchronized static Integer  getPin( Integer pin )
	{
	     return PINS.get( pin );
	 
	}
	
	public static synchronized void writeQueueAdd( byte mode, int pin, int value )
	{
	     WRITE_QUEUE.add( new Pin( mode, pin, value) );
	
	}
	
	public static synchronized Pin writeQueuePoll( )
	{
	    return WRITE_QUEUE.poll();
	}
	public static void main(String[] args) throws Exception {
		new Arduino();
		
		
		System.out.println("Started");
	}
}
 


