document.addEventListener('DOMContentLoaded', async () => {
    const catalogue = document.querySelector('.catalogue');
    if (!catalogue) return;

    try {
        const response = await fetch('/api/goods');
        if (!response.ok) {
            console.error('Erreur lors du chargement des annonces:', response.status);
            return;
        }

        const goods = await response.json();

        // On vide le contenu statique (les 6 cartes en dur)
        catalogue.innerHTML = '';

        if (goods.length === 0) {
            catalogue.innerHTML = '<p>Aucune annonce disponible pour le moment.</p>';
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
                    <div class="item-price">${good.pricePerDay} € / jour</div>
                </div>
            `;
            // Rendre la carte cliquable vers la page détail
            card.addEventListener('click', () => {
                window.location.href = `item.html?id=${good.id}`;
            });
            catalogue.appendChild(card);
        });

    } catch (error) {
        console.error('Erreur réseau:', error);
    }
});