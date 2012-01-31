<?php


require_once( "php/lib/Db.php");
require_once( "php/lib/config.php");

$returnValue = "error";

$db = new DB();


if(  $db->connect() )
{
  $query = "SELECT * FROM registeredArduinos";
  
  $result = $db->query( $query );
  $currentTime = time();
  $noneAvailable = TRUE;
  
//  if( sqlite_num_rows( $result ) > 0 )//
//  {//
     while( $row = $db->fetchArray( $result ) )
     {
        if( $currentTime - $row['lastRegistered'] <=
                ARDUINO_NETWORK_REGISTRATION_RATE )
        {
         echo $row['name'] . " ";
         echo $row['address'] . " ";
         echo $row['port'] . " ";
         $noneAvailable = FALSE;
        }    
     }
     
     if( $noneAvailable )
     {
       echo "none available";
     }
          
          
//  }//
        
        
}
else
{
   echo "db error";
}
?>
