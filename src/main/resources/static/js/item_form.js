document.addEventListener('DOMContentLoaded', async () => {
    const form = document.getElementById('itemForm');
    if (!form) return;

    // On regarde si un id est présent dans l'URL (ex: item_form.html?id=3)
    const params = new URLSearchParams(window.location.search);
    const itemId = params.get('id');
    const isEditMode = itemId !== null;

    // En mode édition, on pré-remplit le formulaire avec les données existantes
    if (isEditMode) {
        document.querySelector('h2').textContent = "Modifier l'annonce";
        form.querySelector('button[type="submit"]').textContent = "Enregistrer les modifications";

        try {
            const response = await fetch(`/api/goods/${itemId}`);
            const good = await response.json();

            document.getElementById('title').value = good.title;
            document.getElementById('description').value = good.description;
            document.getElementById('pricePerDay').value = good.priceperday;
            document.getElementById('category').value = good.category;
            document.getElementById('location').value = good.location;
        } catch (error) {
            console.error('Erreur lors du chargement de l\'annonce:', error);
        }
    }

    form.addEventListener('submit', async (event) => {
        event.preventDefault();

        const goodData = {
            title: document.getElementById('title').value,
            description: document.getElementById('description').value,
            priceperday: parseFloat(document.getElementById('pricePerDay').value),
            category: document.getElementById('category').value,
            location: document.getElementById('location').value
        };

        try {
            const url = isEditMode ? `/api/goods/${itemId}` : '/api/goods';
            const method = isEditMode ? 'PUT' : 'POST';

            const response = await fetch(url, {
                method: method,
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(goodData)
            });

            if (response.ok) {
                alert(isEditMode ? 'Annonce modifiée avec succès !' : 'Annonce publiée avec succès !');
                window.location.href = 'my_items.html';
            } else if (response.status === 401 || response.status === 403) {
                alert('Vous devez être connectée pour effectuer cette action.');
                window.location.href = 'login.html';
            } else {
                const errorData = await response.json();
                console.error('Erreur:', errorData);
                alert('Erreur : vérifiez que tous les champs sont bien remplis.');
            }
        } catch (error) {
            console.error('Erreur réseau:', error);
        }
    });
});