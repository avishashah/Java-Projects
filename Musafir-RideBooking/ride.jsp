<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Book Ride - Musafir</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="css/style.css">
</head>
<body>

<%@ include file="navbar.jsp" %>

<div class="container d-flex justify-content-center align-items-center mt-5">
  <div class="card shadow-lg border-0 rounded col-md-6">
    
    <!-- Card Header -->
    <div class="card-header bg-danger text-white text-center rounded-top">
      <h4 class="mb-0">Book Your Ride</h4>
    </div>
    
    <!-- Card Body -->
    <div class="card-body bg-light p-4">
      <form id="bookRideForm" action="bookRide" method="post">
        
        <div class="mb-3">
          <label class="form-label fw-semibold">Pickup Location</label>
          <input type="text" name="pickup" placeholder="Enter Pickup Location" 
                 class="form-control" required>
        </div>
        
        <div class="mb-3">
          <label class="form-label fw-semibold">Drop Location</label>
          <input type="text" name="drop" placeholder="Enter Drop Location" 
                 class="form-control" required>
        </div>
        
        <div class="mb-3">
          <label class="form-label fw-semibold">Payment Method</label>
          <select name="payment" class="form-select" required>
            <option value="cash">Cash</option>
            <option value="online">Online</option>
          </select>
        </div>

        <!-- ✅ Vehicle Type -->
        <div class="mb-3">
          <label class="form-label fw-semibold">Vehicle Type</label>
          <select name="vehicleType" class="form-select" required>
            <option value="car_economy">Car (Economy)</option>
            <option value="car_premium">Car (Premium)</option>
            <option value="car_6seater">Car (6 Seater)</option>
            <option value="auto">Auto</option>
            <option value="bike">Bike</option>
          </select>
        </div>
        
        <button type="submit" class="btn btn-danger w-100 fw-bold">Book Ride</button>
      </form>
      
      <!-- Message container to show server response -->
      <div id="rideMessage" class="mt-3"></div>
      
    </div>
    
  </div>
</div>

<%@ include file="footer.jsp" %>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<script>
document.getElementById('bookRideForm').onsubmit = function(e) {
    e.preventDefault();
    
    const form = e.target;
    const data = new FormData(form);

    fetch(form.action, {
      method: 'POST',
      body: data
    })
    .then(response => response.text())
    .then(html => {
        document.getElementById('rideMessage').innerHTML = html;
    })
    .catch(err => {
        document.getElementById('rideMessage').innerHTML = "<div class='text-danger'>Error processing ride booking.</div>";
    });
};
</script>

</body>
</html>
