var userComponents = [];
var numOfComponents = 0;

$(document).ready( function(){
  $('#configureComponent').css('display','none');
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
		 configureComponent(createSingleInputGraph);

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
       // $('#configureComponent').dialog("open");               
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
       //   $('#configureComponent').dialog("open");  
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
  
 
 
  // End of component configuration dialog


  
});

 