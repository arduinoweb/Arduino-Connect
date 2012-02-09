package ucc.arduino.configuration;


public interface SerialConfiguration{

  public String getSerialPort();
  
  public Integer getSerialBaudRate();
  
  public Integer getSerialDataBits();
  
  public Integer getSerialStopBits();
  
  public Integer getSerialParity();
    

}
