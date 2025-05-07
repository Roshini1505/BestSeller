$(document).ready(function () {
  function loadProducts() {
    $.get("/api/products", function (products) {
      let rows = products.map(
        (p, index) => `
        <tr data-id="${p.id}">
          <td>${index + 1}</td>
          <td>${p.name}</td>
          <td>${p.description}</td>
          <td>${p.price}</td>
          <td>${p.tax}</td>
          <td>${p.stockQuantity}</td>
          <td>
            <button class="edit-product">Edit</button>
            <button class="delete-product">Delete</button>
          </td>
        </tr>`
      );
      $("#productTable tbody").html(rows.join(""));
    });
  }

  loadProducts();

  $("#productForm").submit(function (e) {
    e.preventDefault();

    const id = $("#productId").val();
    const name = $("#pname").val();
    const description = $("#desc").val();
    const price = parseFloat($("#price").val());
    const tax = parseFloat($("#tax").val());
    const stock = parseInt($("#stock").val());

    if (isNaN(price) || isNaN(stock)) {
      alert("Price and Stock must be numbers");
      return;
    }

    
    $.get("/api/products", function (products) {
      const existingProduct = products.find(
        (p) => p.name.toLowerCase() === name.toLowerCase() && p.id !== id
      );

      if (existingProduct) {
        alert(
          "Product with this name already exists! You cannot add a duplicate product."
        );
        return;
      }

      const product = { name, description, price, tax, stockQuantity: stock };
      const method = id ? "PUT" : "POST";
      const url = id ? `/api/products/${id}` : "/api/products";

      $.ajax({
        url,
        method,
        contentType: "application/json",
        data: JSON.stringify(product),
        success: function () {
          $("#productForm")[0].reset();
          $("#productId").val("");
          loadProducts();
        },
      });
    });
  });

  $("#productTable").on("click", ".edit-product", function () {
    const row = $(this).closest("tr");
    const id = row.data("id");
    const name = row.find("td:eq(1)").text();
    const description = row.find("td:eq(2)").text();
    const price = row.find("td:eq(3)").text();
    const tax = row.find("td:eq(4)").text();
    const stock = row.find("td:eq(5)").text();

    $("#productId").val(id);
    $("#pname").val(name);
    $("#desc").val(description);
    $("#price").val(price);
    $("#tax").val(tax);
    $("#stock").val(stock);
  });

  $("#productTable").on("click", ".delete-product", function () {
    const id = $(this).closest("tr").data("id");
    if (confirm("Are you sure to delete this product?")) {
      $.ajax({
        url: `/api/products/${id}`,
        method: "DELETE",
        success: loadProducts,
      });
    }
  });
});
