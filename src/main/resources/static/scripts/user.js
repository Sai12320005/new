// User information
let currentUser = null;
let cart = [];

// DOM Elements
const searchInput = document.getElementById('searchInput');
const searchIcon = document.querySelector('.search-icon');
const productResults = document.getElementById('productResults');
const cartBtn = document.getElementById('cartBtn');
const addressBtn = document.getElementById('addressBtn');
const cartCount = document.querySelector('.cart-count');

// Event Listeners
document.addEventListener('DOMContentLoaded', () => {
    // Get user information
    fetchUserInfo();
    
    // Setup search
    searchIcon.addEventListener('click', handleSearch);
    searchInput.addEventListener('keypress', (e) => {
        if (e.key === 'Enter') handleSearch();
    });

    // Setup category cards
    document.querySelectorAll('.category-card').forEach(card => {
        card.addEventListener('click', () => {
            const category = card.querySelector('.category-name').textContent;
            fetchProductsByCategory(category);
        });
    });

    // Setup modals
    setupModals();
});

// Fetch user information
async function fetchUserInfo() {
    try {
        const response = await fetch('/api/users/current');
        if (response.ok) {
            currentUser = await response.json();
            document.getElementById('welcomeMessage').textContent = `Welcome, ${currentUser.fullName}!`;
        }
    } catch (error) {
        console.error('Error fetching user info:', error);
    }
}

// Search functionality
async function handleSearch() {
    const query = searchInput.value.trim();
    if (!query) return;

    try {
        const response = await fetch(`/api/products/search?query=${encodeURIComponent(query)}`);
        const products = await response.json();
        displayProducts(products);
    } catch (error) {
        console.error('Error searching products:', error);
    }
}

// Display products
function displayProducts(products) {
    productResults.style.display = 'grid';
    productResults.innerHTML = products.map(product => `
        <div class="product-card">
            <img src="${product.imageUrl}" alt="${product.name}" class="product-image">
            <div class="product-details">
                <h3 class="product-title">${product.name}</h3>
                <p class="product-price">₹${product.price}</p>
                <button class="add-to-cart" onclick="addToCart(${product.id})">
                    Add to Cart
                </button>
            </div>
        </div>
    `).join('');
}

// Cart functionality
function addToCart(productId) {
    const product = cart.find(item => item.id === productId);
    if (product) {
        product.quantity++;
    } else {
        cart.push({ id: productId, quantity: 1 });
    }
    updateCartCount();
}

function updateCartCount() {
    const totalItems = cart.reduce((sum, item) => sum + item.quantity, 0);
    cartCount.textContent = totalItems;
}

// Modal setup
function setupModals() {
    // Address modal
    const addressModal = document.getElementById('addressModal');
    addressBtn.addEventListener('click', () => {
        addressModal.style.display = 'block';
        loadAddresses();
    });

    // Cart modal
    const cartModal = document.getElementById('cartModal');
    cartBtn.addEventListener('click', () => {
        cartModal.style.display = 'block';
        loadCart();
    });

    // Close buttons
    document.querySelectorAll('.close-modal').forEach(btn => {
        btn.addEventListener('click', () => {
            btn.closest('.modal').style.display = 'none';
        });
    });
}

// Load addresses
async function loadAddresses() {
    try {
        const response = await fetch(`/api/addresses/${currentUser.id}`);
        const addresses = await response.json();
        const addressList = document.getElementById('addressList');
        addressList.innerHTML = addresses.map(address => `
            <div class="address-item">
                <p>${address.street}</p>
                <p>${address.city}, ${address.state} ${address.postalCode}</p>
                <p>${address.country}</p>
                <button onclick="deleteAddress(${address.id})" class="btn btn-secondary">Delete</button>
            </div>
        `).join('');
    } catch (error) {
        console.error('Error loading addresses:', error);
    }
}

// Load cart
async function loadCart() {
    const cartItems = document.getElementById('cartItems');
    const cartTotal = document.getElementById('cartTotal');
    let total = 0;

    cartItems.innerHTML = cart.map(item => {
        const product = getProduct(item.id); // You'll need to implement this
        total += product.price * item.quantity;
        return `
            <div class="cart-item">
                <img src="${product.imageUrl}" alt="${product.name}">
                <div class="cart-item-details">
                    <h4>${product.name}</h4>
                    <p>₹${product.price} x ${item.quantity}</p>
                </div>
                <div class="cart-item-actions">
                    <button onclick="updateQuantity(${item.id}, ${item.quantity - 1})">-</button>
                    <span>${item.quantity}</span>
                    <button onclick="updateQuantity(${item.id}, ${item.quantity + 1})">+</button>
                </div>
            </div>
        `;
    }).join('');

    cartTotal.textContent = `Total: ₹${total}`;
}