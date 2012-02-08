

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Arduino Connect</title>
<link rel="stylesheet" type="text/css" href="css/main.css" />
-<link rel="stylesheet" type="text/css" href="css/Aristo/Aristo.css" />
<link rel="stylesheet" type="text/css" href="js/gritter/css/jquery.gritter.css" />
<!--<link rel="stylesheet" type="text/css" href="js/jquery/css/flick/jquery-ui-1.8.16.custom.css" />-->



<script type="text/javascript" src="js/jquery/js/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="js/jquery/js/jquery-ui-1.8.16.custom.min.js"></script>

<script type="text/javascript" src = "js/arduinolist.js"></script>

<script type="text/javascript" src="js/flot/jquery.flot.min.js"></script>
<script type="text/javascript" src="js/flot/jquery.flot.threshold.js"></script>
<script type="text/javascript" src="js/jquery.jeditable.js"></script>
<script type="text/javascript" src="js/gritter/js/jquery.gritter.js"></script>
   <script src="js/tween-min.js"></script>
<script src="js/steelseries.min.js"></script>

<script type="text/javascript" src="js/Component.js"></script>

<script type="text/javascript" src="js/LineChart.js"></script>
<script type="text/javascript" src="js/BarChart.js"></script>

<script type="text/javascript" src="js/HorizontalGauge.js"></script>
<script type="text/javascript" src="js/VerticalGauge.js"></script>
<script type="text/javascript" src="js/RadialGauge.js"></script>
<script type="text/javascript" src="js/PointerGauge.js"></script>
<script type="text/javascript" src="js/Lcd.js"></script>



<script>
var components;

$(document).ready( function(){
                

     components = [];
     
     restore();
     
     
     $('#componentButton').click( function(){
                     $('#header').toggle();
                     
                     return false;
     });
    
    
  
  //$('.demoComponent').css('overflow','visible');
                

     
  $('.demoComponent').draggable({
                revert: true,
helper: "clone",
opacity: .8,
containment: 'window',
scroll: false,
appendTo: 'body'

             
                  
  });

  
  $('#rightcolumn').droppable({
                  
                  accept: '.demoComponent',
                  drop: function(event, ui){
                    var name = $(ui.draggable).attr('alt');
                   
                    if( name == "linechart")
                    {
                      var id = generateId();
                      var tmp= new LineChart( id );
                     
                      tmp.init();
                      components[ id ] = tmp;
                      saveComponent( id );
                      
                      // tmp.createContainer(numComponents);
                     // var tmp = new LineChart( ++numComponents );
                     // tmp.draw();
                    }
                    else if( name == "horizontalgauge" )
                    {   var id = generateId();
                            
                        var tmp = new HorizontalGauge( id);
                        tmp.init();
                        components[ id ] = tmp;
                        saveComponent( id );
                        
                    }
                    else if( name == "barchart" )
                    {
                      var id= generateId();
                    
                      var tmp = new BarChart( id );
                      tmp.init();
                     components[ id ] = tmp;
                      saveComponent( id );
                            
                    }
                    else if( name == "verticalgauge" )
                    {
                      var id= generateId();
                    
                      var tmp = new VerticalGauge( id );
                      tmp.init();
                     components[ id ] = tmp;
                      saveComponent( id );
                            
                    }
                    else if( name == "radialgauge" )
                    {
                      var id= generateId();
                    
                      var tmp = new RadialGauge( id );
                      tmp.init();
                     components[ id ] = tmp;
                      saveComponent( id );
                            
                    }
                    else if( name == "pointergauge" )
                    {
                      var id= generateId();
                    
                      var tmp = new PointerGauge( id );
                      tmp.init();
                     components[ id ] = tmp;
                      saveComponent( id );
                            
                    }
                     else if( name == "lcd" )
                    {
                      var id= generateId();
                    
                      var tmp = new Lcd( id );
                      tmp.init();
                     components[ id ] = tmp;
                      saveComponent( id );
                            
                    }
                  }
  });
   
var radial1;
 var sections = [steelseries.Section(0, 25, 'rgba(0, 0, 220, 0.3)'),
                        steelseries.Section(25, 50, 'rgba(0, 220, 0, 0.3)'),
                        steelseries.Section(50, 75, 'rgba(220, 220, 0, 0.3)') ];

        // Define one area
        var areas = [steelseries.Section(75, 100, 'rgba(220, 0, 0, 0.3)')];

        // Define value gradient for bargraph
        var valGrad = new steelseries.gradientWrapper(  0,
                                                        100,
                                                        [ 0, 0.33, 0.66, 0.85, 1],
                                                        [ new steelseries.rgbaColor(0, 0, 200, 1),
                                                          new steelseries.rgbaColor(0, 200, 0, 1),
                                                          new steelseries.rgbaColor(200, 200, 0, 1),
                                                          new steelseries.rgbaColor(200, 0, 0, 1),
                                                          new steelseries.rgbaColor(200, 0, 0, 1) ]);
   
function generateId()
{
   var tmpNum = 0;

   while( typeof components[tmpNum] !== 'undefined' ||
            components[tmpNum] != null )
       tmpNum = Math.floor( Math.random() * 10000); 
       

   return tmpNum;
        
}
	
function saveComponent( componentId )
{
    var tmpComponent = components[ componentId ];
    
    $('#footer').append( components[componentId].getId() );
    $('#footer').append( components[componentId].getArduinoName() );
    $('#footer').append( components[componentId].getComponentTitle() );    
    
    var tmp =  components[componentId].getIsActive();
    if( tmp == true )
    {
            
          $('#footer').append( "true");
    }
    else
    {
            $('#footer').append( "false");
    }
            
  /*  $('#footer').append( components[componentId].getRefreshRate() );
    $('#footer').append( components[componentId].getInput() );
    $('#footer').append( components[componentId].getType() );
    $('#footer').append( components[componentId].offsetLeft() );
    $('#footer').append( components[componentId].offsetTop() );
    $('#footer').append( components[componentId].getZIndex() );*/
      $.gritter.add({
                    title: "New Component",
                  text: "creating new component"
                  });      
    $.post( "persist.php", 
            {
             componentId: components[componentId].getId(),
             arduinoName: components[componentId].getArduinoName(),
             componentTitle: components[componentId].getComponentTitle(),
             isActive: components[componentId].getIsActive(),
             refreshRate: components[componentId].getRefreshRate(),
             input: components[componentId].getInput(),
             type: components[componentId].getType(),
             left: components[componentId].offsetLeft(),
             top:components[componentId].offsetTop(),
             zindex:components[componentId].getZIndex()
            
            
            
            },
            function( data ){
                    
               data = $.trim( data );
               
               if( data != "ok" )
               {
                    $.gritter.add({
                    title: "Component Creation Error" + data,
                  text: "unable to access database to create component"
                  });      
                       
                     $('#componentContainer'+componentId).remove();
                     components[ _self.__componentId__] = null;
                       
               }
                    
            }
    )
    .error( function(){
            $.gritter.add({
                    title: "Component Creation Error",
                  text: "unable to access web server to create component"
                  });      
                       
                     $('#componentContainer'+componentId).remove();
                     components[ _self.__componentId__] = null;          
                    
                    
    });
    
}
$('#accordion').accordion({fillSpace: true});
$('#components').dialog( {autoOpen: false, position:"left" });

 $('#componentButton').button();
 $('#componentButton').click( function(){
                 
    if( $('#components').dialog("isOpen") )
    {
       $('#components').dialog("close");
    }
    else
    {
       $('#components').dialog("open");       
    }
         
 });
 
 
 function restore()
 {
         $.post("restore.php",function( data ){
                         
               var tmp = JSON.parse( data );
               var tmpComponent = null;
               var tmpId;
               //alert( data );
               for( var i = 0; i < tmp.length; i++)
               {
                    tmpId = tmp[i].id;
                   // alert( tmp[i].id);
                    switch( tmp[i].type )
                    {
                    case "horizontalgauge" : { tmpComponent = new HorizontalGauge( tmpId);
                                               break;
                                             } 
                    case "radialgauge" : {tmpComponent = new RadialGauge( tmpId );
                                                           break;
                                         }
                   case "verticalgauge" : {tmpComponent = new VerticalGauge( tmpId );
                                                         break;
                                 }
                   case "pointergauge" : {tmpComponent = new PointerGauge( tmpId );
                                                         break;
                                 }
                    case "lcd" : {tmpComponent = new Lcd( tmpId );
                                                         break;
                                 }
                    case "linechart" : {tmpComponent = new LineChart( tmpId );
                                                         break;
                                 }
                    case "barchart" : {tmpComponent = new BarChart( tmpId );
                                                         break;
                                 }
                            
                    }
                  if( tmpComponent != null )
                  {
                    tmpComponent.setArduinoName( tmp[i].arduino );
                    tmpComponent.setComponentTitle( tmp[i].title );
                    tmpComponent.setInput( tmp[i].input );
                    tmpComponent.setRefreshRate( tmp[i].refreshRate );
                    
                    tmpComponent.init();
                    
                   
                    $('#refreshSlider'+tmpId).slider("option","value",(tmp[i].refreshRate/1000));
                    $('#value'+tmpId).html( tmp[i].refreshRate/1000 + "s");
                    $('#pinSlider'+tmpId).slider("option","value",tmp[i].input);
                    $('#pinValue'+tmpId).html("Pin " + tmp[i].input);
                    
                    if( tmp[i].arduino != "none" )
                    {
                     $('#arduinoName'+tmpId).html( tmp[i].arduino );
                    }
                    
                    if( tmp[i].isActive == "true" )
                    {
                        tmpComponent.startScheduler();
                    }
                    
                    components[tmpId] = tmpComponent;
                  }
               }
                       
         });
         
         
 }
});
</script>
</head>

<body>

   <!-- Begin Wrapper -->
   <div id="wrapper">
   	 <!-- Begin Navigation -->
        <!-- <div id="navigation" class="shadowed">
		 
		    
		 </div>-->
		 <!-- End Navigation -->
         <!-- Begin Header -->
       <!--  <div id="header">
		 
 

         <div style="display: inline; float:left;margin-right: 40px;margin-top: 40px;"><button class="next"><<</button></div>        
         <div class="componentScroller shadowed"  style="width: 1000px;border: 1px solid black; padding:5px">


   
   
	</div>
	<div style="position:absolute;display: inline; left:1040px; top: 100px;"><button class="prev">>></button></div>		   

 </div>-->
		 <!-- End Header -->
		 
	
		 
		 <!-- Begin Left Column -->
		 <div id="leftcolumn" >
		   <!--<h3 class="ui-widget-header">Arduinos-->
		 <a id="componentButton" href="#"><img src="img/tools.png" /></a> 
		  <div id="components" style="height: 500px"> 
     <div id="accordion"> 
     <h3><a href="#">Arduinos  <img id="arduinoListRefresh" 
		      src="img/reload.png" alt="refresh list of Arduinos icon"
		                   title="refresh list of Arduinos"/></a></h3>
     <div>
		      <ul id="arduinoList">
		        <li class="arduino greyblue">none available</li>
		       
		     </ul>
		  </div>
     <h3><a href="#">Charts</a></h3>
        <div>
        <ul>
        <li><img src="img/examples/linechart.png"  class="demoComponent" alt="linechart" width="200" height="100"/></li> 
        <li><img src="img/examples/barchart.png" class="demoComponent" alt="barchart" width="200" height="100"/></li>
        </ul>
        </div>
        <h3><a href="#">Gauges</a></h3>
        <div>
        <ul>
       <li><img src="img/examples/horizontalgauge1.png" class="demoComponent" alt="horizontalgauge" width="200" height="100" /></li>
       <li><img src="img/examples/verticalgauge.png" class="demoComponent" alt="verticalgauge" width="44" height="100" /></li>
       <li><img src="img/examples/radialgauge.png" class="demoComponent" alt="radialgauge" width="100" height="100" /></li>
       <li><img src="img/examples/pointergauge.png" class="demoComponent" alt="pointergauge" width="100" height="100" /></li>
       <li><img src="img/examples/lcd.png" class="demoComponent" alt="lcd" /></li>

       </li>
       </div>
       
    </div>   

   
   
   </div>

		 
		<!--   <div>
		      <ul id="arduinoList">
		        <li class="arduino greyblue">none available</li>
		       
		     </ul>
		  </div>-->
		 
		 
		 </div>
		 <!-- End Left Column -->
		 
		 <!-- Begin Right Column -->
		 <div id="rightcolumn" class="shadowed">
		       
	        
		 </div>
		 <!-- End Right Column -->
		 
		 <!-- Begin Footer -->
		 <div id="footer">
		       
			
			    
	     </div>
		 <!-- End Footer -->
		 
   </div>
   <!-- End Wrapper -->
  
</body>
</html>
