package ucc.arduino.scripting;

import ucc.arduino.net.Messenger;
import ucc.arduino.main.KeyValue;
import ucc.arduino.scripting.Invocator;

import javax.script.*;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;

import java.util.HashMap;
import java.util.concurrent.TransferQueue;
import java.util.concurrent.LinkedTransferQueue;

public class Scripter implements Runnable {
        
   private boolean stayAlive;     
  // private  final File SCRIPT;
   private final HashMap<String, Object> SESSION_MAP;
   private final Messenger messenger;
 //  private ScriptEngineManager scriptEngineManager;
 //  private ScriptEngine scriptEngine;
   private CompiledScript compiledScript;
 //  private Compilable compilable;
   private Bindings bindings;
   private final Invocator INVOCATOR;
   private final TransferQueue<Invocation> INVOCATOR_QUEUE;
   
   private final TransferQueue<KeyValue<Integer,Integer>> SCRIPT_INVOCATION_QUEUE;
   private final HashMap< Integer, Integer> PIN_MAP_COPY;
   private final boolean USE_INVOCATION_THREAD;
   
   public Scripter( final File SCRIPT, 
                    final TransferQueue<KeyValue<Integer,Integer>> SCRIPT_INVOCATION_QUEUE,
                    final boolean USE_INVOCATION_THREAD ) 
                                                  throws FileNotFoundException,
                                                         IOException,
                                                         ScriptException
   {

      //SCRIPT = script;   
      messenger = new Messenger();
      ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
      ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("JavaScript");
      
       
      Compilable compilable = (Compilable) scriptEngine;
      compiledScript = compilable.compile( new FileReader( SCRIPT ) );
      
      this.USE_INVOCATION_THREAD = USE_INVOCATION_THREAD;
      this.SCRIPT_INVOCATION_QUEUE = SCRIPT_INVOCATION_QUEUE;
      
      PIN_MAP_COPY = new HashMap<Integer, Integer>();
      
      SESSION_MAP = new HashMap<String, Object>();
     
      bindings = scriptEngine.createBindings();
      bindings.put( "messenger", messenger);
    
      if( USE_INVOCATION_THREAD )
      {
          INVOCATOR_QUEUE = new LinkedTransferQueue< Invocation >();
          INVOCATOR = new Invocator( INVOCATOR_QUEUE );
          new Thread( INVOCATOR ).start();
          
          bindings.put( "INVOCATOR_QUEUE", INVOCATOR_QUEUE );
          
      }
      else
      {
         INVOCATOR = null;
         INVOCATOR_QUEUE = null;
      }
      bindings.put( "SESSION_MAP", SESSION_MAP);
     
     
      
      compilable = null;
      scriptEngine = null;
      scriptEngineManager = null;
   }
   
   
   
   public void run()
   {
      KeyValue<Integer,Integer> updatedPin;
   
      
      while( true )
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
           

      
   }
   
       
 
