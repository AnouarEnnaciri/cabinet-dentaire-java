// factures.js - Modal payment handling with custom annulation modal

let currentFacture = {
    id: null,
    total: 0,
    dejaPaye: 0
};

let pendingAnnulationId = null;

function payerFacture(factureId, montantTotal, dejaPaye) {
    currentFacture.id = factureId;
    currentFacture.total = montantTotal;
    currentFacture.dejaPaye = dejaPaye;

    let reste = montantTotal - dejaPaye;

    // Update modal with values
    document.getElementById('modalTotal').textContent = montantTotal.toFixed(2);
    document.getElementById('modalDejaPaye').textContent = dejaPaye.toFixed(2);
    document.getElementById('modalReste').textContent = reste.toFixed(2);
    document.getElementById('montantPaiement').value = reste.toFixed(2);

    // Show modal
    document.getElementById('paiementModal').classList.add('active');
}

function confirmerPaiement() {
    let montantPaye = parseFloat(document.getElementById('montantPaiement').value);

    if (isNaN(montantPaye) || montantPaye <= 0) {
        alert('Veuillez entrer un montant valide');
        return;
    }

    let nouveauMontant = currentFacture.dejaPaye + montantPaye;

    fetch('/paiement', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: 'factureId=' + currentFacture.id + '&montantPaye=' + nouveauMontant
    })
        .then(response => {
            if (response.ok) {
                fermerModalPaiement();
                location.reload();
            } else {
                alert('Erreur lors du paiement');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Erreur de connexion');
        });
}

function fermerModalPaiement() {
    document.getElementById('paiementModal').classList.remove('active');
    currentFacture = { id: null, total: 0, dejaPaye: 0 };
}

// Annulation with custom modal (no more confirm popup!)
function annulerFacture(factureId) {
    pendingAnnulationId = factureId;
    document.getElementById('annulationModal').classList.add('active');
}

function confirmerAnnulation() {
    if (pendingAnnulationId) {
        fetch('/annuler-facture', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: 'factureId=' + pendingAnnulationId
        })
            .then(response => {
                if (response.ok) {
                    fermerModalAnnulation();
                    location.reload();
                } else {
                    response.text().then(text => alert('Erreur: ' + text));
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Erreur de connexion: ' + error.message);
            });
    }
}

function fermerModalAnnulation() {
    document.getElementById('annulationModal').classList.remove('active');
    pendingAnnulationId = null;
}

// Close modals when clicking outside
window.addEventListener('click', function(e) {
    const paiementModal = document.getElementById('paiementModal');
    const annulationModal = document.getElementById('annulationModal');

    if (e.target === paiementModal) {
        fermerModalPaiement();
    }
    if (e.target === annulationModal) {
        fermerModalAnnulation();
    }
});