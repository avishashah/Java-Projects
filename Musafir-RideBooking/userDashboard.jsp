<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Musafir - Welcome</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="css/style.css">
</head>
<body>
<%@ include file="navbar.jsp" %>

<div class="container mt-5 text-center">
    <div class="row justify-content-center">
        <div class="col-md-6 p-4 bg-white border rounded shadow-sm">
            <h2 class="text-danger fw-bold mb-4">Welcome, User</h2>
            <a href="ride.jsp" class="btn btn-danger btn-lg w-100">Book a Ride</a>
        </div>
    </div>
</div>

<%@ include file="footer.jsp" %>
</body>
</html>
