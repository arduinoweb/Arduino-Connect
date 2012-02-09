package ucc.arduino.serial;


import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.InputStream;
import java.io.IOException;
import java.util.concurrent.TransferQueue;

public class SerialReader implements SerialPortEventListener{

  private final InputStream INPUTSTREAM;
  private final TransferQueue< Integer > SERIAL_INPUT_QUEUE;
  private int bytesAvailable;
  
  public SerialReader( InputStream INPUTSTREAM,
                       TransferQueue<Integer> SERIAL_INPUT_QUEUE )
  {
     this.INPUTSTREAM = INPUTSTREAM;
     this.SERIAL_INPUT_QUEUE = SERIAL_INPUT_QUEUE;
          
  }
  
  public void serialEvent( SerialPortEvent serialEvent )
  {
     try{
                  
         if( serialEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE )
         {
             bytesAvailable = INPUTSTREAM.available();
             
             while( bytesAvailable > 0 )
             {
                 SERIAL_INPUT_QUEUE.add( INPUTSTREAM.read() );
                 bytesAvailable--;
                    
             }
                 
                 
         }
                  
                  
     }catch( IOException e ){
         System.err.println( e );
     }
          
          
  }


}
