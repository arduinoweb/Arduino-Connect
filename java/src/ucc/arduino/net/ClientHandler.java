/** Class that handles messages to clients and the
  * messages received from them.
  * @author: Gary Smith
  */

package ucc.arduino.net;

import ucc.arduino.main.Arduino;
import ucc.arduino.net.ClientConnection;
import java.util.concurrent.ConcurrentLinkedQueue;


public class ClientHandler extends ConcurrentLinkedQueue<ClientConnection> implements Runnable
{
  // Message definitions, move them somewhere else eventually
  private static final String UNKNOWN_PIN = "UNKNOWN PIN";
  private static final String END_OF_MESSAGE = "E";
  private static final String BAD_MESSAGE_FORMAT = "BAD MESSAGE FORMAT";
  private static final String READ_MESSAGE = "R";
  private static final String WRITE_MESSAGE = "W";
  private static final String DIGITAL_WRITE = "D";
  private static final String ANALOG_WRITE = "A";
  private static final String OK = "OK";
  // End of Message definitions

  /** Stores whether to keep the thread alive or not */
  private boolean stayAlive;
   
  /** Constructor */
  public ClientHandler()
  {
     super();
     stayAlive = true;
  
  }

  /** Main Loop that handles messages from clients*/
  public void run()
  {
     ClientConnection clientConnection = null;
     String clientReply;
     
	 
     while( stayAlive )
     {
        clientReply = BAD_MESSAGE_FORMAT;
	  
        // Check if we have any outstanding write requests in the queue
	if( ( clientConnection = this.poll() ) != null )
        {
          String msg = clientConnection.getMessage();
          
          // There's a chance we could have a client but the
          // thread executor hasn't had a chance to invoke the thread yet
          // or the client has connected but not sent anything yet.
          if( msg != null)
          {
            String[] msgParts = msg.split(" " );

            // Any message has a minimum length and end of message marker
	    if( msgParts.length > 2 && 
	     ( msgParts[ msgParts.length -1 ].equals( END_OF_MESSAGE ) ) )
	    {
	     
             if( msgParts[ 0 ].equals( READ_MESSAGE ) )
	     {
	       clientReply = processRead( msgParts );
	     }
	     else if( msgParts[ 0 ].equals( WRITE_MESSAGE )  )
	     {
	        clientReply = processWrite( msgParts );
	     }
             
            clientConnection.sendMessage( clientReply );

	    clientConnection.close();
	  }
         }
         else if( clientConnection.isAlive() )
         {
           //Client might not have had time to send a message
           // so if it's connection hasn't timed out add it to the end
           // of the queue again.
           this.add( clientConnection);
         }
	    
	
        }
	 
     }
     
  }
  
  /** Processes write requests from clients
    * @param: msg - an array representing a client message
    * @return: String - the reply to send back to the client
    */
  private String processWrite( String[] msg )
  {

    boolean noError = true;
    String clientReply = OK;
    int indexer = 1;
    
    while( indexer < msg.length - 1 && noError )
     {      
        // Check if the message starts with a valid character
        if( msg[indexer].equals( DIGITAL_WRITE ) ||
            msg[indexer].equals( ANALOG_WRITE ) )
        {
            byte mode = (byte) msg[indexer].charAt( 0 );

            try{
              indexer++;    
              Integer pinNumber = Integer.parseInt(  msg[ indexer ] );
              indexer++;
              Integer pinValue = Integer.parseInt( msg[ indexer ] );
              // If we have a valid message add it to the write queue
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
  
  /** Processes client read requests
    * @param: msg - an array containing the read request
    */
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
	
            // We through an error if a pin is unknown to us.
            // Probably change this approach.                              
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