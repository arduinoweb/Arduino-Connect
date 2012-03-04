var idGenerator = new IdGenerator();

$(document).ready( function(){
                
                
    $('#startButton').button();
    $('#stopButton').button();
     $('#startButton').click( startReading);
     $('#stopButton').click(stopReading);             
                
    $( "#monitorTarget" ).droppable({
		        activeClass: 'highlight', 
		        accept: '.arduino',
		        tolerance: 'pointer',
		        drop: function( event, ui ){
		           var arduinoName = $(ui.draggable).attr('id');
		           var ipaddressTarget = $(ui.draggable).attr('title');
		           var ipaddressTarget = ipaddressTarget.split(":");
		           
		           addRow(arduinoName, ipaddressTarget[0], ipaddressTarget[1] );
		       
		        }
		}); 
    
    
    function stopReading()
    {
       var arduino = null;
            
       for( i in tableEntries )
       {
          arduino = tableEntries[i];
               
          arduino.stop();     
               
       }
         $.gritter.add({
	        title: "Stop",
                text: "Stopping all monitoring",
           });      
    }
    
    
    function startReading()
    {
       var pin = null;
       var name = null;
       var id = null;
       var monitor = null;
       for( i in tableEntries )
       {
               monitor = tableEntries[i];
               name = monitor.getName();    
               monitor.clear();
               
               $("tr").each( function(){
                  
                  var tmp = $(this).attr('class');
                  var id = $(this).attr('id');
              
                if( tmp != undefined && tmp.substring(1) == name )
                  {
                      monitor.addPin( $('#pin'+id).html() );      
                          
                  }
                               
               });
              
               monitor.start();
       }
            
    }
    
    function addRow( name, ipaddress, port )
    {    
           
       var monitor = tableEntries[ name ];
         
       if( monitor == undefined )
       {
         tableEntries[ name ] = new Monitor( name, ipaddress, port );
        
       }
          
       monitor = tableEntries[ name ];
       
       var id = idGenerator.getNextId();
    
       var html = constructRowHtml( name, ipaddress, port, id );
     
      // monitor.addRow( new TableEntry( id, -1, false) );
       $('#monitorTable').append( html );   
     
       $('#pin'+id).editable( handlePinChange);
       $('#delete'+id).click( handleDelete );
                   
     
    }
    
    function constructRowHtml( name, ipaddress, port, id )
    {
            var html = '<tr class=".'+name+'" id="'+id+'"><td>'+name+'</td>';
            html+='<td>'+ipaddress+':'+port+'</td><td id="pin'+id+'">-1</td><td id="value'+id+'">-</td>';
            html+='<td class="deleteContainer" ><span id="delete'+id+'" title="delete" class="deleteRow">x</span></td></tr>';
            
            return html;
              
            
    }
    
    
    function displayMsg( msg )
    {
         $.gritter.add({
	        title: "Input Error",
                text: msg
           });    
            
    }
    
    function handlePinChange( value, settings  )
    {
       value = parseInt( value );
            
       if( isNaN(value )  )
       {
         displayMsg("Numbers only");
         value=-1;
        }
       
        

        
      
                       
    return(value);              
           
    }
    
    
   
    
    function handleDelete()
    {
          var id = $(this).parent().parent().attr( "id");
       
          $('#'+id).css( { "color":"red","background-color":"white","font-size":"13px"});
          $('#'+id).fadeOut('slow', function(){
                          
             $(this).remove();               
          });
       

          


          idGenerator.removeId( id );
          
            
    }
            
           
});

