function TableEntry( id, pin, enabled )
{
   this.id =id;
   this.pin=pin;
   this.enabled=false;
        
        
        
        
}
        

TableEntry.prototype.getId = function() { return this.id;}
TableEntry.prototype.getPin= function() { return this.pin;}


//TableEntry.prototype.isEnabled = function() { return this.enabled;}



//TableEntry.prototype.setEnabled = function( enabled ){ this.enabled=enabled;}



TableEntry.prototype.setPin = function( pin ) { this.pin = pin;}




