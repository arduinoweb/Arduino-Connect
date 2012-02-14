package ucc.arduino.net;

import ucc.arduino.net.HandShaker;

import java.net.Socket;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.PrintWriter;

import java.io.IOException;


public class WebSocket implements Runnable {

   private final BufferedReader BUFFERED_READER;
   private final InputStream INPUTSTREAM;
   private final OutputStream OUTPUTSTREAM;
   private final PrintWriter PRINT_WRITER;
   private  HandShaker handShaker;
   
   private final Socket SOCKET;
   
   public WebSocket( final Socket SOCKET ) throws IOException
   {

       this.SOCKET = SOCKET;
       INPUTSTREAM = this.SOCKET.getInputStream();
       OUTPUTSTREAM = this.SOCKET.getOutputStream();
       BUFFERED_READER = new BufferedReader(
                                   new InputStreamReader( INPUTSTREAM ) );
       PRINT_WRITER = new PrintWriter( OUTPUTSTREAM );      
       handShaker = new HandShaker( BUFFERED_READER, PRINT_WRITER );     
   }
        
        
   public void run()
   {
      
    
      System.out.println("WebSocket Run Method");
    
      if( handShaker.doHandshake() )
      {
         System.out.println( "Handshake Succeeded");
         handShaker = null;
      }
           
   }
        
        
        
}
