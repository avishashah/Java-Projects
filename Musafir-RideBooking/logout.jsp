<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Logged Out - Musafir</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="css/style.css">
</head>
<body>
<%@ include file="navbar.jsp" %>
<div class="container d-flex justify-content-center align-items-center" style="min-height:60vh;">
  <div class="card shadow-sm border-0 col-md-6 rounded text-center p-4">
    <h2 class="mb-3 text-danger">You have been logged out.</h2>
    <p class="text-muted mb-4">Thank you for using Musafir!<br>
      <a href="login.jsp" class="btn btn-danger mt-2">Login again</a>
      <a href="index.jsp" class="btn btn-outline-dark mt-2 ms-2">Go to Home</a>
    </p>
  </div>
</div>
<%@ include file="footer.jsp" %>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
