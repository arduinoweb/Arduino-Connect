/**
  * @author: Gary Smith
  ** <p>The entry point to the Arduino program,
  * Handles initialisation and
  * the connecting of clients via sockets</p>
  */


package ucc.arduino.main;

import ucc.arduino.configuration.Configuration;
import ucc.arduino.scripting.Scripter;
import ucc.arduino.net.WebServerSocket;

import ucc.arduino.main.PinMap;
import ucc.arduino.main.KeyValue;

import ucc.arduino.net.ClientConnection;
import ucc.arduino.serial.SerialComm;

import ucc.arduino.main.KeyValue;
import ucc.arduino.net.NetworkRegister;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

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

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;
public class Arduino{
     /** Stores the Configuration info supplied by the user*/
    public static Configuration CONFIGURATION = new Configuration( );
    /**The map of (pin,value) that clients are fed data from and the Arduino writes to*/
    private static PinMap PIN_MAP;

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
    /** Thread that runs a user supplied script*/
    private static Scripter scripter;
    /**Queue that feeds Scripter class with details of pin changes*/
    private static TransferQueue<KeyValue<Integer,Integer>> 
      SCRIPT_INVOCATION_QUEUE = new LinkedTransferQueue<KeyValue<Integer,Integer>>();
    /** Queue that stores the messages received via serial port from the Arduino*/
    private static TransferQueue< Integer > serialInputQueue;
    /**A Queue of the write requests received from clients*/
    private static TransferQueue<Pin> serialOutputQueue;

    private final TransferQueue<KeyValue<Integer,Integer>> WEBSOCKET_UPDATE_QUEUE;
    
    
    /**Constructor*/
    public Arduino( File configurationFile ) throws Exception {
        CONFIGURATION = new Configuration( configurationFile );
        PIN_MAP = new PinMap( );
       
       
        
       if( CONFIGURATION.getNetworkRegister().equals("yes") )
       {
         networkRegister = new NetworkRegister(
                              CONFIGURATION.getNetworkName(),
                              
                              CONFIGURATION.getNetworkAddress().getHostAddress(),
                              CONFIGURATION.getNetworkPort(),
                              CONFIGURATION.getWebServerUrl() );
        
        
       registrationService = Executors.newScheduledThreadPool( 1 );
	
       
       registrationService.scheduleWithFixedDelay( networkRegister,
	  0, CONFIGURATION.getNetworkRegistrationRate(), TimeUnit.SECONDS 
	                                           );
       }
       
      serverSocket = new ServerSocket( CONFIGURATION.getNetworkPort(),
                                         CONFIGURATION.getNetworkQueueLength(),
                                         CONFIGURATION.getNetworkAddress() );
        
	
     scripter = null;
     
     if( CONFIGURATION.getScriptName() != null )
    {
     try{
        scripter = new Scripter(  new File( CONFIGURATION.getScriptName()), 
                                                    SCRIPT_INVOCATION_QUEUE,
                                  CONFIGURATION.getUseInvocationThread().equals("yes"));
         new Thread( scripter).start();
         PIN_MAP.enableScripting( SCRIPT_INVOCATION_QUEUE );
         
         }catch( FileNotFoundException fnfe ){
            
            System.err.println( fnfe );
         }catch( IOException ioe ){
            System.err.println( ioe );       
         }catch( ScriptException se ){
            System.err.println( se );       
         }
     }
   
     String serialPort = CONFIGURATION.getSerialPort();
     
     
     if( ! serialPort.equals("none") )
     {        
              /** Property used by RxTx class*/
        System.setProperty("gnu.io.rxtx.SerialPorts",
                            serialPort );
       serialInputQueue = new LinkedTransferQueue< Integer >();
       serialOutputQueue = new LinkedTransferQueue<Pin>();
       serialComm = new SerialComm( CONFIGURATION );
         try{ 
	     serialComm.connect(serialOutputQueue,
	                        serialInputQueue,
	                        PIN_MAP);
         

	   }catch( Exception e ){
	    System.err.println( e );
	     System.err.println("Exception in thread creation");
	     System.exit( 1 );
	   }
	
     }
    
     
     serialPort = null;
     WEBSOCKET_UPDATE_QUEUE = new LinkedTransferQueue< KeyValue<Integer,Integer>>();
     PIN_MAP.enableWebSocketQueue( WEBSOCKET_UPDATE_QUEUE );
     new WebServerSocket( "192.168.1.103", 10004, PIN_MAP, 
                           serialOutputQueue,WEBSOCKET_UPDATE_QUEUE).start();
    /** try{
             
      new Thread(new WebSocketServer(EXECUTOR_SERVICE )).start();       
     }catch(UnknownHostException uhe ){
             System.err.println( uhe );
     }catch( IOException ioe ){
             System.err.println( ioe );
     }*/
     
     }
   
  /** Main routine that waits for a client connection
   */	
  public void start()
  {

   System.out.println("Arduino Connect Started.");
	
   while( true )
   {
	  
      try{
	    client = serverSocket.accept();
	    System.out.println("Client Connected");
             
            client.setSoTimeout( CONFIGURATION.getNetworkTimeout() );
            ClientConnection tmp = 
                 new ClientConnection( client, PIN_MAP, serialOutputQueue );
              
            EXECUTOR_SERVICE.execute( tmp );
                
             
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
        serverSocket.close();
        EXECUTOR_SERVICE.shutdown();
        System.exit( 0 );
	
    }catch( Exception e ){ }
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
 


