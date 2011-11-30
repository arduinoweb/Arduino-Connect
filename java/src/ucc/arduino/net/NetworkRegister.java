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

import java.net.URL;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.conn.SingleClientConnManager;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import org.apache.http.params.HttpParams;
import org.apache.http.params.BasicHttpParams;

public class NetworkRegister{
  
 private static boolean success;
 
 public static boolean  register( ) 
 {
     DefaultHttpClient httpClient = null;
     success = true;
     
      
    
     try{  
             
       URL url = new URL( Arduino.CONFIGURATION.getWebServerUrl() );
       
       if( url.getProtocol().equalsIgnoreCase("https" ) )
       {
          int port = url.getPort();
          
          if( port == -1)
          {
             port = 443;
          }
          
          httpClient = getUnauthenticatedSSLClient( port );
       }
       else
       {
           httpClient = new DefaultHttpClient();
       }
       
     HttpPost httpost = new HttpPost( Arduino.CONFIGURATION.getWebServerUrl() );
      List<NameValuePair> nvps = new ArrayList<NameValuePair>();
         nvps.add( new BasicNameValuePair( "arduino", 
                                      Arduino.CONFIGURATION.getArduinoNetworkName() ) );
         
         httpost.setEntity( new UrlEncodedFormEntity( nvps, HTTP.UTF_8 ) );
      
         HttpResponse response = httpClient.execute( httpost );
         HttpEntity entity = response.getEntity();
       
         System.out.println( response.getStatusLine() );
        
         if( entity != null )
         {
            InputStream instream = entity.getContent();
            
            try{
                    String data = "";
                    
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader( instream ) );
                    while( ( data = reader.readLine() ) != null )
                    {
                   System.out.println( data );
                    }
            }catch( RuntimeException re ){
                    success = false;
                httpost.abort();
                                    
            }finally{ instream.close() ;}
         
         }
         
                                        
     }catch( IOException ioe ){
         System.err.println("Unable to connect to Web Server");
         success = false;
         System.exit( 1 );
     }catch(Exception e ){
             System.err.println( e.getMessage() );
             success = false;
          
     }finally{
        httpClient.getConnectionManager().shutdown();       
     }
        java.lang.System.setProperty(
              "sun.security.ssl.allowUnsafeRenegotiation", "false" );
         return false;
 }
 

 private static DefaultHttpClient getUnauthenticatedSSLClient( final int port )
 {
     DefaultHttpClient defaultClient = null;
   
   try{  
    java.lang.System.setProperty(
              "sun.security.ssl.allowUnsafeRenegotiation", "true" );
         
         X509TrustManager trustManager = 
            new X509TrustManager(){
                public void checkClientTrusted( X509Certificate[] chain,
                                                String authType)
                                            throws CertificateException{
                   
                                            }
            
               public void checkServerTrusted(X509Certificate[] chain,
                      String authType) throws CertificateException { }
     
               public X509Certificate[] getAcceptedIssuers() 
               {
                 return null;
               }
        };
         
    // Now put the trust manager into an SSLContext.
   // Supported: SSL, SSLv2, SSLv3, TLS, TLSv1, TLSv1.1
   SSLContext sslContext = SSLContext.getInstance("SSL");
   sslContext.init(null, new TrustManager[] { trustManager },
                   new SecureRandom());
   
   // Use the above SSLContext to create your socket factory
   SSLSocketFactory sf = new SSLSocketFactory(sslContext);
   
   // Accept any hostname, so the self-signed certificates don't fail
   sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
   
   // Register our new socket factory with the typical SSL port and the
   // correct protocol name.
   Scheme httpsScheme = new Scheme("https", sf, port);
   SchemeRegistry schemeRegistry = new SchemeRegistry();
   schemeRegistry.register(httpsScheme);

   HttpParams params = new BasicHttpParams();
   
   ClientConnectionManager cm = new SingleClientConnManager(params,
     schemeRegistry);
   
   defaultClient = new DefaultHttpClient( cm, params );
   
   }catch(Exception e ){
           System.err.println( e.getMessage() );
           success = false;
   }
    
   return defaultClient;
         
 }
        
        
}
