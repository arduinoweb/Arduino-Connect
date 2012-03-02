<?php

   require_once( './php/lib/Db.php');
   require_once( './php/lib/String.php');
   
   $ipaddress = $_POST['ipaddress'];
   $port = $_POST['port'];
   $script = $_POST['script'];
   

   $sock = @socket_create( AF_INET, SOCK_STREAM, 0 );
   $connected = FALSE;
   
   $reply = array();
   $reply['reply'] = "error";
   $error = TRUE;
  
   $script = "base64". base64_encode( $script );
   if( $sock && ( $connected = @socket_connect( $sock, $ipaddress, $port) ) )
   {
      if( $connected = @socket_write( $sock, $script ."\n", strlen( $script)+1))
      {
              
        if( ( $connected = @socket_read( $sock, 10000, PHP_NORMAL_READ ) ) )
        {
           $reply['reply'] = trim( $connected );
           $error = FALSE;     
        }
      }
           
   }
  
   if( $error )
   {
     $ipaddress = String::safeSql( $ipaddress);
     $port = String::safeSql( $port );
           
     $query = "DELETE FROM registeredArduinos WHERE address='{$ipaddress}' AND port={$port}";
          
     $dbConnection = new Db();
     $dbConnection->connect();
     $dbConnection->query( $query );
   }
   
   echo json_encode( $reply );
?>
