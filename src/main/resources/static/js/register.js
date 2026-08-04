document.addEventListener('DOMContentLoaded', () => {
    
    const registerForm = document.getElementById('registerForm');
    // Add event listener for form submission
    if (registerForm) {
        registerForm.addEventListener('submit', async (event) => {
            event.preventDefault(); // Prevent the default form submission behavior
            
            // Get form values
            const firstName = document.getElementById('firstName').value;   
            const lastName = document.getElementById('lastName').value;
            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;

            // Create JSON object to hold the user data
            const userData = {
                firstName: firstName,
                lastName: lastName,
                email: email,
                password: password
            };

            try {
                // Send the user data to the backend API for registration
                const response = await fetch('/api/auth/register', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(userData)
                });
                
                // Check if the response is successful
                if (response.ok) {
                    alert('Registration successful!');
                    window.location.href = 'login.html'; // Redirect to login page
                } else {
                    const errorData = await response.json();
                    console.error('Registration failed:', errorData);
                }

            } catch (error) {
                console.error('Error during registration:', error);
            }
        });
    }
});