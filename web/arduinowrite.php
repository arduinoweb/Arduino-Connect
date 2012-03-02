<?php

   require_once( 'php/lib/Db.php');
   require_once( 'php/lib/String.php');
   
   $command = $_POST['command'];
      
   $ipaddress = $_POST['ipaddress'];
   $port = $_POST['port'];

    $reply = array();
   $reply['reply'] = "error";

   $sock = @socket_create( AF_INET, SOCK_STREAM, 0 );
   $connected = FALSE;
   
  
   $error = TRUE;
  
   if( $sock && ( $connected = @socket_connect( $sock, $ipaddress, $port) ) )
   {
         $reply['reply'] = "socketcreated and connected";
      if( $connected = @socket_write( $sock, $command ."\n", strlen( $command)+1))
      {
            $reply['reply'] = "connected" . " " . $command;  
        if( ( $connected = @socket_read( $sock, 10000, PHP_NORMAL_READ ) ) )
        {
           if( strpos( $connected, "pins") )
           {        
             echo trim($connected);
            die();
           }
           else
           {
              $reply['reply'] =  trim( $connected );
              echo json_encode( $reply );
              die();
           }
        }
        
      }
           
   }
  

     $ipaddress = String::safeSql( $ipaddress);
     $port = String::safeSql( $port );
           
     $query = "DELETE FROM registeredArduinos WHERE address='{$ipaddress}' AND port={$port}";
          
     $dbConnection = new Db();
     $dbConnection->connect();
     $dbConnection->query( $query );

   
 
   echo json_encode( $reply );   


?>
