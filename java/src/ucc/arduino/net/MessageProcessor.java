package ucc.arduino.net;

import ucc.arduino.main.PinMap;
import ucc.arduino.main.Pin;
import ucc.arduino.configuration.Protocol;

import java.util.concurrent.TransferQueue;

public class MessageProcessor{
                       
 public static String processRead( String message,
                                   final PinMap PIN_MAP )
 {
   message = message.substring(2,message.length()-1);
   
   String[] msgParts = message.split( " " );
    
   int pinNumber = -1;
    
   Integer pinValue = null;
    
   StringBuffer tmpReply = new StringBuffer( "{ \"pins\" : [");
    
   int count = 0;
    
   for( String pin : msgParts )
   {
       pinNumber = Integer.parseInt( pin );
       
       pinValue = PIN_MAP.update( pinNumber, null );
       
       tmpReply.append("{\"pin\":\""+pinNumber+"\", \"value\" : \""+pinValue+"\"}");
      if( count < msgParts.length - 1 )
       {
          tmpReply.append(",");       
       }
       
       count++;
    }
         
    tmpReply.append( "] }");
    
    msgParts = null;
    pinValue = null;
          
         
    return tmpReply.toString();      
 }
 
 
 public static String processWrite( 
                                 String message,
                                 final PinMap PIN_MAP,
                                 final TransferQueue< Pin > SERIAL_OUTPUT_QUEUE
                                    )
 {
    message = message.substring( 2, message.length()-1);
    String[] msgParts = message.split( " " );     
  
    int index = 0;
    byte mode = -1;
    
    Integer pinNumber = null;
    Integer pinValue  = null;
 
    while( index < msgParts.length  )
    {
           
       mode = (byte)msgParts[ index++ ].charAt( 0 );
       pinNumber = Integer.parseInt( msgParts[ index++ ]);
       pinValue  = Integer.parseInt( msgParts[ index++ ] );
       
        if( mode == Protocol.VIRTUAL_WRITE )
        {
                PIN_MAP.update( pinNumber, pinValue );
        }
        else if( SERIAL_OUTPUT_QUEUE != null  ) 
        {
           SERIAL_OUTPUT_QUEUE.add( new Pin( mode, pinNumber, pinValue) );
        }
           
    }
         
    return "OK";       
 }
 
 public static int[] extractPinNumbers( String message )
 {
    message = message.substring( 2, message.length() -1 );
    String[] msgParts = message.split( " " );

    int[] pins = new int[msgParts.length];
    
    for( int i = 0; i < pins.length; i++ )
    {
       try{    
         pins[i] = Integer.parseInt( msgParts[i] );
            }catch( NumberFormatException nfe ){ System.err.println(nfe);}
    }
     
    msgParts = null;
    
    return pins;
 }
 
        
}
