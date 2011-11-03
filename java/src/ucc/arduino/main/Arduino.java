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
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class Arduino{

    private static final HashMap< Integer, Integer> PINS = new HashMap<Integer, Integer>();
    private static final ClientHandler CLIENT_HANDLER = new ClientHandler();
    private static final ConcurrentLinkedQueue<Pin> WRITE_QUEUE = new ConcurrentLinkedQueue<Pin>();
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    private static SerialComm serialComm;
    private static final int CLIENT_TIMEOUT = 30000;	
   // private static Serial serial;
    private Socket client;
    private static int serverPortNumber = 10002;
	
    //private ClientConnection testClientConnection;
    private static  ServerSocket serverSocket;
    private String serialPort ="/dev/ttyUSB3";
	
    public Arduino() throws IOException {
	
        serverSocket = new ServerSocket( serverPortNumber );
        new Thread( CLIENT_HANDLER ).start();
	
		   
     }
	
     public void start()
     {
	 serialComm = new SerialComm( serialPort );
	
         try{
	     serialComm.connect( );
	     new Thread( serialComm).start();

	   }catch( Exception e ){
	     System.err.println( e );
	     System.err.println("Exception in thread creation");
	     System.exit( 1 );
	   }
	 
        System.out.println("Arduino Connect Started.");
	while( true )
	{
	  try{
		client = serverSocket.accept();
		System.out.println("Client Connected");
                client.setSoTimeout( CLIENT_TIMEOUT);
                ClientConnection tmp = new ClientConnection( client );
              
                EXECUTOR_SERVICE.execute( tmp );
                
                CLIENT_HANDLER.add( tmp  );
		 
	}catch( IOException ioe ){
		System.err.println( ioe );
		shutdown();
	}
     }
  }
	
  private void shutdown()
  {
    try{
	CLIENT_HANDLER.stop();
	serialComm.stop();
        serverSocket.close();
        EXECUTOR_SERVICE.shutdown();
     
    
	
    }catch( Exception e ){ }
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
   new Arduino().start();
 }

}
 


