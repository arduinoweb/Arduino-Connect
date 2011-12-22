<?php

 
 require_once( './php/lib/config.php');
  
 require_once( './php/lib/SSL.php');

 require_once( './php/lib/Session.php');

 require_once( './php/lib/Type.php' );
 
 Session::start();
 
 if( ! Session::isAuthenticated() || ( Session::isAuthenticated() &&
         Session::getRole() != Type::USER_ROLE ) )
 {
         header( "location: login.php");
         die();
         
 }
 elseif( HTTPS_URL &&  SSL::notHTTPSUrl() )
 {
          SSL::redirectToSSLUrl();
          die();
 }
 
?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Arduino Connect</title>
<link rel="stylesheet" type="text/css" href="css/main.css" />
-<link rel="stylesheet" type="text/css" href="css/Aristo/Aristo.css" />
<!--<link rel="stylesheet" type="text/css" href="js/jquery/css/flick/jquery-ui-1.8.16.custom.css" />-->



<script type="text/javascript" src="js/jquery/js/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="js/jquery/js/jquery-ui-1.8.16.custom.min.js"></script>

<script type="text/javascript" src = "js/arduinolist.js"></script>
<script type="text/javascript" src="js/jcarousellite_1.0.1.min.js"></script>
<script type="text/javascript" src="js/flot/jquery.flot.min.js"></script>
<script type="text/javascript" src="js/jquery.jeditable.js"></script>
<script type="text/javascript" src="js/Component.js"></script>
<script>
$(document).ready( function(){
                
     var numComponents = 0;
     var components = [];
     
     $('#componentButton').click( function(){
                     $('#header').toggle();
                     
                     return false;
     });
    
     $(".componentScroller").jCarouselLite({
        btnNext: ".next",
        btnPrev: ".prev"
    });
  
  $('.demoComponent').css('overflow','visible');
                

     
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
                   
                    if( name == "newgraph")
                    {
                       var tmp= new Component(++numComponents);
                       tmp.drawGraph();
                    }
                  }
  });
   
  

 
  
});
</script>
</head>

<body>

   <!-- Begin Wrapper -->
   <div id="wrapper">
   	 <!-- Begin Navigation -->
         <div id="navigation" class="shadowed">
		 
		    <a id="componentButton" href="#">Show/Hide</a> 
		 </div>
		 <!-- End Navigation -->
         <!-- Begin Header -->
         <div id="header">
		 
 

         <div style="display: inline; float:left;margin-right: 40px;margin-top: 40px;"><button class="next"><<</button></div>        
         <div class="componentScroller shadowed"  style="width: 1000px;border: 1px solid black; padding:5px">


    <ul>
        <li><img src="img/examples/graph.png"  class="demoComponent" alt="newgraph" width="200" height="100"/ ></li>
       <li class="demoComponent"><img src="someimage" alt="dsdsa" width="200" height="100" ></li>
        <li class="demoComponent"><img src="someimage" alt="dsds" width="200" height="100" ></li>
        <li class="demoComponent"><img src="someimage" alt="dsdsad" width="200" height="100" ></li>
    </ul>
   
	</div>
	<div style="position:absolute;display: inline; left:1040px; top: 100px;"><button class="prev">>></button></div>		   

 </div>
		 <!-- End Header -->
		 
	
		 
		 <!-- Begin Left Column -->
		 <div id="leftcolumn" class="ui-widget ui-content ui-corner-all shadowed"
		                            style="margin:0px; padding:0px; width: 200px;">
		   <h3 class="ui-widget-header">Arduinos
		   <img id="arduinoListRefresh" 
		      src="img/reload.png" alt="refresh list of Arduinos icon"
		                   title="refresh list of Arduinos"/></h3>
		 
		   <div>
		      <ul id="arduinoList">
		        <li class="arduino greyblue">none available</li>
		       
		     </ul>
		  </div>
		 
		 
		 </div>
		 <!-- End Left Column -->
		 
		 <!-- Begin Right Column -->
		 <div id="rightcolumn" class="shadowed">
		       
	        
		 </div>
		 <!-- End Right Column -->
		 
		 <!-- Begin Footer -->
		 <div id="footer">
		       
			   This is the Footer		
			    
	     </div>
		 <!-- End Footer -->
		 
   </div>
   <!-- End Wrapper -->
   <div id="status"></div>
</body>
</html>
