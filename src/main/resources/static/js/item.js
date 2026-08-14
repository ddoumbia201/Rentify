document.addEventListener('DOMContentLoaded', async () => {
    const params = new URLSearchParams(window.location.search); // get the query parameters from the URL
    const goodId = params.get('id'); // get the value of the 'id' parameter from the URL
    if (!goodId) return;

    // Load and display the good's details
    try {
        const response = await fetch(`/api/goods/${goodId}`);
        const good = await response.json();

        // Update the page with the good's details
        document.querySelector('.item-title-large').textContent = good.title;
        document.querySelector('.item-category').textContent = good.category;
        document.querySelector('.item-description').textContent = good.description;
        document.querySelector('.booking-box .price').innerHTML =
            `${good.priceperday} € <span>/ jour</span>`;
    } catch (error) {
        console.error('Error while loading the good\'s details:', error);
    }

    // Handle the submission of the booking form
    const bookingForm = document.querySelector('.booking-box form');
    if (!bookingForm) return;

    // Add an event listener to the booking form to handle the submission
    bookingForm.addEventListener('submit', async (event) => {
        event.preventDefault();

        const rentalData = {
            goodId: parseInt(goodId),
            startDate: document.getElementById('booking-start').value,
            endDate: document.getElementById('booking-end').value
        };

        try {
            const response = await fetch('/api/rentals', {
                method: 'POST',
                headers: { 
                    'Content-Type': 'application/json',
                    'X-XSRF-TOKEN': getCsrfToken() // Include the CSRF token in the request headers
                },
                body: JSON.stringify(rentalData)
            });

            if (response.ok) {
                alert('Booking submitted! It is pending confirmation.');
                window.location.href = 'my_bookings.html';
            } else if (response.status === 401 || response.status === 403) {
                alert('You must be logged in to book.');
                window.location.href = 'login.html';
            } else {
                const errorData = await response.json();
                console.error('Error:', errorData);
                alert('Error while submitting the booking: please check the dates.');
            }
        } catch (error) {
            console.error('Network error:', error);
        }
    });
});