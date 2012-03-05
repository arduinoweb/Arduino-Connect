$(document).ready( function(){



     scriptEditor = CodeMirror.fromTextArea(document.getElementById("scriptEditor"), {lineNumbers: true,
      theme: "elegant",
     lineWrapping: true});
  
             
       $.extend($.gritter.options, { 
        position: 'bottom-left', // defaults to 'top-right' but can be 'bottom-left', 'bottom-right', 'top-left', 'top-right' (added in 1.7.1)
	fade_in_speed: 'fast', // how fast notifications fade in (string or int)
	fade_out_speed: 1000, // how fast the notices fade out
	time: 2500,
	
});
       
         
       
        
         $(function() {
                       
		$( "#scriptTarget" ).droppable({
		        activeClass: 'highlight', 
		        accept: '.arduino',
		        tolerance: 'pointer',
		        
			drop: function( event, ui ) {
		              var id = $(ui.draggable).attr('id');
		              $('#scriptTarget').html( id );
		              $('#scriptTarget').attr( 'title', $(ui.draggable).attr('title'));
		              $('#uploadButton').button("option","disabled", false);
		              
		              
			}
		});
		
		
		      
	});
	 
	 $('#uploadButton').button({disabled:false});
	
	 $('#uploadButton').click( function(){

	        var target = $('#scriptTarget').html();
	        
	        if( target == "upload script" )
	        {
	          $.gritter.add({
	               title: "Upload Script",
                       text: "Drag and drop an Arduino to upload script to it"
                  });        
	                
	        }
	        else
	        {
	            var scriptName = $('#fileName').val();
	            scriptName = $.trim( scriptName );
	            
	            var scriptContents = scriptEditor.getValue();
	            scriptContents = $.trim(scriptContents);
	           
	            if( scriptContents == undefined ||
	                scriptContents.length == 0)
	            {
	               $.gritter.add({
	               title: "Upload Script",
                       text: "You must select a script to upload"
                      });
	            }
	            else
	            {
	               var ipaddressTarget = $('#scriptTarget').attr( 'title');
	               scriptContents = "<script>"+scriptContents+"<\/script>";
	               ipaddressTarget = ipaddressTarget.split(":");
	               $('#uploadButton').button("option","disabled",true);
	               
	                $.gritter.add({
	               title: "Uploading Script",
                       text: "..please wait"
                      });
	            
	               $.post("scriptupload.php", { ipaddress:ipaddressTarget[0],
	                               port:ipaddressTarget[1],
	               script:scriptContents}, function( data ){
	                       
	                   
	                       try{
	                        data = JSON.parse( data );        
	                            
	                        if( data.reply == "error" )
	                        {
	                             $.gritter.add({
	                                             title: "Uploading Script: Error",
	                                 text: "..unable to connect to Arduino"
                                    });    
	                                      
	                        }
	                        else if( data.reply == "OK" )
	                        {
	                           $.gritter.add({
	                                  title: "Uploading Script",
	                                 text: "..script uploaded."
                                    });      
	                                
	                        }
	                       
	                       }catch( err ){
	                          console.log( err );
	                       }
	                 
	              // $('#scriptTarget').html("upload script");      
	              
	                       
	                       
	               }).complete( function(){
	                       
	                  $('#uploadButton').button("option","disabled", false);     
	               });
	            }
	                
	        }
	            
	 });
	 
	 $('#saveButton').button({disabled:false});
	 $('#deleteButton').button({disabled:false});
	 $('#clearButton').button({disabled:false});
	 
	 $('#clearButton').click( function(){
	                 
	     scriptEditor.setValue("");             
	 });
	 
	 
	 $('#deleteButton').click( function(){
	    
	     var scriptName = $('#fileName').val();
	     var scriptInList = false;
	     var scriptToDelete = null;
	     
	     scriptName = $.trim( scriptName );
	     
	     $('#fileList > option').each( function(){
	                       
	        if( this.value.toLowerCase() == scriptName.toLowerCase() )
	        {
	            scriptToDelete = $(this);
	            scriptInList = true;
	            return false;
	             
	        }
	     });
	     
	     if( scriptInList && scriptName !='..')
	     {
	             $.post( "scriptdelete.php", { name : scriptName },
	                   function( data )
	                   {
	                      var response = "";
	                      try{
	                        response = JSON.parse( data ); 
	                              
	                      }catch( err ){
	                         console.log( error );       
	                      }
	                   
	                      
	                      if( response.reply == "OK" )
	                      {
	                        $.gritter.add({
	                           title: "Deleting Script: " + scriptName,
                                   text: "..script deleted"
                                   
                                  
                                 });    
	                          $(scriptToDelete).remove(); 
	                          var tmp = $('#fileList').val();
	                          $('#fileName').val( "" );
	                          scriptEditor.setValue("");
	                      }
	                      else
	                      {
	                          $.gritter.add({
	                              title: "Delete Script: " + scriptName,
                                      text: "A problem has occurred accessing the database. Script Not Deleted."
                                   });   
                              }   
	                           
	                   }
	                   ).error( function(){
	                      $.gritter.add({
	                              title: "Delete Script: " + scriptName,
                                      text: "A network error occurred. Script Not Deleted."
                                   });      
	                           
	                           
	                   });
	             
	             
	             
	     }
	     
	                 
	 });
	 
	 
	 $('#fileList').change(function(){
	       var selectedFileName = $(this).val();
	       selectedFileName = $.trim( selectedFileName);
	       
	       if( selectedFileName.length > 0 )
	       {
	         if( selectedFileName == "..")
	         {
	           $('#fileName').val( "");
	           scriptEditor.setValue("");
	         }
	         else
	         {
	           $('#fileName').val( selectedFileName);
	           scriptEditor.setValue( scripts[selectedFileName] );
	         }
	       }
	 });
	 
	 $('#fileName').change( function(){
	         var fileName = $(this).val();
	         fileName = $.trim( fileName );
	         fileContents = scriptEditor.getValue();
	         fileContents = $.trim( fileContents );
	         
	     
	 });
	 
	 
	 $('#saveButton').click( function(){
	    var fileName = $('#fileName').val();
	    fileName = $.trim( fileName );
	   
	    if( fileName == ".." )
	    {
	      $.gritter.add({
	            title: "Invalid Script Name: " + fileName,
                    text: "Not permitted to use .. as a Script Name"
                  });         
	            
	    }
	    else if( fileName.length > 0 && fileName.length <= 40  )
	    {
	            var contents =scriptEditor.getValue();
	            contents = $.trim( contents );
	            
	            var saveMode = "save";
	            
	            $('#fileList > option').each( function(){
	                          
	             if( this.value.toLowerCase() == fileName.toLowerCase() )
	               {
	                             saveMode = "update";
	                             return false;
	               }
	             });
	            
	               $.gritter.add({
	                              title: "Saving Script: " + fileName,
                                      text: "sending to database..."
                                   });
	            
	            $.post("scriptpersist.php",
	                    {mode:saveMode, name:fileName, content:contents},
	                    function( data){
	                       var newScript = true;
                              
	                       var response ="";
	                       
	                       try{
	                         response = JSON.parse( data );
	                       }catch(err ){
	                          console.log( err );
	                       }
	                                       
	                       if( response.reply == "OK" )
	                       {        
	                          scripts[fileName]=contents;
	                          $('#fileList').val( fileName );
	                           if( saveMode == "save" )
	                           {
	                             var newScript = '<option class=".script" value="'+fileName+'">'+fileName+'</option>';
	                           
	                             $('#fileList').append( newScript );
	                             $('#fileList').val( fileName );
	                           }
	                           
	                            $.gritter.add({
	                              title: "Saving Script: " + fileName,
                                      text: "..stored in databse"
                                   });
	                       }
	                       else
	                       {
	                           $.gritter.add({
	                              title: "Saving Script: " + fileName,
                                      text: "A problem has occurred accessing the database. Script Not Saved"
                                   });     
	                               
	                       }
	            
	                    });
	                    
	                    
	               
	                    
	                    
	    }
	    else
	    {
	        $.gritter.add({
                    title: "Invalid Script Name",
                  text: "script name must be between 1 and 40 characters in length."
                  });
                
	            
	            
	    }
	    
	                 
	                 
	 }).error( function(){
	                      $.gritter.add({
	                              title: "Saving Script: " + scriptName,
                                      text: "A network error occurred. Script Not Deleted."
                                   });      
	                           
	                           
	                   });
	 
	 
	 $.get("scriptrestore.php", function( data ){
	                 
	      var response = "";
	      try{
	        response = JSON.parse( data );       
	      }catch( err ){ 
	          console.log( err );
	      }
	               
	      for( name in response )
	      {
	              scripts[ name ] = response[name];
	              var script = '<option class=".script" value="'+name+'">'+name+'</option>';
	              $('#fileList').append( script );
	              
	      }
      
	 });
	 
	 scriptEditor.refresh();
	 });
	 
