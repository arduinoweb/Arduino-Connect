package ucc.arduino.net;

import ucc.arduino.main.KeyValue;

import net.tootallnate.websocket.WebSocket;


import java.util.concurrent.TransferQueue;
import java.util.concurrent.LinkedTransferQueue;

import java.util.concurrent.ConcurrentHashMap;

import java.util.concurrent.CopyOnWriteArraySet;

public class WebSocketUpdateProcessor implements Runnable{

  private final TransferQueue<KeyValue<Integer,Integer>> WEBSOCKET_UPDATE_QUEUE;
  private final 
   ConcurrentHashMap<Integer, CopyOnWriteArraySet<WebSocket>> REGISTERED_SOCKETS;
  
  public WebSocketUpdateProcessor( 
          final TransferQueue<KeyValue<Integer,Integer>> WEBSOCKET_UPDATE_QUEUE )
  {
     this.WEBSOCKET_UPDATE_QUEUE = WEBSOCKET_UPDATE_QUEUE;       
     REGISTERED_SOCKETS = new ConcurrentHashMap< Integer, 
                              CopyOnWriteArraySet<WebSocket>>();        
  }


  public void run()
  {
      CopyOnWriteArraySet<WebSocket> tmpSet = null;
      KeyValue<Integer, Integer> updatedPin = null;
      String message = null;
      
      while( true )
      {
       try{
         
         updatedPin = WEBSOCKET_UPDATE_QUEUE.take();
      
        if( ( tmpSet = REGISTERED_SOCKETS.get( updatedPin.getKey() ) ) != null )
         {
                 message = "{\"pin\":"+updatedPin.getKey()+",\"value\":"
                 +updatedPin.getValue()+"}";

           for( WebSocket tmpWebSocket : tmpSet )
           {
             try{       
             	  if( tmpWebSocket.isClosed() )
             	  {
             	    if( tmpSet.remove( tmpWebSocket ) )
             	    {
             	      System.out.println("Socket in set - now removed");
             	    }
             	    else
             	    {
             	      System.out.println("Socket not in Set");
             	    }
                  }
             	  else
             	  {
             	    tmpWebSocket.send( message );
             	  }
                  
             	  tmpWebSocket = null;
             	  
             }catch( Exception ie ){
                     System.err.println( ie );
             }
           }
         }
       
       }catch(InterruptedException ie ){
                System.err.println( ie );
       }
       
        updatedPin = null;
        tmpSet = null;
        message = null;
      }
          
  }
  
  public void registerSocket( Integer pinNumber, WebSocket webSocket )
  {
        CopyOnWriteArraySet<WebSocket> existingSet = 
                                         REGISTERED_SOCKETS.get( pinNumber );
        CopyOnWriteArraySet<WebSocket> tmpSet = null;
        System.out.println("In registerSocket" );
        if( existingSet == null )
        {
           System.out.println("Existing set is null");
          tmpSet = new CopyOnWriteArraySet<WebSocket>();
          tmpSet.add( webSocket );
          REGISTERED_SOCKETS.put( pinNumber, tmpSet );
          
                
        }
        else
        {
          System.out.println("Set exists");
          existingSet.add( webSocket );
        }
        
        existingSet = null;
        tmpSet = null;
        System.out.println("Leaving Register Socket");  
  }
}