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

<!DOCTYPE html>
<html>
 <head>
  <meta charset="utf-8">
  <title>Arduino Connect</title>
  
  
  
  
  <link rel="stylesheet" type="text/css" href="css/arduino.css" />
  <link rel="stylesheet" type="text/css" href="js/jquery/css/flick/jquery-ui-1.8.16.custom.css" />


  <script type="text/javascript" src="js/jquery/js/jquery-1.6.2.min.js"></script>
  <script type="text/javascript" src="js/jquery/js/jquery-ui-1.8.16.custom.min.js"></script>

  <script type="text/javascript" src="js/jquery.layout.min-1.2.0.js"></script>  
  <script type="text/javascript">
  
  var layout;
  
  $(document).ready( function(){
                  
        
           
           $('#header a').button();
           $('#widgets').accordion();
           $('#navToggleLink').click( function(){
                           $('#nav').toggle( "blind",[],100, function(){
              checkNavPanelState();
                           });
              return false;             
           });
          
           $('#toggleLinkContainer').click( function(){
                           $('#nav').toggle("blind",[],100, function(){
              checkNavPanelState();
                           });
           });
           $('#toggleLinkContainer').hover( togglePanelHighlight);
               
           
           
           $('#toggleLinkContainer').mouseout( checkNavPanelState);
              
        
            
            
           $('#nav').toggle();
           
           
  });
          
  function checkNavPanelState()
  {
        $('#content').html( $('#nav').css('display') );
        var tmpState = $('#nav').css('display');
        
        if( tmpState == 'none' )
               {
                  togglePanelNormal();
                  
               }
               else
               {
                  togglePanelHighlight();
               
               }   
          
          
  }
  
  function togglePanelHighlight()
  {
           $('#toggleLinkContainer').css("background-color", "orange");
                $('#navToggleLink').css("background-color","orange");
          
  }
  
  function togglePanelNormal()
  {
            $('#toggleLinkContainer').css("background-color", "#616160");
                $('#navToggleLink').css("background-color","#616160");
          
  }
  </script>
 </head>
 <body>     
   
 <div id="header" >
    
    
 
   
    <h1>arduino</h1>
  
       <a href="login.php?logout=true" id="logoutLink">logout</a>
       <a href="arduinomanage.php" id="adminLink">admin</a>
      
    
    
    
    
   </div>

   <div id="toggleLinkContainer" class="shadowed" ><span class="shadowed" id="navToggleLink">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></div>
   <div id="nav">
    
    <div id="navContent">
   <div id="widgets">
    <h3><a href="#">Widgets</a></h3>
    <div>
      <p>Widgets 1</p>
    </div>
    <h3><a href="#">Widgets 2</a></h3>
    <div>
     <p>Widgets 2</p>
    
    </div>
    </div>
   </div>
   
 </div>  
   
   <div id="content" style="border-style: solid; border-width: 1px" >Center
   
   
   </div>

 </body>



</html>
