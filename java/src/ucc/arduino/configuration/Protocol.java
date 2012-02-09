package ucc.arduino.configuration;

/** Review code to get rid of basically the same Messages*/
public class Protocol{


  public final static String MESSAGE_DELIMITER = " ";
  public final static byte   SERIAL_START_MESSAGE = 'S';
  public final static byte   SERIAL_END_MESSAGE = 'E';

  public final static byte   ANALOG_WRITE = 'A';
  public final static byte   DIGITAL_WRITE = 'D';
  public final static byte   VIRTUAL_WRITE = 'V';

  public final static String UNKNOWN_PIN = "x";
  public final static String NET_END_MESSAGE = "E";
  public final static String BAD_MESSAGE_FORMAT = "BAD MESSAGE FORMAT";
  public final static String NET_READ_MESSAGE = "R";
  public final static String NET_WRITE_MESSAGE = "W";
  public final static String OK = "OK";
}