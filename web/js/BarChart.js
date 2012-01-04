function BarChart( id ){
        
       
      Component.call( this, id );
        
      this.__type__ = "barchart";
      this.__data__ = [];
      
      this.__totalPoints__ = 20;
      
      for( var i = 0; i < this.__totalPoints__; i++)
      {
              this.__data__.push(0);
      }
        
}

BarChart.prototype = new Component();

BarChart.prototype.createContainer = function(){
          
    $('#rightcolumn').append(
             '<div id="componentContainer'+this.__componentId__+'" class="componentContainer shadowed"><span class="edit title" id="componentTitle'+this.__componentId__+'">'
             + this.__componentTitle__ +'</span><span id="close'+this.__componentId__+'" class="close"></span><div id="component'+this.__componentId__+'" class="graph">'+
             
             '</div><div  style="margin-top: 20px"><img id="refreshIcon'+this.__componentId__+'" alt="refresh rate icon" title="click to edit refresh rate" src="img/clock.png" />'+
             
             '<div id="refreshRate'+this.__componentId__+'" class="refreshpanel" ><label id="value'+this.__componentId__+'">1s</label><div id="refreshSlider'+this.__componentId__+'"></div></div>'
             +
             
             '<div class="inputs" ><span id="inputArea'+this.__componentId__+'"></span><div id="pinPanel'+this.__componentId__+'" class="pinPanel"><label id="pinValue'+this.__componentId__+'">Pin 0</label><div id="pinSlider'+this.__componentId__+'"></div></div></div>'
             
             +'<img id="statusLight'+this.__componentId__+'" src="img/redlight.png" style="width: 20px; height:20px; float:right" /></div>'
      
             
             
     );
}

BarChart.prototype.draw = function(){
   
   
   this.__component__ =  $.plot( $("#component"+this.__componentId__),
                                 [this.__data__],{
                                         bars: { show: true, barWidth:0.8},
	   
                                         xaxis:{show:false},
	        yaxis:{ min: 0, max:255}
   });
       
}



BarChart.prototype.update = function( updatedValue ){

        this.__data__ = this.__data__.slice( 1 );
        
        this.__data__.push( updatedValue);
        
        var res = [];
        
        for( var i = 0; i < this.__data__.length; ++i )
        {
                res.push( [i, this.__data__[i]] );       
                
        }
        
        this.__component__.setData( [res] );
        this.__component__.setupGrid();
        this.__component__.draw();
}


