<?php

 
 require_once( './php/lib/config.php');
  
 require_once( './php/lib/SSL.php');

 require_once( './php/lib/Session.php');
 
 require_once( './php/lib/Db.php');
 

 Session::start();
 
 if( ! Session::isAuthenticated() || ( Session::isAuthenticated() && 
                                        Session::getRole() != 0 ) )
 {
         header( "location: login.php");
         die();
         
 }
 elseif( HTTPS_URL &&  SSL::notHTTPSUrl() )
 {
          SSL::redirectToSSLUrl();
          die();
 }
 
?>

<!DOCTYPE html>
<html>
 <head>
  <meta charset="utf-8">
  <title>Arduino Connect- Manage</title>
  
  
  
  
  <link rel="stylesheet" type="text/css" href="css/arduino.css" />
  <link rel="stylesheet" type="text/css" href="js/jquery/css/flick/jquery-ui-1.8.16.custom.css" />


  <script type="text/javascript" src="js/jquery/js/jquery-1.6.2.min.js"></script>
  <script type="text/javascript" src="js/jquery/js/jquery-ui-1.8.16.custom.min.js"></script>

  <script type="text/javascript" src="js/jquery.layout.min-1.2.0.js"></script>  
  <script type="text/javascript">
  
  var layout;
  
  $(document).ready( function(){
                  
        
           
           $('#header a').button();
           $('#addLink').button().click( function(){
                 resetFormFields();
                 resetErrorFields();
                                         
                 $('#editBox').dialog('open');           
                              
           });
           
           $('#editLink').button();
           $('#deleteLink').button();
           
           
           $('#editBox').dialog( {
                           
                           autoOpen: false,
                           height: 450,
                           width: 750,
                           modal: true,
                           buttons: {
                                   
                                   "Add" : function(){ 
                                   
                                   var name = $.trim( $('#name').val() );
                                   $('#name').val( name );
                                   
                                   var password = $.trim( $('#password').val() );
                                   $('#password').val( password );
                                   
                                   var retypePassword = $.trim( $('#retypePassword').val() );
                                   $('#retypePassword').val( retypePassword );
                                   var userType = $('input:radio[name=userType]:checked' ).val();
                                   
                                   resetErrorFields();
                                   
                                    var jqxhr = $.post( "useradd.php", $('#inputForm').serialize() )
                                       .success( evaluateResponse )
                                       .error( function() { alert("error");} );
                                               
                                         
                                   },
                                   Cancel: function() {
                                    $(this).dialog( "close");
                                   }
                                   
                           }
                           
                          
                           
           });
           
           
   function evaluateResponse( data )
   {
         var response = JSON.parse( data );
         
         if( response.name != "OK" )
         {
            $('#nameError').html( response.name );
         }
         
         if( response.password != "OK" )
         {
             $('#passwordError').html( response.password );       
         }
           
         if( response.userType != "OK" )
         {
            $('#userTypeError').html( response.userType );       
         }
   }
        

  function resetFormFields()
  {
       $('#name').val( "");
       $('#password').val("");
       $('#retypePassword').val("");
       $('input:radio[name=userType]:nth(0)').attr('checked',true);
       $('input:radio[name=userType]:nth(1)').attr('checked',false);   
          
          
  }
  
  function resetErrorFields()
  {
       $('#nameError').html("");
       $('#userTypeError').html("");
       $('#passwordError').html("");     
          
          
  }
  });
          

  </script>
 </head>
 <body>     
   
 <div id="header" >
    
    
 
   
    <h1>arduino</h1>
  
       <a href="login.php?logout=true" id="logoutLink">logout</a>
       <a href="index.php" id="indexLink">main</a>
       
    
    
    
    
   </div>
   <div id="operations"><a href="#" id="addLink">Add</a>
     <a href="#" id="editLink">Edit</a>
     <a href="#" id="deleteLink">Delete</a></div>
   <div id="toggleLinkContainer" class="shadowed"  ></div>
   
   

   
   <div id="content" >
   
     
    <?php
      if( Db::connect() )
      {
    
         $query = "SELECT * FROM users";
         
         $queryResult = Db::query( $query );
         
         if( sqlite_num_rows( $queryResult ) > 0 )
         {
            $tmpArray = sqlite_fetch_array( $queryResult );
            
            echo '<div><table id="userTable">';
            echo '<tr class="bordered"><th></th><th>Name</th><th>Type</th></tr>';
            echo '<tr><td><input type="checkbox" /></td>';
            echo '<td>'.$tmpArray['userId'].'</td>';
            echo '<td>' . ( $tmpArray['type'] == '0' ? "admin" : "arduino" ) . '</td></tr>';
            
            echo "</table></div>";
                 
         }
         else
         {
                 
         }
              
      }
    ?>
    
    
  
   
   </div>

   <div id="editBox" title = "Add User">
     <p class="validateTips">All fields are required.</p>
     
     <form id="inputForm">
       <fieldset>
       <div>
       <label for="name" >Name</label>
          <input type="text" name="name" id="name" class="text ui-widget ui-corner-all"/>
          <span id="nameError" class="error"></span>
       </div>
       
       <div>
       <label for="userType" >Type</label>
         <input type="radio" name="userType" value ="arduino" />Arduino
         <input type="radio" name="userType" value="admin" />Admin
         <span id="userTypeError" class="error"></span>
       
       </div>
       
       <div>
       
       
       <label for="password" >Password</label>
          <input type="password" name="password" id="password" class="text ui-widget ui-corner-all" />
          <span id="passwordError" class="error"></span>
       </div>   
       
       <div >
       <label for="retypePassword" >Retype Password</label>
          <input type="password" name="retypePassword" id="retypePassword" class="text ui-widget ui-corner-all" />
       </div> 
       
       </fieldset>
     
     
     
     </form>
   
   </div>
 </body>



</html>
