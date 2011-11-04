/**
  *  @author: Gary Smith
  *  A class that represents a pin on the Arduino
  */

package ucc.arduino.main;

public class Pin{
   /** The number of the pin, change this to PIN_NUMBER */
   private final int PIN_NAME;
   /** The value to be assigned to the pin */
   private final int PIN_VALUE;
   /** The type of the pin - A = Analog, D = Digital */
   private final byte MODE;
   
   /** Constructor
     * @param: byte mode - A = Analog, D = Digital
     * @param: int  name - the pin number
     * @param: int  value - the value assigned to the pin
     */
   public Pin( byte mode, int name, int value)
   {
      PIN_NAME = name;
	  PIN_VALUE = value;
      MODE = mode;
   }
   
   /** Retreives the pin number
     * @return int  - the pin number
     */
   public int getName()
   {
     return PIN_NAME;
   }
   
   /** Retreives the value assigned to the pin
     * @return: int the value assigned to the pin
     */
   public int getValue()
   {
     return PIN_VALUE;
   }

   /** Retreives the type of pin, A = Analog, D = Digital
     * @return: byte - the type of the pin
     */
   public byte getMode()
   {
     return MODE;
   }



}
