package ucc.arduino.serial;

import ucc.arduino.main.Pin;

import java.util.concurrent.TransferQueue;
import java.io.OutputStream;
import java.io.IOException;

public class SerialOutputProcessor implements Runnable{
        
    private byte[] message = new byte[ 5 ];
    private final TransferQueue< Pin > SERIAL_OUTPUT_QUEUE;
    private final OutputStream SERIAL_OUT;
    
    private Pin writeToPin;
    
    public SerialOutputProcessor( final TransferQueue<Pin> SERIAL_OUTPUT_QUEUE,
                                  final OutputStream SERIAL_OUT)
    {
            
       this.SERIAL_OUTPUT_QUEUE = SERIAL_OUTPUT_QUEUE;       
       this.SERIAL_OUT = SERIAL_OUT; 
       message[0] = (byte)'R';
       message[4] = (byte)'E';
    }
        
    
    public void run()
    {
            
      while( true )
      {
              try{      
         writeToPin = SERIAL_OUTPUT_QUEUE.take();
         
         System.out.println("Writing: Pin " + writeToPin.getName() + 
                 " Mode: " + writeToPin.getMode() + " Value: " + writeToPin.getValue() );
         
         message[1] = ( byte )writeToPin.getMode();
         message[2] = ( byte )writeToPin.getName();
         message[3] = (byte)writeToPin.getValue();
        
         try{
         SERIAL_OUT.write( message );
         SERIAL_OUT.flush();
         }catch(IOException  ioe ){ System.err.println( ioe );}
      }catch( InterruptedException ie ){
              System.err.println( ie );
      }
         writeToPin = null;
              
      }
            
    }
        
        
        
        
}
