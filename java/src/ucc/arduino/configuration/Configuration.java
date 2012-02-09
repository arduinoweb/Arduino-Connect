package ucc.arduino.configuration;

import ucc.arduino.configuration.DefaultConfiguration;
import ucc.arduino.configuration.SerialConfiguration;

import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;

import java.net.InetAddress;

public class Configuration  implements SerialConfiguration{
  /** Stores the configuration details */
  private Properties configuration;
  /** Stores the network address to bind to*/
  private InetAddress inetAddress;

  public Configuration( File configurationFile )
  {
    this();

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

  public Configuration(){ configuration = 
                          new Properties( new DefaultConfiguration()); }

  private void validateProperties()
  {
     boolean validEntries = false;

     if( ! isInt( "NETWORK_PORT")                ||
         ! isInt( "NETWORK_TIMEOUT")             ||
         ! isInt( "SERIAL_BAUD_RATE")            ||
         ! isInt( "SERIAL_DATA_BITS")            ||
         ! isInt( "SERIAL_STOP_BITS")            ||
         ! isInt( "SERIAL_PARITY")               ||
         ! isValidAddress( "NETWORK_ADDRESS")    ||
         ! hasProtocol( "WEB_SERVER_URL")   ||
         ! isInt( "NETWORK_REGISTRATION_RATE") ||
         ! isInt( "NETWORK_QUEUE_LENGTH") )
     {
        System.out.println("Please check your configuration file. Exiting...");
        System.exit( 1 );
     }
  }

  private boolean hasProtocol( String url )
  {
     
     boolean hasProtocol = false;
     
     url = configuration.getProperty( url ).trim().toLowerCase();       
     
     if( url.startsWith( "http://") ||
             url.startsWith( "https://") )
     {
         hasProtocol = true;
     }
     else
     {
       System.err.println("WEBSERVER_URL must begin with http:// or https://");
     }
     
     return hasProtocol;
  }
  
  
  private boolean isValidAddress( String address )
  {
     boolean isValid = true;

     try{
          
        inetAddress = InetAddress.getByName( 
                         configuration.getProperty( address ) );

     }catch( Exception e ){
         isValid = false;
         System.out.println("Unable to verify " + address );
     }

     return isValid;
  }

  private boolean isInt( String property )
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
  public Integer getNetworkPort()
  {
      return Integer.parseInt( configuration.getProperty( "NETWORK_PORT") );

  }

  public String getSerialPort()
  {
      return configuration.getProperty( "SERIAL_PORT");

  }

  public Integer getNetworkTimeout()
  {
      return Integer.parseInt( configuration.getProperty( "NETWORK_TIMEOUT"));
  }

  public  Integer getSerialBaudRate(){
    
      return Integer.parseInt( configuration.getProperty( "SERIAL_BAUD_RATE"));
  }


  public Integer getSerialDataBits()
  {
    return Integer.parseInt( configuration.getProperty( "SERIAL_DATA_BITS") );
  }

  public Integer getSerialStopBits()
  {
    return Integer.parseInt( configuration.getProperty( "SERIAL_STOP_BITS") );
  }

  public Integer getSerialParity()
  {
    return Integer.parseInt( configuration.getProperty( "SERIAL_PARITY") );
  }

  public InetAddress getNetworkAddress()
  {
    InetAddress iAddress = null;
    
    try{
          System.out.println("In getNetworkAddress");
        iAddress = InetAddress.getByName( 
                      configuration.getProperty( "NETWORK_ADDRESS" ) );
      

     }catch( Exception e ){
         System.out.println("Unable to verify " + 
                      configuration.getProperty( "NETWORK_ADDRESS") );
     }

     return iAddress;  
  }

  public Integer getNetworkQueueLength()
  {
    return Integer.parseInt( 
                       configuration.getProperty( "NETWORK_QUEUE_LENGTH" ));
  }
  
 
  public String getWebServerUrl()
  {
    return configuration.getProperty("WEB_SERVER_URL");
    
  }
  
  public String getNetworkName()
  {
    return configuration.getProperty("NETWORK_NAME");       
          
  }
  
  
  public Integer getNetworkRegistrationRate()
  {
      return Integer.parseInt( 
                    configuration.getProperty(
                             "NETWORK_REGISTRATION_RATE" ) );
          
  }
  
 
  
  public String getScriptName()
  {
     return configuration.getProperty("SCRIPT");       
  }
  
  public String getNetworkRegister()
  {
     return configuration.getProperty( "NETWORK_REGISTER").toLowerCase();       
  }
  
  public String getUseInvocationThread()
  {
     return configuration.getProperty( "USE_INVOCATION_THREAD").toLowerCase();       
          
  }
  
}