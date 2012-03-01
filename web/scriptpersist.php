<?php
   

    require_once( 'php/lib/Db.php');
    require_once( 'php/lib/String.php');
    
   $mode = $_POST['mode'];
   $name = $_POST['name'];
   $content = $_POST['content'];
   
   
   $name = String::safeSql( $name );
   $content = String::safeSql( $content );
   
  // echo $mode . " "  . " " . $name . " " . $content;
  $query = "";
  
  if( $mode == "save" )
  {
          $query = "INSERT INTO scripts values( '{$name}','{$content}')";
      
         // exit(0);
  }
  elseif( $mode == "update")
  {
          $query = "UPDATE scripts SET content='{$content}' WHERE name='{$name}'";        
  }
   
  $dbConnection = new Db();
  
  if( ! $dbConnection )
  {
          echo "mistake";
  }
  elseif( $dbConnection->connect() && $dbConnection->query( $query ) )
  {
          $reply['reply'] = "OK";
          
          echo json_encode( $reply );
          
          
  }
  else
  {
     $reply['reply'] = "Database Error";
     
     echo json_encode( $reply );       
  }
?>
