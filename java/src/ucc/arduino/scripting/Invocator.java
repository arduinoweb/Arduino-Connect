package ucc.arduino.scripting;

import java.util.concurrent.TransferQueue;

public class Invocator implements Runnable{

  private final TransferQueue< Runnable > INVOCATION_QUEUE;
  private Runnable invocationObject;
  
  public Invocator( final TransferQueue< Runnable > INVOCATION_QUEUE )
  {
      this.INVOCATION_QUEUE = INVOCATION_QUEUE;          
      invocationObject = null;
  }


  public void run()
  {
     while( true )
     {
       try{
       
        invocationObject = INVOCATION_QUEUE.take();
        
        invocationObject.run();
       }catch( InterruptedException ie ){
           System.err.println( ie );
       }
       
       invocationObject = null;
     }
          
  }



}
