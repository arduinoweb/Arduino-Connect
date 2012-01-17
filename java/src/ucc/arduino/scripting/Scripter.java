package ucc.arduino.scripting;

//import bsh.Interpreter;
import ucc.arduino.main.Arduino;
import ucc.arduino.net.Messenger;

import org.mozilla.javascript.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.HashMap;

public class Scripter implements Runnable {
        
   private boolean stayAlive;     
  
   private final Arduino ARDUINO;
   private final File SCRIPT;
;
   private boolean invoke;

   private boolean firstRun;
   private HashMap<String, Object> state;
   private Script compiledScript;
   private Messenger messenger;
   
   //Rhino stuff
   private Context context;
   
   public Scripter(Arduino arduino, File script ) throws FileNotFoundException,
                                                         IOException
   {
       
       invoke = false;
       stayAlive = true;
       ARDUINO = arduino;
       SCRIPT = script;
       state = new HashMap<String, Object>();
       messenger = new Messenger();
   }
   
   public void run()
   {
      while( stayAlive )
      {
       
      // interpreter = new Interpreter();
        if( invoke )
        {
     
        context = Context.enter();
        
        try{
           Scriptable scope = context.initStandardObjects(); 
           Object arduinoWrapped = Context.javaToJS( ARDUINO, scope );
           Object firstRunWrapped = Context.javaToJS( firstRun, scope );
           Object stateWrapped = Context.javaToJS( state, scope );
           Object messengerWrapped = Context.javaToJS( messenger, scope );
           
           ScriptableObject.putProperty( scope, "ARDUINO", arduinoWrapped);
           ScriptableObject.putProperty( scope, "firstRun",firstRunWrapped);
           ScriptableObject.putProperty( scope, "state", stateWrapped);
           ScriptableObject.putProperty( scope, "messenger", messengerWrapped);
           Script compiledScript = context.compileReader(
                   new java.io.FileReader(SCRIPT), "test.bsh", 1, null );
           
           
           compiledScript.exec( context, scope );           
          // state = scope.get( "state", scope );
           firstRun = false;
           
        }catch(Exception e ){ System.err.println(e);        
        }finally{
          Context.exit();       
        }
        
        }    
       }

       invoke = false;
       context = null;
       
      Thread.yield();       
      }
           
    
    public synchronized void runScript()
    {
        if( ! invoke )
        {
           invoke = true;
        }
            
    }
      
   }
   
       
 
