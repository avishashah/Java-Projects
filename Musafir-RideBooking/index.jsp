<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Musafir - Check Ride Price</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="css/style.css">
</head>
<body>
<%@ include file="navbar.jsp" %>
<div class="container mt-5">
  <div class="row">
    <!-- Left Side: Booking Form -->
    <div class="col-md-6 p-4 bg-white border rounded shadow-sm">
      <h3 class="text-danger fw-bold">Check Ride Price</h3>
      <form id="priceForm" action="checkPrice" method="post">
        <div class="mb-3">
          <label class="form-label fw-semibold">Pickup Location</label>
          <input type="text" name="pickup" class="form-control" required>
        </div>
        <div class="mb-3">
          <label class="form-label fw-semibold">Drop Location</label>
          <input type="text" name="drop" class="form-control" required>
        </div>
        <div class="mb-3">
          <label class="form-label fw-semibold">Vehicle</label>
          <select name="vehicle" class="form-control" required>
            <option value="car_economy" selected>Car - Economy</option>
            <option value="car_premium">Car - Premium</option>
            <option value="car_6seater">Car - 6 Seater</option>
            <option value="auto">Auto</option>
            <option value="bike">Bike</option>
          </select>
        </div>
        <button type="submit" class="btn btn-danger w-100">Check Price</button>
      </form>
      <!-- Show fare result here -->
      <div id="fareResult" class="mt-3"></div>
    </div>
    <!-- Right Side: Banner/Image -->
    <div class="col-md-6 text-center p-4">
      <img src="img/musafir_banner.png" class="img-fluid rounded shadow" alt="Ride with Musafir">
      <p class="mt-3 text-muted">Your reliable ride companion — quick, safe, affordable.</p>
    </div>
  </div>
</div>
<%@ include file="footer.jsp" %>

<!-- AJAX script -->
<script>
document.getElementById('priceForm').onsubmit = function(e) {
    e.preventDefault();

    const form = e.target;
    const data = new FormData(form);

    fetch(form.action, {
        method: 'POST',
        body: data
    })
    .then(response => response.text())
    .then(html => {
        document.getElementById('fareResult').innerHTML = html;
    })
    .catch(error => {
        document.getElementById('fareResult').innerHTML = "<span style='color: red;'>Error fetching fare.</span>";
    });
};
</script>
</body>
</html>
