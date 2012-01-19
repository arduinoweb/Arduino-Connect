package ucc.arduino.scripting;


import ucc.arduino.main.Arduino;
import ucc.arduino.net.Messenger;


import javax.script.*;

import java.util.HashMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.HashMap;

public class Scripter extends ConcurrentLinkedQueue<HashMap<Integer,Integer>> implements Runnable {
        
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
      
   public Scripter( File script ) throws FileNotFoundException,
                                                         IOException,
                                                         ScriptException
   {
      super();
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
      long lastModified;
      
      while( stayAlive )
      {
      
        if( (pinState = this.poll() ) != null )   
        {
          
        try{
           bindings.put("pins", pinState); 
           compiledScript.eval( bindings);
         }catch(ScriptException e ){ 
           System.err.println(e);        
         }    
        }
     
      pinState = null;
      Thread.yield();       
      }
   }
           
    
  public synchronized void stop()
  {
      stayAlive = false;
      bindings = null;
  }
      
   }
   
       
 
