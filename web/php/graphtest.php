<?php

  session_start();

  if( ! isset( $_SESSION[ 'x' ] ) )
  {
       
      $_SESSION['x'] = 0;
  }
   
  $_SESSION['x']++;


  $number = array( rand( 0, 255 ));
  
  

  echo json_encode( $number ); 

?>