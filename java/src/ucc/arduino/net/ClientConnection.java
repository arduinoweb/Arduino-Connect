/** Class that represents a client connection
  * @author: Gary Smith
  */

package ucc.arduino.net;


import ucc.arduino.main.Pin;
import ucc.arduino.main.PinMap;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import ucc.arduino.configuration.Protocol;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import java.util.concurrent.TransferQueue;

public class ClientConnection  implements Runnable{
   /** The inputstream side of the clients socket */
   private  BufferedReader DATA_IN;
   /** The outputstream side of the clients socket */
   private  PrintWriter    DATA_OUT;
   /** The clients socket */
   private  Socket         SOCKET;
   /** The message received from the client via the inputstream */
   private String message;
   /** Indicates whether to keep the thread alive or not*/
   private boolean stayAlive;

   private  PinMap PIN_MAP;
   private  TransferQueue< Pin > SERIAL_OUTPUT_QUEUE;
   
   /** Constructor
     * @param: socket - the client socket 
     */
   public ClientConnection( final Socket socket, 
                            final PinMap pinMap,
                            final TransferQueue< Pin> SERIAL_OUTPUT_QUEUE ) throws IOException
   {
      DATA_IN =  new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
      DATA_OUT = new PrintWriter( socket.getOutputStream(), true );
      SOCKET = socket;
      message = null;  
      stayAlive = true;
      PIN_MAP = pinMap;
      this.SERIAL_OUTPUT_QUEUE = SERIAL_OUTPUT_QUEUE;
    
   }
   
 
   /** Send a message to the client
     * @param: msg - the message to send
     */
   private void sendMessage( String msg )
   {
        DATA_OUT.println( msg );
        close(); 
   }
   
   /** Shuts down the client and terminate the thread */
   public  void close( )
   {
       try{
	    
            DATA_OUT.flush();
	    DATA_IN.close();
	    DATA_OUT.close();
	    SOCKET.close();
	      
	   }catch( IOException ioe ){
	      System.err.println( ioe );
	   }
    
      DATA_OUT = null;
      DATA_IN = null;
     // DATA_OUT = null;
      SOCKET = null;
      message = null;
      stayAlive = false;
      PIN_MAP = null;
      SERIAL_OUTPUT_QUEUE = null;
   }
   
 /** Waits for client to send a message or an exception occurs,
   * at the moment we only wait for one message then close
   * the connection, the thread is kept alive in case the
   * thread Executor tries to reclaim and use the thread
   * before we've had a chance to retreive the message
   */
 public void run()
 {
   String[] msgParts = null;
   String clientReply = Protocol.BAD_MESSAGE_FORMAT;
   try{
        message = DATA_IN.readLine();
       message = message.trim();

       System.out.println("Message Received: " + message );
      
       msgParts = message.split(" ");
         
       if( msgParts.length > 2 &&
                 (msgParts[msgParts.length - 1].equals( 
                                           Protocol.END_OF_CLIENT_MESSAGE ) ) )
       {
          if( msgParts[0].equals( Protocol.READ_MESSAGE ) )
          {
                    
                 clientReply = processRead( msgParts );   
          }
          else if( msgParts[0].equals( Protocol.WRITE_MESSAGE ) )
          {
                 clientReply = processWrite( msgParts );   
                    
          }
         
         }
               
       System.out.println( "Reply to client: " + clientReply );
          sendMessage( clientReply ); 
   }catch(SocketTimeoutException soe){
      System.err.println( soe.getMessage() );
    
   }catch( IOException ioe ){
      System.err.println( ioe.getMessage() );   
     
   }

   msgParts = null;
   clientReply = null;
   
 }



 private String processRead( String[] msg )
  {
    Integer pinValue = 1 ;
    String clientReply = Protocol.BAD_MESSAGE_FORMAT;
    StringBuffer replyBuilder = new StringBuffer();
    int indexer = 1;
   System.out.println("In processRead");
    while( indexer < msg.length -1 && pinValue != null )
    {
	                            
       try{
	    Integer pinNumber = Integer.parseInt( msg[ indexer ] );
	                             
	    pinValue = PIN_MAP.update( pinNumber, null );
	    System.out.println( "Process Read pinValue: " + pinValue );
            // If we don't have a record of the pin requested
            // mark it's place in the reply with a 'x'                              
	    if( pinValue == null )
	    {
	       //clientReply = Protocol.UNKNOWN_PIN; 
               replyBuilder.append( Protocol.UNKNOWN_PIN + " " );
	    }
	    else
	    {
	       replyBuilder.append( pinValue + " " );
	    }
	                             
	    indexer++;
	                             
	}catch( NumberFormatException nfe ){
	     pinValue = null;   
	     System.err.println( nfe );
	}
    }
     
    	                       
    if( pinValue != null )
    {
        clientReply = replyBuilder.toString().trim();
    }
	                            
    return clientReply;      
  }

  
 private String processWrite( String[] msg )
  {

    boolean noError = true;
    String clientReply = Protocol.OK;
    int indexer = 1;
    
    while( indexer < msg.length - 1 && noError )
     {      
        // Check if the message starts with a valid character
        if( msg[indexer].equals( Protocol.DIGITAL_WRITE ) ||
            msg[indexer].equals( Protocol.ANALOG_WRITE ) )
        {
            byte mode = (byte) msg[indexer].charAt( 0 );

            try{
              indexer++;    
              Integer pinNumber = Integer.parseInt(  msg[ indexer ] );
              indexer++;
              Integer pinValue = Integer.parseInt( msg[ indexer ] );
              // If we have a valid message add it to the write queue
              System.out.println("Adding: " + mode + " " + pinNumber + " "
                                     + pinValue + " to serial write queue");
            SERIAL_OUTPUT_QUEUE.add( new Pin( mode, pinNumber, pinValue) );       
                    
            }catch( NumberFormatException nfe ){
                    noError = false;
                    clientReply = Protocol.BAD_MESSAGE_FORMAT;
            }
        }
        else
        {
          noError = false;
          clientReply = Protocol.BAD_MESSAGE_FORMAT;
        }
        
        indexer++;
             
     }
   
     return clientReply;
  }

}
