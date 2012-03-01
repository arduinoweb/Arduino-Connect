<?php
   
   require_once( "../../php/lib/Db.php");
   $mode = $_POST['mode'];
   $name = $_POST['name'];
   $content = $_POST['content'];
   
   
  // echo $mode . " "  . " " . $name . " " . $content;
  $query = "";
  
  if( $mode == "save" )
  {
          $query = "INSERT INTO scripts( '{$name}','{$content}')";
  }
   
  $dbConnection = new Db();
  
  if( ! $dbConnection )
  {
          echo "mistake";
  }
  elseif( $dbConnection->connect() && $dbConnection->query( $query ) )
  {
          echo json_encode( '{"OK"}' );       
          
          
  }
  else
  {
     echo json_encode( '{"Database Connection Error"}');       
  }
?>
