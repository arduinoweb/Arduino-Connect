<?php

 require_once( './php/lib/config.php');
  
// require_once( './php/lib/SSL.php');

 //require_once( './php/lib/Session.php');

 require_once( './php/lib/Type.php' );
 
 require_once( './php/lib/Db.php');
 
 require_once( './php/lib/String.php');

 /*Session::start();
 
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
 }*/
 
 $safeId = String::safeSql( $_POST['componentId'] );
 
 $query = "DELETE FROM components WHERE id={$safeId}";
 $result = "error";
 
 if( Db::connect() && Db::query( $query ) )
 {
     $result = "ok";       
         
 }
 
  echo $result;

?>