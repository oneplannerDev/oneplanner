<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
    <script src="https://code.jquery.com/jquery-1.9.1.min.js"></script>
    <script>
    $( document ).ready(function() {
    	
        console.log( "document loaded" );
    });
 
    $( window ).load(function() {
        console.log( "window loaded" );
    });
    </script>
</head>
<body>
    <iframe src="http://techcrunch.com"></iframe>
</body>
</html>
	


