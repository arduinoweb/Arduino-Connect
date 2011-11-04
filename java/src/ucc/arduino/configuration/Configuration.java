package ucc.arduino.configuration;

import ucc.arduino.configuration.DefaultConfiguration;

import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;

public class Configuration {

  
  private static final DefaultConfiguration DEFAULT_CONFIGURATION = new DefaultConfiguration();
  private static final Properties configuration = new Properties( DEFAULT_CONFIGURATION);
 
  public Configuration( File configurationFile )
  {
    try{    
       FileInputStream fileInputStream = new FileInputStream( configurationFile.toString() );
       configuration.load( fileInputStream );
       fileInputStream.close();

       validateProperties();
      }catch( IOException ioe ){
          System.err.println( ioe );
          System.exit( 1 );
      }
  }

  public Configuration(){ }

  private static void validateProperties()
  {
     boolean validEntries = false;

     if( ! isInt( "ARDUINO_PORT")   ||
         ! isInt( "CLIENT_TIMEOUT") ||
         ! isInt( "BAUD_RATE")      ||
         ! isInt( "SERIAL_TIMEOUT") ||
         ! isInt( "DATA_BITS")      ||
         ! isInt( "STOP_BITS")      ||
         ! isInt( "PARITY") )
     {
        System.out.println("Please check your configuration file. Exiting...");
        System.exit( 1 );
     }
  }

  private static boolean isInt( String property )
  {
     boolean isInteger = true;

     try{
          Integer.parseInt( configuration.getProperty( property) );
          
     }catch( NumberFormatException nfe ){
        System.out.println("Invalid: " + property);
        isInteger = false;
     }

     return isInteger;
  }
  public static Integer getArduinoPort()
  {
      return Integer.parseInt( configuration.getProperty( "ARDUINO_PORT") );

  }

  public static String getSerialPort()
  {
      return configuration.getProperty( "SERIAL_PORT");

  }

  public static Integer getClientTimeout()
  {
      return Integer.parseInt( configuration.getProperty( "CLIENT_TIMEOUT"));
  }

  public static Integer getBaudRate(){
      return Integer.parseInt( configuration.getProperty( "BAUD_RATE"));
  }

  public static Integer getSerialTimeout()
  {
     return Integer.parseInt( configuration.getProperty( "SERIAL_TIMEOUT") );
  }

  public static Integer getDataBits()
  {
    return Integer.parseInt( configuration.getProperty( "DATA_BITS") );
  }

  public static Integer getStopBits()
  {
    return Integer.parseInt( configuration.getProperty( "STOP_BITS") );
  }

  public static Integer getParity()
  {
    return Integer.parseInt( configuration.getProperty( "PARITY") );
  }
}