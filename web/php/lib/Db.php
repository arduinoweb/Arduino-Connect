<?php
require_once( 'config.php');
require_once( 'Type.php');

class Db{
        

   private $connection = NULL;
    /**
      * Creates a connection to the database if one doesn't already exist
      * @return the connection or null if unable to create one
      */
    public function connect()
    {  
      
     if(SQLITE_VERSION < 3)
     {
       $this->connection = sqlite_open( DATABASE_FILE, 0666, $error );
      

            
     }
     else 
     {
     	     try{
       $this->connection = new SQLite3( DATABASE_FILE, SQLITE3_OPEN_READWRITE );
	     }catch(Exception $e ){
       	     	        
       	     }
     }
     
      return ( $this->connection ? TRUE : FALSE ); 
     
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
            	if( SQLITE_VERSION == 3 )
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
