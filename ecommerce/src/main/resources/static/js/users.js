$(document).ready(function () {
  let users = [];

  function loadUsers() {
    $.get("/api/users", function (data) {
      users = data;
      let rows = users.map(
        (user, index) => `
        <tr data-id="${user.id}">
          <td>${index + 1}</td>
          <td>${user.name}</td>
          <td>${user.email}</td>
          <td>${user.phoneNumber}</td>
          <td>
            <button class="edit-user">Edit</button>
            <button class="delete-user">Delete</button>
          </td>
        </tr>`
      );
      $("#userTable tbody").html(rows.join(""));
    });
  }

  loadUsers();

  $("#userForm").submit(function (e) {
    e.preventDefault();

    const name = $("#name").val();
    const email = $("#email").val();
    const phone = $("#phone").val();
    const id = $("#userId").val();

    if (!/^[\w-.]+@([\w-]+\.)+[\w-]{2,4}$/.test(email)) {
      alert("Invalid email format");
      return;
    }
    if (!/^\d{10}$/.test(phone)) {
      alert("Phone number should be 10 digits");
      return;
    }

    const isDuplicate = users.some(
      (u) => (u.email === email || u.phoneNumber === phone) && u.id != id
    );

    if (isDuplicate) {
      alert("Email or phone number already exists");
      return;
    }

    const userData = { name, email, phoneNumber: phone };
    const method = id ? "PUT" : "POST";
    const url = id ? `/api/users/${id}` : "/api/users";

    $.ajax({
      url,
      method,
      contentType: "application/json",
      data: JSON.stringify(userData),
      success: function () {
        $("#userForm")[0].reset();
        $("#userId").val("");
        loadUsers();
      },
    });
  });

  $("#userTable").on("click", ".edit-user", function () {
    const row = $(this).closest("tr");
    const id = row.data("id");
    const name = row.find("td:eq(1)").text();
    const email = row.find("td:eq(2)").text();
    const phone = row.find("td:eq(3)").text();

    $("#userId").val(id);
    $("#name").val(name);
    $("#email").val(email);
    $("#phone").val(phone);
  });

  $("#userTable").on("click", ".delete-user", function () {
    const id = $(this).closest("tr").data("id");
    if (confirm("Are you sure to delete this user?")) {
      $.ajax({
        url: `/api/users/${id}`,
        method: "DELETE",
        success: loadUsers,
      });
    }
  });
});
