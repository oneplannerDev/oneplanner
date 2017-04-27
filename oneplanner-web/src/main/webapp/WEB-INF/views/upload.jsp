<%@ page contentType="text/html;charset=UTF-8" language="java" %>  
<html>  
<head>  
    <title>Upload Javapointers</title>  
</head>  
<body>  
<h3>Browse And Upload your File</h3>  
    <form method="post" action="upload" enctype="multipart/form-data">  
		<input id="file-name" type="hidden" name="name" value="Hello There"> 
        <input id="file-id" type="file" name="file">  
        <br>  
        <input type="submit" value="Upload">  
    </form>  
</body>  
</html>  