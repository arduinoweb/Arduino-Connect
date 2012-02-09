package ucc.arduino.main;

public class KeyValue<T, V>{
   
  private final T KEY;
  private final V VALUE;
  
  public KeyValue( final T KEY, final V VALUE )
  {
          
     this.KEY = KEY;
     this.VALUE = VALUE;
          
  }
  
  public T getKey()
  {
      return KEY;
  }
  
  public V getValue()
  {
      return VALUE;
  }
        
}
