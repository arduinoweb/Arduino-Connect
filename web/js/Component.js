function Component( id )
{
  var d3 = [[0, 12], [7, 12], null, [7, 2.5], [12, 2.5]];
  var refreshRate = 1000;
  var prevRefreshRate = 1000;
  var graph = null;
  var componentId = id;
  var input = -1;
  var title = "Title";
  var arduinoName = "none";
  var isActive = false;
  var scheduler = null;
  
  $('#rightcolumn').append(
             '<div id="componentContainer'+componentId+'" class="componentContainer shadowed"><span class="edit title" id="componentTitle'+componentId+'">'
             + 'Title</span><span id="close'+componentId+'" class="close"></span><div id="component'+componentId+'" class="graph">'+
             
             '</div><div  style="margin-top: 20px"><img id="refreshIcon'+componentId+'" alt="refresh rate icon" title="click to edit refresh rate" src="img/clock.png" />'+
             
             '<div id="refreshrate'+componentId+'" class="refreshpanel" ><label id="value'+componentId+'">1s</label><div id="refreshSlider'+componentId+'"></div></div>'
             +
             
             '<div class="inputs" ><span id="inputArea'+componentId+'"></span><div id="pinPanel'+componentId+'" class="pinPanel"><label id="pinValue'+componentId+'">Pin -1</label><div id="pinSlider'+componentId+'"></div></div></div>'
             
             +'<img id="statusLight'+componentId+'" src="img/redlight.png" style="width: 20px; height:20px; float:right" /></div>'
      
             
             
     );
     
   
       
       $('#componentContainer'+componentId).draggable({
                       
                       containment: '#rightcolumn',
                       scroll: true
                      
       });
              
		
       $('#close'+componentId).click( function(){
                       
         $('#componentContainer'+componentId).remove();        
       });
   
       $('#componentTitle'+componentId).editable( "save.php",{
                       indicator: '<img src="img/ajax-loader.gif" />',
                       tooltip : 'click to edit...'
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
                       min: -1,
                       max: 12,
                       slide: function( event, ui ){
                           $('#pinValue'+componentId).html("Pin " + ui.value );
                           input = ui.value;
                           
                           if( ui.value == -1 )
                           {
                             $('#statusLight'+componentId).click();
                           }
                               
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
       
          if( status == 'img/greenlight.png')
          {
             $(this).attr( 'src','img/redlight.png');
             isActive = false;
          }
          else
          {
            
             
             if( input != -1 && arduinoName != 'none')
             {
                 $(this).attr( 'src','img/greenlight.png');
                 isActive = true;
             }
          }
                      
      });
      
      this.drawGraph = function()
      {
         graph = $.plot( $("#component"+componentId), [d3],{
                bars: { show: true},
	        lines:{ show: true},
	        points:{ show: true},
	        xaxis:{  show: false },
	        yaxis:{ min: 0, max:255}
		
    
             });
              
      }
    
}
