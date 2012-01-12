package ucc.arduino.scripting;

import bsh.Interpreter;
import ucc.arduino.main.Arduino;


import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class Scripter implements Runnable{
        
   private boolean stayAlive;     
   private Interpreter interpreter;
   private Arduino arduino;

   public Scripter(Arduino arduino)
   {
       stayAlive = true;
       this.arduino = arduino;
    
       
       
   }
   
   public void run()
   {
      while( stayAlive )
      {
       
        interpreter = new Interpreter();
         try{
         // interpreter.eval();
         interpreter.set("Arduino", arduino);
          interpreter.source( "test.bsh");
        }catch( Exception e){System.err.println( e );}
        //interpreter = null;     
             
       }
            interpreter = null;  
       Thread.yield();       
      }
           
           
   }
       
 
