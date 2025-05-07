$(document).ready(function () {
  $.ajax({
    url: "/api/users",
    method: "GET",
    success: function (data) {
      $("#user-count h3").text(data.length);
    },
  });

  $.ajax({
    url: "/api/products",
    method: "GET",
    success: function (data) {
      $("#product-count h3").text(data.length);
    },
  });

  $.ajax({
    url: "/api/orders",
    method: "GET",
    success: function (data) {
      $("#order-count h3").text(data.length);
    },
  });

  $(".stat-box").click(function () {
    const type = $(this).data("type");
    loadDetails(type);
  });

  function loadDetails(type) {
    let url, title, headers;
    switch (type) {
      case "users":
        url = "/api/users";
        title = "Users List";
        headers = `<th>ID</th><th>Name</th><th>Email</th>`;
        break;
      case "products":
        url = "/api/products";
        title = "Products List";
        headers = `<th>ID</th><th>Name</th><th>Description</th><th>Price</th><th>Stock Quantity</th>`;
        break;
      case "orders":
        url = "/api/orders";
        title = "Orders List";
        headers = `<th>ID</th><th>User</th><th>Product</th><th>Quantity</th><th>Total Amount</th><th>Order Date</th>`;
        break;
    }

    $.ajax({
      url: url,
      method: "GET",
      success: function (data) {
        let rows = data
          .map((item) => {
            if (type === "users") {
              return `<tr><td>${item.id}</td><td>${item.name}</td><td>${item.email}</td></tr>`;
            } else if (type === "products") {
              return `<tr><td>${item.id}</td><td>${item.name}</td><td>${item.description}</td><td>${item.price}</td><td>${item.stockQuantity}</td></tr>`;
            } else if (type === "orders") {
              return `<tr><td>${item.id}</td><td>${item.userName}</td><td>${item.productName}</td><td>${item.quantity}</td><td>${item.totalAmount}</td><td>${item.orderDate}</td></tr>`;
            }
          })
          .join("");

        $("#detail-title").text(title);
        $("#detail-header").html(headers);
        $("#detail-body").html(rows);
        $("#details").show();
      },
    });
  }
});
