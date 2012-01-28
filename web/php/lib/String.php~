<?php

require_once( 'Type.php');

class String{
        
      
      /**
 	 * Makes a string safe for inserting into database
 	 * avoiding sql injection attacks
 	 * @param string $str the string that is to be made safe
 	 * @return the string after being made safe or otherwise NULL
 	 * 
 	 */
 	static public function safeSql( $str )
 	{   
 		$returnValue = NULL;
 		
 		if( isset( $str ))
 		{
 			$str = self::undoMagicQuotes( $str );
 			$returnValue = sqlite_escape_string( $str );
 		}
 		
 		
 		return $returnValue;
 	}         
         
  /**
    * 
    * If magic quotes is activated on the server this function undoes the work
    * @param string $str the string to be de-slashed
    * @return the de-slashed string
    */
   static private function undoMagicQuotes( $str )
   {
   	 if(function_exists("get_magic_quotes_gpc")
          && get_magic_quotes_gpc() ) {
          $str = stripslashes($str);
      }
   	
   	 return $str;
   }
         
 /**
    * 
    * Checks wether a given string is within $minLength <= n <= $maxLength
    * @param int $minLength
    * @param int_type $maxLength
    * @param string $str
    * @return TRUE if $str.length is within bounds or FALSE otherwise
    */
   static public function isValidLength( $minLength, $maxLength, $str )
   {
   	    $isValid = FALSE;
   	    
   	    if( isset( $minLength) && isset( $maxLength) && isset( $str ) 
   	        && is_int( $minLength ) && is_int( $maxLength) ) 
   	    {
   	      $isValid =  ( strlen( $str ) >= $minLength && 
   	                    strlen( $str ) <= $maxLength );
   	    }
   	
        return $isValid;
   }
        
      
    /**
    * 
    * Creates a hash of given $salt and $password
    * @param string $salt
    * @param string $password
    * @return the hashed password and salt
    */
   static public function hashPassword( $salt, $password)
   {
   	 $hashedPassword = null;
   	 
   	 if( isset( $salt) && strlen( $salt ) > 0 && isset( $password )
   	     && strlen( $password) > 0 )
   	 {
   	    $hashedPassword = sha1( $salt . $password );
   	 }
   	 
   	 return $hashedPassword;
   }
       
   /**
     * 
     * Makes sure that any data to be displayed in user's browser
     * does not contain XSS exploits
     * @param string $str the string to convert
     * @return string the converted string with HTML characters converted to 
     *      their corresponding HTML entity value otherwise NULL
     */
     static public function safeHtml( $str )
     {
 	$returnValue = NULL;
 		
 	if( isset( $str ))
 	{
 	   $str = self::undoMagicQuotes( $str );
 	   $returnValue = htmlentities( $str, ENT_QUOTES, 'UTF-8' );
 	}
 		
 	return $returnValue;
     }
     
     static public function getRandomBytes()
     {
             
         $random = NULL;
         
         if( function_exists( "openssl_random_pseudo_bytes" ) )
         {
            $random = openssl_random_pseudo_bytes( Type::OPENSSL_NUM_BYTES );
            $random = bin2hex( $random );
         }
         else
         {
             $random = mt_rand();
         }
         echo $random;
         return $random;
             
     }
     
     static public function getNewURL( $oldPage, $newPage, $protocol )
     {
             $url = str_replace( $oldPage, $newPage, $_SERVER['PHP_SELF'] );
             
             return $protocol . $_SERVER['HTTP_HOST'] . $url;
             
     }
}


?>
