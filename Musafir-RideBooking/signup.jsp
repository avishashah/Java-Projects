<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Signup - Musafir</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="css/style.css">
</head>
<body>

<%@ include file="navbar.jsp" %>

<div class="container d-flex justify-content-center align-items-center mt-5">
  <div class="card shadow-lg border-0 rounded col-md-6">
    
    <!-- Card Header -->
    <div class="card-header bg-danger text-white text-center rounded-top">
      <h4 class="mb-0">Create Your Account</h4>
    </div>
    
    <!-- Card Body -->
    <div class="card-body bg-light p-4">
      <form action="signup" method="post">
        
        <div class="mb-3">
          <label class="form-label fw-semibold">Full Name</label>
          <input type="text" name="name" placeholder="Enter Full Name" class="form-control" required>
        </div>
        
        <div class="mb-3">
          <label class="form-label fw-semibold">Email</label>
          <input type="email" name="email" placeholder="Enter Email" class="form-control" required>
        </div>
        
        <div class="mb-3">
          <label class="form-label fw-semibold">Password</label>
          <input type="password" name="password" placeholder="Enter Password" class="form-control" required>
        </div>
        
        <div class="mb-3">
          <label class="form-label fw-semibold">Phone</label>
          <input type="text" name="phone" placeholder="Enter Phone Number" class="form-control" required>
        </div>
        
        <div class="mb-3">
          <label class="form-label fw-semibold">City</label>
          <input type="text" name="city" placeholder="Enter City" class="form-control" required>
        </div>
        
        <button type="submit" class="btn btn-danger w-100 fw-bold">Signup</button>
      </form>
      
      <!-- Extra link -->
      <p class="mt-3 mb-0 text-center text-muted">
        Already have an account? <a href="login.jsp" class="text-danger fw-semibold">Login</a>
      </p>
    </div>
    
  </div>
</div>

<%@ include file="footer.jsp" %>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
