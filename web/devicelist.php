<?php


require_once( "php/lib/Db.php");
require_once( "php/lib/config.php");

$returnValue = "error";



if( Db::connect() )
{
  $query = "SELECT * FROM registeredArduinos";
  
  $result = Db::query( $query );
  $currentTime = time();
  $noneAvailable = TRUE;
  
  if( sqlite_num_rows( $result ) > 0 )
  {
     while( $row = sqlite_fetch_array( $result ) )
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
          
          
  }
        
        
}
else
{
   echo "db error";
}
?>
