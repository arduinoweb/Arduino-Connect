/** Class that handles the serial port communication
  * @author: Gary Smith
  */

package ucc.arduino.serial;
import ucc.arduino.main.Arduino;
import ucc.arduino.main.Pin;
import ucc.arduino.configuration.Protocol;
import ucc.arduino.scripting.Scripter;


import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class SerialComm implements Runnable
{
    /** The size of the buffer to use when reading from Arduino */
    private final static int  READ_BUFFER_SIZE = 256;
    /** The buffer to use when reading from Arduino */
    private  static byte[]  READ_BUFFER =  new byte[ READ_BUFFER_SIZE ];

    /** The amount of time to wait between each byte received */
    private int serialTimeout;
    /** Communicator between USB and Arduino */
    private static SerialPort serialPort;

    /** The inputstream of the serial port */
    private InputStream inputStream;
    /** The outputstream of the serial port */
    private OutputStream outputStream;
    /** Whether to keep the thread alive or not */
    private boolean stayAlive;
   
    private Scripter scripter;

    /** Constructor
      */
    public SerialComm( Scripter scripter)
    {
        super();
        this.scripter = scripter;
	stayAlive = true;
        serialTimeout = Arduino.CONFIGURATION.getSerialTimeout();
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
	  }
          else
          {
            System.out.println("Error: Only serial ports are handled by this example.");
          }
          commPort = null;
        }
        
        portIdentifier = null;
    }
	
    /** Terminates the thread */
    public void stop()
    { 
      stayAlive = false;
    }
	
    /** Main loop that handles communication between USB and Arduino */
    public void run()
    {
       // Send a single byte to Arduino to let it know we're here.
       // Probably remove this eventually.
       try{
	   outputStream.write( (byte) Protocol.NULL_BYTE );
	}catch(IOException ioe ){ 
             System.err.println(ioe);
        }
	
        // Used in calculation of timeouts 
        long startTime = 0;
        long elapsedTime = 0;

        // Used to index READ_BUFFER
        int index = 0;
         
        // Stores the last byte read from the inputstream
        byte byteRead = '\0';

        // The message to send to the Arduino
        byte[] writeMsg = new byte[4];
        // Every write message starts with a WRITE_SYNC byte 
        writeMsg[0] = Protocol.WRITE_SYNC;
         
	while( stayAlive )
	{
     	  try{
        
	    if( inputStream.available() > 0 )
            {
              index = 0;
              byteRead = (byte)inputStream.read();
              
	      if( byteRead == Protocol.READ_SYNC )
              {
		outputStream.write( Protocol.READ_SYNC );
               
                startTime = System.currentTimeMillis();
                elapsedTime = 0;
		
                // check we haven't waited to long and
                // make sure we don't run over the end of 
                // the READ_BUFFER
                while( byteRead != Protocol.END_OF_MESSAGE 
                       && index < READ_BUFFER_SIZE 
                       && elapsedTime < serialTimeout )
	        {
                  if( inputStream.available() > 0 )
	          {
		     byteRead = ( byte)inputStream.read();
	             READ_BUFFER[index] = byteRead;
	             index++;
		  }

                  elapsedTime = System.currentTimeMillis() - startTime;
		}
		
               // If this is true then we should have received a complete
               // message.
               if( byteRead == Protocol.END_OF_MESSAGE)
	       {
                 String message = new String( READ_BUFFER, 0, index - 1 );
                 String[] parts = message.split( Protocol.MESSAGE_DELIMITER );

                 try{
                       // Belt and braces here, add the (pin,value) pair
                       // received to the PINS map.
                       if( parts.length == 2 ){
                               int pin = Integer.parseInt( parts[0] );
                               int value = Integer.parseInt( parts[1] );
                                Arduino.setPin( pin, value );
                                
                              
                                   scripter.runScript();
                               
                       }

                    }catch( NumberFormatException nfe ) { }
                    
                    message = null;
                    parts = null;
               }
             
	      }
              else if( byteRead == Protocol.WRITE_SYNC )
              {
                Pin pin = null;
                //Check if we have any messages to write to the Arduino
		if( ( pin = Arduino.writeQueuePoll()) == null )
		{
		  outputStream.write( Protocol.NOTHING_TO_SEND );
                  outputStream.flush();
	       }
	       else
	       {
                  //Construct the message and send it
                  writeMsg[ 1 ] = (byte)pin.getMode();
		  writeMsg[ 2 ] = (byte)pin.getName();
		  writeMsg[ 3 ] = (byte)pin.getValue();
	   	   
		  outputStream.write( writeMsg );
		  outputStream.flush();
                  writeMsg[ 1 ] = Protocol.NULL_BYTE;
                  writeMsg[ 2 ] = Protocol.NULL_BYTE;
                  writeMsg[ 3 ] = Protocol.NULL_BYTE;
                  pin = null;
	       }

             }
	   }

          }catch( IOException ioe ){ System.err.println( ioe ); }

       
          Thread.yield();
          
      }	// End of While Loop
	close();
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
