function Lcd( id ){
        
   Component.call( this, id );       
   this.__type__="lcd";
   
}

Lcd.prototype = new Component();

Lcd.prototype.createContainer = function() {
        
   $('#rightcolumn').append(
             '<div id="componentContainer'+this.__componentId__+'" class="lcdGaugeContainer shadowed" ><span class="edit title" id="componentTitle'+this.__componentId__+'">'
             + this.__componentTitle__ +'</span><span id="close'+this.__componentId__+'" class="close"></span><div><canvas id="component'+this.__componentId__+'">'+
             
             '</canvas></div><div  style="margin-top: 20px"><img id="refreshIcon'+this.__componentId__+'" alt="refresh rate icon" title="click to edit refresh rate" src="img/clock.png" />'+
             
             '<div id="refreshRate'+this.__componentId__+'" class="refreshpanel" ><label id="value'+this.__componentId__+'">1s</label><div id="refreshSlider'+this.__componentId__+'"></div></div>'
             +
             
             '<div class="inputs" ><span id="inputArea'+this.__componentId__+'"></span><div id="pinPanel'+this.__componentId__+'" class="pinPanel"><label id="pinValue'+this.__componentId__+'">Pin 0</label><div id="pinSlider'+this.__componentId__+'"></div></div></div>'
             
             +'<img id="statusLight'+this.__componentId__+'" src="img/redlight.png" style="width: 20px; height:20px; float:right" /></div>'
      
    );
        
        
        
}

Lcd.prototype.draw = function(){
		var sections = [steelseries.Section(0, 70, 'rgba(255, 0, 0, 1.0)'),
                        steelseries.Section(70, 95, 'rgba(255, 255, 0, 1.0)'),
                        steelseries.Section(95, 100, 'rgba(0, 255, 0, 1.0)') ];
 this.__component__ = new steelseries.DisplaySingle('component'+this.__componentId__, {
                      
                 width: 120,
                 height: 50,
                 titleString: "hello",
                 unitString: "gg"
                            });
 
        this.__component__.setLcdColor( steelseries.LcdColor.BEIGE);
        
       // this.__component__.setFrameDesign(steelseries.FrameDesign.METAL);
        //this.__component__.setBackgroundColor(steelseries.BackgroundColor.WHITE);


}


Lcd.prototype.update = function( value ) {

 this.__component__.setValue( value );

}
