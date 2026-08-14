document.addEventListener('DOMContentLoaded', async () => {
    
    const navActions = document.querySelector('.nav-actions');
    if (!navActions) return;

    try {
        const response = await fetch('/api/auth/me');
        if (response.ok) {
            const user = await response.json();
            navActions.innerHTML = `
                <span>Welcome, ${user.firstName}!</span>
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