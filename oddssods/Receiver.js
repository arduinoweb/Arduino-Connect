function Receiver( targetUrl, controlId, outputId, refreshRate){
   
        this.error = false;
		this.targetUrl = targetUrl;
		this.controlId = controlId;
		this.refreshRate = refreshRate;	
        this.outputId = outputId;
		this.sendMsg = "io=R&value=9";
	
}

Receiver.prototype.connect= function(){
      
	    var tmpControlId = this.controlId;
        var tmpOutputId = this.outputId;
	  
		
		
		
        $.ajax({
                  type: "GET",
                  dataType: 'json',
                  cache: false,
                  url: this.targetUrl,
				  data: this.sendMsg,
                  error: function(jqXHR, textStatus, errorThrown){
                         this.error = true;
                          $(tmpOutputId).append(
                            '<div>'+textStatus+ '  ' + errorThrown +'</div>');
                        },
                        success: function( data, textStatus, jqXHR){
                        
                        
						if( data.msg < 256 )
						{
						 var op = data.msg / 255;
						 
						//$(tmpControlId).slider("option", "value", data.msg);
						//		$( tmpOutputId ).val( $( tmpControlId ).slider( "value" ) );
                         tmpOutputId.setValue( data.msg );						 
						
                        }
						else{
						
						   $('#errors').append("<p>"+data.msg+"</p>");
						
						}},
                        
						complete: function( jqXHR, textStatus){
                        
				            
							 	$(tmpControlId).trigger("myCustomEvent");
						     
						}
                        
        });
};

