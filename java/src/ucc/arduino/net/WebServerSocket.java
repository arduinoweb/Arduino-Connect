package ucc.arduino.net;

import net.tootallnate.websocket.Handshakedata;
import net.tootallnate.websocket.WebSocket;
import net.tootallnate.websocket.WebSocketServer;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class WebServerSocket extends WebSocketServer{
        
        
  public WebServerSocket( String address, int port ) throws UnknownHostException
  {
          
      super( new InetSocketAddress( InetAddress.getByName( address ), port ) );
    //  WebSocket.DEBUG = true;
          
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
         System.out.println("message received " + message  + " from " + tmp.getAddress().getHostAddress() 
                                                      + " " + tmp.getPort());
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
