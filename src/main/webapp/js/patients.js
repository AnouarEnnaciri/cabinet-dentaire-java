document.addEventListener("DOMContentLoaded", () => {


    // PATIENT DELETE MODAL

    window.confirmDelete = function (id) {
        const modal = document.getElementById("deleteModal");
        const input = document.getElementById("deletePatientId");
        input.value = id;
        modal.classList.add("active");
    };

    window.closeModal = function () {
        document.getElementById("deleteModal").classList.remove("active");
    };


    // PATIENT EDIT MODAL

    window.openEditModal = function (id, name, phone, email, age) {
        document.getElementById("editId").value = id;
        document.getElementById("editName").value = name;
        document.getElementById("editPhone").value = phone;
        document.getElementById("editEmail").value = email;
        document.getElementById("editAge").value = age;
        document.getElementById("editModal").classList.add("active");
    };

    window.closeEditModal = function () {
        document.getElementById("editModal").classList.remove("active");
    };


    // CLOSE MODALS ON OUTSIDE CLICK

    window.addEventListener("click", function (e) {
        const deleteModal = document.getElementById("deleteModal");
        const editModal = document.getElementById("editModal");

        if (e.target === deleteModal) deleteModal.classList.remove("active");
        if (e.target === editModal) editModal.classList.remove("active");
    });

});