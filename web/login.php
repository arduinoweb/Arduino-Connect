<?php
 
  require_once( './php/lib/SSL.php');
  
  // Contains methods for accessing database 
  require_once( './php/lib/Db.php');
  
  
  // Contains some required constants
  require_once( './php/Type.php');
  
  if(  SSL::notHTTPSUrl() )
  {
          SSL::redirectToSSLUrl();
  }

  if( ! Db::connect() )
  {
      echo "error connecting to database";       
          
  }
  
 
?>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Arduino Connect: Login</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="css/style.css" rel="stylesheet" type="text/css" />

<!--<script type="text/javascript" src="js/jquery/js/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src= "js/jquery/js/jquery-ui-1.8.16.custom.min.js"></script>-->
</head>
<body>

  <div id="header" class="logoBordered shadowed">
   
        
      <h1>arduino</h1> <h2>connect</h2>
      
    </div>
  </div>
  <div class="content">
    
  <div id="loginForm" class="lightBordered shadowed">
    <h1>login</h1>
    <div class="clr"></div>
    <form method = "post">
        <div>
          <label for="userName">User</label>
          <input type="text" name="userName" id="userName" />
        </div>
        <div>
         <label for="password">Password</label>
         <input type="password"  name="password" id="password" />
        </div>
       
          <input type="submit" name="submit" id="submit" value="login" />
       
    
    </form>
  
  </div>
 
</div>
</body>
</html>
