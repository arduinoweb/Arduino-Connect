#include <stdio.h>

/*
  Analog input, analog output, serial output
 
 Reads an analog input pin, maps the result to a range from 0 to 255
 and uses the result to set the pulsewidth modulation (PWM) of an output pin.
 Also prints the results to the serial monitor.
 
 The circuit:
 * potentiometer connected to analog pin 0.
   Center pin of the potentiometer goes to the analog pin.
   side pins of the potentiometer go to +5V and ground
 * LED connected from digital pin 9 to ground
 
 created 29 Dec. 2008
 Modified 4 Sep 2010
 by Tom Igoe
 
 This example code is in the public domain.
 */
// These constants won't change.  They're used to give names
// to the pins used:
const int analogInPin = A0;  // Analog input pin that the potentiometer is attached to
const int analogOutPin = 9; // Analog output pin that the LED is attached to

int sensorValue = 0;        // value read from the pot
byte outputValue = 0;        // value output to the PWM (analog out)




#define  WRITE_SYNC 'R'
#define  READ_SYNC  'W'
#define END_OF_MESSAGE 'E'
#define NOTHING_TO_RECEIVE 'N'
#define  ANALOG 'A'
#define DIGITAL 'D'
#define SPACE ' '
#define UNSET '\0'
#define TIMEOUT 50

int value1 = 255;
int value2 = 10;
int value3 = 10;
int value4 = 10;
int value5 = 10;
int value6 = 10;
int value7 = 10;
int value9 = 10;

void setup() {
  // initialize serial communications at 9600 bps:
 Serial.begin(9600); 
 pinMode( A0, INPUT );

 value1 = value2 = analogRead( A0 );
 sendMsg( 10, value1 );
 
 
}


void loop() {
 
   // change the analog out value:
  // analogWrite(analogOutPin, outputValue);           
 
// value1 = (value1 + 1) % 255;
// value2 = (value2 + 1) % 255;  
// value3 = (value3 + 3) % 255;
// value4 = (value4 + 4) % 255;
// value5 = (value5 + 5) % 255;
// value6 = (value6 + 6) % 255;
//value7 = (value7 + 7) % 255;
// value9 = (value9 + 1) % 255;
//  sendMsg(2, value2);
  
//  readMsg();
  
// sendMsg( 2, value2 );
// sendMsg( 3, value3 );
// sendMsg( 4, value4 );
// sendMsg( 5, value5 );
// sendMsg( 6, value6 );
// sendMsg( 7, value7 );
//sendMsg( 9, value9 );
// readMsg();
  //delay( 100 );*/
  
  value1 = analogRead( A0 );
  value1 = map( value1, 0,1023,0,255);
  //delay( 1000);
  
  if( value2 != value1 )
  {
  sendMsg( 10, value1 );
  value2 = value1;
  }
  
  readMsg();
}

 void sendMsg(int pin,   int msg )
 {
     
     

     //Serial.print(  WRITE_SYNC, BYTE );
      Serial.write( WRITE_SYNC );
     waitForData();
     
      if( Serial.peek() == WRITE_SYNC )
      {
        Serial.read();
      
      Serial.print( pin, DEC);
     
   
      
      //Serial.print(SPACE, BYTE); 
      Serial.write( SPACE );
   
      Serial.print( msg, DEC );
  
      //Serial.print( END_OF_MESSAGE, BYTE ); 
      Serial.write( END_OF_MESSAGE);
      }
      else
        Serial.read();
      
     
      
 } 
 

   
void readMsg()
{
  byte pinType = -1;
  byte pinNum = -1;
  byte pinValue = -1;
  
  //Serial.print( READ_SYNC, BYTE );
  Serial.write( READ_SYNC );
  waitForData();
  
  if( Serial.peek() == READ_SYNC )
  {
    Serial.read();
    waitForData();
    pinType = Serial.read();
    waitForData();
    pinNum = Serial.read();
    waitForData();
    pinValue = Serial.read();
    
    if( pinType == ANALOG )
    {
       analogWrite( pinNum, pinValue );
       
    }
    else if( pinType == DIGITAL )
    {
      digitalWrite( pinNum, pinValue );
    }
    sendMsg( pinNum, pinValue );
    
  }
  else
    Serial.read();
  
 
  
    

}
void waitForData()
{
  unsigned long startTime = millis();
  unsigned long currentTime = startTime;
  
  while( ( currentTime - startTime ) < TIMEOUT
          && Serial.available() == 0 )
  {
    currentTime = millis();
  }
  
}


