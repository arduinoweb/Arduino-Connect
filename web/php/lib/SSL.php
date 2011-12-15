<?php

class SSL{

 static public function notHTTPSUrl()
 {
    $isSSLURL = FALSE;
         
    if( ! isset ( $_SERVER[ 'HTTPS'] ) ||
                  $_SERVER['HTTPS'] != 'on' )
    {
            $isSSLURL = TRUE;       
    }
         
    return $isSSLURL;
 }
 
 static public function redirectToSSLUrl()
 {
         
         header( "location: https://" . $_SERVER['HTTP_HOST'] .       
                                        $_SERVER['PHP_SELF'] );
         die();
 }
}
?>
