document.getElementById('registerForm').addEventListener('submit', function (e) {
    e.preventDefault(); // Prevent default form submission

    const formData = {
        fullName: document.getElementById('fullName').value,
        email: document.getElementById('email').value,
        password: document.getElementById('password').value,
        address: document.getElementById('address').value,
        phone: document.getElementById('phone').value,
        dob: document.getElementById('dob').value,
        gender: document.getElementById('gender').value
    };

    fetch('/api/users/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
        .then(response => {
            if (response.ok) {
                alert('Registration successful!');
                document.getElementById('registerForm').reset();
            } else {
                alert('Registration failed!');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Something went wrong.');
        });
});
