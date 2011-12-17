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
<link rel="stylesheet" type="text/css" href="css/Aristo/Aristo.css" />

<script type="text/javascript" src="js/jquery/js/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="js/jquery/js/jquery-ui-1.8.16.custom.min.js"></script>

<script type="text/javascript" src = "js/arduinolist.js"></script>
</head>

<body>

   <!-- Begin Wrapper -->
   <div id="wrapper">
   	 <!-- Begin Navigation -->
         <div id="navigation" class="shadowed">
		 
		       <img src="img/arduino.png" />	 
			   
		 </div>
		 <!-- End Navigation -->
         <!-- Begin Header -->
         <div id="header">
		 
		       		 
			   
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
   
</body>
</html>
