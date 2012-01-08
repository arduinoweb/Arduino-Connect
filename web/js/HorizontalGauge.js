function HorizontalGauge( id ){
        
   Component.call( this, id );       
   this.__type__="horizontalgauge";
   this.__minValue__=0;
   this.__maxValue__=255;
   this.__threshold__=this.__maxValue__;
   this.__units__="Unit";
}

HorizontalGauge.prototype = new Component();

HorizontalGauge.prototype.createContainer = function() {
        
   $('#rightcolumn').append(
             '<div id="componentContainer'+this.__componentId__+'" class="horizontalGaugeContainer shadowed"><img id="statusLight'+this.__componentId__+'" src="img/redlight.png" style="width: 20px; height:20px; float:left" /><span id="close'+this.__componentId__+'" class="close"></span><div><canvas id="component'+this.__componentId__+'">'+
             
             '</canvas></div><div  style="margin-top: 20px"><img id="refreshIcon'+this.__componentId__+'" alt="refresh rate icon" title="click to edit refresh rate" src="img/clock.png" />'+
             
             '<div id="refreshRate'+this.__componentId__+'" class="refreshpanel" ><label id="value'+this.__componentId__+'">1s</label><div id="refreshSlider'+this.__componentId__+'"></div></div>'
             +
             
             '<div class="inputs" ><span id="inputArea'+this.__componentId__+'"></span><div id="pinPanel'+this.__componentId__+'" class="pinPanel"><label id="pinValue'+this.__componentId__+'">Pin 0</label><div id="pinSlider'+this.__componentId__+'"></div></div></div>'
             
             +'</div>'
      
    );
        
        
        
}

HorizontalGauge.prototype.draw = function(){

 this.__component__ = new steelseries.Linear('component'+this.__componentId__, {
                        width: 320,
                        height: 140,
                        lcdVisible: true,
                        minValue: this.__minValue__,
                        maxValue: this.__maxValue__,
                        threshold: this.__threshold__,
                        titleString: "id: " + this.__componentTitle__,
                        unitString: this.__units__
                        
                            });
        
        this.__component__.setFrameDesign(steelseries.FrameDesign.METAL);
        this.__component__.setBackgroundColor(steelseries.BackgroundColor.WHITE);


}


HorizontalGauge.prototype.update = function( value ) {

 this.__component__.setValueAnimated( value );


}

HorizontalGauge.prototype.setTitle = function( ){
        $('#footer').append("well");
  this.__component__.setTitleString( this.__componentTitle__); 
  this.setMinValue();
        
        
}

HorizontalGauge.prototype.setMinValue = function(){
        
  //  this.__component__ = null;
    this.__minValue__ = 100;
    this.__component__.setMinValue( 50 );
    
    //$('#component'+this.__componentId__).remove();
    //this.draw();
}
