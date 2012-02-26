package ucc.arduino.net;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class MessageFormatChecker{
        
 private boolean isReadMessage;
 private boolean isWriteMessage;

 private final String pattern = 
      "^((R (\\d{1,3} )+)|(W ((A|D|V) (\\d{1,3}+) (\\d{1,3}+) )+))E$"; 
 
 private final Pattern compiledPattern = Pattern.compile( pattern );
 
 private Matcher matcher;
 
 
 public boolean isValidMessageFormat( String message )
 {
     isReadMessage = false;
     isWriteMessage = false;
   
     matcher = compiledPattern.matcher( message );    
         
     if( matcher.matches() )
     {
        switch( message.charAt(0) ){
            case 'R' : { isReadMessage = true; break; }
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
 


 
}
