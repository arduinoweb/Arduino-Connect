package ucc.arduino.main;

import ucc.arduino.main.KeyValue;

import java.util.HashMap;
import java.util.concurrent.TransferQueue;
import java.util.AbstractMap.SimpleEntry;

public class PinMap{
        
  private final HashMap<Integer, Integer> PIN_MAP;
  private TransferQueue<KeyValue<Integer,Integer>> scriptInvocationQueue;
  private TransferQueue<KeyValue<Integer,Integer>> webSocketOutQueue;
  
  public PinMap( )
  {
    PIN_MAP = new HashMap<Integer, Integer>();
    scriptInvocationQueue = null;
  }
  
public synchronized Integer update(final Integer pinNumber, final Integer pinValue  )
  {
        Integer currentValue = PIN_MAP.get( pinNumber );
        KeyValue<Integer, Integer> updatedPin = null;
        
        if( pinValue == null )
        {
           return currentValue;
        }
        else 
        {
           PIN_MAP.put( pinNumber, pinValue );
         
           updatedPin = new KeyValue< Integer, Integer>(pinNumber, pinValue );
           
           if( scriptInvocationQueue != null )
           {
             scriptInvocationQueue.add( updatedPin );
           }
           
           if( webSocketOutQueue != null )
           {
              webSocketOutQueue.add( updatedPin );       
                   
           }
        }  
        
        updatedPin = null;
        currentValue = null;
        
        return pinValue;
  }
  
 public void enableScripting(TransferQueue<KeyValue<Integer,Integer>> scriptInvocationQueue)
 {

    this.scriptInvocationQueue = scriptInvocationQueue;
         
 }
 
 public void enableWebSocketQueue( TransferQueue<KeyValue<Integer,Integer>>
                                           webSocketOutQueue )
 {
    this.webSocketOutQueue = webSocketOutQueue;
               
 }
        
}
