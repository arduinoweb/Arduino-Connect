package ucc.arduino.net;

import java.util.HashMap;
import sun.misc.BASE64Encoder;
import java.security.MessageDigest;
import java.io.UnsupportedEncodingException;
import java.io.IOException;

import java.security.NoSuchAlgorithmException;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class HandShaker{

 private final HashMap<String, String> HEADERS;
 private StringBuffer response;
 private final BufferedReader BUFFERED_READER;
 private final PrintWriter PRINT_WRITER;
 
 public HandShaker(final BufferedReader BUFFERED_READER,
                   final PrintWriter PRINT_WRITER )
 {
   HEADERS = new HashMap<String, String>();
   this.BUFFERED_READER = BUFFERED_READER;
   this.PRINT_WRITER = PRINT_WRITER;
         
 }

 public boolean doHandshake()
 {
    boolean handshakeSent = false;
    String[] headerParts = null;
    String dataIn = null;
    try{
           dataIn = BUFFERED_READER.readLine();
           System.out.println( dataIn ); 
           
           if( dataIn.startsWith( "GET") && dataIn.endsWith( "HTTP/1.1") )
           {
              while(  (dataIn = BUFFERED_READER.readLine()) != null && dataIn.trim().length() != 0  )
              {
                      dataIn = dataIn.trim();
                      headerParts = dataIn.split(": ");
                   
                      if( headerParts.length == 2 )
                      {
                         System.out.println( dataIn );    
                         HEADERS.put( headerParts[0].trim(), headerParts[1].trim() );       
                              
                      }
                      else
                      {
                         throw new IOException();       
                              
                      }
                    System.out.println("dataIn.length = " + dataIn.length() );  
              }
                   
           }
           else
           {
              System.out.println("Get header bad");
           }
           
           System.out.println("END OF HEADERS");
           
           if( HEADERS.size() > 0 )
           {
             String response = getHandShakeResponse();
             System.out.println("Response is:");
             System.out.println( response );
            
             if( response != null )
             {
                     
               PRINT_WRITER.print( response );
               PRINT_WRITER.flush();
               handshakeSent = true;
             }
         
             response = null;
           }
         
       }catch( IOException ioe ){
           System.err.println( ioe );        
       }
       
       headerParts = null;       
        
       return handshakeSent;
         
 }
 
 public String getHandShakeResponse()
 {
     String key = "";
 
     if( HEADERS.containsKey( "Sec-WebSocket-Key") &&
         ( key = HEADERS.get( "Sec-WebSocket-Key") ) != null ) 
     {
     
         key+="258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
     try{    
         MessageDigest messageDigest = MessageDigest.getInstance("SHA");
         byte[] hash = messageDigest.digest( key.getBytes("UTF8") );
         String base64 = new BASE64Encoder().encode( hash );
         
         response = new StringBuffer();
         response.append( "HTTP/1.1 101 Switching Protocols\r\n");
         response.append( "Upgrade: websocket\r\n");
         response.append( "Connection: Upgrade\r\n");
         response.append( "Sec-WebSocket-Accept: " );
         response.append( base64 );
         response.append("\r\n\r\n");
       }catch(UnsupportedEncodingException uee ){
        System.err.println( uee );       
       }catch(NoSuchAlgorithmException nsae ){
        System.err.println( nsae );
       }
                 
             
     }
             
         
      return response.toString();   
 }

}
