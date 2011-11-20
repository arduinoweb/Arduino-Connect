<?php

$db = sqlite_popen( 'db/test.db',0666, $error );

if( ! $db ) die ( $error );

$query = "DROP TABLE graphs";
sqlite_exec( $db, $query, $error );

$query = "CREATE TABLE graphs( id INT NOT NULL,
                               userId INT NOT NULL,
                               url VARCHAR( 255 ) NOT NULL,
                               port INT NOT NULL DEFAULT 80,
                               yMin INT NOT NULL DEFAULT 0,
                               yMax INT NOT NULL DEFAULT 255,
                               showLines INT NOT NULL DEFAULT 1,
                               showPoints INT NOT NULL DEFAULT 0,
                               showBars INT NOT NULL DEFAULT 0,
                               legendText VARCHAR( 255 ) NOT NULL DEFAULT 
                                       'GRAPH',
                               refreshRate INT NOT NULL DEFAULT 30000,
                               pinNum INT NOT NULL,
                               PRIMARY KEY( id, userId)
                               )";
                               
$success = sqlite_exec( $db, $query, $error );

if( ! $success ) die ( "Cannot execute query . $error");




?>
