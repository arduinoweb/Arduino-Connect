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
    private final static int  READ_BUFFER_SIZE = 256;
    private final static byte[]  READ_BUFFER =  new byte[ READ_BUFFER_SIZE ];
    
    private final static String MESSAGE_DELIMITER = " ";
    private final static byte READ_SYNC = 'R';
    private final static byte WRITE_SYNC = 'W';
    private final static byte END_OF_MESSAGE = 'E';
    private final static byte NOTHING_TO_SEND = 'N';
   
    private final static int SERIAL_TIMEOUT = 10; 	
    private static SerialPort serialPort;
    private InputStream inputStream;
    private OutputStream outputStream;
    private boolean stayAlive;
    private static String portName;
   
    public SerialComm( String portName )
    {
        super();	
        this.portName  = portName;
	stayAlive = true;
    }
    
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
	
    public void stop()
    { 
      stayAlive = false;
    }
	
    public void run()
    {
       try{
	   outputStream.write( (byte) 1 );
	}catch(IOException ioe ){ 
             System.err.println(ioe);
        }
	 long startTime = 0;
         long elapsedTime = 0;
         int index = 0;
         
         byte byteRead = '\0';

         byte[] writeMsg = new byte[4];
         
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
		
               if( byteRead == END_OF_MESSAGE)
	       {
                 String message = new String( READ_BUFFER, 0, index - 1 );
                 String[] parts = message.split( MESSAGE_DELIMITER );

                 try{
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
                
		if( ( pin = Arduino.writeQueuePoll()) == null )
		{
		  outputStream.write( NOTHING_TO_SEND );
                  outputStream.flush();
	       }
	       else
	       {
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
