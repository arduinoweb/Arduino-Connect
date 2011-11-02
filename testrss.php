<?php
header("ContentType: text/xml");
$pinStartDelimiter = '{';
$pinEndDelimiter = '}';
$address = 'localhost';
$port = '1002';
$feed = array( 'channelTitle' =>'Channel {1}','channelDescription' =>'The tem {2} {3}','channelLink' =>'http'
,'items'=>array(
'Item 1 {1}'=>'Desc1 {5} {12}'
,'Item 2'=>'Desc{2} {10}'
));

$pinsReferenced = array(1 =>'', 2 =>'', 3 =>'', 5 =>'', 10 =>'', 12 =>'');
//This is new stuff


	
//End of new Stuff
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
