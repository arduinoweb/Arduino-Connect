package ucc.arduino.scripting;

import javax.script.*;

public class ScriptCompiler{
        
  private final String SCRIPT_TYPE = "JavaScript";       
   
  private final String SCRIPT;
  
  private final ScriptEngineManager SCRIPT_ENGINE_MANAGER;
  private final ScriptEngine SCRIPT_ENGINE;
  private final Compilable  COMPILABLE;
  
 public ScriptCompiler( final String SCRIPT )
                                      throws ScriptException
 {
    this.SCRIPT = SCRIPT;
     
    SCRIPT_ENGINE_MANAGER = new ScriptEngineManager();
    SCRIPT_ENGINE = SCRIPT_ENGINE_MANAGER.getEngineByName( SCRIPT_TYPE );
    COMPILABLE = (Compilable ) SCRIPT_ENGINE;
    
 }
        
        
 public CompiledScript compile() throws ScriptException
 {
         return  COMPILABLE.compile( SCRIPT );
         
 }
        
        
        
        
        
        
        
}
