$(document).ready( function(){
                
  $('#sendButton').button( {disabled:false});     
                
  $('#sendTarget').droppable({  
     activeClass: 'ui-state-highlight', 
     accept: '.arduino',
     tolerance: 'pointer',
     drop: function( event, ui ) {
        var id = $(ui.draggable).attr('id');
            $('#sendTarget').html( id );
            $('#sendTarget').attr( 'title', $(ui.draggable).attr('title'));
         }
   }); 
  
  
  $('#sendButton').click(function(){
       var sendTarget = $('#sendTarget').html();
       var sendCommand = $('#sendCommand').val();
       var goodToGo = true;
       
       sendCommand = $.trim( sendCommand );
       
       if( sendTarget == "write to arduino" )
       {
           $.gritter.add({
	        title: "Write",
                text: "Drag and drop an Arduino to write to it"
           });
           
          goodToGo = false;
       }
       
       if( goodToGo )
       {
          if( sendCommand == null || sendCommand.length == 0 )
          {
               $.gritter.add({
	        title: "Write",
                text: "You must enter a command to send"
           });     
         
              
           goodToGo == false;
          }
          
       }
       
       if( goodToGo )
       {
           var ipaddressTarget = $('#sendTarget').attr( 'title');
           ipaddressTarget = ipaddressTarget.split(":");
           sendCommand = sendCommand.toUpperCase();
           
           $.post('arduinowrite.php', {ipaddress:ipaddressTarget[0],port:ipaddressTarget[1],command:sendCommand }, 
                   function( data ){
                   data = $.trim( data );
                   var response = "";
                   var error = true;
                   try{
                           response = JSON.parse( data );
                           
                   }catch( err ){
                       console.log( err );

                   }
                 
                   
                   if( response.reply == "error" )
                   {   $.gritter.add({
	                  title: "Send Command",
                          text: "Unable to contact arduino at the moment"
                       });  
                   }
                   
                   else if( response.reply == "invalid message format" )
                   {
                      $.gritter.add({
	                  title: "Send Command",
                          text: "invalid message format"
                       });        
                   }
                   
                   else if( response.reply == "OK" )
                   {
                      $.gritter.add({
	                  title: "Send Command",
                          text: "Message Written to Arduino"
                          
                       });     
                           
                   }   
                   
                  else if( response.pins != undefined )
                   {
                         var  result = "";
                         
                         for( var i in response.pins)
                         {
                                 
                                 result += " Pin : " + response.pins[i].pin +
                                 " Value: " + response.pins[i].value + "<br />";
                         }
                          
                          
                       var targetArduino = $('#sendTarget').html();
                       targetArduino += "<br />";
                       targetArduino += ipaddressTarget[0] +":"+ipaddressTarget[1]+"<br/>";
                       var time = new Date();
                       time = time.toString() + "<br />";
                        $.gritter.add({
	                  title: "Value of Pins",
                          text: time + targetArduino + result,
                          sticky: true,
                         
                       
                       });  
                           
                   }
                          
           });
               
       }
    
       
       
       
      
  });
  
  
});

