function Sender( targetUrl, controlId, outputId){
   
        this.error = false;
		this.targetUrl = targetUrl;
		this.controlId = controlId;
		
        this.outputId = outputId;
		this.sendMsg = "io=w";

        $(this.controlId).bind( "slidechange", function( event, ui ){
             var v = $(controlId).slider( "option","value");
			 var msg = "io=W A 9&value="+ v;
			 var tmpUrl = this.targetUrl;
			  $.ajax({
                 type: "GET",
				 cache: false,
				 url: "test.php",
				 data: msg,
				  error: function(jqXHR, textStatus, errorThrown){
                        
                        },
                        success: function( data, textStatus, jqXHR){
                       
					     },
                        
						complete: function( jqXHR, textStatus){
                        
				            
						
						     
						}
                        
        });

        });		
}




