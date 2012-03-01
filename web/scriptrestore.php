<?php

    require_once( 'php/lib/Db.php');
    require_once( 'php/lib/String.php');
    
    
    
    $query = "SELECT * FROM scripts";
    
    
    $dbConnection = new Db();
    $resultSet = "";
    $reply = array();
    
    if( $dbConnection )
    {
       if( $dbConnection->connect() && ($resultSet = $dbConnection->query( $query ))
               && $dbConnection->numRows( $resultSet) > 0 )
       {
               $tmpArray = "";
             
               while( $tmpArray = $dbConnection->fetchArray( $resultSet ) )
               {
                    $reply[ $tmpArray['name'] ] = $tmpArray['content'];   
                     
                       
               }
               
               
               
       }
      
            
    }

    echo json_encode( $reply );
?>
