function SingleInputGraph( graph )
{
   this.graph = graph;
  
}
    
  
SingleInputGraph.prototype.start = function(){
 
  var tmpGraph = this.graph;
  
  // we use an inline data source in the example, usually data would
    // be fetched from a server
    var data = [], totalPoints = 300;
    function getRandomData() {
        if (data.length > 0)
            data = data.slice(1);

        // do a random walk
        while (data.length < totalPoints) {
            var prev = data.length > 0 ? data[data.length - 1] : 50;
            var y = prev + Math.random() * 10 - 5;
            if (y < 0)
                y = 0;
            if (y > 100)
                y = 100;
            data.push(y);
        }

        // zip the generated y values with the x values
        var res = [];
        for (var i = 0; i < data.length; ++i)
            res.push([i, data[i]])
        return res;
    }

    function update(){
      $.ajax({
	url: 'http://localhost/~gary/graphtest.php',
	method: 'GET',
	dataType: 'json',
	success: function( d ){
	  $('#test').html( d[0] + " " + d[1] );
	},
	complete: function( d ){
        tmpGraph.setData( [ getRandomData() ] );
	tmpGraph.setupGrid();
	tmpGraph.draw();
        setTimeout( update, 500);
	}
      });
    }
    
    update();
    
    /*$.ajax({
      url: 'http://localhost/~gary/graphtest.php',
      method: 'GET',
      dataType: 'json',
      success: function( d ,e){
	
	 $('#test').html( d[0] + " " + d[1] );
	 update(d);
   
      }
     
      
    });*/
 
   
 
}  

