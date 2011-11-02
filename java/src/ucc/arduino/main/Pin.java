package ucc.arduino.main;

public class Pin{

   private final int PIN_NAME;
   private final int PIN_VALUE;
   private final byte MODE;
   
   public Pin( byte mode, int name, int value)
   {
      PIN_NAME = name;
	  PIN_VALUE = value;
      MODE = mode;
   }
   
   public int getName()
   {
     return PIN_NAME;
   }
   
   public int getValue()
   {
     return PIN_VALUE;
   }

   public byte getMode()
   {
     return MODE;
   }



}
