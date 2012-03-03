
                
                
function Monitor( name, ipaddress, port )
{
     this.name = name;
     this.ipaddress = ipaddress;
     this.port = port;
     
     this.monitoredPins = new Array(); 
        

     

}

Monitor.prototype.addRow = function( tableentry)
{
   this.monitoredPins[ tableentry.getId()] = tableentry;
}
     
Monitor.prototype.getName = function()
{
  return this.name;       
        
}
 
Monitor.prototype.getRow = function( index )
{
   return this.monitoredPins[index];       
        
}

Monitor.prototype.getNumPins = function()
{
     var count=0;
     
     for( pin in this.monitoredPins )
     {
         count++;
     }
             
     return count;     
        
}

Monitor.prototype.isPinMonitored = function( pin )
{
        
    var isMonitored = false;
    var tmp = null;
    
    for( i in this.monitoredPins )
    {
            tmp = this.monitoredPins[i];
            
            if( pin == tmp.getPin() )
            {
               isMonitored = true;
               break;
            }
    }
        
     return isMonitored;   
}

            
                
                

