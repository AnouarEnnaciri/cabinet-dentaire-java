document.addEventListener("DOMContentLoaded", () => {
    const buttons = document.querySelectorAll(".js-delete-btn");

    const overlay = document.createElement("div");
    overlay.className = "modal-overlay";
    overlay.innerHTML = `
        <div class="modal-box">
            <h3>Confirmation</h3>
            <p>Are you sure you want to delete this user?</p>
            <button id="confirmDelete" class="btn-delete">Delete</button>
            <button id="cancelDelete" class="btn-cancel">Cancel</button>
        </div>
    `;

    document.body.appendChild(overlay);

    let targetForm = null;

    buttons.forEach(btn => {
        btn.addEventListener("click", () => {
            targetForm = btn.closest("form");
            overlay.classList.add("active");
        });
    });

    overlay.addEventListener("click", (e) => {
        if (e.target.id === "cancelDelete" || e.target === overlay) {
            overlay.classList.remove("active");
            targetForm = null;
        }

        if (e.target.id === "confirmDelete") {
            if (targetForm) targetForm.submit();
        }
    });
});