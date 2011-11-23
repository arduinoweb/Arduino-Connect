<?php


require_once( 'Type.php');
require_once( 'String.php');

class Session{
        
        
  static function start()
  {
          
     @session_start();            
          
  }
        
        
  static public function isAuthenticated()
  {
       $authenticated = FALSE;
       
       if( isset( $_SESSION[ 'authenticated' ] ) )
       {
           $authenticated = $_SESSION[ 'authenticated' ];   
       }
           
       return $authenticated;    
          
          
  }
        
        
  static public function setAuthenticated(  $user, $role )
    {
    	  
           if( isset( $_SESSION ) && isset( $user ) && isset( $role ) )
           {
               $_SESSION[ 'user' ] = $user;
               $_SESSION[ 'authenticated' ] = TRUE;
               $_SESSION[ 'role'] = $role;
           }
           
           return $newRole;
    }    
        
        
    static public function getUser()
    {
        $user = NULL;
        
        if( isset( $_SESSION[ 'user' ] ) )
        {
           $user = $_SESSION[ 'user' ];
        }
        return $user;
    }        
        
    static public function getRole()
    {
      $role = NULL;
      
      if( isset( $_SESSION[ 'role' ] ) )
      {
          $role = $_SESSION[ 'role' ];   
      }
      
      return $role;
    }
    
   static public function clear()
   {
       session_unset();
       session_destroy();
       $_SESSION[] = array();
       
   }
   
   static public function generateSyncToken()
   {
     $syncToken = Type::SYNC_TOKEN_GENERATION_ERROR;
           
     if( isset( $_SESSION ) )
     {
        $randomBytes = String::getRandomBytes();
             
        $syncToken = sha1( $randomBytes . Type::SERVER_TOKEN );
        
        $syncToken = base64_encode( $syncToken );
     }
     
     return $syncToken;
     
           
   }
   
   static public function regenerateId()
   {
      if( isset( $_SESSION ) )
      {
         session_regenerate_id();
      }
           
   }
   
   
}







?>
