<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Login - Musafir</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="css/style.css">
</head>
<body>

<%@ include file="navbar.jsp" %>

<div class="container d-flex justify-content-center align-items-center mt-5">
  <div class="card shadow-lg border-0 col-md-5 rounded">
    
    <!-- Header -->
    <div class="card-header bg-danger text-white text-center rounded-top">
      <h4 class="mb-0">Login to Musafir</h4>
    </div>
    
    <!-- Body -->
    <div class="card-body bg-light p-4">
      <form action="login" method="post">
        
        <div class="mb-3">
          <label class="form-label fw-semibold">Email</label>
          <input type="email" name="email" placeholder="Enter Email" class="form-control" required>
        </div>
        
        <div class="mb-3">
          <label class="form-label fw-semibold">Password</label>
          <input type="password" name="password" placeholder="Enter Password" class="form-control" required>
        </div>
        
        <button type="submit" class="btn btn-danger w-100 fw-bold">Login</button>
        
      </form>
      
      <!-- Extra -->
      <p class="mt-3 mb-0 text-center text-muted">
        Don’t have an account? <a href="signup.jsp" class="text-danger fw-semibold">Signup</a>
      </p>
    </div>
    
  </div>
</div>

<%@ include file="footer.jsp" %>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
