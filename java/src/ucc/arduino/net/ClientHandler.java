package ucc.arduino.net;

import ucc.arduino.main.Arduino;

import ucc.arduino.net.ClientConnection;

import java.util.concurrent.ConcurrentLinkedQueue;

import java.util.Scanner;

public class ClientHandler extends ConcurrentLinkedQueue<ClientConnection> implements Runnable
{
  private static final String UNKNOWN_PIN = "UNKNOWN PIN";
  private static final String END_OF_MESSAGE = "E";
  private static final String BAD_MESSAGE_FORMAT = "BAD MESSAGE FORMAT";
  private static final String READ_MESSAGE = "R";
  private static final String WRITE_MESSAGE = "W";
  private static final String DIGITAL_WRITE = "D";
  private static final String ANALOG_WRITE = "A";
  private static final String OK = "OK";
  private static final int MIN_MESSAGE_SIZE =  3;
  private static final int MAX_MESSAGE_SIZE = 100;
  
  private boolean stayAlive;
   
  public ClientHandler()
  {
     super();
     stayAlive = true;
  
  }

  public void run()
  {
     ClientConnection clientConnection = null;
     String clientReply;
     
	 
     while( stayAlive )
     {
        clientReply = BAD_MESSAGE_FORMAT;
	  
	if( ( clientConnection = this.poll() ) != null )
        {
          String msg = clientConnection.getMessage();
         
          String[] msgParts = msg.split(" " );

	  if( msgParts.length > 2 && 
	     ( msgParts[ msgParts.length -1 ].equalsIgnoreCase( END_OF_MESSAGE ) ) )
	  {
	     if( msgParts[ 0 ].equalsIgnoreCase( READ_MESSAGE ) )
	     {
	       clientReply = processRead( msgParts );
	     }
	     else if( msgParts[ 0 ].equals( WRITE_MESSAGE )  )
	     {
	        clientReply = processWrite( msgParts );
	     }
	  }
	    
	  clientConnection.sendMessage( clientReply );

	    clientConnection.close();
        }
	 
     }
     
  }
  
  private String processWrite( String[] msg )
  {

    boolean noError = true;
    String clientReply = OK;
    int indexer = 1;
    
    while( indexer < msg.length - 1 && noError )
     {      
        if( msg[indexer].equalsIgnoreCase( DIGITAL_WRITE ) ||
            msg[indexer].equalsIgnoreCase( ANALOG_WRITE ) )
        {
            byte mode = (byte) msg[indexer].charAt( 0 );

            try{
              indexer++;    
              Integer pinNumber = Integer.parseInt(  msg[ indexer ] );
              indexer++;
              Integer pinValue = Integer.parseInt( msg[ indexer ] );
                               
              System.out.println("Write Request: " + ((char) mode) + " "
                                                   + pinNumber + " "
                                                   + pinValue + " ");
              Arduino.writeQueueAdd( mode, pinNumber, pinValue );       
                    
            }catch( NumberFormatException nfe ){
                    System.err.println( nfe );
                    noError = false;
                    clientReply = BAD_MESSAGE_FORMAT;
            }
            
        }
        else
        {
          noError = false;
          clientReply = BAD_MESSAGE_FORMAT;
        }
        
        indexer++;
             
     }
   
     return clientReply;
  }
  
  private String processRead( String[] msg )
  {
    Integer pinValue = 1 ;
    String clientReply = BAD_MESSAGE_FORMAT;
    StringBuffer replyBuilder = new StringBuffer();
    int indexer = 1;
    
    while( indexer < msg.length -1 && pinValue != null )
    {
	                            
       try{
	    Integer pinNumber = Integer.parseInt( msg[ indexer ] );
	                             
	    pinValue = Arduino.getPin( pinNumber );
	                              
	    if( pinValue == null )
	    {
	       clientReply = UNKNOWN_PIN; 
	    }
	    else
	    {
	       replyBuilder.append( pinValue + " " );
	    }
	                             
	    indexer++;
	                             
	}catch( NumberFormatException nfe ){
	     pinValue = null;        
	}
    }
	                       
    if( pinValue != null )
    {
        clientReply = replyBuilder.toString().trim();
    }
	                            
    return clientReply;      
  }
  
  public void stop()
  {
        stayAlive = false;
  
  }

}