package ucc.arduino.serial;
import ucc.arduino.main.PinMap;
import ucc.arduino.main.Pin;

import java.util.concurrent.TransferQueue;

public class SerialInputProcessor implements Runnable{
        
        
 private final TransferQueue< Integer > SERIAL_INPUT_QUEUE;
 private int inputReceived;

 private final PinMap PIN_MAP;
 private Integer pinNumber;
 private Integer pinValue;
 
 private StringBuffer buffer; 
 private String tmp;
 private String[] msgComponents;
 
 private boolean saveCharacter;
 
 public SerialInputProcessor( final TransferQueue< Integer > SERIAL_INPUT_QUEUE,
                              final PinMap PIN_MAP )
 {
     this.SERIAL_INPUT_QUEUE = SERIAL_INPUT_QUEUE;

     this.PIN_MAP = PIN_MAP;

     saveCharacter = false;
     buffer = new StringBuffer();
 }
     
 public void run()
 {
    while( true )
    {
            try{    
                 inputReceived = SERIAL_INPUT_QUEUE.take();
                
                 if( (char)inputReceived == 'W' )
                 {
                   saveCharacter = true;      
                         
                 }
                 else if( saveCharacter && (char)inputReceived == 'E' )
                 {
                  tmp = buffer.toString();
                  tmp = tmp.trim();
                  msgComponents = tmp.split(" ");

                    if( msgComponents.length == 2 )
                    {   
                       try{
                            pinNumber = Integer.parseInt( msgComponents[0] );
                            pinValue = Integer.parseInt( msgComponents[1] );
                            PIN_MAP.update( pinNumber, pinValue );
                                    
                            }catch(NumberFormatException nfe ){
                                    
                               System.err.println( nfe );       
                            }
                            
                            pinNumber = null;
                            pinValue = null;
                    }
                  
                    saveCharacter = false;
                    msgComponents = null;
                    tmp = null;
                    buffer = null;
                    buffer = new StringBuffer();
                              
                 }
                 else if( saveCharacter )
                 {
                 buffer.append( (char)inputReceived );
                        
                  }
                 
      
            }catch( InterruptedException ie ){
                   System.err.println( ie );       
                 }
    }
         
         
 }
           
        
}
