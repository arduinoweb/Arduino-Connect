<?php
require_once( 'php/lib/String.php');
require_once( 'php/lib/Db.php');

//$db = sqlite_popen( '../db/arduino.db',0666, $error );

if( Db::connect() )
{
   $query = "SELECT * FROM users WHERE userId = 'gary'";
   
   $result = Db::query($query);
        
   
   print_r( sqlite_fetch_array( $result ) );
}

$user = "gary";

$user = String::safeSql( $user );

$b = String::getRandomBytes();

$p = String::hashPassword( $b, "123456" );

$query = "INSERT INTO users values( '{$user}',0,'{$b}','{$p}')";

Db::query( $query );



?>
