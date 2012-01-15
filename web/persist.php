<?php

 require_once( './php/lib/config.php');
  
// require_once( './php/lib/SSL.php');

 //require_once( './php/lib/Session.php');

 require_once( './php/lib/Type.php' );
 
 require_once( './php/lib/Db.php');
 
 require_once( './php/lib/String.php');
 
 //echo $_POST['isActive'];

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
 $safeArduinoName = String::safeSql( $_POST['arduinoName'] );
 $safeComponentTitle = String::safeSql( $_POST['componentTitle'] );
 $safeIsActive = String::safeSql( $_POST['isActive'] );
 $safeRefreshRate = String::safeSql( $_POST['refreshRate'] );
 $safeInput = String::safeSql( $_POST['input'] );
 $safeType = String::safeSql( $_POST['type'] );
 $safeLeft = String::safeSql( $_POST['left'] );
 $safeTop = String::safeSql( $_POST['top'] );
 $safeZIndex = String::safeSql( $_POST['zindex'] );
 
 $result = "error";
 
 $query = "INSERT INTO components VALUES({$safeId}, '{$safeComponentTitle}',"
 ."'{$safeIsActive}',{$safeRefreshRate},{$safeInput},'{$safeType}',"
 ."{$safeLeft}, {$safeTop},'{$safeZIndex}','{$safeArduinoName}')";
 
 if( Db::connect() && Db::query( $query ) )
 {
      $result = "ok";
      
 }
 
 echo $result;                                               

?>