package ucc.arduino.configuration;

import java.util.Properties;


public class DefaultConfiguration extends Properties{

   public DefaultConfiguration()
   {
      super();
      //Default Network Related Properties
      this.setProperty( "NETWORK_ADDRESS", "127.0.0.1" );
      this.setProperty( "NETWORK_PORT", "10002" );
      this.setProperty( "NETWORK_QUEUE_LENGTH", "50");
      this.setProperty( "NETWORK_TIMEOUT", "60000");


      //Default Serial Port Properties
      this.setProperty( "SERIAL_PORT", "/dev/ttyUSB0");
      this.setProperty( "SERIAL_BAUD_RATE", "9600");
      this.setProperty( "SERIAL_TIMEOUT", "50" );
      this.setProperty( "SERIAL_DATA_BITS", "8" );
      this.setProperty( "SERIAL_STOP_BITS", "1" );
      this.setProperty( "SERIAL_PARITY", "0");
      
   }

}