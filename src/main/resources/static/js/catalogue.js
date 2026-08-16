let allGoods = []; 

document.addEventListener('DOMContentLoaded', async () => {
    const catalogue = document.querySelector('.catalogue');
    const searchForm = document.querySelector('.search-bar');
    if (!catalogue) return;

    function renderGoods(goods) {
        catalogue.innerHTML = '';

        if (goods.length === 0) {
            catalogue.innerHTML = '<p>Aucune annonce ne correspond à votre recherche.</p>';
            return;
        }

        goods.forEach(good => {
            const card = document.createElement('article');
            card.className = 'item-card';
            card.innerHTML = `
                <img src="https://images.unsplash.com/photo-1464349153735-7db50ed83c84?w=400" alt="${escapeHtml(good.title)}">
                <div class="item-body">
                    <div class="item-title">${escapeHtml(good.title)}</div>
                    <span class="item-category">${escapeHtml(good.category)}</span>
                    <div class="item-price">${good.priceperday} € / jour</div>
                </div>
            `;
            card.addEventListener('click', () => {
                window.location.href = `item.html?id=${good.id}`;
            });
            catalogue.appendChild(card);
        });
    }

    try {
        const response = await fetch('/api/goods');
        if (!response.ok) {
            console.error('Erreur lors du chargement des annonces:', response.status);
            return;
        }
        allGoods = await response.json();
        renderGoods(allGoods);
    } catch (error) {
        console.error('Erreur réseau:', error);
    }

    // Filtre par catégorie (recherche côté client, sans nouvelle requête)
    if (searchForm) {
        searchForm.addEventListener('submit', (event) => {
            event.preventDefault();

            const selectedCategory = document.getElementById('search-category').value;
            const locationQuery = document.getElementById('search-location').value.trim().toLowerCase();

            let filtered = allGoods;

            if (selectedCategory && selectedCategory !== 'Toutes') {
                filtered = filtered.filter(good => good.category === selectedCategory);
            }

            if (locationQuery) {
                filtered = filtered.filter(good =>
                    good.location && good.location.toLowerCase().includes(locationQuery)
                );
            }

            renderGoods(filtered);
        });
    }
});