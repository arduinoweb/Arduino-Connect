package ucc.arduino.net;


import java.util.HashMap;
import ucc.arduino.net.Device;
import ucc.arduino.main.Arduino;
import java.net.URL;
import java.net.URLConnection;


import java.util.Scanner;

import java.util.concurrent.ConcurrentHashMap;

public class DeviceList extends ConcurrentHashMap<String, Device> implements Runnable {
        
        
        private final URL url;
        private String dataIn;
        private URLConnection urlConnection;
        
        private StringBuffer dataBuffer;
        
        public DeviceList() throws Exception
        {
           super(); 
            
           url = new URL( Arduino.CONFIGURATION.getDeviceListUrl() );
        }
        
        
        public void run()
        {
                
                System.out.println( url.toString() );
                dataIn = "";
                
                try{
                        
                  urlConnection = url.openConnection();
                  
                 Scanner scanner = new Scanner( urlConnection.getInputStream() );
                 scanner.useDelimiter("\\Z");
                 
                 dataIn =  scanner.next() ;
                 
                 scanner = null;
               
                        
                }catch( Exception e ){
                  System.err.println( e );       
                }
                
                String[] activeDevices = dataIn.split( " " );
                if(  activeDevices.length % 3 == 0)
                {
                  System.out.println( "["+dataIn+"]");
           
                  System.out.println( activeDevices.length);
                  
                  int index = 0;
                  
                  Device tmpDevice = null;
                  
                  while( index < activeDevices.length )
                  {
                     tmpDevice = new Device( activeDevices[ index ],
                                       activeDevices[ ++index ],
                                       activeDevices[ ++index]
                                       );
                     
                     this.put( tmpDevice.getName(), tmpDevice );
                     index++;
                   
                  }
                  tmpDevice = null;
                  activeDevices = null;       
                }
                else
                {
                  this.clear();       
                }
                
                urlConnection = null;
                dataIn = null;
                
        }
        
        
        
        
        
}
