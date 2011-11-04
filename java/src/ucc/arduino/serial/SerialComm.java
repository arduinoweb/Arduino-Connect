/** Class that handles the serial port communication
  * @author: Gary Smith
  */

package ucc.arduino.serial;

import ucc.arduino.main.Arduino;
import ucc.arduino.main.Pin;

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
    private final static byte[]  READ_BUFFER =  new byte[ READ_BUFFER_SIZE ];
    
    // Message definitions, these will be moved eventually
    private final static String MESSAGE_DELIMITER = " ";
    private final static byte READ_SYNC = 'R';
    private final static byte WRITE_SYNC = 'W';
    private final static byte END_OF_MESSAGE = 'E';
    private final static byte NOTHING_TO_SEND = 'N';
    // End of message definitions

    /** The amount of time to wait between each byte received */
    private final static int SERIAL_TIMEOUT = 10; 	
    /** Communicator between USB and Arduino */
    private static SerialPort serialPort;

    /** The inputstream of the serial port */
    private InputStream inputStream;
    /** The outputstream of the serial port */
    private OutputStream outputStream;
    /** Whether to keep the thread alive or not */
    private boolean stayAlive;
    /** The port name to be used for serial communication */
    private static String portName;
   

    /** Constructor
      * @param: portName - the port to be used for serial communication
      */
    public SerialComm( String portName )
    {
        super();	
        this.portName  = portName;
	stayAlive = true;
    }
    
    /** Boiler plate code that initialises the serial communication
      * @throws: Exception
      */
      public void connect ( ) throws Exception
    {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        
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
           serialPort.setSerialPortParams(9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                
           inputStream = serialPort.getInputStream();
           outputStream = serialPort.getOutputStream();
	  }
          else
          {
            System.out.println("Error: Only serial ports are handled by this example.");
          }
        }     
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
	   outputStream.write( (byte) 1 );
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
        writeMsg[0] = WRITE_SYNC;
         
	while( stayAlive )
	{
     	  try{
        
	    if( inputStream.available() > 0 )
            {
              index = 0;
              byteRead = (byte)inputStream.read();
              
	      if( byteRead == READ_SYNC )
              {
		outputStream.write( READ_SYNC );
               
                startTime = System.currentTimeMillis();
                elapsedTime = 0;
		
                // check we haven't waited to long and
                // make sure we don't run over the end of 
                // the READ_BUFFER
                while( byteRead != END_OF_MESSAGE 
                       && index < READ_BUFFER_SIZE 
                       && elapsedTime < SERIAL_TIMEOUT )
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
               if( byteRead == END_OF_MESSAGE)
	       {
                 String message = new String( READ_BUFFER, 0, index - 1 );
                 String[] parts = message.split( MESSAGE_DELIMITER );

                 try{
                       // Belt and braces here, add the (pin,value) pair
                       // received to the PINS map.
                       if( parts.length == 2 ){
                                Arduino.setPin( Integer.parseInt( parts[0] ),
                                                Integer.parseInt( parts[1] ) );
                       }

                    }catch( NumberFormatException nfe ) { }
               }
             
	      }
              else if( byteRead == WRITE_SYNC )
              {
                Pin pin = null;
                //Check if we have any messages to write to the Arduino
		if( ( pin = Arduino.writeQueuePoll()) == null )
		{
		  outputStream.write( NOTHING_TO_SEND );
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
                  writeMsg[ 1 ] = '\0';
                  writeMsg[ 2 ] = '\0';
                  writeMsg[ 3 ] = '\0';
	       }

             }
	   }

          }catch( IOException ioe ){ System.err.println( ioe ); }

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
