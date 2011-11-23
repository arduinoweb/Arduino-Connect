<?php
 
  require_once( './php/lib/config.php');
  
  require_once( './php/lib/SSL.php');
  
  // Contains methods for accessing database 
  require_once( './php/lib/Db.php');
  
  
  // Contains some required constants
  require_once( './php/lib/Type.php');
  
  // Contains string functions
  require_once( './php/lib/String.php');
  
  require_once( './php/lib/Session.php');
  
  Session::start();
  
  $protocol = ( HTTPS_URL ? "HTTPS://" : "HTTP://" );
  
  if( Session::isAuthenticated() )
  {       
    header( "location: " . String::getNewURL( "login.php", "index.php",
                                                    $protocol) );
    die();
  }
  
  if( HTTPS_URL &&  SSL::notHTTPSUrl() )
  {
          SSL::redirectToSSLUrl();
          die();
  }

  
  $user = "";
  $password = "";
  $error = FALSE;
  $errorMsg = "";
  
 if( isset( $_POST['submit']) )
 {
   if( isset( $_POST['userName'] ) && isset( $_POST['password'] ) )
   {
        $user = trim( $_POST['userName'] );
        $password = trim( $_POST['password'] );
  
   }
        
  
  if( String::isValidLength(  Type::PASSWORD_MIN_LENGTH,
                              Type::PASSWORD_MAX_LENGTH,
                              $password) &&
      String::isValidLength(  Type::USERNAME_MIN_LENGTH,
                              Type::USERNAME_MAX_LENGTH,
                              $user ) )
  {
           
        if( Db::connect() )
        {
           $safeUser = String::safeSql( $user );
           $query = "SELECT * FROM users WHERE userId = '${safeUser}'";
           $result = Db::query( $query );

           if( sqlite_num_rows( $result ) == 1 ) 
           {
                  $tmpArray = sqlite_fetch_array( $result );
                
                  $tmpHash = String::hashPassword( $tmpArray['salt'], $password );
                  if( $tmpHash == $tmpArray['password'] )
                  {
                     Session::regenerateId();
                     
                     Session::setAuthenticated( $user, $tmpArray['type'] );
                     header( "location: " . String::getNewURL( "login.php", "index.php",
                                                    $protocol) );
                                          
                     die();
                     
                  }
                  else
                  {
                      $error = TRUE;
                      $errorMsg = "* invalid details";
                  }
                        
           }
           else
           {
              $error = TRUE;
              $errorMsg = "* invalid details";
                   
           }
           
                
        }
        else
        {
           $error = TRUE;
           $errorMsg = "* unable to connect to database";
                
        }
          
  }
  else
  {
       $error = TRUE;
       $errorMsg = "* invalid details";
  }
 }
?>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Arduino Connect: Login</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="css/style.css" rel="stylesheet" type="text/css" />

</head>
<body>

  <div id="header" class="logoBordered shadowed">
   
        
      <h1>arduino</h1> <h2>connect</h2>
      
    </div>
  </div>
  <div class="content">
    
  <div id="loginForm" class="lightBordered shadowed" method="post"
       action = "<?php  echo $_SERVER['PHP_SELF']?>">
    <h1>login</h1>
    <div class="clr"></div>
    <form method = "post">
        <div>
          <label for="userName">User</label>
          <input type="text" name="userName" id="userName" 
          value="<?php if( $error ){ echo $user ;}?>"/>
        </div>
        <div>
         <label for="password">Password</label>
         <input type="password"  name="password" id="password" 
         value="<?php if( $error ){ echo $password;}?>"/>
        </div>
       
          <input type="submit" name="submit" id="submit" value="login" />
       
    
    </form>
    <div class="error"><?php echo $errorMsg ?></div>
  </div>
 
</div>
</body>
</html>
