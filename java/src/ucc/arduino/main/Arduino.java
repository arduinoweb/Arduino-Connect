/**
  * @author: Gary Smith
  ** <p>The entry point to the Arduino program,
  * Handles initialisation and
  * the connecting of clients via sockets</p>
  */


package ucc.arduino.main;

import ucc.arduino.configuration.Configuration;

import ucc.arduino.net.ClientConnection;
import ucc.arduino.serial.SerialComm;
import ucc.arduino.net.ClientHandler;
import ucc.arduino.main.Pin;
import ucc.arduino.net.NetworkRegister;

import java.net.ServerSocket;
import java.net.Socket;


import java.io.IOException;
import java.io.File;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.Properties;


public class Arduino{

    public static Configuration CONFIGURATION = new Configuration( );
    /**The map of (pin,value) that clients are fed data from/*/
    private static final HashMap< Integer, Integer> PINS = new HashMap<Integer, Integer>();
    /**Thread that handles the processing of messages from clients*/
    private static final ClientHandler CLIENT_HANDLER = new ClientHandler();
    /**A Queue of the write requests received from clients*/
    private static final ConcurrentLinkedQueue<Pin> WRITE_QUEUE = new ConcurrentLinkedQueue<Pin>();
    /**Handles the creation/reuse of client threads*/
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();
    /**Provides the communication link from the usb port to the Arduino*/
    private static SerialComm serialComm;	
    /**Temporarily stores a newly connected client before it is passed onto CLIENT_HANDLER*/
    private Socket client;
    /**The socket that listens for client connections*/
    private static  ServerSocket serverSocket;
	
    /**Constructor*/
    public Arduino( File configurationFile ) throws Exception {
        CONFIGURATION = new Configuration( configurationFile );
      
        if( NetworkRegister.register() )
        {
           System.out.println("Registered at " + 
                               CONFIGURATION.getWebServerUrl() + " as " +
                               CONFIGURATION.getArduinoNetworkName() );
        }
        else
        {
          System.err.println("Unable to register with web server at " +
                              CONFIGURATION.getWebServerUrl() );
        }
        
        System.exit( 1 );
        serverSocket = new ServerSocket( CONFIGURATION.getNetworkPort(),
                                         CONFIGURATION.getNetworkQueueLength(),
                                         CONFIGURATION.getNetworkAddress() );
        new Thread( CLIENT_HANDLER ).start();
	
		   
     }
   
  /** Main routine that waits for a client connection, assigns it a new thread
   *  and passes the client connection onto CLIENT_HANDLER
   */	
  public void start()
 {
         
	 serialComm = new SerialComm( );
	
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
             
                client.setSoTimeout( CONFIGURATION.getNetworkTimeout() );
                ClientConnection tmp = new ClientConnection( client );
              
                EXECUTOR_SERVICE.execute( tmp );
                
                CLIENT_HANDLER.add( tmp  );
		 
	}catch( IOException ioe ){
		System.err.println( ioe );
		shutdown();
	}
     }
  }
	
  /** Close the program and exit */
  private void shutdown()
  {
    try{
	CLIENT_HANDLER.stop();
	serialComm.stop();
        serverSocket.close();
        EXECUTOR_SERVICE.shutdown();
        System.exit( 0 );
	
    }catch( Exception e ){ }
  }
	
 /** Add/Update a (pin,value) entry in the PINS map
  *  @param: pin - the pin number
  *  @param: value - the value to assign to the pin
  */
 public synchronized static void setPin( Integer pin, Integer value )
 {
    PINS.put( pin, value );
 }

 /** Retreives a (pin,value) entry from the PINS map
  *  @param: pin - the pin number
  *  @return: Pin the (pin,value) entry or null if no entry
  */	
 public synchronized static Integer  getPin( Integer pin )
 {
    return PINS.get( pin );
  }
	
 /** Adds a new write request to the write message to Arduino queue
  *  @param: byte mode - A = Analog pin, D = Digital pin
  *  @param: int pin   - the pin number
  *  @param: int value - the value to assign to the pin
  */
 public static synchronized void writeQueueAdd( byte mode, int pin, int value )
 {
    WRITE_QUEUE.add( new Pin( mode, pin, value) );
 }
	
 /** Retreive the next write message from the head of the write queue
  *  @return: Pin - the (pin,value) entry at the head of the queue
  *                otherwise null if the queue is empty
  */
 public static synchronized Pin writeQueuePoll( )
 {
    return WRITE_QUEUE.poll();
 }

/** Main entry point */
 public static void main(String[] args)  {
 try{
   new Arduino( new File( "/home/gary/public_html/arduino/Arduino-Connect/java/configuration.properties") ).start();
  }catch( Exception e ) { System.err.println( e );}
 }

}
 


