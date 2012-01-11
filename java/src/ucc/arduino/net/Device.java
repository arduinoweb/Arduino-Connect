package ucc.arduino.net;


public class Device{
        
    private String name;
    private String ipAddress;
    private String port;
    
    public Device(){ }
    
    public Device( String name, String ipAddress, String port )
    {
            
       this.name = name;
       this.ipAddress = ipAddress;
       this.port = port;       
            
    }
        
    
    public void setName( String name )
    {
       this.name = name;
    }
    
    public String getName()
    {
       return name;       
    }
    
    public void setIpAddress( String ipAddress )
    {
       this.ipAddress = ipAddress;
    }
    
    public String getIpAddress()
    {
       return ipAddress;
    }
    
    public void setPort( String port )
    {
       this.port = port;
    }
    
    public String getPort( )
    {
        return port;
    }
        
        
        
        
        
        
        
        
}
