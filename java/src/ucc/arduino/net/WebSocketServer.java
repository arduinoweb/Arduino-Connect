package ucc.arduino.net;

import java.net.ServerSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.IOException;
import java.net.Socket;

import java.util.concurrent.ExecutorService;

public class WebSocketServer implements Runnable{
        
        
  private final ServerSocket WEB_SOCKET_SERVER;
  private final ExecutorService EXECUTOR_SERVICE;
  
  
  public WebSocketServer(final ExecutorService EXECUTOR_SERVICE ) throws UnknownHostException,
                                  IOException
  {
          
      WEB_SOCKET_SERVER = new ServerSocket( 10004, 50, InetAddress.getByName("127.0.0.1") );
      this.EXECUTOR_SERVICE = EXECUTOR_SERVICE;
      
          
  }
        
        
  public void run()
  {
    Socket socket;
 
    System.out.println("WebSocketServer thread started");
    while( true )
    {
            try{      
      socket = WEB_SOCKET_SERVER.accept();
      EXECUTOR_SERVICE.execute( new WebSocket( socket ) );
      
            }catch(IOException ioe ){
                    System.err.println( ioe );
            }
      socket = null;      
    }
          
  }
}
