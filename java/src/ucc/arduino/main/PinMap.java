package ucc.arduino.main;

import ucc.arduino.main.KeyValue;

import java.util.HashMap;
import java.util.concurrent.TransferQueue;
import java.util.AbstractMap.SimpleEntry;

public class PinMap{
        
  private final HashMap<Integer, Integer> PIN_MAP;
  private TransferQueue<KeyValue<Integer,Integer>> SCRIPT_INVOCATION_QUEUE;
  
  public PinMap( )
  {
    PIN_MAP = new HashMap<Integer, Integer>();
    SCRIPT_INVOCATION_QUEUE = null;
  }
  
public synchronized Integer update(final Integer pinNumber, final Integer pinValue  )
  {
        Integer currentValue = PIN_MAP.get( pinNumber );
        
        if( pinValue == null )
        {
           return currentValue;
        }
        else 
        {
           PIN_MAP.put( pinNumber, pinValue );
         
           
           if( SCRIPT_INVOCATION_QUEUE != null )
           {
             SCRIPT_INVOCATION_QUEUE.add( 
                     new KeyValue<Integer,Integer>(pinNumber, pinValue ) );
           }
        }  
        
        return pinValue;
  }
  
 public void enableScripting(TransferQueue<KeyValue<Integer,Integer>> SCRIPT_INVOCATION_QUEUE)
 {

    this.SCRIPT_INVOCATION_QUEUE = SCRIPT_INVOCATION_QUEUE;
         
 }
        
}
