<?php
require_once( 'config.php');
require_once( 'Type.php');

class Db{
        

  static private $connection = NULL;
  
    /**
      * Creates a connection to the database if one doesn't already exist
      * @return the connection or null if unable to create one
      */
    static public function connect()
    {  
            self::$connection = sqlite_open( DATABASE_FILE, 0666, $error );
           
      return self::$connection;   
     
    }      
        
    
    /**
      * Performs a query on the sqlite database
      *
      * @param $qry the query to perform
      * @return the result set as an associative array or NULL
      */
    static function query( &$qry )
    {
         $result = array();
         
            if( ! self::$connection )
            {
                    self::connect();
            }
            
            
            if( self::$connection )
            {
                    $result = sqlite_query( self::$connection, $qry, 
                                            SQLITE_ASSOC );      
             
            }
            
           
           return $result;
    }
        
}


?>
