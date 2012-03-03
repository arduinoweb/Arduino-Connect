var idGenerator = new IdGenerator();

$(document).ready( function(){
                
                
                
                  
                
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
     
       monitor.addRow( new TableEntry( id, -1, false) );
       $('#monitorTable').append( html );   
    //   $('#status'+id).button();
       
       $('#pin'+id).editable( handlePinChange);
    }
    
    function constructRowHtml( name, ipaddress, port, id )
    {
            var html = '<tr class=".'+name+'" id="'+id+'"><td>'+name+'</td>';
            html+='<td>'+ipaddress+':'+port+'</td><td id="pin'+id+'">-1</td><td id="value'+id+'">-</td>';
            html+='<td><input id="status'+id+'" type="checkbox" /><label for="status'+id+'"></label></td></tr>';
            
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
            
       if( isNaN(value ) || value < 0 )
       {
         displayMsg("Only positive pin values wll be monitored");
         value=-1;
        }
        else
        {
          var arduinoName = $(this).parent().attr("class");
          var id = $(this).parent().attr("id");
          var arduino = tableEntries[ arduinoName.substring(1) ];
          var tableRow = arduino.getRow( id );
          var currentPin = tableRow.getPin();
          var isMonitored = arduino.isPinMonitored( value );
          
          if( currentPin == -1  && ! isMonitored )
          {
             tableRow.setPin( value );
              displayMsg( "Pin Number Changed");    
          }
          else 
          {
              if( isMonitored )
              {        
                displayMsg( "You are already monitoring this pin");
                value=-1;
              }
              else
              {
                tableRow.setPin( value );
                displayMsg( "Pin Number Changed");
                
              }
          }
        

        
       }
                       
    return(value);              
           
    }
});

