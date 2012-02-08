function Component( id )
{
  this.__arduinoName__="none";
  this.__componentId__=id;
  this.__componentTitle__= id;
  this.__isActive__=false;
  this.__scheduler__=null;
  this.__refreshRate__=1000;
  this.__input__=0;
  this.__component__=null;
  this.__type__="";
  
 
}

Component.prototype.getId = function(){
      return this.__componentId__;       
}

Component.prototype.setId = function( id ){
       this.__componentId__ = id;        
}


Component.prototype.getArduinoName = function(){
      return this.__arduinoName__;       
}

Component.prototype.setArduinoName = function( name ){
      this.__arduinoName__ = name;       
        
}

Component.prototype.getComponentTitle = function(){
      return this.__componentTitle__;       
}

Component.prototype.setComponentTitle = function( title ){
      this.__componentTitle__ = title;       
}

Component.prototype.getIsActive = function(){
       return this.__isActive__;
}

Component.prototype.setIsActive = function( active ){
        this.__isActive__ = active;        
        
}

Component.prototype.getRefreshRate = function(){
     return this.__refreshRate__;       
}

Component.prototype.setRefreshRate = function( rate ){
         this.__refreshRate__ = rate;        
        
}

Component.prototype.getInput = function(){
     return this.__input__;       
        
}
       
Component.prototype.setInput = function( pin ){
     this.__input__ = pin;        
        
}

Component.prototype.getType = function(){
    return this.__type__;       
        
}


Component.prototype.offsetLeft = function(){
    
    var offset = $('#componentContainer'+this.__componentId__).offset();    
    return offset.left;       
        
}

Component.prototype.offsetTop = function(){
    
    var offset = $('#componentContainer'+this.__componentId__).offset();    
  
    return offset.top;       
}

Component.prototype.getZIndex = function(){
        
        
    return $('#componentContainer'+this.__componentId__).css("z-index");        
}

Component.prototype.message = function( msg){
        
    $.gritter.add({
                    title: "ID: " + this.__componentId__,
                  text: msg,
                  });          
        
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
         
    
     
     //components[ _self.__componentId__] = null;
     
     //$('#componentContainer'+_self.__componentId__).remove();  
     $.post( "remove.php",{componentId:_self.__componentId__}, function( data ){
                  data = $.trim( data );
                  
                  _self.message("deleting component");
                  
                  if( data == "ok")
                  {
                     if( _self.__isActive__)
                     {
                       _self.stopScheduler();
                     }
                    
                    $('#componentContainer'+_self.__componentId__).remove(); 
                    components[ _self.__componentId__] = null;
                  }
                  else
                  {
                    _self.message(
                            "currently unable to delete component" + data);
                    
                          
                  }
     })
     .error( function(){
          _self.message(
                            "currently unable to delete component" );
                               
                     
                     
     });
         
   });

  
  //Create the slider for adjusting refresh rate
  $('#refreshSlider'+_self.__componentId__).slider({
         orientation: 'vertical',
         min: 1,
         max: 60,
         slide: function( event, ui ){
               $('#value'+_self.__componentId__).html( ui.value +"s" );
                               
                },
                stop: function( event, ui ){
                    var tmp = ui.value * 1000;
                    if( tmp != _self.__refreshRate__ )
                    {
                         //_self.__refreshRate__ = tmp;
                         _self.message( "updating refresh rate");
                         $.post( "update.php",
                                 {componentId : _self.__componentId__, refreshRate: tmp},
                                 function( data ){
                                     
                                     data = $.trim( data );
                                     
                                     if( data == "ok")
                                     {
                                       _self.__refreshRate__ = tmp;       
                                             
                                             
                                     }
                                     else
                                     {
                                         
                                        $('#value'+_self.__componentId__).html( 
                                               (_self.__refreshRate__/1000) +"s" );
                                        
                                        $('#refreshSlider'+_self.__componentId__).slider(
                                                "option","value", (_self.__refreshRate__/1000));      
                                        _self.message( 
                                          "an error occurred updating the database");
                                                                                     
                                     }
                                   
                                         
                                 })
                         .error( function(){
                                    
                                 $('#value'+_self.__componentId__).html( 
                                               (_self.__refreshRate__/1000) +"s" );
                                        
                                 $('#refreshSlider'+_self.__componentId__).slider(
                                                "option","value", (_self.__refreshRate__/1000));       
                                         
                                 _self.message( 
                                          "an error occurred accessing the web servers");          
                                         
                         });
                            
                    }
                        
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
              var tmp = ui.value;
              
              if( tmp != _self.__input__ )
              {
                      _self.message("updating pin value");
                      $.post( "update.php",{ componentId: _self.__componentId__, input: tmp },
                              function( data ){
                                      
                       
                              data = $.trim( data );
                              
                              if( data == "ok" )
                              {
                                 _self.__input__ = tmp;       
                                      
                              }
                              else
                              {
                                $('#pinSlider'+_self.__componentId__).slider(
                                                "option","value", _self.__input__);
                                $('#pinValue'+_self.__componentId__).html("Pin " + _self.__input__);
                                
                               _self.message( 
                                          "an error occurred updating the database");          
                             
                              }
                                      
                       })
                      .error( function(){
                             $('#pinSlider'+_self.__componentId__).slider(
                                                "option","value", _self.__input__);         
                             $('#pinValue'+_self.__componentId__).html("Pin " + _self.__input__);

                             _self.message( 
                                          "an error occurred accessing the webserver");  
                             
                                      
                      });
                      
                      
                      
                      
              }
            }
  });
  
 /*$('#inputArea'+_self.__componentId__).html(
                '<img id="inputIcon'+_self.__componentId__+
                '" src="img/plug.png" style="margin-right:15px" />'+
                _self.__arduinoName__);*/
                            
            $('#inputIcon'+_self.__componentId__).click( function(){
                  $('#pinPanel'+_self.__componentId__).toggle();           
                                 
                  if( $('#refreshRate'+_self.__componentId__).css('display') 
                            != 'none' &&
                      $('#pinPanel'+_self.__componentId__).css('display')
                            != 'none')
                        $('#refreshRate'+_self.__componentId__).toggle();
                                       
              });
 //Make the component droppable to allow dragging of arduinos on to it
 $('#componentContainer'+_self.__componentId__).droppable({
     accept: '.live',
     drop: function(event, ui){
            var tmp = $(ui.draggable).attr('id');
           
          if( tmp != _self.__arduinoName__ )
          {
                  _self.message("associating with " + tmp );
            $.post("update.php",{componentId:_self.__componentId__,
            arduino : tmp}, function( data ){
                    
               data = $.trim( data );
               
            
           if( data == "ok" ) 
           {     
           //_self.stopScheduler();
           _self.__arduinoName__ = tmp;    
           $('#arduinoName'+_self.__componentId__).html( _self.__arduinoName__);
          /*  $('#inputArea'+_self.__componentId__).html(
                '<img id="inputIcon'+_self.__componentId__+
                '" src="img/plug.png" style="margin-right:15px" />'+
                _self.__arduinoName__);*/
                            
           /* $('#inputIcon'+_self.__componentId__).click( function(){
                  $('#pinPanel'+_self.__componentId__).toggle();           
                                 
                  if( $('#refreshRate'+_self.__componentId__).css('display') 
                            != 'none' &&
                      $('#pinPanel'+_self.__componentId__).css('display')
                            != 'none')
                        $('#refreshRate'+_self.__componentId__).toggle();
                                       
              });*/
           }
           else
           {
                  _self.message( 
                       "an error occurred updating the database");   
                   
                   
           }
            })
            .error( function(){
               _self.message( 
                       "an error contacting the web server");             
                            
            });
          }
          }
      });
 
 
 //Handle turning on and off the component
 $('#statusLight'+_self.__componentId__).click( function(){
                      
          var status = $(this).attr('src');
      
          
          
          _self.hideSliders();
          
          if( _self.__arduinoName__ != "none" )
                  
          {
              var msg = ( _self.__isActive__  ? "stopping" : "starting" );
                  
              _self.message( msg );
                  
              $.post("update.php",
                      { componentId: _self.__componentId__,isActive: ( !_self.__isActive__ )}, function( data ){
                              
                              
                         data = $.trim( data );
                    
                        
                         if( data == "ok" )
                         {
                           _self.__isActive__ = ! _self.__isActive__;
                           
                           
                            if( _self.__isActive__ )
                            {
                               _self.startScheduler();  
                                
                             }
                             else
                             {
                                
                             _self.stopScheduler();
                             }
                         }
                         else
                         {
                              _self.message(
                                      "unable to access database");
                                 
                         }
                              
                       
                      })
                       .error( function(){
                             _self.message(
                                   "unable to contact web server");
                              
                       });
                  
                  
          }
          
                      
      });
 
 _self.draw();
   
}



//Stops the scheduling of data retrieval from the web server
Component.prototype.stopScheduler = function(){
//if( this.__isActive__ == true)
//{
  $('#statusLight'+this.__componentId__).attr('src','img/redlight.png');
  window.clearTimeout( this.__scheduler__ );
  this.__scheduler__ = null;  
  this.__isActive__ = false;
//}
}
     

//Starts the scheduling of data retrieval from the web server
Component.prototype.startScheduler = function(){
   var _self = this;
   
 // if( this.__isActive__ ==false)
 //  {
      $('#statusLight'+this.__componentId__).attr('src','img/greenlight.png');
      this.__isActive__ = true; 
      this.__scheduler__ = window.setTimeout(function(){_self.getData();}, 
                                              0);
 //  }        
           
         
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
         _self.message(  "An error ocurred retreiving data. Retrying..."); 
              
             
     }
     else
     {
       _self.update( tmpValue );             
     }
  })
  .error( function() {
                  
       _self.message("Unable to access webserver");           
      
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

