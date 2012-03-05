$(document).ready( function(){
                
  
           
                
  function getArduinoList()
  {
          
          
    $.post("arduinolist.php", function( data ){
                        
       var response = "";
      
       try{
       var response = JSON.parse( data );
       }catch( e ){
           console.log;
       }
       
       
       var len = response.length;
       var count = 0;
       
       $('.arduino').remove();
       $('#noArduinoMessage').remove();
         for( name in response )
           {
             $('#arduinoList').append('<p id="'+name+'" class="arduino bordered" title="'+ response[name].address+':'+response[name].port+'">'+name+'</p>');
             $('#'+name).draggable({ helper:'clone', revert:'invalid', zIndex:2700,
                    cursor: "move", cursorAt: { top: 20, left: 46 }});
              count++;
              }
          
              
          if( count == 0 )
          {
            $.gritter.add({
	       title: "Active Arduinos",
               text: "none detected will try again in " + arduinoListRetrievalRate + " seconds"
             });     
              
            $('#arduinoList').append('<p id="noArduinoMessage" style="color: #ccc"> no arduino available at the moment</p>');
          }
    }).error( function(){
          $.gritter.add({
	       title: "Active Arduinos",
               text: "Network error: unable to contact server, trying again in" + arduinoListRetrievalRate + " seconds"
             });     
            
    });
              
      
  
  }
// alert( arduinoListRetrievalRate * 1000);
  getArduinoList();
  setInterval( getArduinoList, ( arduinoListRetrievalRate * 1000));
  
});
