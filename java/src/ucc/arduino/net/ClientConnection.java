package ucc.arduino.net;



import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ClientConnection  implements Runnable{

   private final BufferedReader DATA_IN;
   private final PrintWriter    DATA_OUT;
   private final Socket         SOCKET;
   private String message;
   private boolean stayAlive;
   
   public ClientConnection( final Socket socket ) throws IOException
   {
      DATA_IN =  new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
      DATA_OUT = new PrintWriter( socket.getOutputStream() );
      SOCKET = socket;
      message = null;  
      stayAlive = true;
   }
   
   public synchronized String getMessage() 
   {
        return  message;
   }
   
   public synchronized void sendMessage( String msg )
   {
        DATA_OUT.println( msg );
        stayAlive = false;
   }
   
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

public synchronized boolean isAlive()
{
   return stayAlive;
}

}
