


  
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
   var legendText = $('#legend').val().trim();
  

   $('#content').append(
      '<div class="graphContainer"><label class="graphLabel shadowed" for="'+numOfComponents+'">'+legendText+'</label>'+
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
		
    
    });
      
    var tmp = new SingleInputGraph( tmpGraph, url, port, yMin,
                                    yMax, showLines, showPoints,
				    showBars,legendText
    );
    tmp.start();
    userComponents.push( tmp );
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
        '<label for="pinNum1" class="formField">Pin Number</label>'+
        '<input type="text" id="pinNum1" width="4" name="pinNum1" class="formField"/></div>'

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
        '<label for = "legend" class="formField">Legend</label>'+
        '<input type="text"  id="legend" name = "legend" class="formField"/></div>'
        
   );
 }     
 
 
 
