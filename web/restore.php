<?php


require_once("./php/lib/Db.php");


$db = new Db();
$components = NULL;

if( $db->connect() )
{
   $query = "SELECT * FROM components";
   
   $result = $db->query( $query );
   
   if( $db->numRows( $result ) > 0 )
   {
      while( ($tmp = $db->fetchArray( $result ) ) )
      {
         $components[] = $tmp;       
              
      }
           
      echo json_encode( $components );
   }
        
  $db = NULL;      
}



?>
