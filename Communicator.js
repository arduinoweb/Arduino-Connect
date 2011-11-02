function Communicator( targetUrl, controlId, outputId, refreshRate){
   
        this.error = false;
		this.targetUrl = targetUrl;
		this.controlId = controlId;
		this.refreshRate = refreshRate;	
        this.outputId = outputId;
		this.sendMsg = "io=r&value=0";
	
}

Communicator.prototype.connect= function(){
      
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
						$(tmpControlId).slider("option", "value", data.msg);
								$( tmpOutputId ).val( $( tmpControlId ).slider( "value" ) );
						  
                        }
						else{
						
						   $('#errors').append("<p>"+data.msg+"</p>");
						
						}},
                        
						complete: function( jqXHR, textStatus){
                        
				            
							 	$(tmpControlId).trigger("myCustomEvent");
						     
						}
                        
        });
};

