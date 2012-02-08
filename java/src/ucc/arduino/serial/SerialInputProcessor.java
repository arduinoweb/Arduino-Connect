package ucc.arduino.serial;
import ucc.arduino.main.PinMap;
import ucc.arduino.main.Pin;

import java.util.concurrent.TransferQueue;

public class SerialInputProcessor implements Runnable{
        
        
 private final TransferQueue< String > SERIAL_INPUT_QUEUE;
 private String inputReceived;

 private final PinMap PIN_MAP;
 private Integer pinNumber;
 private Integer pinValue;
 
 public SerialInputProcessor( final TransferQueue< String> SERIAL_INPUT_QUEUE,
                              final PinMap PIN_MAP )
 {
     this.SERIAL_INPUT_QUEUE = SERIAL_INPUT_QUEUE;

     this.PIN_MAP = PIN_MAP;
     
 }
     
 public void run()
 {
    while( true )
    {
            try{    
                 inputReceived = SERIAL_INPUT_QUEUE.take();
                
                 int startIndex = -1;
               
                 for( int i = 0; i < inputReceived.length(); i++ )
                 {
                   if( inputReceived.charAt( i ) == 'W' )
                   {
                  
                   if( startIndex != -1)
                   {
                     String tmp = inputReceived.substring( startIndex+2, i );
                     System.out.println( tmp );
                     String[] tmpPin = tmp.split( " " );
                     
                     if( tmpPin.length == 2 )
                     {
                             try{
                                  pinNumber = Integer.parseInt( tmpPin[0] );
                                  pinValue = Integer.parseInt( tmpPin[1] );
                                     
                                  PIN_MAP.update( pinNumber, pinValue);
                                   
                             }catch( NumberFormatException nfe ){ }
                             
                         pinNumber = null;
                         pinValue = null;
                             
                     }
                     
                     startIndex = -1;
                     tmp = null;
                     tmpPin = null;
                           
                   }
                   else 
                   {
                      startIndex = i;       
                           
                   }
                 }
                   
                  
                         
                 }
                 System.out.println();
                 
            }catch( InterruptedException ie ){
                   System.err.println( ie );       
                 }
    }
         
         
 }
           
        
}
