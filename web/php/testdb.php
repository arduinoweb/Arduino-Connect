<?php
require_once( 'lib/String.php');
require_once( 'lib/Db.php');

//$db = sqlite_popen( '../db/arduino.db',0666, $error );

if( Db::connect() )
{
   $query = "SELECT * FROM users WHERE userId = 'gary'";
   
   $result = Db::query($query);
        
   
   print_r( sqlite_fetch_array( $result ) );
}

/*if( ! $result ) die ( $error );*/

/*$ra = openssl_random_pseudo_bytes( 20 ,$rr);
$salt = bin2hex( $ra );
$password = sha1( $ra . "letmein" );

$query = "INSERT INTO users values( 'gary', 0, '".$salt."','"
          .$password."')";
          
//$query = "DROP TABLE graphs";

echo $query;

$success = sqlite_exec( $db, $query, $error );*/

/*if( ! $success ) die ( "Cannot execute query . $error");*/




?>
