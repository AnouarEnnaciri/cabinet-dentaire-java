// rendezvous.js

let pendingDeleteId = null;

function confirmDelete(id) {
    pendingDeleteId = id;
    document.getElementById('deleteRdvModal').classList.add('active');
}

function closeDeleteModal() {
    document.getElementById('deleteRdvModal').classList.remove('active');
    pendingDeleteId = null;
}

function deleteRdv() {
    if (pendingDeleteId) {
        window.location.href = "deleteRdv?id=" + pendingDeleteId;
    }
}

// Set up event listeners when page loads
document.addEventListener('DOMContentLoaded', function() {
    const confirmBtn = document.getElementById('confirmDeleteBtn');
    const cancelBtn = document.getElementById('cancelDeleteBtn');
    const modal = document.getElementById('deleteRdvModal');

    if (confirmBtn) {
        confirmBtn.onclick = deleteRdv;
    }

    if (cancelBtn) {
        cancelBtn.onclick = closeDeleteModal;
    }

    // Close modal when clicking outside
    if (modal) {
        window.addEventListener('click', function(e) {
            if (e.target === modal) {
                closeDeleteModal();
            }
        });
    }
});