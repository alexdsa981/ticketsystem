document.addEventListener("DOMContentLoaded", function () {
    const rowsPerPage = 10; // Número de registros por página
    const paginationContainer = document.getElementById("paginationButtons");
    const prevPageButton = document.getElementById("prevPage");
    const nextPageButton = document.getElementById("nextPage");
    let currentPage = 1;
    let filteredRows = []; // Para almacenar las filas filtradas

    const rows = document.querySelectorAll("#ticketTableBody tr");
    const totalRows = rows.length;

    // Función para mostrar los registros de la página actual
    function showPage(page) {
        const start = (page - 1) * rowsPerPage;
        const end = start + rowsPerPage;

        // Ocultar todas las filas
        const allRows = document.querySelectorAll("#ticketTableBody tr");
        allRows.forEach((row) => {
            row.style.display = "none"; // Ocultar todas las filas
        });

        // Mostrar solo las filas filtradas y dentro del rango de la página actual
        filteredRows.forEach((row, index) => {
            if (index >= start && index < end) {
                row.style.display = ""; // Mostrar fila
            }
        });

        // Resaltar el botón de la página actual
        const buttons = document.querySelectorAll("#paginationButtons button");
        buttons.forEach((button, index) => {
            button.classList.toggle("active", index + 1 === page);
        });

        // Habilitar o deshabilitar los botones de flechas
        prevPageButton.disabled = page === 1;
        nextPageButton.disabled = page === Math.ceil(filteredRows.length / rowsPerPage);

        // Actualizar los botones de paginación visibles
        updatePaginationButtons();
    }

// Definir la función getValueOrDefault
function getValueOrDefault(elementId) {
    const element = document.getElementById(elementId);
    return element ? element.value.trim().toLowerCase() : '';
}

// Filtrar registros y actualizar paginación
document.getElementById('filterButton').addEventListener('click', function () {
    // Obtener los valores de los filtros
    const usuarioFilter = getValueOrDefault('usuarioFilter');
    const usuarioEmisorFilter = getValueOrDefault('usuarioEmisorFilter');
    const fechaFilter = document.getElementById('fechaFilter') ? document.getElementById('fechaFilter').value : '';
    const formattedFechaFilter = fechaFilter ? fechaFilter.split('-').reverse().join('/') : '';

    const clasificacionFilter = getValueOrDefault('clasificacionFilter');
    const urgenciaFilter = getValueOrDefault('urgenciaFilter');

    filteredRows = []; // Reiniciar el array de filas filtradas
    const tableBody = document.getElementById('ticketTableBody');
    const rows = tableBody.getElementsByTagName('tr');

    for (let i = 0; i < rows.length; i++) {
        const row = rows[i];

        // Selecciona las celdas por clase
        const usuarioCell = row.querySelector('.usuario-receptor') ? row.querySelector('.usuario-receptor').textContent.toLowerCase() : '';
        const usuarioEmisorCell = row.querySelector('.usuario-emisor') ? row.querySelector('.usuario-emisor').textContent.toLowerCase() : '';
        const fechaCell = row.querySelector('.fecha') ? row.querySelector('.fecha').textContent : '';
        const clasificacionCell = row.querySelector('.clasificacion') ? row.querySelector('.clasificacion').textContent.toLowerCase() : '';
        const urgenciaCell = row.querySelector('.urgencia') ? row.querySelector('.urgencia').textContent.toLowerCase() : '';

        // Comparar con los filtros
        const matchesUsuario = usuarioFilter === '' || usuarioCell.includes(usuarioFilter);
        const matchesUsuarioEmisor = usuarioEmisorFilter === '' || usuarioEmisorCell.includes(usuarioEmisorFilter);
        const matchesFecha = formattedFechaFilter === '' || fechaCell === formattedFechaFilter;
        const matchesClasificacion = clasificacionFilter === '' || clasificacionCell.includes(clasificacionFilter);
        const matchesUrgencia = urgenciaFilter === '' || urgenciaCell.includes(urgenciaFilter);

        // Mostrar u ocultar fila
        if (matchesUsuario && matchesUsuarioEmisor && matchesFecha && matchesClasificacion && matchesUrgencia) {
            row.style.display = ''; // Muestra la fila
            filteredRows.push(row); // Añadir a las filas filtradas
        } else {
            row.style.display = 'none'; // Oculta la fila
        }
    }

    // Mostrar u ocultar mensaje de "No se ha encontrado ningún elemento"
    const noResultsMessage = document.getElementById('noResultsMessage');
    noResultsMessage.style.display = filteredRows.length === 0 ? '' : 'none';

    currentPage = 1; // Reiniciar a la primera página después del filtro
    showPage(currentPage); // Mostrar la página actual después del filtrado
    createPagination(); // Recrear la paginación basada en los resultados filtrados
});
    // Lógica para el botón de eliminar filtrado
    document.getElementById('clearFilterButton').addEventListener('click', function () {
        // Restablece los campos de filtro
        // Comprobar si los elementos existen antes de vaciar sus valores
        if (document.getElementById('usuarioFilter')) {
            document.getElementById('usuarioFilter').value = '';
        }
        if (document.getElementById('usuarioEmisorFilter')) {
            document.getElementById('usuarioEmisorFilter').value = '';
        }
        if (document.getElementById('fechaFilter')) {
            document.getElementById('fechaFilter').value = '';
        }
        if (document.getElementById('clasificacionFilter')) {
            document.getElementById('clasificacionFilter').value = '';
        }
        if (document.getElementById('urgenciaFilter')) {
            document.getElementById('urgenciaFilter').value = '';
        }


        // Muestra todas las filas de la tabla
        const tableBody = document.getElementById('ticketTableBody');
        const rows = tableBody.getElementsByTagName('tr');
        for (let i = 0; i < rows.length; i++) {
            rows[i].style.display = ''; // Muestra todas las filas
        }
        // Oculta el mensaje de "No se ha encontrado ningún elemento"
        document.getElementById('noResultsMessage').style.display = 'none';

        currentPage = 1; // Reiniciar a la primera página
        filteredRows = Array.from(rows); // Almacenar todas las filas como filtradas
        createPagination(); // Crear paginación para todas las filas
        showPage(currentPage); // Mostrar la primera página
    });
    // Función para crear los botones de paginación
    function createPagination() {
        paginationContainer.innerHTML = ''; // Limpiar el contenedor de paginación
        const totalFilteredRows = filteredRows.length; // Número de filas filtradas
        const totalPages = Math.ceil(totalFilteredRows / rowsPerPage); // Calcular total de páginas

        for (let i = 1; i <= totalPages; i++) {
            const button = document.createElement("button");
            button.innerText = i;
            button.classList.add("btn", "btn-secondary", "mx-1");

            // Añadir un event listener para cambiar de página
            button.addEventListener("click", () => {
                currentPage = i;
                showPage(currentPage);
            });

            paginationContainer.appendChild(button);
        }
    }

    // Función para actualizar los botones de paginación visibles
    function updatePaginationButtons() {
        const buttons = document.querySelectorAll("#paginationButtons button");
        buttons.forEach((button) => {
            button.style.display = "none"; // Ocultar todos los botones
        });

        // Determinar el rango de botones a mostrar
        let startButton = Math.max(currentPage - 2, 1); // Muestra dos botones antes
        let endButton = startButton + 4; // Muestra hasta 5 botones en total

        // Ajustar si hay menos de 5 páginas restantes
        if (endButton > Math.ceil(filteredRows.length / rowsPerPage)) {
            endButton = Math.ceil(filteredRows.length / rowsPerPage); // No exceder el total de páginas
            startButton = Math.max(endButton - 4, 1); // Ajustar inicio
        }

        // Mostrar los botones dentro del rango calculado
        for (let i = startButton; i <= endButton; i++) {
            if (buttons[i - 1]) {
                buttons[i - 1].style.display = ""; // Mostrar botón
            }
        }

    }

    // Navegación con flechas del teclado
    document.addEventListener("keydown", (event) => {
        if (event.key === "ArrowRight") {
            if (currentPage < Math.ceil(filteredRows.length / rowsPerPage)) {
                currentPage++;
                showPage(currentPage);
            }
        } else if (event.key === "ArrowLeft") {
            if (currentPage > 1) {
                currentPage--;
                showPage(currentPage);
            }
        }
    });

    // Navegación con botones de flecha
    prevPageButton.addEventListener("click", () => {
        if (currentPage > 1) {
            currentPage--;
            showPage(currentPage);
        }
    });

    nextPageButton.addEventListener("click", () => {
        if (currentPage < Math.ceil(filteredRows.length / rowsPerPage)) {
            currentPage++;
            showPage(currentPage);
        }
    });


    // Llama a esta función después de agregar un nuevo ticket
    function updateFilteredRows() {
        filteredRows = Array.from(document.querySelectorAll("#ticketTableBody tr")); // Actualizar las filas filtradas
        createPagination();
        showPage(currentPage); // Mostrar la página actual después de agregar la nueva fila
    }



    window.filteredRows = filteredRows;
    window.updateFilteredRows = updateFilteredRows;

    // Inicializar la paginación
    filteredRows = Array.from(rows); // Almacenar todas las filas inicialmente
    createPagination();
    showPage(currentPage); // Mostrar la primera página
});