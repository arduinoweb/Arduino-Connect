<?php

 


?>

<!DOCTYPE html>
<html>
 <head>
  <meta charset="utf-8">
  <title>Arduino Connect</title>
  
  <script src="js/dojo-release-1.6.1/dojo/dojo.js" 
          type="text/javascript"></script>
  
 
  <script type="text/javascript">
  dojo.require("dojo.fx");
  
  dojo.ready(function(){
	// The piece we had before - change our innerHTML
	dojo.byId("greeting").innerHTML += ", from " + dojo.version;

	// Now, slide the greeting
	dojo.fx.slideTo({
		top: 600,
		left: 200,
		node: dojo.byId("greeting")
	}).play();
});
                
                  
  
  
  </script>
 </head>
 <body>
     <h1 id="greeting">Hello</h1>
 
 </body>



</html>
