package ucc.arduino.net;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class MessageFormatChecker{
        
 private boolean isReadMessage;
 private boolean isWriteMessage;
 private boolean isScriptMessage;
 
 private final String messageFormat = 
      "^((R (\\d{1,3} )+)|(W ((A|D|V) (\\d{1,3}+) (\\d{1,3}+) )+))E$"; 

 
 private final Pattern messagePattern = Pattern.compile( messageFormat );
 private Matcher matcher;
 
 public boolean isValidMessageFormat( String message )
 {
     isReadMessage = false;
     isWriteMessage = false;
     isScriptMessage = false;
     
     
     matcher = messagePattern.matcher( message );    
         
     if( matcher.matches() )
     {
        switch( message.charAt(0) ){
            case 'R' : { isReadMessage = true; break; }
            case 'W' : { isWriteMessage = true; break;}
        }
             
     }
     else if( message.startsWith( "<script>") &&
              message.endsWith("</script>") )
     { 
       isScriptMessage = true;

     }
     
    matcher = null;
    
    return ( isReadMessage || isWriteMessage || isScriptMessage );
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
 
 public boolean isScriptMessage()
 {
    return isScriptMessage;       
         
 }

 
}
