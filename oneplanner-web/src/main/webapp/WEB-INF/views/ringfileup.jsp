<%@ page contentType="text/html;charset=UTF-8" language="java" %>  
<html>  
<head>  
    <title>Upload Javapointers</title>  
</head>  
<body>  
<h3>Browse And Upload your File with JSON</h3>  
    <form method="post" action="json/customer/ringfileup" enctype="multipart/form-data" id="upfile-form">  
		<input id="file-name" type="hidden" name="name" value="Hello There"> 
        <input id="file-id" type="file" name="file">  
        <br>  
        <input type="submit" value="Upload">  
    </form>  
<script>
var testForm = document.getElementById('upfile-form');
testForm.onsubmit = function(event) {
  event.preventDefault();

  alert("adsfasdfdas1");
  formData = new FormData();

  formData.append("file", document.forms["upfile-form"].file.files[0]);
  alert("adsfasdfdas2");
  formData.append('user', new Blob([JSON.stringify({
                  "customerId": "Thirdman",
                  "customerName": "He is late."                    
              })], {
                  type: "application/json"
              }));
  alert("adsfasdfdas3");
  var request = require('request');
  request.post({
	        method: 'POST',
	        headers: {'Content-Type': undefined},
	        data: formData,
	        transformRequest: function (data, headersGetterFunction) {
	          return data;
	        }
	        
  });
  alert("adsfasdfdas4");
}
</script>
    
</body>  
</html>  