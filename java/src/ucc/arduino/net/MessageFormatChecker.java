package ucc.arduino.net;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class MessageFormatChecker{
        
 private boolean isReadMessage;
 private boolean isWriteMessage;
 
 private int[] pinsToRead = null;
 
 private final String pattern = 
      "^((R (\\d{1,3} )+)|(W ((A|D|V) (\\d{1,3}+) (\\d{1,3}+) )+))E$"; 
 
 private final Pattern compiledPattern = Pattern.compile( pattern );
 
 private Matcher matcher;
 
 
 public boolean isValidMessageFormat( String message )
 {
     isReadMessage = false;
     isWriteMessage = false;
     pinsToRead = null;
     
     matcher = compiledPattern.matcher( message );    
         
     if( matcher.matches() )
     {
        switch( message.charAt(0) ){
            case 'R' : { isReadMessage = true; 
                          
                         
                         message = message.substring( 2, message.length()-1);
                        
                         String[] msgParts = message.split(" ");
                         pinsToRead = new int[ msgParts.length ];
                         System.out.println("PinsToRead.length " + pinsToRead.length );
                         System.out.println("MsgParts length: " + msgParts.length);    
                         for( int i = 0; i < pinsToRead.length; i++ )
                         {
                             pinsToRead[i] = Integer.parseInt( msgParts[i] );        
                                 
                         }
            
                         msgParts = null;
                         message = null;
                         
                       break; 
                       }
                       
            case 'W' : { isWriteMessage = true; break;}
        }
             
     }
     
     matcher = null;
     
     return ( isReadMessage || isWriteMessage );

 }
 
 public boolean isRegisterMessage()
 {
     return isReadMessage;       
         
 }
 
 public boolean isReadMessage()
 {
     return isReadMessage;       
 }
 
 public boolean isWriteMessage()
 {
   return isWriteMessage;       
 }
 
 public int[] getPinsToRead()
 {
    return pinsToRead;       
         
 }
 public int[] getPinsToRegister()
 {
    return pinsToRead;       
 }
 
}
