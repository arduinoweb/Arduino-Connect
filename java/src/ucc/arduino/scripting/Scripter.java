package ucc.arduino.scripting;


import ucc.arduino.main.Arduino;
import ucc.arduino.net.Messenger;


import javax.script.*;

import java.util.HashMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;

import java.util.HashMap;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;

public class Scripter implements Runnable {
        
   private boolean stayAlive;     
   private  final File SCRIPT;
   private HashMap<String, Object> state;
   private final Messenger messenger;
   private ScriptEngineManager scriptEngineManager;
   private ScriptEngine scriptEngine;
   private CompiledScript compiledScript;
   private Compilable compilable;
   private Bindings bindings;
   private long scriptLastModified;
   private final TransferQueue<HashMap<Integer,Integer>> TRANSFER_QUEUE;
   
   public Scripter( File script, TransferQueue<HashMap<Integer,Integer>> transferQueue ) throws FileNotFoundException,
                                                         IOException,
                                                         ScriptException
   {
      super();
      TRANSFER_QUEUE = transferQueue;
      stayAlive = true;
      SCRIPT = script;
      state = new HashMap<String, Object>();
      messenger = new Messenger();
      scriptEngineManager = new ScriptEngineManager();
      scriptEngine = scriptEngineManager.getEngineByName("JavaScript");
      bindings = scriptEngine.createBindings();
       
      compilable = (Compilable) scriptEngine;    
     
      bindings.put( "messenger", messenger);
      bindings.put( "state", state);
      compiledScript = compilable.compile( new FileReader( SCRIPT ) );
      compilable = null;
      scriptEngine = null;
      scriptEngineManager = null;
   }
   
   
   
   public void run()
   {
      HashMap<Integer,Integer> pinState;
   
      
      while( stayAlive )
      {
      
      //  if( (pinState = this.poll() ) != null )   
       // {
          
        try{
           pinState = TRANSFER_QUEUE.take();
           bindings.put("pins", pinState); 
           compiledScript.eval( bindings);
        }catch( InterruptedException ie ){
                System.err.println( ie );
         }catch(ScriptException e ){ 
           System.err.println(e);        
         }    
         finally{ 
           pinState = null;       
         }
     //   }
     
     // pinState = null;
     // Thread.yield();       
      }
   }
           
    
  public synchronized void stop()
  {
      stayAlive = false;
      bindings = null;
  }
      
   }
   
       
 
