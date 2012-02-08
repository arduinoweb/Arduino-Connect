package ucc.arduino.scripting;

import ucc.arduino.net.Messenger;
import ucc.arduino.main.KeyValue;

import javax.script.*;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;

import java.util.HashMap;
import java.util.concurrent.TransferQueue;

public class Scripter implements Runnable {
        
   private boolean stayAlive;     
   private  final File SCRIPT;
   private final HashMap<String, Object> SESSION_MAP;
   private final Messenger messenger;
   private ScriptEngineManager scriptEngineManager;
   private ScriptEngine scriptEngine;
   private CompiledScript compiledScript;
   private Compilable compilable;
   private Bindings bindings;
   private long scriptLastModified;
   private final TransferQueue<KeyValue<Integer,Integer>> SCRIPT_INVOCATION_QUEUE;
   private final HashMap< Integer, Integer> PIN_MAP_COPY;
   
   public Scripter( final File script, 
                    final TransferQueue<KeyValue<Integer,Integer>> SCRIPT_INVOCATION_QUEUE ) 
                                                  throws FileNotFoundException,
                                                         IOException,
                                                         ScriptException
   {
      super();
      this.SCRIPT_INVOCATION_QUEUE = SCRIPT_INVOCATION_QUEUE;
      stayAlive = true;
      SCRIPT = script;
      PIN_MAP_COPY = new HashMap<Integer, Integer>();
      
      SESSION_MAP = new HashMap<String, Object>();
      messenger = new Messenger();
      scriptEngineManager = new ScriptEngineManager();
      scriptEngine = scriptEngineManager.getEngineByName("JavaScript");
      bindings = scriptEngine.createBindings();
       
      compilable = (Compilable) scriptEngine;    
     
      bindings.put( "messenger", messenger);
      bindings.put( "SESSION_MAP", SESSION_MAP);
     
      compiledScript = compilable.compile( new FileReader( SCRIPT ) );
      
      compilable = null;
      scriptEngine = null;
      scriptEngineManager = null;
   }
   
   
   
   public void run()
   {
      KeyValue<Integer,Integer> updatedPin;
   
      
      while( stayAlive )
      {
      
        try{
           updatedPin = SCRIPT_INVOCATION_QUEUE.take();
           PIN_MAP_COPY.put( updatedPin.getKey(), updatedPin.getValue() );
            bindings.put("PIN_MAP_COPY", PIN_MAP_COPY);
           compiledScript.eval( bindings);
        }catch( InterruptedException ie ){
                System.err.println( ie );
         }catch(ScriptException e ){ 
           System.err.println(e);        
         }    
         finally{ 
              
         }
       
       updatedPin = null;
      }
   }
           
    
  public synchronized void stop()
  {
      stayAlive = false;
      bindings = null;
  }
      
   }
   
       
 
