document.addEventListener('DOMContentLoaded', async () => {
    const container = document.querySelector('.catalogue');
    if (!container) return;

    async function loadMyGoods() {
        try {
            const response = await fetch('/api/goods/mine');

            if (response.status === 401 || response.status === 403) {
                alert('Vous devez être connectée pour voir vos annonces.');
                window.location.href = 'login.html';
                return;
            }

            const goods = await response.json();
            container.innerHTML = '';

            if (goods.length === 0) {
                container.innerHTML = '<p>Vous n\'avez pas encore publié d\'annonce.</p>';
                return;
            }

            goods.forEach(good => {
                const card = document.createElement('article');
                card.className = 'item-card';
                card.innerHTML = `
                    <img src="https://images.unsplash.com/photo-1464349153735-7db50ed83c84?w=400" alt="${good.title}">
                    <div class="item-body">
                        <div class="item-title">${good.title}</div>
                        <span class="item-category">${good.category}</span>
                        <div class="item-price">${good.priceperday} € / jour</div>
                    </div>
                    <div class="item-card-actions">
                        <a href="item_form.html?id=${good.id}" class="btn">Modifier</a>
                        <button class="btn delete-btn" data-id="${good.id}">Supprimer</button>
                    </div>
                `;
                container.appendChild(card);
            });

            // Brancher chaque bouton Supprimer après génération du HTML
            document.querySelectorAll('.delete-btn').forEach(btn => {
                btn.addEventListener('click', async () => {
                    if (!confirm('Supprimer cette annonce ?')) return;

                    const id = btn.dataset.id;
                    const response = await fetch(`/api/goods/${id}`, { method: 'DELETE' });

                    if (response.ok) {
                        loadMyGoods(); // recharge la liste après suppression
                    } else {
                        alert('Erreur lors de la suppression.');
                    }
                });
            });

        } catch (error) {
            console.error('Erreur réseau:', error);
        }
    }

    loadMyGoods();
});