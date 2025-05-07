$(document).ready(function () {
  let users = [];
  let products = [];

  function fetchUsers() {
    $.get("/api/users", function (data) {
      users = data;
      $("#userSearch").autocomplete({
        source: users.map((u) => u.name),
        minLength: 1,
      });
    });
  }

  function fetchProducts() {
    $.get("/api/products", function (data) {
      products = data;
      $("#productSearch").autocomplete({
        source: products.map((p) => p.name),
        minLength: 1,
      });
    });
  }

  function loadOrders() {
    $.get("/api/orders", function (orders) {
      let rows = orders.map(
        (o, index) => `
        <tr data-id="${o.id}">
          <td>${index + 1}</td>
          <td>${o.userName}</td>
          <td>${o.productName}</td>
          <td>${o.quantity}</td>
          <td>${o.totalAmount}</td>
          <td>${o.orderDate}</td>
          <td><button class="delete-order">Delete</button></td>
        </tr>`
      );
      $("#orderTable tbody").html(rows.join(""));
    });
  }

  fetchUsers();
  fetchProducts();
  loadOrders();

  $("#orderForm").submit(function (e) {
    e.preventDefault();

    const userName = $("#userSearch").val().trim();
    const productName = $("#productSearch").val().trim();
    const qty = parseInt($("#qty").val());

    const user = users.find((u) => u.name === userName);
    const product = products.find((p) => p.name === productName);

    if (!user || !product) {
      alert("Please select a valid user and product from suggestions.");
      return;
    }

    if (isNaN(qty) || qty <= 0) {
      alert("Quantity must be a positive number.");
      return;
    }

    if (qty > product.stockQuantity) {
      alert(`Only ${product.stockQuantity} item(s) available in stock.`);
      return;
    }

    const order = {
      userId: user.id,
      productId: product.id,
      quantity: qty,
    };

    $.ajax({
      url: "/api/orders",
      method: "POST",
      contentType: "application/json",
      data: JSON.stringify(order),
      success: function () {
        product.stockQuantity -= qty;
        loadOrders();
        $("#orderForm")[0].reset();
      },
      error: function (xhr) {
        alert("Error placing order: " + xhr.responseText);
      },
    });
  });

  $("#orderTable").on("click", ".delete-order", function () {
    const id = $(this).closest("tr").data("id");
    if (confirm("Are you sure to delete this order?")) {
      $.ajax({
        url: `/api/orders/${id}`,
        method: "DELETE",
        success: loadOrders,
      });
    }
  });
});
