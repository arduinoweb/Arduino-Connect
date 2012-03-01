<?php

    require_once( 'php/lib/Db.php');
    require_once( 'php/lib/String.php');

    $name = String::safeSql( $_POST['name'] );
   
    $dbConnection = new Db();
    
    if( $dbConnection )
    {
      $query = "DELETE FROM scripts WHERE name='{$name}'";       
      
      if( $dbConnection->connect() && $dbConnection->query( $query ) )
      {
          $reply['reply'] = "OK";
          echo json_encode( $reply );
      }
      else
      {
          $reply['reply'] = "Database Error";
          echo json_encode( $reply );
      }
           
    }
?>
