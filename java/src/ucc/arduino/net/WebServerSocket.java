package ucc.arduino.net;

import ucc.arduino.main.KeyValue;
import net.tootallnate.websocket.Handshakedata;
import net.tootallnate.websocket.WebSocket;
import net.tootallnate.websocket.WebSocketServer;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TransferQueue;

public class WebServerSocket extends WebSocketServer{
        
  private WebSocketOutProcessor WEBSOCKET_OUT_PROCESSOR;
  
  public WebServerSocket( String address, int port, 
        final TransferQueue<KeyValue<Integer,Integer>> WEBSOCKET_OUT_QUEUE) 
                                              throws UnknownHostException
  {
          
      super( new InetSocketAddress( InetAddress.getByName( address ), port ) );
    //  WebSocket.DEBUG = true;
       WEBSOCKET_OUT_PROCESSOR = 
           new WebSocketOutProcessor( WEBSOCKET_OUT_QUEUE );
                                  
       new Thread( WEBSOCKET_OUT_PROCESSOR ).start();  
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
         WEBSOCKET_OUT_PROCESSOR.registerSocket( 10, webSocket );
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
