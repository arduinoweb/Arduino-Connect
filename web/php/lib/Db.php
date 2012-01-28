<?php
require_once( 'config.php');
require_once( 'Type.php');

class Db{
        

   private $connection = NULL;
   private $isSQLite3 = FALSE;
    /**
      * Creates a connection to the database if one doesn't already exist
      * @return the connection or null if unable to create one
      */
    public function connect()
    {  
      
     if( function_exists( "sqlite_open"))
     {
       $this->connection = sqlite_open( DATABASE_FILE, 0666, $error );
       
     }
     elseif( class_exists( "SQLite3" ))
     {
     	     try{
       $this->connection = new SQLite3( DATABASE_FILE, SQLITE3_OPEN_READWRITE );
       $this->isSQLite3 = TRUE;
        return TRUE;
       	     }catch(Exception $e ){
       	     	        
       	     }
     }
     
     return FALSE;
       
     
    }      
        
    
    /**
      * Performs a query on the sqlite database
      *
      * @param $qry the query to perform
      * @return the result set as an associative array or NULL
      */
    function query( &$qry )
    {
         $result = array();
         
            if( ! $this->connection )
            {
                    $this->connect();
            }
            
            
            if( $this->connection )
            {
            	if( $this->isSQLite3 )
                {
                  $result = $this->connection->query( $qry );
                  
                }
                else
                    $result = sqlite_query( $this->connection, $qry, 
                                            SQLITE_ASSOC );      
             
            }
            
           
           return $result;
    }
        
}


?>
