package ucc.arduino.net;

import ucc.arduino.main.Arduino;


import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class NetworkRegister{
        
  
 public static boolean  register( ) 
 {
         DefaultHttpClient httpclient = new DefaultHttpClient();
         
     try{
             
         //httpclient = new DefaultHttpClient();
         HttpPost httpost = new HttpPost( Arduino.CONFIGURATION.getWebServerUrl() );
         List<NameValuePair> nvps = new ArrayList<NameValuePair>();
         nvps.add( new BasicNameValuePair( "arduino", 
                                      Arduino.CONFIGURATION.getArduinoNetworkName() ) );
         
         httpost.setEntity( new UrlEncodedFormEntity( nvps, HTTP.UTF_8 ) );
      
         HttpResponse response = httpclient.execute( httpost );
         HttpEntity entity = response.getEntity();
       
         System.out.println( response.getStatusLine() );
         System.out.println( response.getResponseBodyAsString());
         if( entity != null )
         {
            InputStream instream = entity.getContent();
            
            try{
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader( instream ) );
                    
                   System.out.println( reader.readLine() );
                    
            }catch( RuntimeException re ){
                httpost.abort();
                
                    
            }finally{ instream.close() ;}
         
         }
         
                                        
     }catch( IOException ioe ){
         System.err.println("Unable to connect to Web Server");
         System.exit( 1 );
     }
     finally{
        httpclient.getConnectionManager().shutdown();       
     }
      
         return false;
 }
        
        
}
