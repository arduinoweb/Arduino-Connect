/** Class that handles the serial port communication
  * @author: Gary Smith
  */

package ucc.arduino.serial;

import ucc.arduino.main.PinMap;
import ucc.arduino.main.Pin;
import ucc.arduino.configuration.SerialConfiguration;


import ucc.arduino.serial.SerialOutputProcessor;
import ucc.arduino.serial.SerialInputProcessor;
import ucc.arduino.serial.SerialReader;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.util.concurrent.TransferQueue;


public class SerialComm 
{

    private static SerialPort serialPort;
    private final SerialConfiguration CONFIGURATION;

    
    public SerialComm(final SerialConfiguration CONFIGURATION ){
         super();  
         this.CONFIGURATION = CONFIGURATION;                
    }
  

    public void connect ( final TransferQueue< Pin > SERIAL_OUTPUT_QUEUE,
                          final TransferQueue<Integer> SERIAL_INPUT_QUEUE,
                          final PinMap PIN_MAP) 
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
           
           SerialInputProcessor serialInputProcessor =
                                 new SerialInputProcessor( SERIAL_INPUT_QUEUE,
                                                           PIN_MAP );
           new Thread( serialOutputProcessor ).start();
           new Thread( serialInputProcessor ).start();
           
           serialPort.addEventListener(
                   new SerialReader(serialPort.getInputStream(), 
                                    SERIAL_INPUT_QUEUE));
           
           serialPort.notifyOnDataAvailable(true);

           serialOutputProcessor = null;
           serialInputProcessor = null;
           
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
