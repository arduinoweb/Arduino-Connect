<?php

 require_once( './php/lib/config.php');
  


 require_once( './php/lib/Type.php' );
 
 require_once( './php/lib/Db.php');
 

 $dbConnection = new Db();
 
 if( ! $dbConnection->connect() )
 {
      echo json_encode("dberror");
      die();
         
 }
 
 $query = "SELECT * FROM registeredArduinos";
 
 $queryResult = $dbConnection->query( $query );
 
 if( $queryResult && $dbConnection->numRows( $queryResult ) > 0 )
 {
     $tmpArray = NULL;
     $result = array();
     
     $currTime = time();
     
     while( $tmpArray = $dbConnection->fetchArray( $queryResult ) )
     {
     
      if( $currTime - $tmpArray['lastRegistered'] <=
               ARDUINO_NETWORK_REGISTRATION_RATE )
       {
        $name = $tmpArray['name'];
        //$result[] = $tmpArray['name'];
        $result[$name]['address'] = $tmpArray['address'];
        $result[$name]['port']= $tmpArray['port'];
    }     
   }
     
     echo json_encode( $result );
    //echo print_r( $result );
 }
 else
 {
     echo "No Arduinos";       
 }
?>
