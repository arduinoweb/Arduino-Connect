package ucc.arduino.main;

import java.util.HashMap;
import java.util.concurrent.TransferQueue;


public class PinMap{
        
  private final HashMap<Integer, Integer> PIN_MAP;
  private TransferQueue<HashMap<Integer,Integer>> scriptInvocationQueue;
  
  public PinMap( )
  {
    PIN_MAP = new HashMap<Integer, Integer>();
    scriptInvocationQueue = null;
  }
  
public synchronized Integer update(final Integer pinNumber, final Integer pinValue  )
  {
        Integer currentValue = PIN_MAP.get( pinNumber );
        
        if( pinValue == null )
        {
           return currentValue;
        }
        else if( currentValue == null || ( currentValue.intValue() != pinValue.intValue() ) )
        {
           PIN_MAP.put( pinNumber, pinValue );
           currentValue = pinValue;
           
           if( scriptInvocationQueue != null )
           {
             scriptInvocationQueue.add( 
                     new HashMap<Integer,Integer>( PIN_MAP ) );
           }
        }  
        
        return pinValue;
  }
  
 public void enableScripting(TransferQueue<HashMap<Integer,Integer>> scriptInvocationQueue)
 {

    this.scriptInvocationQueue = scriptInvocationQueue;
         
 }
        
}
