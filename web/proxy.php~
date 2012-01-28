<?php

    require_once( './php/lib/config.php');
  
   // require_once( './php/lib/SSL.php');

    require_once( './php/lib/Db.php');
    
    require_once( './php/lib/Type.php');
    
    require_once( './php/lib/String.php');
  
  //  require_once( './php/lib/Session.php');
    
    
  //  Session::start();
    
    
    $returnMsg = json_encode( "unable to contact arduino" );
    
    $arduinoName = String::safeSql( $_POST['arduino'] );
    $text = $_POST['msg'];
    $currTime = time();
    $success = FALSE;
    
    $arduinoDetails = NULL;
    
    if( ! Db::connect() )
    {
       echo "unable to connect to database";
       die();
    }
    
    
    $query = "SELECT * FROM registeredArduinos WHERE
    name='{$arduinoName}'";
    
  
    if(  ($queryResult = Db::query( $query ) ) &&
                 sqlite_num_rows( $queryResult) == 1 )
    {
      
      
      $arduinoDetails = sqlite_fetch_array( $queryResult );
      
      if( $currTime - $arduinoDetails['lastRegistered'] 
               <= ARDUINO_NETWORK_REGISTRATION_RATE )
      {
         $sock = socket_create( AF_INET, SOCK_STREAM, 0 );
         
         if( ($success = socket_connect( $sock, $arduinoDetails['address'], 
                 $arduinoDetails['port'] ) ) )
         {
          // $text = "R 10 E";
         
            if( ($success = socket_write( $sock, $text ."\n", strlen( $text ) + 1 )) )
            {
              if( ( $reply = socket_read( $sock, 10000, PHP_NORMAL_READ ) ) )
              { 
               $reply = trim( $reply );
               $returnMsg = array( 'msg' => $reply );
               $returnMsg = json_encode( $returnMsg );
               $success = TRUE;
              }
            }
         }
      }
           
    }
      
    
    if( ! $success && $arduinoDetails != NULL )
    {
      $query = "DELETE FROM registeredArduinos WHERE name ='{$arduinoName}'";
      Db::query( $query );       
    }
   
    echo $returnMsg;
?>
