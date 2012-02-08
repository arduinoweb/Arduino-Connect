

var pin10 = pins.get(new java.lang.Integer(10));
var threshold = 127;


var prevValue = state.get("10");
  
java.lang.System.out.println( "Running Script: Previous Value of pin 10  " + prevValue);
java.lang.System.out.println( "                Current  Value of pin 10  " + pin10);
  state.put("10", new java.lang.Integer( pin10 ) );

//setLight( pin10);
/**if( pin10 != prevValue ) 
  {
     if( pin10 <= threshold && prevValue > threshold )
     {
        setLight( pin10);
     }
     else if( pin10 > threshold && prevValue <=threshold )
     {
        setLight(pin10);       
     }
     
     state.put("10", new java.lang.Integer( pin10) );
  }


function setLight(value){
     java.lang.System.out.println("Sending message to other Arduino");
     if( pin10 > threshold )
     {
        messenger.sendMsg( "127.0.0.1", 10002, "W D 7 255 E");
     }
     else
     {
        messenger.sendMsg("127.0.0.1", 10002, "W D 7 0 E");
     }

  }*/
