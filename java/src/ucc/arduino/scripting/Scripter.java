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
   private boolean execute;
   public Scripter(Arduino arduino)
   {
       stayAlive = true;
       this.arduino = arduino;
       execute = false;
       interpreter = new Interpreter();
       
   }
   
   public void run()
   {
      while( stayAlive )
      {
       if( execute )
       {
        interpreter = new Interpreter();
         try{
         // interpreter.eval();
         interpreter.set("Arduino", arduino);
          interpreter.source( "test.bsh");
        }catch( Exception e){System.err.println( e );}
        //interpreter = null;     
        execute = false;        
       }
              
       Thread.yield();       
      }
           
           
   }
       
   private void loadScript()
   {
       interpreter = null;
       interpreter = new Interpreter();
       try{
               
         interpreter.set( "Arduino", arduino );
         interpreter.source( "test.bsh");
         
       }catch( Exception e){
         System.err.println( e );       
       }
           
   }
   
   public synchronized void runScript()
   {
       execute = true;       
           
   }
  
        
}
