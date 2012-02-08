

var pin10 = PIN_MAP_COPY.get(new java.lang.Integer(1));

  /*SESSION_MAP.put("10", new java.lang.Integer( pin10 ) );*/
  java.lang.System.out.println( "In Script: Pin 10 " + pin10);
if( pin10 == 190 )
{
    //    java.lang.System.out.println( "In Script: Pin 10 == 190");
        messenger.sendMsg( "127.0.0.1", 10002, "W D 7 255 E");
}
else if ( pin10 == 2 )
{
    //            java.lang.System.out.println( "In Script: Pin 10 == 2");
     messenger.sendMsg( "127.0.0.1", 10002, "W D 7 0 E");
}
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
