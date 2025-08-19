<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Driver Enroll - Musafir</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="css/style.css">
</head>
<body>

<%@ include file="navbar.jsp" %>

<div class="container d-flex justify-content-center align-items-center mt-5">
  <div class="card shadow-lg col-md-6 border-0 rounded">
    <div class="card-header bg-danger text-white text-center rounded-top">
      <h4 class="mb-0">Driver Enrollment</h4>
    </div>
    <div class="card-body bg-light">
      
      <form action="enrollDriver" method="post">
        
        <div class="mb-3">
          <label class="form-label fw-semibold">Vehicle Type</label>
          <select name="vehicle" class="form-select mb-2" required>
            <option value="car_economy">Car - Economy</option>
            <option value="car_premium">Car - Premium</option>
            <option value="car_6seater">Car - 6 Seater</option>
            <option value="auto">Auto</option>
            <option value="bike">Bike</option>
          </select>
        </div>
        
        <div class="mb-3">
          <label class="form-label fw-semibold">Number Plate</label>
          <input type="text" name="plate" placeholder="Enter Vehicle Number Plate" 
                 class="form-control mb-2" required>
        </div>
        
        <button type="submit" class="btn btn-danger w-100 fw-bold">Enroll Now</button>
      </form>
      
    </div>
  </div>
</div>

<%@ include file="footer.jsp" %>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
