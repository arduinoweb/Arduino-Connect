/** Class that handles the serial port communication
  * @author: Gary Smith
  */

package ucc.arduino.serial;
import ucc.arduino.main.Arduino;
import ucc.arduino.main.Pin;
import ucc.arduino.configuration.Protocol;
import ucc.arduino.scripting.Scripter;
import ucc.arduino.serial.SerialOutputProcessor;

import ucc.arduino.main.PinMap;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;


import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.concurrent.TransferQueue;

import java.util.HashMap;


public class SerialComm 
{
    /** Communicator between USB and Arduino */
    private static SerialPort serialPort;

    /** The inputstream of the serial port */
    private InputStream inputStream;
    /** The outputstream of the serial port */
    private OutputStream outputStream;

      /** Constructor
      */
          
    private TransferQueue< Integer > SERIAL_INPUT_QUEUE;   
    private TransferQueue<Pin> SERIAL_OUTPUT_QUEUE;
    private SerialOutputProcessor serialOutputProcessor;
    

   public SerialComm( TransferQueue< Pin > SERIAL_OUTPUT_QUEUE,
                      TransferQueue<Integer> SERIAL_INPUT_QUEUE )
    {
       super();
       this.SERIAL_OUTPUT_QUEUE = SERIAL_OUTPUT_QUEUE;
       this.SERIAL_INPUT_QUEUE = SERIAL_INPUT_QUEUE;
         System.out.println("Trying to use serial port: " + 
                                                         Arduino.CONFIGURATION.getSerialPort());
    }
    
    /** Boiler plate code that initialises the serial communication
      * @throws: Exception
      */
      public void connect ( ) throws Exception
    {
        CommPortIdentifier portIdentifier = 
                    CommPortIdentifier.getPortIdentifier(Arduino.CONFIGURATION.getSerialPort());
                    System.out.println("Trying to use serial port: " + 
                                                         Arduino.CONFIGURATION.getSerialPort());
        if ( portIdentifier.isCurrentlyOwned() )
        {
            System.out.println("Error: Port is currently in use");
        }
        else
        {
          CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);
            
          if ( commPort instanceof SerialPort )
          {

           serialPort = (SerialPort) commPort;
           serialPort.setSerialPortParams(Arduino.CONFIGURATION.getSerialBaudRate(),
                                          Arduino.CONFIGURATION.getSerialDataBits(),
                                          Arduino.CONFIGURATION.getSerialStopBits(), 
                                          Arduino.CONFIGURATION.getSerialParity() 
                                         );
                
           inputStream = serialPort.getInputStream();
           outputStream = serialPort.getOutputStream();
           serialOutputProcessor = new SerialOutputProcessor( SERIAL_OUTPUT_QUEUE,
                                                              outputStream );
           new Thread( serialOutputProcessor ).start();
            serialPort.addEventListener(new SerialReader(inputStream, SERIAL_INPUT_QUEUE));
                serialPort.notifyOnDataAvailable(true);
            SERIAL_INPUT_QUEUE = null;
            SERIAL_OUTPUT_QUEUE = null;
	  }
          else
          {
            System.out.println("Error: Only serial ports are handled by this example.");
          }
          commPort = null;
        }
        
        portIdentifier = null;
    }
	
   
    /**
     * Handles the input coming from the serial port. 
     * Modified Boiler Plate code from example.
     */
    public static class SerialReader implements SerialPortEventListener 
    {
        private InputStream in;
  
        private final TransferQueue< Integer > SERIAL_INPUT_QUEUE;
        private int len;
        
       public SerialReader ( InputStream in, 
                             TransferQueue<Integer> SERIAL_INPUT_QUEUE )
       {
            this.in = in;
            this.SERIAL_INPUT_QUEUE =SERIAL_INPUT_QUEUE;

       }
        
       public void serialEvent(SerialPortEvent event) 
       {
            
            try
            {
              if( event.getEventType() == SerialPortEvent.DATA_AVAILABLE )
              {
               len = in.available();
                while ( len > 0 )
                {
                      SERIAL_INPUT_QUEUE.add( in.read() );
                      len--;           
                }
              } 
             
            }
            catch ( IOException e )
            {
                e.printStackTrace();
                System.exit(-1);
            }             
        }

    }
    
    private void close()
	{
	   try{
	    outputStream.flush();
            inputStream.close();
            serialPort.close();			
	   
	   }catch( Exception e ){
	   
	   }
	
	
	}
	
	
   
    
}
