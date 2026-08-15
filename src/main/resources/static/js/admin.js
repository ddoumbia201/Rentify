document.addEventListener('DOMContentLoaded', async () => {
    const usersTbody = document.getElementById('users-tbody');
    const goodsTbody = document.getElementById('goods-tbody');

    if (!usersTbody || !goodsTbody) return;

    async function loadUsers() {
        try {
            const response = await fetch('/api/admin/users');

            // verify if the user is an admin
            if (response.status === 401 || response.status === 403) {
                alert('Accès réservé aux administrateurs.');
                window.location.href = 'index.html';
                return;
            }

            const users = await response.json();
            usersTbody.innerHTML = '';

            // Populate the users table
            users.forEach(user => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${escapeHtml(user.email)}</td>
                    <td>${escapeHtml(user.role)}</td>
                    <td><button class="btn delete-user-btn" data-id="${user.id}">Supprimer</button></td>
                `;
                usersTbody.appendChild(row);
            });

            // Add event listeners for delete buttons
            document.querySelectorAll('.delete-user-btn').forEach(btn => {
                btn.addEventListener('click', async () => {
                    if (!confirm('Supprimer cet utilisateur ?')) return;
                    const id = btn.dataset.id;
                    const res = await fetch(`/api/admin/users/${id}`, {
                        method: 'DELETE',
                        headers: { 'X-XSRF-TOKEN': getCsrfToken() } // Include the CSRF token in the request headers
                    });
                    if (res.ok) loadUsers();
                    else alert('Erreur lors de la suppression.');
                });
            });

        } catch (error) {
            console.error('Erreur lors du chargement des utilisateurs:', error);
        }
    }

    async function loadGoods() {
        try {
            const response = await fetch('/api/admin/goods');
            const goods = await response.json();
            goodsTbody.innerHTML = '';

            // Populate the goods table
            goods.forEach(good => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${escapeHtml(good.title)}</td>
                    <td>${escapeHtml(good.category)}</td>
                    <td>${good.owner ? escapeHtml(good.owner.email) : '—'}</td>
                    <td><button class="btn delete-good-btn" data-id="${good.id}">Supprimer</button></td>
                `;
                goodsTbody.appendChild(row);
            });

            // Add event listeners for delete buttons 
            document.querySelectorAll('.delete-good-btn').forEach(btn => {
                btn.addEventListener('click', async () => {
                    if (!confirm('Supprimer cette annonce ?')) return;
                    const id = btn.dataset.id;
                    const res = await fetch(`/api/admin/goods/${id}`, {
                        method: 'DELETE',
                        headers: { 'X-XSRF-TOKEN': getCsrfToken() } // Include the CSRF token in the request headers
                    });

                    if (res.ok) {
                        loadGoods();
                    } else if (res.status === 409) {
                        alert('Impossible de supprimer : cette annonce a des réservations associées.');
                    } else {
                        alert('Erreur lors de la suppression.');
                    }
                });
            });

        } catch (error) {
            console.error('Erreur lors du chargement des annonces:', error);
        }
    }

    loadUsers();
    loadGoods();
});