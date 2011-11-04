package ucc.arduino.configuration;

import java.util.Properties;


public class DefaultConfiguration extends Properties{

   public DefaultConfiguration()
   {
      super();
      this.setProperty( "ARDUINO_ADDRESS", "127.0.0.1" );
      this.setProperty( "ARDUINO_PORT", "10002" );
      
      this.setProperty( "CLIENT_TIMEOUT", "60000");


      //Serial Port Related Properties
      this.setProperty( "SERIAL_PORT", "/dev/ttyUSB0");
      this.setProperty( "BAUD_RATE", "9600");
      this.setProperty( "SERIAL_TIMEOUT", "50" );
      this.setProperty( "DATA_BITS", "8" );
      this.setProperty( "STOP_BITS", "1" );
      this.setProperty( "PARITY", "0");

   }




}