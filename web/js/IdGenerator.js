function IdGenerator( ){
        
      this.currentId = -1;
      this.assignedId = [];

      
}

IdGenerator.prototype.getNextId = function() { 

        var newId = this.currentId;
        
        if( this.currentId == -1 )
        {
           newId = 0;
           this.currentId = 0;
           
        }
        else
        {

          var i = 0;
          newId = -1;
          
          while( newId == -1 && i < this.assignedId.length )
          {
              if( this.assignedId[i] == null )
              {
                newId = i;
              }
                  i++;
          }
          
          if( newId == -1 )
          {              
              ++this.currentId;
              newId = this.currentId;
          }
        }
        
        this.assignedId[newId] ="assigned";
        
        return newId;     
        
}


IdGenerator.prototype.removeId = function( id ){ 

this.assignedId[id] = null;

} 
        
        



