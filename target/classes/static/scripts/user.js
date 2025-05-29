document.addEventListener('DOMContentLoaded', () => {
    const userId = localStorage.getItem('userId') || 1;
    let currentCategory = 'all';
    let products = [];

    // Load products
    function loadProducts() {
        fetch('/api/products')
            .then(response => response.json())
            .then(data => {
                products = data;
                filterProducts();
            })
            .catch(error => console.error('Error:', error));
    }

    // Filter products by category and search query
    function filterProducts() {
        const searchQuery = document.getElementById('searchInput').value.toLowerCase();
        let filtered = products;

        if (currentCategory !== 'all') {
            filtered = filtered.filter(p => p.category === currentCategory);
        }

        if (searchQuery) {
            filtered = filtered.filter(p =>
                p.name.toLowerCase().includes(searchQuery) ||
                p.description.toLowerCase().includes(searchQuery)
            );
        }

        displayProducts(filtered);
    }

    // Display products in grid
    function displayProducts(products) {
        const grid = document.getElementById('productsGrid');
        grid.innerHTML = '';

        products.forEach(product => {
            const card = document.createElement('div');
            card.className = 'product-card';
            card.innerHTML = `
                <img src="${product.imageUrl}" alt="${product.name}" class="product-image">
                <div class="product-info">
                    <h3 class="product-name">${product.name}</h3>
                    <div class="rating">
                        ${getStarRating(product.averageRating)}
                        (${product.averageRating.toFixed(1)})
                    </div>
                    <p class="product-price">$${product.price}</p>
                    <button onclick="showProductDetails(${product.id})" class="btn-primary">
                        View Details
                    </button>
                </div>
            `;
            grid.appendChild(card);
        });
    }

    // Star rating helper
    function getStarRating(rating) {
        return '⭐'.repeat(Math.round(rating));
    }

    // Search functionality
    document.getElementById('searchInput').addEventListener('input', filterProducts);
    document.getElementById('searchBtn').addEventListener('click', filterProducts);

    // Category filtering
    document.querySelectorAll('.category-btn').forEach(btn => {
        btn.addEventListener('click', () => {
            document.querySelectorAll('.category-btn').forEach(b => b.classList.remove('active'));
            btn.classList.add('active');
            currentCategory = btn.dataset.category;
            filterProducts();
        });
    });

    // Product details modal
    window.showProductDetails = (productId) => {
        const product = products.find(p => p.id === productId);
        const modal = document.getElementById('productModal');
        const details = document.getElementById('productDetails');

        details.innerHTML = `
            <h2>${product.name}</h2>
            <img src="${product.imageUrl}" alt="${product.name}" style="max-width: 300px;">
            <p>${product.description}</p>
            <div class="rating">${getStarRating(product.averageRating)}</div>
            <p class="product-price">$${product.price}</p>
        `;

        loadProductReviews(productId);
        modal.style.display = 'block';
    };

    // Reviews
    function loadProductReviews(productId) {
        fetch(`/api/reviews/product/${productId}`)
            .then(response => response.json())
            .then(reviews => {
                const reviewsList = document.getElementById('reviewsList');
                reviewsList.innerHTML = reviews.map(review => `
                    <div class="review">
                        <div class="rating">${'⭐'.repeat(review.rating)}</div>
                        <p>${review.comment}</p>
                        <small>By ${review.user.fullName}</small>
                    </div>
                `).join('');
            });
    }

    // Review submission
    document.getElementById('reviewForm').addEventListener('submit', (e) => {
        e.preventDefault();
        const productId = currentProductId;
        const rating = document.querySelector('.stars .fas').length;
        const comment = document.getElementById('reviewComment').value;

        fetch(`/api/reviews/product/${productId}/user/${userId}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ rating, comment })
        })
            .then(() => {
                loadProductReviews(productId);
                document.getElementById('reviewForm').reset();
            })
            .catch(error => console.error('Error:', error));
    });

    // Star rating interaction
    document.querySelectorAll('.stars i').forEach(star => {
        star.addEventListener('click', () => {
            const rating = star.dataset.rating;
            document.querySelectorAll('.stars i').forEach(s => {
                s.className = s.dataset.rating <= rating ? 'fas fa-star' : 'far fa-star';
            });
        });
    });

    // Address management
    const addressBtn = document.getElementById('manageAddressBtn');
    const addressContainer = document.getElementById('addressContainer');

    addressBtn.addEventListener('click', () => {
        addressContainer.style.display = 'block';
        loadAddresses();
    });

    function loadAddresses() {
        fetch(`/api/addresses/${userId}`)
            .then(response => response.json())
            .then(addresses => {
                const addressesList = document.getElementById('addressesList');
                addressesList.innerHTML = addresses.map(addr => `
                    <div class="address-card">
                        <p>${addr.street}</p>
                        <p>${addr.city}, ${addr.state} ${addr.postalCode}</p>
                        <p>${addr.country}</p>
                        <button onclick="deleteAddress(${addr.id})" class="btn-danger">Delete</button>
                    </div>
                `).join('');
            });
    }

    document.getElementById('addressForm').addEventListener('submit', (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        const address = Object.fromEntries(formData.entries());

        fetch(`/api/addresses/${userId}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(address)
        })
            .then(() => {
                loadAddresses();
                e.target.reset();
            })
            .catch(error => console.error('Error:', error));
    });

    window.deleteAddress = (addressId) => {
        if (confirm('Are you sure you want to delete this address?')) {
            fetch(`/api/addresses/${addressId}`, { method: 'DELETE' })
                .then(() => loadAddresses())
                .catch(error => console.error('Error:', error));
        }
    };

    // Close modals when clicking outside
    window.onclick = (event) => {
        if (event.target.classList.contains('modal')) {
            event.target.style.display = 'none';
        }
    };

    // Initial load
    loadProducts();
});