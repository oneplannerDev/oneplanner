<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
<h1>
	Hello world!  
</h1>

<P>  The time on the server is ${serverTime}. </P>
          <table border="1">
                <th>No</th>
                <th>Name</th>
                 
                <c:forEach var="customer" items="${listCustomer}" varStatus="status">
                <tr>
                    <td>${status.index + 1}</td>
                    <td>${customer.customerId}</td>
                    <td>${customer.customerName}</td>
                    <td>
                        <a href="/editContact?id=${customer.customerId}">Edit</a>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <a href="/deleteContact?id=${customer.customerName}">Delete</a>
                    </td>
                             
                </tr>
                </c:forEach>             
            </table>
</body>
</html>
