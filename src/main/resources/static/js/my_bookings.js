document.addEventListener('DOMContentLoaded', async () => {
    const tbody = document.querySelector('.admin-table tbody');
    if (!tbody) return;

    const statusLabels = {
        'EN_ATTENTE': { text: 'En attente', class: 'status-en-attente' },
        'CONFIRMEE': { text: 'Confirmée', class: 'status-confirmee' },
        'REFUSEE': { text: 'Refusée', class: 'status-refusee' }
    };

    try {
        const response = await fetch('/api/rentals/mine');

        // Check if the user is not logged in or does not have permission
        if (response.status === 401 || response.status === 403) {
            alert('You must be logged in to view your bookings.');
            window.location.href = 'login.html';
            return;
        }

        const rentals = await response.json();
        tbody.innerHTML = ''; // Clear the table body before populating it

        // If there are no rentals, display a message
        if (rentals.length === 0) {
            tbody.innerHTML = '<tr><td colspan="4">No bookings at the moment.</td></tr>';
            return;
        }

        // Populate the table with the user's bookings
        rentals.forEach(rental => {
            const status = statusLabels[rental.status] || { text: rental.status, class: '' };
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${escapeHtml(rental.good.title)}</td>
                <td>${escapeHtml(rental.startDate)}</td>
                <td>${escapeHtml(rental.endDate)}</td>
                <td><span class="status-badge ${status.class}">${status.text}</span></td>
            `;
            tbody.appendChild(row); // Append the row to the table body
        });

    } catch (error) {
        console.error('Network error:', error);
    }
});