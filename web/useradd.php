<?php

 require_once( './php/lib/config.php');
  
 require_once( './php/lib/SSL.php');

 require_once( './php/lib/Session.php');
 
 require_once( './php/lib/Db.php');
 
 require_once( './php/lib/String.php');
 
 require_once( './php/lib/Type.php');
 

 Session::start();
 
 if( ! Session::isAuthenticated() || ( Session::isAuthenticated() &&
                                       Session::getRole() != 
                                       Type::USER_ROLE ) )
 {
         header( "location: login.php");
         die();
         
 }
 elseif( HTTPS_URL &&  SSL::notHTTPSUrl() )
 {
          SSL::redirectToSSLUrl();
          die();
 }
 
 $validPasswordLength = FALSE;
 $passwordsMatch = FALSE;
 $validUserName = FALSE;
 $validUserType = FALSE;
 
 $errors = array(  'name' => "error",
                  'userType' => 'error',
                  'password' => 'error',
                  'retypePassword' => 'error' );
 
 
 if( isset( $_POST['name'] ) )
 {
         $_POST['name'] = trim( $_POST['name'] );
         
         if( String::isValidLength( Type::USERNAME_MIN_LENGTH, 
                 Type::USERNAME_MAX_LENGTH, $_POST['name'] ) )
         {
              $errors['name'] = "OK";
              $validUserName = TRUE;
                 
         }
         else
         {
              $errors['name'] = "* name must be between " .
              Type::USERNAME_MIN_LENGTH . " and " . 
              Type::USERNAME_MAX_LENGTH . " characters";       
         }
 
 }
 else
 {
    $errors['name'] = '* you must provide a user name';
         
 }
 
 $validUserType = ( isset( $_POST['userType'] ) &&
                    ($_POST['userType'] == "arduino" ||
                     $_POST['userType'] == "admin" ) );
 
 if( ! $validUserType )
 {
    $errors['userType'] = "* you must select a user type";
 }
 else
 {
    $errors['userType'] = "OK";       
 }

 
 if( isset( $_POST['password'] ) )
 {
         $_POST['password'] = trim( $_POST['password'] );
         
         if( String::isValidLength( Type::PASSWORD_MIN_LENGTH,
                 Type::PASSWORD_MAX_LENGTH, $_POST['password'] ) )
         {
            $errors['password'] = "OK";
            $validPasswordLength = TRUE;
         }
         else
         {
             $errors['password'] = "* must be between " .
             Type::PASSWORD_MIN_LENGTH . " and " . Type::PASSWORD_MAX_LENGTH .
              " characters";       
         }
         
         
 }
 else
 {
        $errors['password'] = "* you must provide a password";       
         
 }
 
 
 if( $validPasswordLength )
 { 
     if( isset( $_POST['retypePassword'] ) &&
          ( $_POST['retypePassword']=
                  trim( $_POST['retypePassword'] ) ) == $_POST['password'] )
     {
          $passwordsMatch = TRUE;
          $errors['password'] = "OK";
     }
     else
     {
         $errors['password'] = "* passwords do not match";
     }
         
 }

 
 if( $validUserName && $passwordsMatch && $validUserType )
 {
         if( Db::connect() )
         {
              
            $userName = strtolower( $_POST['name'] );
            
            $safeName = String::safeSql( $userName );
            $query = "SELECT * FROM users WHERE userId = '{$safeName}'";
            
            $queryResult = Db::query( $query );
            
            if( $queryResult && sqlite_num_rows( $queryResult ) == 0 )
            {
              $userType = ( $_POST['userType'] == "admin" ? 0 : 1 );
                    
               
              $salt = String::getRandomBytes();
              $hashPassword = String::hashPassword( $salt, $_POST['password'] );
                  
              $query = "INSERT INTO users 
                  values( '{$safeName}',$userType,'{$salt}','{$hashPassword}')";
                  
              $queryResult = Db::query( $query );
                  
              if( ! $queryResult )
              {
                 $errors['name']="* an error occurred updating database";       
              }
            }
            else
            {
                  $errors['name']="name exists";       
            }
                 
         }
         else
         {
             $errors['name'] = "* unable to connect to database";
         }
       
         
 }
 
 echo json_encode( $errors );
 
?>
