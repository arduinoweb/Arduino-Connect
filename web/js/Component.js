function Component( id )
{
  this.__arduinoName__="none";
  this.__componentId__=id;
  this.__componentTitle__="click to edit";
  this.__isActive__=false;
  this.__scheduler__=null;
  this.__refreshRate__=1000;
  this.__input__=0;
  this.__component__=null;
  
 
}




Component.prototype.init = function(){

   var _self = this;
   _self.createContainer();
   
    //Make the component draggable
   $('#componentContainer'+_self.__componentId__).draggable({
         containment: '#rightcolumn',
         scroll: true
   });
   
   //Make the component closeable
    $('#close'+_self.__componentId__).click( function( ){
         
     if( _self.__isActive__)
     {
            _self.stopScheduler();
     }
     
     
     $('#componentContainer'+_self.__componentId__).remove();   
         
   });
    
    
  //Make the components title editable
  $('#componentTitle'+_self.__componentId__).editable( function( value,settings){
                       _self.__componentTitle__ = value;
                       return value;
                       
  });
  
  //Create the slider for adjusting refresh rate
  $('#refreshSlider'+_self.__componentId__).slider({
         orientation: 'vertical',
         min: 1,
         max: 60,
         slide: function( event, ui ){
               $('#value'+_self.__componentId__).html( ui.value +"s" );
                   _self.__refreshRate__ = ui.value * 1000;
                               
                }
    });
    
  //Make the icon clickabel to show/hide the refresh slider
  $('#refreshIcon'+_self.__componentId__).click( function(){
     $('#refreshRate'+_self.__componentId__).toggle();  
   
         if( $(this).css('display') != 'none' &&
             $('#pinPanel'+_self.__componentId__).css('display') != 'none' )
         {
                 $('#pinPanel'+_self.__componentId__).toggle();       
         }

      });
 
 //Create the slider that allows pin selection
 $('#pinSlider'+_self.__componentId__).slider({
       orientation:'vertical',
       min: 0,
       max: 12,
       slide: function( event, ui ){
            $('#pinValue'+_self.__componentId__).html("Pin " + ui.value );
            
            },
            stop: function( event, ui ){
               _self.__input__ = ui.value;       
            }
  });
  
 
 //Make the component droppable to allow dragging of arduinos on to it
 $('#componentContainer'+_self.__componentId__).droppable({
     accept: '.live',
     drop: function(event, ui){
            _self.__arduinoName__ = $(ui.draggable).attr('id');
           
                
           _self.stopScheduler();
                            
            $('#inputArea'+_self.__componentId__).html(
                '<img id="inputIcon'+_self.__componentId__+
                '" src="img/plug.png" style="margin-right:15px" />'+
                _self.__arduinoName__);
                            
            $('#inputIcon'+_self.__componentId__).click( function(){
                  $('#pinPanel'+_self.__componentId__).toggle();           
                                 
                  if( $('#refreshRate'+_self.__componentId__).css('display') 
                            != 'none' &&
                      $('#pinPanel'+_self.__componentId__).css('display')
                            != 'none')
                        $('#refreshRate'+_self.__componentId__).toggle();
                                       
              });
          }
      });
 
 
 //Handle turning on and off the component
 $('#statusLight'+_self.__componentId__).click( function(){
                      
          var status = $(this).attr('src');
          _self.hideSliders();
          if( status == 'img/greenlight.png')
          {
            _self.stopScheduler();
          }
          else
          {
              
             if( _self.__arduinoName__ != 'none')
             {
                 
                 if( _self.__isActive__ ==true)
                 {
                    window.clearInterval( _self.__scheduler__ );
                    _self.__scheduler__ = null;
                    _self.__isActive=false;
                          
                 }
               
                 
               _self.startScheduler();
                 
             }
          }
                      
      });
 
 _self.draw();
   
}



//Stops the scheduling of data retrieval from the web server
Component.prototype.stopScheduler = function(){
if( this.__isActive__ == true)
{
  $('#statusLight'+this.__componentId__).attr('src','img/redlight.png');
  window.clearTimeout( this.__scheduler__ );
  this.__scheduler__ = null;  
  this.__isActive__ = false;
}
}
     

//Starts the scheduling of data retrieval from the web server
Component.prototype.startScheduler = function(){
   var _self = this;
   
  if( this.__isActive__ ==false)
   {
      $('#statusLight'+this.__componentId__).attr('src','img/greenlight.png');
      this.__isActive__ = true; 
      this.__scheduler__ = window.setTimeout(function(){_self.getData();}, 
                                              0);
   }        
           
         
 }


 
//Hides the refresh and pin selector sliders
 Component.prototype.hideSliders = function(){
         
      var refresh = $('#refreshRate'+this.__componentId__).css('display');
      var pin = $('#pinPanel'+this.__componentId__).css('display');
      
      if( refresh != 'none') $('#refreshRate'+this.__componentId__).toggle();
      
      if( pin != 'none') $('#pinPanel'+this.__componentId__).toggle();   
         
         
         
 }
 
 Component.prototype.getData = function(){
        
  var request = "R " + this.__input__ + " E";
  var _self = this;
  
  $.post("proxy.php",{arduino:_self.__arduinoName__,msg:request}, function(data){
                  
     var response = JSON.parse( data );
     
     var tmpValue = parseInt( response.msg );
     
     if( isNaN( tmpValue ) )
     {
        $.gritter.add({
                  title: _self.__componentTitle__,
                  text: 'An error ocurred retreiving data. Retrying...',
                  });         
             
     }
     else
     {
       _self.update( tmpValue );             
     }
  })
  .error( function() {
       $.gritter.add({
           title: _self.__componentTitle__,
           text: "Unable to access webserver",
       });
       _self.stopScheduler();
                  
  })
  .complete( function(){
                  
     if( _self.__isActive__ == true )
     {
     this.__scheduler__ = window.setTimeout(function(){_self.getData();}, 
                                              _self.__refreshRate__);

     }             
  });
                      
  
  
        
}

