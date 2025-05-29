document.getElementById("loginForm").addEventListener("submit", function (e) {
    e.preventDefault(); // Prevent default form submission

    const email = document.querySelector("input[name='email']").value;
    const password = document.querySelector("input[name='password']").value;

    const loginData = {
        email: email,
        password: password
    };

    fetch("/api/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(loginData)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Login failed. Please check your credentials.");
            }
            return response.json();
        })
        .then(data => {
            alert("Login successful!");
            // Redirect to users.html after successful login
            window.location.href = "users.html";
        })
        .catch(error => {
            alert(error.message);
        });
});
