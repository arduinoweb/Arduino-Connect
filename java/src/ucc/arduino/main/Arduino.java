/**
  * @author: Gary Smith
  ** <p>The entry point to the Arduino program,
  * Handles initialisation and
  * the connecting of clients via sockets</p>
  */


package ucc.arduino.main;

import ucc.arduino.configuration.Configuration;
import ucc.arduino.scripting.Scripter;
import ucc.arduino.net.DeviceList;
import ucc.arduino.net.Device;

import ucc.arduino.net.ClientConnection;
import ucc.arduino.serial.SerialComm;
import ucc.arduino.net.ClientHandler;
import ucc.arduino.main.Pin;
import ucc.arduino.net.NetworkRegister;

import java.net.ServerSocket;
import java.net.Socket;


import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import java.util.Properties;
import javax.script.*;


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
    /**Thread that regularly notifies web server of device presence*/
    private static NetworkRegister networkRegister;
    /**Schedules and runs threads that are to be run periodically*/
    private static ScheduledExecutorService registrationService;
    
    private static Scripter scripter;
    
    private static DeviceList deviceListRetriever;
    
    /**Constructor*/
    public Arduino( File configurationFile ) throws Exception {
        CONFIGURATION = new Configuration( configurationFile );
      
       
        System.setProperty("gnu.io.rxtx.SerialPorts",
                            CONFIGURATION.getSerialPort() );
        
        networkRegister = new NetworkRegister(
                              CONFIGURATION.getArduinoNetworkName(),
                              
                              CONFIGURATION.getNetworkAddress().getHostAddress(),
                              CONFIGURATION.getNetworkPort(),
                              CONFIGURATION.getDeviceRegistrationUrl() );
        
        deviceListRetriever = new DeviceList();
        
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
        scripter = null;
         if( CONFIGURATION.getScriptName() != null )
        {
         try{
        scripter = new Scripter(  new File( CONFIGURATION.getScriptName()) );
        new Thread( scripter).start();
         }catch( FileNotFoundException fnfe ){
            System.err.println( fnfe );
         }catch( IOException ioe ){
            System.err.println( ioe );       
         }catch( ScriptException se ){
            System.err.println( se );       
         }
	}
         
	 serialComm = new SerialComm( scripter );
	
         try{
	     serialComm.connect( );
         
	     new Thread( serialComm).start();

	   }catch( Exception e ){
	     System.err.println( e );
	     System.err.println("Exception in thread creation");
	     System.exit( 1 );
	   }
	
	   
	 registrationService = Executors.newScheduledThreadPool( 2 );
	 
	registrationService.scheduleWithFixedDelay( networkRegister,
	                              
	   0, CONFIGURATION.getArduinoNetworkRegistrationRate(), TimeUnit.SECONDS 
	                                           );
	registrationService.scheduleWithFixedDelay( deviceListRetriever,
	                              
	   2, CONFIGURATION.getArduinoNetworkRegistrationRate()+2, TimeUnit.SECONDS 
	                                           );
	 
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
                client = null;
                tmp = null;
		 
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

 
 public static synchronized Device getDevice( String deviceName )
 {
       return deviceListRetriever.get( deviceName );       
         
 }
 
 
 public static synchronized HashMap<Integer,Integer> copyPins()
 {
    return new HashMap<Integer,Integer>( PINS );       
         
 }
/** Main entry point */
 public static void main(String[] args)  {
 try{
    
    
    if( args.length == 0 )
    {
       new Arduino( new File( "configuration.properties") ).start();       
    }
   else if( args.length == 1 )
   {
       new Arduino( new File( args[0] ) ).start();     
   }
   else
   {
       System.err.println("invalid number of arguments");       
   }
    
   
           
  }catch( Exception e ) { System.err.println( e );}
 
 }
 
}
 


