/**
  *  @author: Gary Smith
  *  A class that represents a pin on the Arduino
  */

package ucc.arduino.main;


import ucc.arduino.main.KeyValue;


public class Pin extends KeyValue<Integer,Integer>{
        
   private final byte MODE;
   
   public Pin( byte mode, int pinNumber, int value )
   {
       super( pinNumber, value );
  
        MODE = mode;     
           
           
   }
        
   public int getPinNumber()
   {
      return getKey();
   }
   
   public byte getMode()
   {
      return MODE;
   }
      

}




