function Component( id )
{
  var data = [];
  
  
  var totalPoints = 20;
  
  for( var i = 0; i < totalPoints; i++)
  {
            data.push( 0);       
  }



  var refreshRate = 1000;
 
  var graph = null;
  var componentId = id;
  var input = 0;
  var componentTitle = "Title ..click to edit";
  var arduinoName = "none";
  var isActive = false;
  var scheduler = null;
  var request = "";
  
  $('#rightcolumn').append(
             '<div id="componentContainer'+componentId+'" class="componentContainer shadowed"><span class="edit title" id="componentTitle'+componentId+'">'
             + componentTitle +'</span><span id="close'+componentId+'" class="close"></span><div id="component'+componentId+'" class="graph">'+
             
             '</div><div  style="margin-top: 20px"><img id="refreshIcon'+componentId+'" alt="refresh rate icon" title="click to edit refresh rate" src="img/clock.png" />'+
             
             '<div id="refreshrate'+componentId+'" class="refreshpanel" ><label id="value'+componentId+'">1s</label><div id="refreshSlider'+componentId+'"></div></div>'
             +
             
             '<div class="inputs" ><span id="inputArea'+componentId+'"></span><div id="pinPanel'+componentId+'" class="pinPanel"><label id="pinValue'+componentId+'">Pin 0</label><div id="pinSlider'+componentId+'"></div></div></div>'
             
             +'<img id="statusLight'+componentId+'" src="img/redlight.png" style="width: 20px; height:20px; float:right" /></div>'
      
             
             
     );
     
   
       
       $('#componentContainer'+componentId).draggable({
                       
                       containment: '#rightcolumn',
                       scroll: true
                      
       });
              
		
       $('#close'+componentId).click( function(){
         
         if( isActive == true )
         {
            stopScheduler();
         }
         
         $('#componentContainer'+componentId).remove();   
         
       });
   
       $('#componentTitle'+componentId).editable( function( value,settings){
                       componentTitle = value;
                       return value;
                       
       });
       
       $('#refreshSlider'+componentId).slider({
                       orientation: 'vertical',
                       min: 1,
                       max: 60,
                       slide: function( event, ui ){
                               
                               $('#value'+componentId).html( ui.value +"s" );
                               refreshRate = ui.value * 1000;
                               
                       }
                       
       });
       
       $('#pinSlider'+componentId).slider({
                       
                       orientation:'vertical',
                       min: 0,
                       max: 12,
                       slide: function( event, ui ){
                           $('#pinValue'+componentId).html("Pin " + ui.value );
                           //input = ui.value;
                         
                               
                       },
                       stop: function( event, ui ){
                           input = ui.value;       
                       }
       });
     $('#refreshIcon'+componentId).click( function(){
         $('#refreshrate'+componentId).toggle();  
         
         if( $(this).css('display') != 'none' &&
             $('#pinPanel'+componentId).css('display') != 'none' )
         {
                 $('#pinPanel'+componentId).toggle();       
         }
                    
      });
      
      $('#componentContainer'+componentId).droppable({
                      
                      accept: '.live',
                      drop: function(event, ui){
                            arduinoName = $(ui.draggable).attr('id');
                            isActive = false;
                            
                            stopScheduler();
                            
                            $('#inputArea'+componentId).html(
                                    '<img id="inputIcon'+componentId+'" src="img/plug.png" style="margin-right:15px" />'+arduinoName);
                            
                              $('#inputIcon'+componentId).click( function(){
                                
                                 $('#pinPanel'+componentId).toggle();           
                                 
                                   if( $('#refreshrate'+componentId).css('display') 
                                           != 'none' &&
                                        $('#pinPanel'+componentId).css('display')
                                           != 'none')
                                        $('#refreshrate'+componentId).toggle();
                                       
                                 });
                      }
      });
          
    
      $('#statusLight'+componentId).click( function(){
                      
          var status = $(this).attr('src');
          hideSliders();
          if( status == 'img/greenlight.png')
          {
            stopScheduler();
          }
          else
          {
              
             if( arduinoName != 'none')
             {
                 
                 if( isActive == true )
                 {
                    window.clearInterval( scheduler );
                    scheduler = null;
                          
                 }
                 else
                 {
                    isActive = true;
                 }
                 
                 startScheduler();
                 
             }
          }
                      
      });
      
      this.drawGraph = function()
      {
         graph = $.plot( $("#component"+componentId), [data],{
                bars: { show: false},
	        lines:{ show: true},
	        points:{ show: true},
	        xaxis:{  show: false },
	        yaxis:{ min: 0, max:255}
		
    
             });
              
      }
    function getData()
    {          
            request = "R " + input + " E";
            $.post("proxy.php", { arduino: arduinoName, msg:request},function( data ){
                            
               var response = JSON.parse( data );
               
               var tmpValue = parseInt( response.msg );
               
               if( isNaN( tmpValue ) )
               {
           
                 $.gritter.add({
                  title: componentTitle,
                  text: 'An error ocurred retreiving data.Possible causes:<ul class="errorMessage"><li class="errorMessage">Arduino is offline</li><li class="errorMessage">Invalid Pin Number</li></ul>',
                  });
                 stopScheduler();
               }            
               else
               {
               
                 
                  graph.setData( [ updateData( tmpValue) ] );
	          graph.setupGrid();
	          graph.draw();
                
                 
               }
                            
                            
                            
                            
                            
                            
                            
            })
            .error( function(){
                 $.gritter.add({
                  title: componentTitle,
                  text: "Unable to access webserver",
                  });              
                 stopScheduler();
            });
            
    }
    
    function updateData( updatedValue )
    {
        data = data.slice(1);

      
        data.push( updatedValue );
        // zip the generated y values with the x values
       
        var res = [];
        for (var i = 0; i < data.length; ++i)
            res.push([i, data[i]])
            
           
        return res;
         
            
            
    }
    
    function startScheduler()
    {
      $('#statusLight'+componentId).attr('src','img/greenlight.png');
      isActive = true;
      scheduler = window.setInterval(getData, refreshRate, "JavaScript" );
            
    }
    
    function stopScheduler()
    {
             $('#statusLight'+componentId).attr('src','img/redlight.png');
                   window.clearInterval( scheduler );
                   scheduler = null;  
                   isActive = false;
            
    }
    
    function hideSliders()
    {
      var refresh = $('#refreshrate'+componentId).css('display');
      var pin = $('#pinPanel'+componentId).css('display');
      
      if( refresh != 'none') $('#refreshrate'+componentId).toggle();
      
      if( pin != 'none') $('#pinPanel'+componentId).toggle();
            
            
    }
}
