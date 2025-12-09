document.addEventListener("DOMContentLoaded", () => {
    const input = document.getElementById("search-input");
    const tipo = input.closest("[data-tipo]").dataset.tipo; // "hechos" o "colecciones"
    const suggestionsContainer = document.createElement("ul");


    suggestionsContainer.id = "suggestions-list";
    suggestionsContainer.className = "absolute bg-white border border-gray-300 rounded-md mt-1 w-full z-50 shadow-lg text-black";
    input.parentNode.appendChild(suggestionsContainer);
    suggestionsContainer.style.display = "none";

    let debounceTimer;

    input.addEventListener("input", () => {
        clearTimeout(debounceTimer);
        const query = input.value.trim();

        if (!query) {
            suggestionsContainer.innerHTML = "";
            suggestionsContainer.style.display = "none";
            return;
        }

        debounceTimer = setTimeout(async () => {
            const endpoint = tipo === "colecciones" ? "http://api-publica:8085/apiPublica/colecciones/index" : "http://api-publica:8085/apiPublica/hechos/index";
            console.log(endpoint)
            try {
                const response = await fetch(`${endpoint}?search=${encodeURIComponent(query)}&limit=5`);
                const data = await response.json();

                suggestionsContainer.innerHTML = "";
                if (data.length === 0) {
                    suggestionsContainer.style.display = "none";
                    return;
                }
                suggestionsContainer.style.display = "block";
                data.forEach(item => {
                    const li = document.createElement("li");
                    li.textContent = item;
                    li.className = "px-4 py-2 hover:bg-gray-100 cursor-pointer";
                    li.onclick = () => {
                        input.value = item;
                        suggestionsContainer.innerHTML = "";
                        handleSearch(new Event("submit")); // dispara la función de búsqueda
                    };
                    suggestionsContainer.appendChild(li);
                });
            } catch (err) {
                console.error("Error obteniendo sugerencias:", err);
            }
        }, 300); // debounce 300ms
    });

    // Cerrar sugerencias al hacer clic fuera
    document.addEventListener("click", (e) => {
        if (!e.target.closest("#search-input")) {
            suggestionsContainer.innerHTML = "";
            suggestionsContainer.style.display = "none";
        }
    });
});

function handleSearch(event) {
    event.preventDefault();

    const searchValue = document.getElementById('search-input').value.trim();
    const currentUrl = new URL(window.location.href);

    currentUrl.searchParams.set('search', searchValue);
    currentUrl.searchParams.set('page', 0)
    window.location.href = currentUrl.toString();

    return false;
}