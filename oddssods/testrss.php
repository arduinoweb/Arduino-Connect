<?php
header("ContentType: text/xml");
$pinStartDelimiter = '{';
$pinEndDelimiter = '}';
$address = 'localhost';
$port = '10002';
$feed = array( 'channelTitle' =>'Test Feed {1}','channelDescription' =>'Test Feed Description {1} {1}','channelLink' =>'http://localhost'
,'items'=>array(
'Pin 1'=>'Value {1}'
,'Pin 2'=>'Value {2}'
,'Pin 3'=>'Value {3}'
,'Pin 4'=>'Value {4}'
));
$pinsReferenced = array(1 =>'', 2 =>'', 3 =>'', 4 =>'');
$message = "R 1 2 3 4 E";
//
$sock = socket_create( AF_INET, SOCK_STREAM, 0 );

$success = socket_connect( $sock, $address, $port );

if( $success ){

     if( socket_write( $sock, $message."\n", strlen( $message ) + 1 ) )
     {
       if(  ( $reply = socket_read( $sock, 10000, PHP_NORMAL_READ )) )
       {

        $valueArray = explode( " ", $reply );
        $index = 0;

        foreach( $pinsReferenced as $key => $value )
        {
           $pinsReferenced[ $key ] = $valueArray[ $index ];
          
          
        $feed['channelTitle'] = str_replace( $pinStartDelimiter.$key.$pinEndDelimiter,
                                             $valueArray[ $index ],
                                             $feed['channelTitle'] );

        $feed['channelDescription'] = str_replace( $pinStartDelimiter.$key.$pinEndDelimiter,
                                                   $valueArray[ $index ],
                                                   $feed['channelDescription']);
        $items = $feed['items'];

        foreach( $items as $itemKey => $itemValue )
        {
           $feed[ 'items'][ $itemKey ] = str_replace( $pinStartDelimiter.$key.$pinEndDelimiter,
                                                      $valueArray[$index],
                                                      $feed['items'][$itemKey]); 
        }
        
          $index++;                                   
        }
        $reply = trim( $reply );
        
       }
     }
}    
//newstuff

  echo '<?xml version="1.0" encoding="UTF-8"?>';

  echo '<rss version="2.0">';

  echo '<channel>';

  echo '<title>'.$feed["channelTitle"].'</title>';

  echo '<description>'.$feed["channelDescription"].'</description>';

echo '<link>'.$feed["channelLink"].'</link>';

$items = $feed['items'];
foreach( $items as $title=> $description ){
echo '<item>';
echo '<title>'.$title.'</title>';
echo '<description>'.$description.'</description>';
echo '</item>';}
echo '</channel>';
echo '</rss>';



?>
