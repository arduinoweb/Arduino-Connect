<?php

  $reply = "bollocks";
  
 
 
  $arrOpt = array('l_onoff' =>1, 'l_linger'=> 1);
  
  $sock = socket_create( AF_INET, SOCK_STREAM, 0);
  //socket_set_block( $sock );
 // socket_set_option( $sock, SOL_SOCKET, SO_LINGER, $arrOpt );
  
  $succ = socket_connect( $sock, "localhost", 10002);
  
  $text= $_GET['io'] . " " . $_GET['value'] . " E";
  
  
  socket_write( $sock, $text."\n", strlen( $text ) + 1 );
  
 
  $reply = socket_read( $sock, 10000, PHP_NORMAL_READ );
  $reply = trim( $reply );
  $arr = array(  $reply );

  echo json_encode( $arr );
  
  socket_shutdown( $sock );
  socket_close( $sock );
  
  //$arr = array( 'msg' => $reply);
 // $arr = array( 'msg' => $data );
  
  
 // echo json_encode( $arr );


?>
