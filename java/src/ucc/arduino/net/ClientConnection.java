package ucc.arduino.net;



import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.Socket;

public class ClientConnection  {

   private final BufferedReader DATA_IN;
   private final PrintWriter    DATA_OUT;
   private final Socket         SOCKET;
   private String message;
   
  
   private boolean communicationComplete;
  
   public ClientConnection( final Socket socket ) throws IOException
   {
      DATA_IN =  new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
	  
	  DATA_OUT = new PrintWriter( socket.getOutputStream() );
	  
	  SOCKET = socket;
	  
	  communicationComplete = false;
	  message = null;
	  retrieveMessage();
	  
   }
   
   public synchronized boolean isComplete()
   {
       return communicationComplete;
   }
   
   public synchronized String getMessage() 
   {
        return  message;
   }
   
   public synchronized void sendMessage( String msg )
   {
        DATA_OUT.println( msg );
        communicationComplete = true;
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
   
   }
   

   public void retrieveMessage()
   {
       try{
	       message = DATA_IN.readLine();
		   
	   
	   }catch( IOException ioe ){
	      System.err.println( ioe );
		  communicationComplete = true;
		  close();
	   }
   
   
   }
   


}
