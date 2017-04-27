<%@ page contentType="text/html;charset=UTF-8" language="java" %>  
<html>  
<head>  
    <title>Upload Javapointers</title>  
</head>  

<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script type="text/javascript" >
$(document).ready(function(){
	console.log("document ready");
  sendAjax();
});
 
function sendAjax() {
	
$.ajax({ 
    url: "/ringcatcher/home", 
    type: 'POST', 
    dataType: 'json', 
    data: "{\"name\":\"hmkcode\",\"id\":2}", 
    contentType: 'application/json',
    mimeType: 'application/json' ,
    success: function(data) { 
        alert(data.id + " " + data.name);
    },
    error:function(data,status,er) { 
    	alert("error: "+data+" status: "+status+" er:"+er);
    } 
});
}
</script>
<body>  
<h3>JSON Jquery test</h3>  
</body>
</html>  
<!--     data: "{\"userId\":\"asdfasdfasdf\",\"userNum\":\"weweweewe\",\"userEmail\":\"jinnonspot\@gmail.com\",\"rcomId\":\"weweweewe\"}", 
 -->