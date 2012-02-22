package ucc.arduino.net;

import ucc.arduino.main.KeyValue;
import ucc.arduino.net.MessageFormatChecker;


import net.tootallnate.websocket.Handshakedata;
import net.tootallnate.websocket.WebSocket;
import net.tootallnate.websocket.WebSocketServer;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TransferQueue;

public class WebServerSocket extends WebSocketServer{
        
  private WebSocketUpdateProcessor WEBSOCKET_UPDATE_PROCESSOR;
  
  private final MessageFormatChecker messageFormatChecker;
  
  public WebServerSocket( String address, int port, 
        final TransferQueue<KeyValue<Integer,Integer>> WEBSOCKET_UPDATE_QUEUE) 
                                              throws UnknownHostException
  {
          
      super( new InetSocketAddress( InetAddress.getByName( address ), port ) );
    //  WebSocket.DEBUG = true;
       WEBSOCKET_UPDATE_PROCESSOR = 
           new WebSocketUpdateProcessor( WEBSOCKET_UPDATE_QUEUE );
                                  
      new Thread( WEBSOCKET_UPDATE_PROCESSOR ).start();
      messageFormatChecker = new MessageFormatChecker();
  }
        
        
        
   
        
        
 @Override 
 public void onError( WebSocket webSocket, Exception exception)
 {
         
 }
 
 @Override
 public void onClientMessage( WebSocket webSocket, String message )
 {
         //System.out.println("mesage received: " + message );
         InetSocketAddress tmp = webSocket.getRemoteSocketAddress();
         System.out.println("message received " + message  + " from "
                                + tmp.getAddress().getHostAddress() 
                                + " " + tmp.getPort());
         

          if( messageFormatChecker.isValidMessageFormat( message ) )
          {
             if( messageFormatChecker.isRegisterMessage() )
             {
                 System.out.println( "Read message received" );
                 int[] pins = messageFormatChecker.getPinsToRegister();
                 System.out.println("Pins to read: " );
                 for( int pin : pins )
                 {
                    System.out.print( pin + " ");
                    WEBSOCKET_UPDATE_PROCESSOR.registerSocket( pin, webSocket );
                 }
                 
                 System.out.println();
             }
             else if( messageFormatChecker.isWriteMessage() )
             {
                 System.out.println("Write message received" );       
             }
          }
 }
 
 @Override
 public void onClientClose( WebSocket webSocket, int code, String reason,
                                                           boolean remote)
 {
         System.out.println("close received");
         webSocket.closeConnection( 1, "goodbye", false );
         
 }
 
 @Override
 public void onClientOpen( WebSocket webSocket, Handshakedata handshake )
 {
         System.out.println("Client Connected");
         
         
 }
        
}
