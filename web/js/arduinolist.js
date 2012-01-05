$(document).ready( function(){
                
              
                
 var arduinoListIsRefreshing = false;
 
                
 function refreshArduinoList()
 {
       arduinoListIsRefreshing= true;
    
       
       $('#arduinoListRefresh').attr('src','img/ajax-loader.gif');         
                      
                       $.post("arduinolist.php", function( data ){
                        
                       var response = JSON.parse( data );
                       
                       var len = response.length;
                        $('.arduino').remove();
                       if( len == 0 )
                       {   
                       $('#arduinoList').append('<li class="arduino greyblue">none available</li>');
                       }
                       else if( len == 1  && response[0] == 'dberror')
                       {
                          $('#arduinoList').append(
                           '<li class="arduino error">unable to connect to database</li>'       
                                  
                          );
                       }
                       else
                       {
                         for( var i = 0; i < len; i++ )
                         {
                           $('#arduinoList').append(
                              '<li class="arduino live" id="'+response[i]+'">' +
                               response[i] + '<img src="img/greenlight.png" /></li>' );
                           
                           $('#'+response[i]).draggable({
                                           helper: 'clone',
                                           revert: 'true',
                                           scroll: false,
                                           containment: 'window',
                                           appendTo: 'body'
                                           
                           });
                                   
                                 
                         }
                               
                       }
                       
                       })
                       .error( function(){ 
                           $('.arduino').remove();
                          $('#arduinoList').append(
                              '<li class="arduino error">an error has occurred contacting the server</li>');
                              arduinoListIsRefreshing= false;
                          
                       })
                       .complete( function(){
                            $('#arduinoListRefresh').attr('src','img/reload.png'); 
                            arduinoListIsRefreshing= false;
                          
                       });                  
                        
 }
       
 function automateArduinoListRefresh()
 {
     if( !  arduinoListIsRefreshing )
     {
        refreshArduinoList();
     }
         
 }
 $('#arduinoListRefresh').click( refreshArduinoList );
 
 refreshArduinoList();
 
 window.setInterval( automateArduinoListRefresh , 30000,"JAVASCRIPT" );
});
