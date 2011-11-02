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
import java.util.Arrays;

public class SerialComm implements Runnable
{
    private final static int  READ_BUFFER_SIZE = 1024;
    private final static byte[]  READ_BUFFER =  new byte[ READ_BUFFER_SIZE ];
    private final static int WAIT_TIMEOUT = 50;

 
    private final static byte READ_SYNC = 'R';
	private final static byte WRITE_SYNC = 'W';
	private final static byte ARRAY_CLEAR = 0;
    private final static byte SPACE = ' ';
	private final static byte END_OF_MESSAGE = 'E';
	private final static byte NOTHING_TO_SEND = 'N';
	
	private static SerialPort serialPort;
	
	private   InputStream inputStream;
	private   OutputStream outputStream;
	private   boolean stayAlive;

	
	
    public SerialComm(  )
    {
        super();
		
		stayAlive = true;
		
		
    }
    

    public void connect ( String portName ) throws Exception
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
                serialPort.setSerialPortParams(57600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                
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
		}catch(IOException ioe ){ System.err.println(ioe);}
		
                int bytesAvailable = 0;
         int index = 0;
         Pin pin = null;
         byte[] writeMsg = new byte[4];
         writeMsg[0] = WRITE_SYNC;

		while( stayAlive )
		{
     	         try{
        
          pin = null;

	  if( ( bytesAvailable = inputStream.available() )> 0 )
          {
              index = 0;
              byte b = (byte)inputStream.read();
              System.out.println( "Bytes Available: " + bytesAvailable );
	      if( b == READ_SYNC )
              {
		System.out.println("Write Sync Recieved");
		outputStream.write( READ_SYNC );
                byte temp = ' ';
                long startTime = System.currentTimeMillis();
                long elapsedTime = 0;
		while( elapsedTime < WAIT_TIMEOUT && temp != END_OF_MESSAGE )
	        {
                     if( inputStream.available() > 0 )
	             {
			     temp = ( byte)inputStream.read();
			     READ_BUFFER[index] = temp;
			     index++;
		     }
                  elapsedTime = System.currentTimeMillis() - startTime;

		}
		
               if( temp == END_OF_MESSAGE)
		{String message = new String( READ_BUFFER, 0, index - 1 );
                 String[] parts = message.split(" ");

                try{
                 if( parts.length == 2 ){
                   Arduino.setPin( Integer.parseInt( parts[0] ),
                                   Integer.parseInt( parts[1] ) );
                   }
                }catch( NumberFormatException nfe ) { }
              
	        System.out.println( message );
                 }
                 else
              {
                 System.out.println( "Java program timed out reading from Arduino");
              }
	      }
              
              else if( b == WRITE_SYNC )
              {
                System.out.println( "Okay to write");
		if( ( pin = Arduino.writeQueuePoll()) == null )
		{
                  System.out.println("Nothing to send to Arduino");
		  outputStream.write( NOTHING_TO_SEND );
                  outputStream.flush();
	       }
	       else
	       {
                    System.out.println( "Writing to Arduino");
                   
		    
		    writeMsg[ 1 ] = (byte)pin.getMode();
		    writeMsg[ 2 ] = (byte)pin.getName();
		    writeMsg[ 3 ] = (byte)pin.getValue();
	   	   
		   outputStream.write( writeMsg );
		   outputStream.flush();
	       }

             }
	}
      }catch( IOException ioe ){ System.err.println( ioe ); }

	    	

		  
		
		}	
	
	}
 
    public  void  readyToReceive()
    {
      

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
