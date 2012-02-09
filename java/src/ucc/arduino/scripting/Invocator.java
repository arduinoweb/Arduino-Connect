package ucc.arduino.scripting;

import ucc.arduino.scripting.Invocation;

import java.util.concurrent.TransferQueue;

public class Invocator implements Runnable{

  private final TransferQueue< Invocation> INVOCATION_QUEUE;
  private Invocation invocationObject;
  
  public Invocator( final TransferQueue< Invocation> INVOCATION_QUEUE )
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
        
        invocationObject.invoke();
       }catch( InterruptedException ie ){
           System.err.println( ie );
       }
       
       invocationObject = null;
     }
          
  }



}
