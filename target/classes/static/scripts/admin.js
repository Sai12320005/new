document.addEventListener('DOMContentLoaded', () => {
    // Tab switching
    const tabs = document.querySelectorAll('.tab-btn');
    const tabContents = document.querySelectorAll('.tab-content');

    tabs.forEach(tab => {
        tab.addEventListener('click', () => {
            tabs.forEach(t => t.classList.remove('active'));
            tabContents.forEach(c => c.classList.remove('active'));
            tab.classList.add('active');
            document.getElementById(`${tab.dataset.tab}Tab`).classList.add('active');
            if (tab.dataset.tab === 'users') {
                loadUsers();
            } else {
                loadProducts();
            }
        });
    });

    // Load users
    function loadUsers() {
        fetch('/api/users')
            .then(response => response.json())
            .then(users => {
                const tableBody = document.querySelector('#userTable tbody');
                tableBody.innerHTML = '';
                users.forEach(user => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                        <td>${user.id}</td>
                        <td>${user.fullName}</td>
                        <td>${user.email}</td>
                        <td>${user.address}</td>
                        <td>${user.phone}</td>
                    `;
                    tableBody.appendChild(row);
                });
            })
            .catch(error => console.error('Error loading users:', error));
    }

    // Product management
    const addProductBtn = document.getElementById('addProductBtn');
    const productForm = document.getElementById('productForm');
    const addProductForm = document.getElementById('addProductForm');

    addProductBtn.addEventListener('click', () => {
        productForm.style.display = 'block';
    });

    window.closeProductForm = () => {
        productForm.style.display = 'none';
        addProductForm.reset();
    };

    addProductForm.addEventListener('submit', (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        const product = Object.fromEntries(formData.entries());
        product.price = parseFloat(product.price);
        product.stockQuantity = parseInt(product.stockQuantity);

        fetch('/api/products', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(product)
        })
            .then(response => response.json())
            .then(() => {
                closeProductForm();
                loadProducts();
            })
            .catch(error => console.error('Error adding product:', error));
    });

    function loadProducts() {
        fetch('/api/products')
            .then(response => response.json())
            .then(products => {
                const tableBody = document.querySelector('#productTable tbody');
                tableBody.innerHTML = '';
                products.forEach(product => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                        <td><img src="${product.imageUrl}" alt="${product.name}" style="width: 50px; height: 50px; object-fit: cover;"></td>
                        <td>${product.name}</td>
                        <td>${product.category}</td>
                        <td>$${product.price}</td>
                        <td>${product.stockQuantity}</td>
                        <td>${product.averageRating.toFixed(1)} ⭐</td>
                        <td>
                            <button onclick="deleteProduct(${product.id})" class="btn-danger">Delete</button>
                        </td>
                    `;
                    tableBody.appendChild(row);
                });
            })
            .catch(error => console.error('Error loading products:', error));
    }

    window.deleteProduct = (productId) => {
        if (confirm('Are you sure you want to delete this product?')) {
            fetch(`/api/products/${productId}`, { method: 'DELETE' })
                .then(() => loadProducts())
                .catch(error => console.error('Error deleting product:', error));
        }
    };

    // Initial load
    loadUsers();
});