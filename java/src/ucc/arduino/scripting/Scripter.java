package ucc.arduino.scripting;

import bsh.Interpreter;
import ucc.arduino.main.Arduino;


import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Scripter implements Runnable {
        
   private boolean stayAlive;     
   private Interpreter interpreter;
   private Arduino arduino;
   private final File SCRIPT;
   private String scriptContents;
   private boolean invoke;
   private Integer prevValue = 0;
   
   public Scripter(Arduino arduino, File script ) throws FileNotFoundException,
                                                         IOException
   {
       invoke = false;
       stayAlive = true;
       this.arduino = arduino;
       SCRIPT = script;
       
       /*BufferedReader br = new BufferedReader( new FileReader( script) );
     
       String dataIn = null;
       StringBuffer readBuffer = new StringBuffer();
       
       while( ( dataIn = br.readLine() ) != null )
       {
          readBuffer.append( dataIn );
          
               
       }
       scriptContents = readBuffer.toString();
       dataIn = null;
       readBuffer = null;
       interpreter = new Interpreter();
       try{
       interpreter.set("Arduino", arduino);
       }catch( bsh.EvalError ee){}*/
   }
   
   public void run()
   {
      while( stayAlive )
      {
       
        interpreter = new Interpreter();
        if( invoke )
        {
         try{
         // interpreter.eval();
          interpreter.set("prevValue", prevValue);
          interpreter.set("Arduino",arduino);
          interpreter.source( "test.bsh");
          prevValue = (Integer)interpreter.get("prevValue");
        }catch( Exception e){System.err.println( e );}
        //interpreter = null;     
        }    
       }
          interpreter = null; 
       invoke = false;
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
   
       
 
