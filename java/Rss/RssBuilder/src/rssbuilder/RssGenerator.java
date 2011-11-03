/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rssbuilder;

/**
 *
 * @author gary
 */


import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.IOException;
import java.util.Iterator;

public class RssGenerator {
    
    private static final Pattern PIN_PATTERN = Pattern.compile("\\{(\\d)+\\}");
    private static final char PIN_START_DELIMITER = '{';
    private static final char PIN_END_DELIMITER = '}';
    
    private static final HashSet< Integer> PINS_REFERENCED = 
                                             new HashSet< Integer >();
    
    public static void generateFeed( final ArrayList< Item> itemsList,
                                      String fileName,
                                     final String arduinoAddress,
                                     final String arduinoPort,
                                     final String channelTitle,
                                     final String channelLink,
                                     final String channelDescription)
    {
        PrintWriter rssFile;
        StringBuffer message = new StringBuffer("R ");
        
       
        
        if( ! fileName.endsWith(".php"))
        {
            fileName = fileName+".php";
        }
        
        try{
          rssFile = new PrintWriter( new BufferedWriter ( 
                                       new FileWriter( fileName ) ), true );
          PINS_REFERENCED.clear();
        
        //  checkForPinReference( channelTitle );
        //  checkForPinReference( channelDescription );
         
          rssFile.println("<?php");
          rssFile.println("header(\"ContentType: text/xml\");");
          rssFile.println("$pinStartDelimiter = '"+ PIN_START_DELIMITER + "';");
          rssFile.println("$pinEndDelimiter = '"+ PIN_END_DELIMITER + "';");
          
          rssFile.println( "$address = '"+ sanitise( arduinoAddress)+"';");
          rssFile.println( "$port = '"+arduinoPort+"';");
          rssFile.println( "$feed = array( 'channelTitle' =>'"+sanitise( channelTitle )+"',"+
                           "'channelDescription' =>'"+sanitise( channelDescription )+
                           "','channelLink' =>'"+sanitise( channelLink) +"'");
         
          int numOfItems = itemsList.size();
          
          if( numOfItems > 0 )
          {
              int count=0;
              
              rssFile.println( ",'items'=>array(");
              
              for( Item item: itemsList )
              {
               
              // checkForPinReference( item.getTitle());
               checkForPinReference( item.getDescription());
               
               rssFile.println("'"+sanitise( item.getTitle() )+"'=>'"+
                               sanitise( item.getDescription())+"'");
               if( count < numOfItems - 1)
               {
                   rssFile.print(",");
               }
               count++;
              }
              rssFile.print(")");
              rssFile.println(");");
              
              if( PINS_REFERENCED.size() > 0 )
              {
                  count = 0;
                  
                  Iterator< Integer > iterator = PINS_REFERENCED.iterator();
                  
                  rssFile.print("$pinsReferenced = array(");
                  
                  while( iterator.hasNext() )
                  {
                      Integer pin = iterator.next();
                      message.append( pin );
                      message.append( " ");
                      
                      rssFile.print( pin + " =>''");
                      
                      if( count < PINS_REFERENCED.size() - 1 )
                      {
                          rssFile.print(", ");
                      }
                      count++;
                  }
                  rssFile.println( ");");
                 
                  rssFile.println( "$message = \"" + message.toString() + "E\";");
              }
          }
          
          rssFile.println("$sock = socket_create( AF_INET, SOCK_STREAM, 0);");
          rssFile.println("$success = socket_connect( $sock, $address, $port);");
          
          rssFile.println( "if( $success){");
          rssFile.println("   if(socket_write( $sock,$message.\"\\n\", strlen( $message ) + 1)){");
          rssFile.println("    if( ( $reply = socket_read( $sock, 10000, PHP_NORMAL_READ))){");
          rssFile.println("         $valueArray = explode( \" \", $reply);");
          rssFile.println("         $index = 0;");
          rssFile.println("         foreach( $pinsReferenced as $key => $value ){");
          rssFile.println("           $pinsReferenced[$key]= $valueArray[$index];");
       //   rssFile.println("           $feed['channelTitle']= str_replace( $pinStartDelimiter.$key.$pinEndDelimiter,");
       //   rssFile.println("                                               $valueArray[$index],\n");
       //   rssFile.println("                                               $feed['channelTitle'] );");
       //   rssFile.println("           $feed['channelDescription'] = str_replace( $pinStartDelimiter.$key.$pinEndDelimiter,");
       //   rssFile.println("                                                      $valueArray[ $index],\n");
      //    rssFile.println("                                                      $feed['channelDescription']);");
          rssFile.println("           $items = $feed['items'];");
          rssFile.println("           foreach( $items as $itemKey => $itemValue ){");
          rssFile.println("             $feed['items'][$itemKey] = str_replace( $pinStartDelimiter.$key.$pinEndDelimiter,");
          rssFile.println("                                                     $valueArray[$index],");
          rssFile.println("                                                     $feed['items'][$itemKey]);");
          rssFile.println("             }");
          rssFile.println("        $index++;");
          rssFile.println("       }");
          rssFile.println("    $reply = trim( $reply );");
          rssFile.println("   }");
          rssFile.println(" }");
         
          
          
          
          
          
          
          
          rssFile.println("}");
          
          
          
          
          
          rssFile.println("echo '<?xml version=\"1.0\" encoding=\"UTF-8\"?>';\n");
          rssFile.println("echo '<rss version=\"2.0\">';\n");
          rssFile.println("echo '<channel>';\n");
          rssFile.println("echo '<title>'.$feed[\"channelTitle\"].'</title>';\n");
          rssFile.println("echo '<description>'.$feed[\"channelDescription\"].'</description>';\n");
          rssFile.println("echo '<link>'.$feed[\"channelLink\"].'</link>';\n");
        
          if( numOfItems > 0 )
          {
            rssFile.println( "$items = $feed['items'];");
            
            rssFile.println( "foreach( $items as $title=> $description ){");
            
            rssFile.println( "echo '<item>';");
            rssFile.println( "echo '<title>'.$title.'</title>';");
            rssFile.println( "echo '<description>'.$description.'</description>';");
            
            rssFile.println( "echo '</item>';}");
                    
          }
          
          rssFile.println("echo '</channel>';");
          
          rssFile.println("echo '</rss>';");
          rssFile.println("?>");
          
        }catch( IOException ioe ){
            System.err.println( ioe );
        }
        
      
    }
    
    private static String sanitise( String text )
    {
        text = text.replaceAll("'","&apos;");
        text = text.replaceAll("\"", "&quot;");
        
        return text;
    }
    
   
    private static void checkForPinReference( final String text )
    {
        Matcher pinMatcher = PIN_PATTERN.matcher( text );
        String pinNumber = "";
        
        while( pinMatcher.find() )
        {
          pinNumber = pinMatcher.group();
            
          PINS_REFERENCED.add( Integer.parseInt( 
                                 pinNumber.substring( 1, pinNumber.length()-1)
                                ));
        }
        
    }
    
}
