<?php

 require_once( './php/lib/config.php');
  
 //require_once( './php/lib/SSL.php');

// require_once( './php/lib/Session.php');

 require_once( './php/lib/Type.php' );
 
 require_once( './php/lib/Db.php');
 
// Session::start();
 
/* if( ! Session::isAuthenticated() || ( Session::isAuthenticated() &&
         Session::getRole() != Type::USER_ROLE ) )
 {
         header( "location: login.php");
         die();
         
 }
 elseif( HTTPS_URL &&  SSL::notHTTPSUrl() )
 {
          SSL::redirectToSSLUrl();
          die();
 }*/
 
 
 if( ! Db::connect() )
 {
      echo json_encode("dberror");
      die();
         
 }
 
 $query = "SELECT * FROM registeredArduinos";
 
 $queryResult = Db::query( $query );
 
 if( $queryResult && sqlite_num_rows( $queryResult ) > 0 )
 {
     $tmpArray = NULL;
     $result = array();
     
     $currTime = time();
     
     while( $tmpArray = sqlite_fetch_array( $queryResult ) )
     {
     
       if( $currTime - $tmpArray['lastRegistered'] <=
               ARDUINO_NETWORK_REGISTRATION_RATE )
       {
        $result[] = $tmpArray['name'];
       }     
     }
     
     echo json_encode( $result );
 }
 else
 {
     echo "No Arduinos";       
 }
?>
