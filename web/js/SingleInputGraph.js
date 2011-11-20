function SingleInputGraph( graph, url,port, pinNum,
                           yMin, yMax, showLines, showPoints,
			   showBars,refreshRate,legendText
)
{
   this.graph = graph;
   this.url = url;
   this.port = port;
   this.yMin = yMin;
   this.yMax = yMax;
   this.showLines = showLines;
   this.showPoints = showPoints;
   this.showBars = showBars;
   this.legendText = legendText;
   this.refreshRate = refreshRate;
   this.pinNum = pinNum;
}
    
  
SingleInputGraph.prototype.start = function(){
 
  var tmpGraph = this.graph;
  var tmpUrl = this.url;
  var tmpRefreshRate = this.refreshRate;
  var query = "io=R&value="+this.pinNum;
 
  // we use an inline data source in the example, usually data would
    // be fetched from a server
    var data = [], totalPoints = 50;
    for( var i = 0; i < totalPoints; i++)
    {
            data.push( 0);       
    }
    function updateData(updatedValue) {
       
            data = data.slice(1);

      
        data.push( updatedValue );
        // zip the generated y values with the x values
       
        var res = [];
        for (var i = 0; i < data.length; ++i)
            res.push([i, data[i]])
            
           
        return res;
       
    }

    function update(){
      var updatedValue = 0;
      
      $.ajax({
	//url: 'http://localhost/~gary/graphtest.php',
	url: tmpUrl,
	method: 'GET',
	data: query,
	dataType: 'json',
	success: function( d ){
	  $('#test').html( d[0]  );
	  updatedValue = d[0];
	},
	complete: function( d ){
        tmpGraph.setData( [ updateData( updatedValue) ] );
	tmpGraph.setupGrid();
	tmpGraph.draw();
        setTimeout( update, tmpRefreshRate);
	}
      });
    }
    
    update();
    
}  

