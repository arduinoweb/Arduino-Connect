
                
                
function Monitor( name, ipaddress, port )
{
     this.name = name;
     this.ipaddress = ipaddress;
     this.port = port;
     
     this.isRunning = false;
     this.readRequest = null;
     this.refreshPeriod = 1000;
     this.pins = new Array();
     this.numOfErrors = 0;
     
     this.isColoredGreen = false;
     this.isColoredOrange = false;
     
     this.changeColor = function( color ){
             var tmpName = this.name;
             var tmpColor = color;
             
             $("tr").each( function(){
                             
                var tmp = $(this).attr('class');
                
                if( tmp != null && tmp.substring(1) == tmpName )
                {
                   $(this).css("color",color);        
                }
                             
             });
             
     }
     
     this.read = function(){
             var self = this;
          
             $.post("arduinowrite.php", {ipaddress:this.ipaddress, port:this.port,command:this.readRequest},
                      function( data )
                      {
                        data = $.trim( data );
                        
                        var response = "";
                        var error = true;

                        try{
                              response = JSON.parse( data );  
                        }catch( err ){
                                console.log( err );
                        }
                        
                     if( response.reply == "error" )
                       {  

                       
                       self.refreshPeriod *= 2;
                       self.numOfErrors++;
                      
                     
                       if( self.numOfErrors > 3 )
                       {
                         self.changeColor( "red");
                         $.gritter.add({
	                  title: "Send Command",
                          text: "Unable to contact arduino at the moment"
                         });
                         self.isRunning = false;
                      //   $('#startButton').button('option','disabled',false);
                       }
                       else if( ! self.isColoredOrange )
                       {
                        self.changeColor("orange");
                        self.isColoredOrange = true;
                        self.isColoredGreen = false;
                       }
                      
                      
                      }
                   
                     else if( response.reply == "invalid message format" )
                     {
                       self.isRunning = false;
                      $.gritter.add({
	                  title: "Send Command",
                          text: "invalid message format"
                       });  
                      
                       self.numOfErrors = 4;
                      
                       $('#startButton').button('option','disabled',false);
                      }
                   
                    else if( response.reply == "OK" )
                    {
                      $.gritter.add({
	                  title: "Send Command",
                          text: "Message Written to Arduino"
                          
                       });     
                   }   
                   
                  else if( response.pins != undefined )
                   {
                         var result = "";
                         var pinNumber = "";
                         var pinValue = "";
                         
                         for( i in response.pins )
                         {
                             pinNumber = response.pins[i].pin;
                             pinValue = response.pins[i].value;
                             
                             $('tr').each( function(){
                                             
                                var tmp = $(this).attr('class');
                                var id = $(this).attr('id');
                                var tablePin = $('#pin'+id).html();
                                
                                if( tablePin == pinNumber && tmp.substring(1) == self.name )
                                {
                                   $('#value'+id).html( pinValue );
                                }
                                        
                                
                                             
                             });
                                             
                                             
                                 
                                 
                         }
                   --(self.numOfErrors);
                  
                           
                  
                   
                    
                    if(  (self.refreshPeriod /=2) < 1000 )
                    {
                        self.refreshPeriod == 1000;
                        
                   }
                   
                   if( ! self.isColoredGreen )
                   {
                     self.changeColor( "green");
                     self.isColoredGreen = true;
                     self.isColoredOrange = false;
                   }
                   
                 }
                      }).complete( function(){
              
                    if( self.isRunning && self.numOfErrors <=3){
                        window.setTimeout( function(){self.read();}, self.refreshPeriod);  
                    }      
                 
                         
                 }).error( function(){
                      self.numOfErrors++;
                      self.refreshPeriod *= 2;
                      
                     if( self.numOfErrors > 3 )
                      {
                        $.gritter.add({
	                  title: "Network Error",
                          text: "Unable to contact the web server"
                          
                       });
                        self.isRunning=false;
                        self.changeColor( "red");
                        $('#startButton').button('option','disabled',false);
                      }
                      else if( self.isRunning )
                      {
                          if( ! self.isColoredOrange )
                          {
                             self.changeColor( "orange");
                             self.isColoredGreen = false;
                          }
                          window.setTimeout( function(){self.read();}, self.refreshPeriod);     
                      }
                         
                 });
             
     };
     
}

Monitor.prototype.getName = function()
{
        
   return this.name;       
        
}

Monitor.prototype.start = function()
{
     if( ! this.isRunning )
    {   
      var count = 0;
      for( i in this.pins )
      {
              count++;
              
      }
      this.readRequest = "";
      if( count > 0 )
      {
        this.readRequest = "R";
        
        for( i in this.pins )
        {
                this.readRequest += " " + this.pins[i];       
                
        }
        this.readRequest += " E";
        this.numOfErrors = 0;
        this.refreshPeriod = 1000;
        this.isColoredOrange = false;
        this.changeColor( "green");
        this.isColoredGreen = true;
     //   $('#startButton').button('option','disabled', true );
        
        
         this.read();
         this.isRunning = true;
        }
       
              
      }
        
}

Monitor.prototype.stop = function()
{
    this.isRunning = false;       
        
}

Monitor.prototype.isRunning = function()
{
  return this.isRunning;       
}
Monitor.prototype.addPin = function( pin )
{
        var pin = $.trim( pin );
        this.pins.push( pin );
        
        
}

Monitor.prototype.clear = function()
{
    this.pins = new Array();
}
