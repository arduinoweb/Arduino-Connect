<?php
require_once("php/lib/Db.php");

$dbConnection = new Db();
$query = "SELECT * FROM components";
if( $dbConnection->connect() )
{
  $dbConnection->query( $query );
  echo "opened";
}
else
{
   echo "not opened";	
}

$dbConnection = NULL;
?>
