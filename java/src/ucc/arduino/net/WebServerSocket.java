package ucc.arduino.net;

import ucc.arduino.main.KeyValue;
import ucc.arduino.main.PinMap;
import ucc.arduino.main.Pin;

import ucc.arduino.net.MessageFormatChecker;
import ucc.arduino.net.MessageProcessor;


import net.tootallnate.websocket.Handshakedata;
import net.tootallnate.websocket.WebSocket;
import net.tootallnate.websocket.WebSocketServer;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TransferQueue;

public class WebServerSocket extends WebSocketServer{
        
  private WebSocketUpdateProcessor WEBSOCKET_UPDATE_PROCESSOR;
  private final PinMap PIN_MAP;
  private final TransferQueue< Pin > SERIAL_OUTPUT_QUEUE;
  
  private final MessageFormatChecker messageFormatChecker;
  
  public WebServerSocket( String address, int port, 
        final PinMap PIN_MAP,
        final TransferQueue<Pin> SERIAL_OUTPUT_QUEUE,
        final TransferQueue<KeyValue<Integer,Integer>> WEBSOCKET_UPDATE_QUEUE) 
                                              throws UnknownHostException
  {
          
      super( new InetSocketAddress( InetAddress.getByName( address ), port ) );
    //  WebSocket.DEBUG = true;
       WEBSOCKET_UPDATE_PROCESSOR = 
           new WebSocketUpdateProcessor( WEBSOCKET_UPDATE_QUEUE );
                                  
      new Thread( WEBSOCKET_UPDATE_PROCESSOR ).start();
      messageFormatChecker = new MessageFormatChecker();
      this.PIN_MAP = PIN_MAP;
      this.SERIAL_OUTPUT_QUEUE = SERIAL_OUTPUT_QUEUE;
  }
        
        
        
   
        
        
 @Override 
 public void onError( WebSocket webSocket, Exception exception)
 {
         System.err.println( exception );
         System.err.println("Error occurred");
         webSocket.closeConnection( 1, "goodbye", false );
 }
 
 @Override
 public void onClientMessage( WebSocket webSocket, String message )
 {
     InetSocketAddress tmp = webSocket.getRemoteSocketAddress();
     String clientReply = "{}";
      if( messageFormatChecker.isValidMessageFormat( message ) )
      {
             message = message.substring( 2, message.length() - 1 );
             
             if( messageFormatChecker.isRegisterMessage() )
             {
                
                int[] pins = MessageProcessor.extractPinNumbers(
                                  message );
                 
                 for( int pin : pins )
                 { 
                    WEBSOCKET_UPDATE_PROCESSOR.registerSocket( pin, webSocket );
                 }
                 
              MessageProcessor.processRead( message, PIN_MAP );
              clientReply = "{\"OK\"}";                
             }
             else if( messageFormatChecker.isWriteMessage() )
             {
                 clientReply = MessageProcessor.processWrite( message,
                                                PIN_MAP,
                                                SERIAL_OUTPUT_QUEUE );
                 
             }
       }
       
       try{
            webSocket.send( clientReply );   
       }catch(InterruptedException ie ){
               System.err.println( ie );
       }
       
       clientReply = null;
       
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
