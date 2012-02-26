/** Class that represents a client connection
  * @author: Gary Smith
  */

package ucc.arduino.net;


import ucc.arduino.main.Pin;
import ucc.arduino.main.PinMap;
import ucc.arduino.net.MessageProcessor;

import ucc.arduino.net.MessageFormatChecker;

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
   

   
 public void run()
 {
   
   String clientReply = "{}";
   try{
       message = DATA_IN.readLine();
       message = message.trim();
  
       MessageFormatChecker messageFormatChecker =
                             new MessageFormatChecker();
     
       if( messageFormatChecker.isValidMessageFormat( message ) )
       {
           message = message.substring( 2, message.length() - 1 );    
           if( messageFormatChecker.isReadMessage() )
           {
              clientReply = 
                 MessageProcessor.processRead(message, PIN_MAP );
           }
           else if( messageFormatChecker.isWriteMessage() )
           {
              clientReply =     
                 MessageProcessor.processWrite( message,  
                                               PIN_MAP, SERIAL_OUTPUT_QUEUE );
           }
               
               
       }
       
    messageFormatChecker = null;
       
    sendMessage( clientReply );
       
   }catch(SocketTimeoutException soe){
      System.err.println( soe.getMessage() );
    
   }catch( IOException ioe ){
      System.err.println( ioe.getMessage() );   
     
   }

   clientReply = null;
   
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
      SOCKET = null;
      message = null;
      PIN_MAP = null;
      SERIAL_OUTPUT_QUEUE = null;
   }

}
