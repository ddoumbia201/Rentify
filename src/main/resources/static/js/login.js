document.addEventListener('DOMContentLoaded', () => {
    
    const loginForm = document.getElementById('loginForm');
    // Add event listener for form submission
    if (loginForm) {
        loginForm.addEventListener('submit', async (event) => {
            event.preventDefault(); // Prevent the default form submission behavior

            // Get form values
            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;

            // Create JSON object to hold the login data
            const loginData = {
                email: email,
                password: password
            };

            try {
                // Send the login data to the backend API for authentication
                const response = await fetch('/api/auth/login', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'X-XSRF-TOKEN': getCsrfToken() // Include the CSRF token in the request headers
                    },
                    body: JSON.stringify(loginData)
                });

                if (response.ok) {
                    const user = await response.json();
                    sessionStorage.setItem('currentUser', JSON.stringify(user)); // Store user data in session storage
                    window.location.href = 'index.html'; // Redirect to home page
                } else {
                    const errorData = await response.json();
                    console.error('Login failed:', errorData);
                }
            } catch (error) {
                console.error('Error during login:', error);
            }
        });
    }
});