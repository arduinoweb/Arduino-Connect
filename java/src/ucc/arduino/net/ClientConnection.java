/** Class that represents a client connection
  * @author: Gary Smith
  */

package ucc.arduino.net;



import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ClientConnection  implements Runnable{
   /** The inputstream side of the clients socket */
   private final BufferedReader DATA_IN;
   /** The outputstream side of the clients socket */
   private final PrintWriter    DATA_OUT;
   /** The clients socket */
   private final Socket         SOCKET;
   /** The message received from the client via the inputstream */
   private String message;
   /** Indicates whether to keep the thread alive or not*/
   private boolean stayAlive;
   
   /** Constructor
     * @param: socket - the client socket 
     */
   public ClientConnection( final Socket socket ) throws IOException
   {
      DATA_IN =  new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
      DATA_OUT = new PrintWriter( socket.getOutputStream() );
      SOCKET = socket;
      message = null;  
      stayAlive = true;
   }
   
   /** Retreive the message sent from the client
     * @return: String - the message received otherwise null if nothing received
     */
   public synchronized String getMessage() 
   {
        return  message;
   }
   
   /** Send a message to the client
     * @param: msg - the message to send
     */
   public synchronized void sendMessage( String msg )
   {
        DATA_OUT.println( msg );
        stayAlive = false;
   }
   
   /** Shuts down the client and terminate the thread */
   public synchronized void close( )
   {
       try{
	    
            DATA_OUT.flush();
	    DATA_IN.close();
	    DATA_OUT.close();
	    SOCKET.close();
	      
	   }catch( IOException ioe ){
	      System.err.println( ioe );
	   }
    
      stayAlive = false;
   }
   
 /** Waits for client to send a message or an exception occurs,
   * at the moment we only wait for one message then close
   * the connection, the thread is kept alive in case the
   * thread Executor tries to reclaim and use the thread
   * before we've had a chance to retreive the message
   */
 public void run()
 {
   try{
       message = DATA_IN.readLine();
   }catch(SocketTimeoutException soe){
      System.err.println( soe.getMessage() );
      close();
   }catch( IOException ioe ){
      System.err.println( ioe.getMessage() );   
      close();
   }
   

 while( stayAlive ) { };

   
 }

/** Check whether the thread is still alive
  * @return: boolean  True if it is otherwise false
  */
public synchronized boolean isAlive()
{
   return stayAlive;
}

}
