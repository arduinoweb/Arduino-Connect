$(document).ready( function(){
  
  //Define the behaviour for the "padlock" button
  $('#lockButton').click( function()
  {
            
    if( $('#lockButton').attr('title') == 'locked')
    {
      $('#lockButton').attr( 'title','unlocked');
      $('#lockButton img').attr('src','img/padlock-open.png');
      $('#addButton').button( "option", "disabled", false);
    }
    else
    {
      $('#lockButton').attr( 'title','locked');
      $('#lockButton img').attr('src','img/padlock-close.png');
      $('#addButton').button( "option","disabled", true );

      if( $('#componentsDialog').dialog( 'isOpen') )
      {
       $('#componentsDialog').dialog('close'); 
      }
    }

  });
  //End of behaviour for "padlock" button
  
 //Button that brings up the components dialog
 $('#addButton').button( {disabled : true});
 
 $('#addButton').click( function(){
        if( $('#componentsDialog').dialog( 'isClosed') )
        {
          $('#componentsDialog').dialog('open'); 
               $(this).button( "option", "disabled", true);
        }
  });
 //End of addButton
 
 //The example components
 var chartExampleOptions = {
                            series: {
                               lines: {show: true},
                              points: {show: true},
                              },
                             xaxis: { show: false},
                             yaxis: { show: false}
                            };
  
  $.plot($("#exampleChart1"), [[[1, 2],[4,45.12],[5,13.1],[7,33.6],[9,85.9],[11,219.9]]],
	               chartExampleOptions);
  
  
   $('#exampleChart1').click( function(){
                 appendSingleInputGraphFields();
                 $('#configureComponent').dialog("open");  
  });
 
   
 $('#verticalSlider').slider({
          value: 10,
    orientation: "vertical",
            min: 0,
            max: 255,
       disabled: true,
          value: 127
   });

 $('#verticalSlider').click( function(){
        $('#configureComponent').dialog("open");               
   });
   
 $('#horizontalSlider').slider({
          value: 10,
    orientation: "horizontal",
            min: 0,
            max: 255,
          value: 127,
       disabled: true,
  });

   
  $('#horizontalSlider').click( function(){
          $('#configureComponent').dialog("open");  
  });
  
  $('#iButton').button();
  
  $('#rssFeed').mousedown( function(){
     $(this).attr( "src","img/rss-feed-click.png");
   });
  
  $('#rssFeed').mouseup( function(){
     $(this).attr( "src", "img/rss-feed.png");
   });
 
  $('#rssFeed').click( function(){
      $('#configureComponent').dialog("open");
   });
  
   $('#rssFeed').mouseenter( function(){
      $(this).attr("src","img/rss-feed-hover.png");
   });

   $('#rssFeed').mouseleave( function(){
       $(this).attr("src","img/rss-feed.png");
  });
  //End of example components 
  
  //The dialog that contains examples of components that 
  //can be added
  $('#components').accordion({
             fillSpace: true,
             collapsible: true,
             active: false,
             animated: 'bounceslide',
  });
   
  $('#componentsDialog').dialog({
  autoOpen: false,
    height: 600,
     width: 450,
   buttons: {
             close : function(){
                 $(this).dialog('close');
                 $('#addButton').button( "option", "disabled", false);
             },

            },
             open: function( event, ui ){
	         // Get rid of the "x" close symbol
                 $(' a.ui-dialog-titlebar-close').remove();
             },
   });
  //End of component selection dialog
  
  
  //Dialog that contains fields allowing you to configure
  // selected component.
  // Should wrap this up in a function call as options
  // are going to be different depending upon the component
  $('#configureComponent').dialog({
          autoOpen: false,
            height: 600,
             width: 650,
             modal: true,
           buttons: {
               "Create" :function(){
                var pinNumber = $('#pinNum1').val().trim();
                var xMin = $('#xMin').val().trim();
                var xMax = $('#xMax').val().trim();
                var yMin = $('#yMin').val().trim();
                var yMax = $('#yMax').val().trim();
                         
               
                $('#configureFields input.formField').remove();
                $('#configureFields label.formField').remove();
               
                $('#content').append('<div id="chart2Container" class="componentContainer"><div id="chart2" class="graph" ></div></div>');
               var o = {
		  series: { shadowSize: 0 }, // drawing is faster without shadows
        yaxis: { min: 0, max: 100 },
        xaxis: { show: false }
		 
	       };
               $(this).dialog("close");
		var tmpChart = $.plot($("#chart2"), [ [] ],o);
		var tmp = new SingleInputGraph( tmpChart );
		tmp.start();
		//schedule( tmp, 5000);
		 
		userComponents.push( tmp );
		
               },
               Cancel: clearForm
             },  //end of buttons
  });
  // End of component configuration dialog
  
  
});

 function schedule( a, b )
 {
     setInterval( a.start, 1000); 
   
 }
 
 function drawGraph( d, g )
 {
   g.setData( [ g ] );
   g.setupGrid();
   g.draw();
   
   
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
         '<label for="xMin" class="formField">X Min</label>' +
         '<input type="text" id="xMin" name="xMin" class="formField"/>'+
         '<div>'

   );

  $('#configureFields').append(
        '<div class="formField" >'+
        '<label for = "xMax" class="formField">X Max</label>'+
        '<input type="text" id="xMax" name="xMax"class="formField"/></div>'
        
   );

    $('#configureFields').append(
        '<div class="formField">'+
        '<label for = "yMin" class="formField">Y Min</label>'+
        '<input type="text" id="yMin" name="yMin" class="formField"/></div>'
        
   );

   $('#configureFields').append(
        '<div class="formField">'+
        '<label for = "yMax" class="formField">Y Max</label>'+
        '<input type="text" id="yMax" name = "yMax" class="formField"/></div>'
        
   );
   
 }     
 
 function clearForm()
 {
    $('#configureFields input.formField').remove();
    $('#configureFields label.formField').remove();
    $(this).dialog("close");
 }
 
 var userComponents = [];
 