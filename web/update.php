<?php


require_once( './php/lib/config.php');
  
//require_once( './php/lib/SSL.php');

//require_once( './php/lib/Session.php');

require_once( './php/lib/Type.php' );

require_once( './php/lib/Db.php');

require_once( './php/lib/String.php');

/* Session::start();
 
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
 
 
 if( ! isset( $_POST['componentId'] ) )
 {
     echo "error";
     die();
 }
 
 $safeComponentId = String::safeSql( $_POST['componentId'] );
 $query = "";
 $safeField = "";
 $safeValue = "";
 $result = "error";
 
 
 if( isset( $_POST['refreshRate'] ) )
 {
   $safeField="refreshRate";
   $safeValue = String::safeSql( $_POST['refreshRate'] );
 }
 elseif( isset( $_POST['arduino'] ) )
 {
    $safeField ="arduino";
    $safeValue = "'".String::safeSql( $_POST['arduino'] )."'";
         
 }
 elseif( isset( $_POST['isActive'] ) )
 {
    $safeField = "isActive";
    $safeValue = "'".String::safeSql( $_POST['isActive'] )."'";
    
 }
 elseif( isset( $_POST['input'] ) )
 {
    $safeField = "input";
    $safeValue = String::safeSql( $_POST['input'] );
 }
 
 if( Db::connect() )
 {
         $query = "UPDATE components SET " . $safeField ."={$safeValue} WHERE"
         . " id = {$safeComponentId}";
         
        
         
 }

 
 
 
 if( $query != "" )
 {
    if( Db::query( $query ) )
    {
        $result = "ok";       
    }
   
         
 }

//echo $query;
echo $result;
?>


