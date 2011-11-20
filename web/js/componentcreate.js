


  
function clearForm()
 {
    $('#configureFields input.formField').remove();
    $('#configureFields label.formField').remove();
   
 }

 function createSingleInputGraph()
 {
   numOfComponents++;
   var url = $('#url').val().trim();
   var port = $('#port').val().trim();
   var yMin = $('#yMin').val().trim();
   var yMax = $('#yMax').val().trim();
   var showLines =   $('#lines').attr('checked') == 'checked' ? 'true' : 'false';
   var showPoints =  $('#points').attr('checked') == 'checked' ? 'true' : 'false';
   var showBars =   $('#bars').attr('checked') == 'checked' ? 'true' : 'false';
   var refreshRate = $('#refreshRate').val().trim();
   var legendText = $('#legend').val().trim();
   var pinNum = $('#pinNum').val().trim();
   

   $('#content').append(
      '<div class="graphContainer">'+
      '<img src="img/pointing-up.png" id="sc'+numOfComponents+'" alt="Hide Graph" class="showControl" />'+
      '<label class="graphLabel shadowed" for="'+numOfComponents+'">'+legendText+'</label>'+
      
      '<div id="'+numOfComponents+'" class="graph"></div></div>');
   
   
 
 /* var d1 = [];
    for (var i = 0; i < 14; i += 0.5)
        d1.push([i, Math.sin(i)]);

    var d2 = [[0, 3], [4, 8], [8, 5], [9, 13]];
*/
    // a null signifies separate line segments
    var d3 = [[0, 12], [7, 12], null, [7, 2.5], [12, 2.5]];
    
    /*$.plot($("#1"), [ d1, d2, d3 ]);*/
    
    var tmpGraph = $.plot( $("#"+numOfComponents), [d3],{
                bars: { show: eval(showBars)},
	        lines:{ show: eval(showLines)},
	        points:{ show: eval(showPoints)},
	        xaxis:{  show: false },
	        yaxis:{ min: eval( yMin), max:eval(yMax)}
		
    
    });
 
    var tmp = new SingleInputGraph( tmpGraph, url, port, pinNum, yMin,
                                    yMax, showLines, showPoints,
				    showBars,refreshRate,legendText
    );
    $('#'+numOfComponents).resize(function(){});
    userComponents.push( tmp );
    
    $('#'+numOfComponents).parent().draggable();
    $('#sc'+numOfComponents).click( function(){
       //alert("hello");
       $('#'+numOfComponents).toggle('blind',[], 100);
       
       if( $(this).attr( 'src' ) == "img/pointing-up.png" )
       {
            $(this).attr( 'src', 'img/pointing-down.png');
       }
       else
       {
            $(this).attr( 'src', 'img/pointing-up.png');
       }
      
       return false;
    });
  
    tmp.start();
 }
 
 
   
 //Dialog that contains fields allowing you to configure
  // selected component.
  // Should wrap this up in a function call as options
  // are going to be different depending upon the component
function configureComponent( createFunction )
{
  $('#configureComponent').dialog({
          autoOpen: true,
            height: 600,
             width: 650,
             modal: true,
           buttons: {
               "Create" :function(){

		createFunction();
		clearForm();
		$(this).dialog("close");
		
               },
               Cancel: function(){
		 clearForm();
		$(this).dialog("close");
		
	       }
             },  //end of buttons
  });
} 
 
function appendSingleInputGraphFields()
 {
  clearForm();
  
   $('#configureFields').append(
        '<div class="formField">'+
        '<label for="pinNum" class="formField">Pin Number</label>'+
        '<input type="text" id="pinNum" width="4" name="pinNum" class="formField"/></div>'

   );
  

    $('#configureFields').append(
        '<div class="formField">'+
        '<label for = "yMin" class="formField">Minimum Value</label>'+
        '<input type="text" id="yMin" name="yMin" class="formField" value="0"/></div>'
        
   );

   $('#configureFields').append(
        '<div class="formField">'+
        '<label for = "yMax" class="formField">Maximum Value</label>'+
        '<input type="text" id="yMax" name = "yMax" class="formField" value="255"/></div>'
        
   );
 
   
   $('#configureFields').append(
        '<div class="formField">'+
        '<label for = "lines" class="formField">Show Lines</label>'+
        '<input type="checkbox" id="lines" name = "lines" class="formField" checked="checked"/></div>'
        
   );
 
   $('#configureFields').append(
        '<div class="formField">'+
        '<label for = "points" class="formField">Show Points</label>'+
        '<input type="checkbox"  id="points" name = "points" class="formField"/></div>'
        
   );
   
    $('#configureFields').append(
        '<div class="formField">'+
        '<label for = "bars" class="formField">Show Bars</label>'+
        '<input type="checkbox"  id="bars" name = "bars" class="formField"/></div>'
        
   );
     $('#configureFields').append(
        '<div class="formField">'+
        '<label for = "refreshRate" class="formField">Refresh Rate(ms)</label>'+
        '<input type="text"  id="refreshRate" name = "refreshRate" class="formField" value="60000"/></div>'
        
   );
    
   $('#configureFields').append(
        '<div class="formField">'+
        '<label for = "legend" class="formField">Legend</label>'+
        '<input type="text"  id="legend" name = "legend" class="formField"/></div>'
        
   );
 }     
 
 
 
