<?php

   require_once( './php/lib/Db.php');
   
   $ipaddress = $_POST['ipaddress'];
   $port = $_POST['port'];
   $script = $_POST['script'];
   
   
  // echo $ipaddress . " " . $port . " " . $script;

   $sock = @socket_create( AF_INET, SOCK_STREAM, 0 );
   $connected = FALSE;
   
   $reply = "error";
   
   if( ( $connected = @socket_connect( $sock, $ipaddress, $port) ) )
   {
      if( $success = @socket_write( $sock, $script ."\n", strlen( $script)+1))
      {
              
        if( ( $reply = @socket_read( $sock, 10000, PHP_NORMAL_READ ) ) )
        {
           $reply = trim( $reply );
          
        }
      }
           
   }
   
   echo $reply;
?>
