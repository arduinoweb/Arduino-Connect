<?php
  require_once( './php/lib/config.php');
  
  require_once( './php/lib/SSL.php');
  
  require_once( './php/lib/Db.php');
  
  require_once( './php/lib/String.php');
  
  require_once( './php/lib/Type.php');
  
  
  $response = "error";
  
  $protocol = ( HTTPS_URL ? "HTTPS://" : "HTTP://" );
  
  if( HTTPS_URL &&  SSL::notHTTPSUrl() )
  {
          echo "sslonly";
          die();
  }
  
  if( isset( $_POST['arduinoName'] )     &&
      isset( $_POST['arduinoPassword'] ) &&
      isset( $_POST['arduinoAddress'] )  &&
      isset( $_POST['arduinoPort'] )  && Db::connect() )
  {
              
          
    $int_options = array("options"=>
            array("min_range"=>Type::MIN_PORT_NUMBER, 
                  "max_range"=>Type::MAX_PORT_NUMBER));
        
       
    $safeName = String::safeSql( $_POST['arduinoName'] );
    $query = "SELECT * FROM users WHERE userId = '{$safeName}'";
       
    $queryResult = Db::query( $query );
       
    if( $queryResult && sqlite_num_rows( $queryResult ) == 1 
           && String::isValidLength( 
                   Type::NETWORK_ADDRESS_MIN_LENGTH, 
                   Type::NETWORK_ADDRESS_MAX_LENGTH, 
                   $_POST['arduinoAddress'] )
       && filter_var( $_POST['arduinoPort'], FILTER_VALIDATE_INT, $int_options )
      )
    {
       $tmpArray = sqlite_fetch_array( $queryResult );
         
       $tmpHash = String::hashPassword( $tmpArray['salt'], 
                                        $_POST['arduinoPassword'] );
         
       if( $tmpArray['type'] == Type::ARDUINO_ROLE 
                                   &&  $tmpHash == $tmpArray['password'] )
       {
         $safeAddress = String::safeSql( $_POST['arduinoAddress'] );
         
         $query = "SELECT * FROM registeredArduinos WHERE name='{$safeName}'";
         
         $queryResult = Db::query( $query );
         
         if( $queryResult )
         {
         
           $currTime = time();
           $numRows = sqlite_num_rows( $queryResult );
           
           if( $numRows == 0 )
           {
        
           
           $query = "INSERT INTO registeredArduinos 
                     VALUES( '{$safeName}','{$safeAddress}',
                     {$_POST['arduinoPort']}, {$currTime})";
           }
           elseif( $numRows == 1 )
           {
          $query = "UPDATE registeredArduinos SET lastRegistered={$currTime},
          port={$_POST['arduinoPort']}, address='{$safeAddress}'
                     WHERE name='{$safeName}'";
                   
           }
                 
          if(  Db::query( $query ) != FALSE )
          {
              $response = "OK";
          }
          
            

         }
       
       }
       
    }     
  }
  
  echo $response;

?>
