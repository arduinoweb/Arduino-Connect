package ucc.arduino.net;

import java.io.PrintWriter;
import java.net.Socket;

public class Messenger{
        
   
   public void sendMsg( String ipAdress, int port, String msg )
   {      
           try{
               Socket socket = new Socket( ipAdress, port );        
               PrintWriter pw = new PrintWriter( socket.getOutputStream(), true );
              
              pw.println( msg );
              
              socket.close();
              
              pw = null;
              socket = null;
           }catch( Exception e ){ 
               System.err.println( e );
           }
           
           
   }
}
