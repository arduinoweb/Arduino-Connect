/** Class that handles the serial port communication
  * @author: Gary Smith
  */

package ucc.arduino.serial;

import ucc.arduino.main.Pin;
import ucc.arduino.configuration.Configuration;


import ucc.arduino.serial.SerialOutputProcessor;
import ucc.arduino.serial.SerialReader;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.util.concurrent.TransferQueue;


public class SerialComm 
{
    /** Communicator between USB and Arduino */
    private static SerialPort serialPort;
    private final Configuration CONFIGURATION;

    
    public SerialComm(Configuration CONFIGURATION ){
         super();  
         this.CONFIGURATION = CONFIGURATION;                
    }
  

    public void connect ( final TransferQueue< Pin > SERIAL_OUTPUT_QUEUE,
                          inal TransferQueue<Integer> SERIAL_INPUT_QUEUE) 
                                          throws Exception
    {
        CommPortIdentifier portIdentifier = 
                    CommPortIdentifier.getPortIdentifier(CONFIGURATION.getSerialPort());
                    System.out.println("Trying to use serial port: " + 
                                                         CONFIGURATION.getSerialPort());
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
           serialPort.setSerialPortParams(CONFIGURATION.getSerialBaudRate(),
                                          CONFIGURATION.getSerialDataBits(),
                                          CONFIGURATION.getSerialStopBits(), 
                                          CONFIGURATION.getSerialParity() 
                                         );
                
           SerialOutputProcessor serialOutputProcessor = 
                                 new SerialOutputProcessor( SERIAL_OUTPUT_QUEUE,
                                                 serialPort.getOutputStream() );
           
           new Thread( serialOutputProcessor ).start();
           
           serialPort.addEventListener(
                   new SerialReader(serialPort.getInputStream(), 
                                    SERIAL_INPUT_QUEUE));
           
           serialPort.notifyOnDataAvailable(true);

           
	  }
          else
          {
            System.out.println("Error: Only serial ports are handled");
          }
          commPort = null;
        }
        
        portIdentifier = null;
       
    }
	
   
   
    
    private void close()
	{
	   try{
	   
            serialPort.close();			
	   
	   }catch( Exception e ){
	   
	   }
	
	
	}
	
	
   
    
}
