document.addEventListener('DOMContentLoaded', async () => {

    const navActions = document.querySelector('.nav-actions');
    if (!navActions) return;

    try {
        const response = await fetch('/api/auth/me');
        if (response.ok) {
            const user = await response.json();

            // link to admin dashboard appears only if the an Admin is connected
            const adminLink = user.role === 'ADMIN'
                ? `<a href="admin.html" class="btn">Admin</a>`
                : '';

            navActions.innerHTML = `
                <a href="item_form.html" class="btn">+ Ajouter une annonce</a>
                <a href="my_items.html" class="btn">Mes annonces</a>
                <a href="my_bookings.html" class="btn">Mes réservations</a>
                ${adminLink}
                <span>Bienvenue, ${escapeHtml(user.firstName)} !</span>
                <button id="logoutBtn" class="btn btn-primary">Déconnexion</button>
            `;

            document.getElementById('logoutBtn').addEventListener('click', async () => {
                const response = await fetch('/api/auth/logout', {
                    method: 'POST',
                    headers: { 'X-XSRF-TOKEN': getCsrfToken() }
                });

                if (response.ok) {
                    window.location.href = 'index.html';
                }
            });
        }
    } catch (error) {
        console.error('Navbar auth error:', error);
    }
});