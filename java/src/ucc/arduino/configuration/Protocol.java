package ucc.arduino.configuration;

/** Review code to get rid of basically the same Messages*/
public class Protocol{

  // For communicating with the Arduino
  public final static String MESSAGE_DELIMITER = " ";
  public final static byte   READ_SYNC = 'R';
  public final static byte   WRITE_SYNC = 'W';
  public final static byte   END_OF_MESSAGE = 'E';
  public final static byte   NOTHING_TO_SEND = 'N';
  public final static byte   NULL_BYTE = '\0';
  
  // For communicating with network clients
  public final static String UNKNOWN_PIN = "x";
  public final static String END_OF_CLIENT_MESSAGE = "E";
  public final static String BAD_MESSAGE_FORMAT = "BAD MESSAGE FORMAT";
  public final static String READ_MESSAGE = "R";
  public final static String WRITE_MESSAGE = "W";
  public final static String DIGITAL_WRITE = "D";
  public final static String ANALOG_WRITE = "A";
  public final static String OK = "OK";
}