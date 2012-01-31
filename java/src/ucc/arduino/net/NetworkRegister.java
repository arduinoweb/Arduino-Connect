package ucc.arduino.net;

import java.net.URL;
import java.net.URLEncoder;

import java.net.MalformedURLException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;

import java.io.IOException;
import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;


import java.util.Scanner;

public class NetworkRegister implements Runnable{
 
 private final URL url;
 private HttpURLConnection urlConnection;
 private final String postData;
 private DataOutputStream dataOut;
 private BufferedReader dataIn;
 
 private String responseLine;
 private StringBuffer responseBuffer;
 
 private final String contentLength;
 public NetworkRegister( String arduinoNetworkName,
                         String arduinoNetworkAddress,
                         int networkPort,
                         String webServerUrl) throws MalformedURLException,
                                                     UnsupportedEncodingException
{
   url = new URL( webServerUrl );
   System.out.println( "["+webServerUrl+"] in here" );
   
   postData = "arduinoName=" + URLEncoder.encode( arduinoNetworkName,"UTF-8") +
              "&arduinoAddress="+URLEncoder.encode( arduinoNetworkAddress,"UTF-8" ) +
              "&arduinoPort="+URLEncoder.encode( networkPort+"", "UTF-8" );
              
   contentLength = Integer.toString( postData.getBytes().length);
        
        
}
        
        
 public void run()
 {
         try{
             urlConnection = (HttpURLConnection)url.openConnection();
             urlConnection.setDoInput( true );
             urlConnection.setDoOutput( true );
             urlConnection.setUseCaches( false );
            
             
             urlConnection.setRequestMethod( "POST" );
             urlConnection.setRequestProperty("Content-Length", contentLength);
                    
             urlConnection.setRequestProperty( "Content-Type",
                                   "application/x-www-form-urlencoded");
             urlConnection.setRequestProperty("Content-Language","en-IE");
             
             dataOut = new DataOutputStream(
                     urlConnection.getOutputStream() );
             dataOut.writeBytes( postData );
             dataOut.flush();
             dataOut.close();
             
             responseBuffer = new StringBuffer();
             
             dataIn = new BufferedReader( new InputStreamReader(
                           urlConnection.getInputStream() ) );
             
             while( ( responseLine = dataIn.readLine() ) != null )
             {
                responseBuffer.append( responseLine ); 
                
             }
             System.out.println( responseBuffer.toString() );
             
             dataIn.close();
             
         }catch( IOException ioe ){
                 System.err.println( ioe );
         }
         
        urlConnection = null;
        dataOut = null;
        dataIn = null;
        responseBuffer = null;
 }
}
