
// CONTEXT PATH (from JSP body attribute)

const contextPath = document.body.getAttribute("data-context") || "";


// ACTES DYNAMIQUES

let acteCounter = document.querySelectorAll('.acte-row').length || 1;

function addActeRow() {
    const container = document.getElementById('actes-list');
    const template = document.getElementById('acte-options-template');
    const currentIndex = acteCounter;

    const newRow = document.createElement('div');
    newRow.className = 'acte-row';

    const select = document.createElement('select');
    select.name = `acteId_${currentIndex}`;
    select.className = 'acte-select';
    select.onchange = function() { toggleDentField(this, currentIndex); };

    Array.from(template.options).forEach(opt => {
        select.appendChild(opt.cloneNode(true));
    });

    const dentInput = document.createElement('input');
    dentInput.type = 'number';
    dentInput.name = `numDent_${currentIndex}`;
    dentInput.className = 'dent-input';
    dentInput.placeholder = 'N° dent';
    dentInput.style.display = 'none';

    const quantiteLabel = document.createElement('label');
    quantiteLabel.textContent = 'Qté:';
    quantiteLabel.className = 'inline-label';

    const quantiteInput = document.createElement('input');
    quantiteInput.type = 'number';
    quantiteInput.name = `quantite_${currentIndex}`;
    quantiteInput.className = 'quantite-input';
    quantiteInput.value = '1';

    const removeBtn = document.createElement('button');
    removeBtn.type = 'button';
    removeBtn.className = 'btn-remove-acte';
    removeBtn.textContent = '-';
    removeBtn.onclick = function() {
        this.parentElement.remove();
    };

    newRow.appendChild(select);
    newRow.appendChild(dentInput);
    newRow.appendChild(quantiteLabel);
    newRow.appendChild(quantiteInput);
    newRow.appendChild(removeBtn);
    container.appendChild(newRow);
    acteCounter++;
}

function toggleDentField(selectElement, index) {
    if (!selectElement) return;

    const selectedText = selectElement.options?.[selectElement.selectedIndex]?.text?.toLowerCase() || '';
    const dentInput = document.querySelector(`input[name="numDent_${index}"]`);

    if (!dentInput) return;

    const needsDent = selectedText.includes('extraction') ||
        selectedText.includes('implant') ||
        selectedText.includes('couronne');

    dentInput.style.display = needsDent ? 'inline-block' : 'none';
    dentInput.required = needsDent;

    if (!needsDent) dentInput.value = '';
}


// PRESCRIPTIONS DYNAMIQUES

let prescriptionCounter = document.querySelectorAll('.prescription-row').length || 1;

function addPrescriptionRow() {
    const container = document.getElementById('prescriptions-list');
    if (!container) return;

    const newRow = document.createElement('div');
    newRow.className = 'prescription-row';

    const firstSelect = document.querySelector('.medicament-select');
    let optionsHtml = '';
    if (firstSelect) {
        optionsHtml = firstSelect.innerHTML;
    } else {
        optionsHtml = `
            <option value="">-- Sélectionner un médicament --</option>
            <option value="___CUSTOM___">--- Écrire un autre médicament ---</option>
        `;
    }

    newRow.innerHTML = `
        <select name="medicament_${prescriptionCounter}" class="medicament-select" onchange="toggleCustomInput(this, ${prescriptionCounter})">
            ${optionsHtml}
        </select>
        <input type="text" name="custom_medicament_${prescriptionCounter}" class="custom-medicament" placeholder="Nom du médicament" style="display: none;">
        <input type="text" name="posologie_${prescriptionCounter}" class="posologie-input" placeholder="Posologie (ex: 2x/jour, 7 jours)">
        <button type="button" class="btn-remove-prescription" onclick="this.parentElement.remove()">-</button>
    `;

    container.appendChild(newRow);
    prescriptionCounter++;
}

function toggleCustomInput(selectElement, index) {
    const customInput = document.querySelector(`input[name="custom_medicament_${index}"]`);
    if (selectElement.value === '___CUSTOM___') {
        customInput.style.display = 'inline-block';
        selectElement.name = '';
    } else {
        customInput.style.display = 'none';
        selectElement.name = `medicament_${index}`;
    }
}


// DELETE CONSULTATION MODAL

let pendingConsultationId = null;
let pendingPatientId = null;

function deleteConsultation(consultationId, patientId) {
    pendingConsultationId = consultationId;
    pendingPatientId = patientId;
    const modal = document.getElementById('consultationDeleteModal');
    if (modal) modal.classList.add('active');
}

function confirmDeleteConsultation() {
    if (pendingConsultationId && pendingPatientId) {
        window.location.href = contextPath + "/editConsultation?action=delete&id=" + pendingConsultationId + "&patientId=" + pendingPatientId;
    }
}

function closeDeleteConsultationModal() {
    const modal = document.getElementById('consultationDeleteModal');
    if (modal) modal.classList.remove('active');
    pendingConsultationId = null;
    pendingPatientId = null;
}


// FACTURE

function genererFacture(patientId, consultationId) {
    window.location.href = contextPath + "/generer-facture?patientId=" + patientId + "&consultationId=" + consultationId;
}


// CERTIFICAT MÉDICAL

function genererCertificat(patientId, consultationId) {
    console.log("=== BUTTON CLICKED ===");
    console.log("Patient: " + patientId + ", Consultation: " + consultationId);

    const select = document.getElementById('certificatType_' + consultationId);
    console.log("Select element:", select);

    const type = select ? select.value : 'standard';
    console.log("Type: " + type);

    const url = contextPath + "/certificat?action=save&patientId=" + patientId + "&consultationId=" + consultationId + "&type=" + type;
    console.log("URL: " + url);

    window.location.href = url;
}


// ANTÉCÉDENTS

function openAddAntecedentModal(patientId) {
    document.getElementById('antecedentPatientId').value = patientId;
    const modal = document.getElementById('antecedentModal');
    if (modal) modal.classList.add('active');
}

function closeAntecedentModal() {
    const modal = document.getElementById('antecedentModal');
    if (modal) modal.classList.remove('active');
}

function saveAntecedent() {
    const patientId = document.getElementById('antecedentPatientId').value;
    const type = document.querySelector('#antecedentForm select[name="type"]').value;
    const description = document.querySelector('#antecedentForm textarea[name="description"]').value;

    fetch(contextPath + '/add-antecedent', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: 'patientId=' + patientId + '&type=' + type + '&description=' + encodeURIComponent(description)
    }).then(() => {
        closeAntecedentModal();
        location.reload();
    }).catch(err => console.error('Error:', err));
}


// DELETE ANTÉCÉDENT

let pendingAntecedentId = null;
let pendingAntecedentPatientId = null;

function deleteAntecedentWithModal(antecedentId, patientId) {
    pendingAntecedentId = antecedentId;
    pendingAntecedentPatientId = patientId;
    const modal = document.getElementById('deleteAntecedentModal');
    if (modal) modal.classList.add('active');
}

function closeDeleteAntecedentModal() {
    const modal = document.getElementById('deleteAntecedentModal');
    if (modal) modal.classList.remove('active');
    pendingAntecedentId = null;
    pendingAntecedentPatientId = null;
}

function confirmDeleteAntecedent() {
    if (pendingAntecedentId && pendingAntecedentPatientId) {
        fetch(contextPath + '/delete-antecedent?id=' + pendingAntecedentId + '&patientId=' + pendingAntecedentPatientId)
            .then(() => {
                closeDeleteAntecedentModal();
                location.reload();
            })
            .catch(err => console.error('Error:', err));
    }
}


// INITIALISATION

document.addEventListener('DOMContentLoaded', function() {
    const firstActeSelect = document.querySelector('select[name="acteId_0"]');
    if (firstActeSelect) {
        toggleDentField(firstActeSelect, 0);
    }

    const firstPrescriptionSelect = document.querySelector('select[name="medicament_0"]');
    if (firstPrescriptionSelect) {
        toggleCustomInput(firstPrescriptionSelect, 0);
    }

    const confirmBtn = document.getElementById('confirmDeleteAntecedentBtn');
    if (confirmBtn) {
        confirmBtn.onclick = confirmDeleteAntecedent;
    }
});

// Fermer les modaux en cliquant à l'extérieur
window.addEventListener("click", function(e) {
    const consultationModal = document.getElementById('consultationDeleteModal');
    const antecedentModal = document.getElementById('antecedentModal');
    const deleteAntecedentModal = document.getElementById('deleteAntecedentModal');

    if (e.target === consultationModal) consultationModal.classList.remove('active');
    if (e.target === antecedentModal) antecedentModal.classList.remove('active');
    if (e.target === deleteAntecedentModal) deleteAntecedentModal.classList.remove('active');
});