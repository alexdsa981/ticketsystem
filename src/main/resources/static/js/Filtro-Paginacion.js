document.addEventListener('DOMContentLoaded', function() {
    const ticketTableBody = document.getElementById('ticketTableBody');
    const noResultsMessage = document.getElementById('noResultsMessage');

    // Verifica si hay filas en el tbody de la tabla
    if(ticketTableBody){
    const rows = ticketTableBody.getElementsByTagName('tr');
        if(noResultsMessage){
        noResultsMessage.style.display = rows.length === 0 ? '' : 'none';
        }
    }

});



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
        if(prevPageButton && nextPageButton){
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
    }

// Definir la función getValueOrDefault
function getValueOrDefault(elementId) {
    const element = document.getElementById(elementId);
    return element ? element.value.trim().toLowerCase() : '';
}

const filterButton = document.getElementById('filterButton');
// Filtrar registros y actualizar paginación
if (filterButton) {
    filterButton.addEventListener('click', function () {
        // Obtener los valores de los filtros
        const usuarioReceptorFilter = getValueOrDefault('usuarioReceptorFilter');
        const usuarioEmisorFilter = getValueOrDefault('usuarioEmisorFilter');
        const usuarioAtencionFilter = getValueOrDefault('usuarioAtencionFilter');

        const fechaFilter = document.getElementById('fechaFilter') ? document.getElementById('fechaFilter').value : '';
        const formattedFechaFilter = fechaFilter ? fechaFilter.split('-').reverse().join('/') : '';

        const clasificacionFilter = getValueOrDefault('clasificacionFilter');
        const urgenciaFilter = getValueOrDefault('urgenciaFilter');
        const clasificacionServicioFilter = getValueOrDefault('clasificacionServicioFilter');

        filteredRows = []; // Reiniciar el array de filas filtradas
        const tableBody = document.getElementById('ticketTableBody');
        const rows = tableBody.getElementsByTagName('tr');

        for (let i = 0; i < rows.length; i++) {
            const row = rows[i];

            // Selecciona las celdas por clase
            const usuarioReceptorCell = row.querySelector('.usuario-receptor') ? row.querySelector('.usuario-receptor').textContent.toLowerCase() : '';
            const usuarioAtencionCell = row.querySelector('.usuario-atencion') ? row.querySelector('.usuario-atencion').textContent.toLowerCase() : '';
            const usuarioEmisorCell = row.querySelector('.usuario-emisor') ? row.querySelector('.usuario-emisor').textContent.toLowerCase() : '';
            const fechaCell = row.querySelector('.fecha') ? row.querySelector('.fecha').textContent : '';
            const clasificacionCell = row.querySelector('.clasificacion') ? row.querySelector('.clasificacion').textContent.toLowerCase() : '';
            const urgenciaCell = row.querySelector('.urgencia') ? row.querySelector('.urgencia').textContent.toLowerCase() : '';
            const clasificacionServicioCell = row.querySelector('.clasificacion-servicio') ? row.querySelector('.clasificacion-servicio').textContent.toLowerCase() : '';

            // Comparar con los filtros
            const matchesUsuarioReceptor = usuarioReceptorFilter === '' || usuarioReceptorCell.includes(usuarioReceptorFilter);
            const matchesUsuarioEmisor = usuarioEmisorFilter === '' || usuarioEmisorCell.includes(usuarioEmisorFilter);
            const matchesUsuarioAtencion = usuarioAtencionFilter === '' || usuarioAtencionCell.includes(usuarioAtencionFilter);
            const matchesFecha = formattedFechaFilter === '' || fechaCell === formattedFechaFilter;
            const matchesClasificacion = clasificacionFilter === '' || clasificacionCell.includes(clasificacionFilter);
            const matchesUrgencia = urgenciaFilter === '' || urgenciaCell.includes(urgenciaFilter);
            const matchesClasificacionServicio = clasificacionServicioFilter === '' || clasificacionServicioCell.includes(clasificacionServicioFilter);

            // Mostrar u ocultar fila
            if (matchesUsuarioReceptor && matchesUsuarioEmisor && matchesUsuarioAtencion && matchesFecha && matchesClasificacion && matchesUrgencia && matchesClasificacionServicio) {
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
}


const clearFilterButton = document.getElementById('clearFilterButton');
    // Lógica para el botón de eliminar filtrado
    if (clearFilterButton) {
        clearFilterButton.addEventListener('click', function () {
            // Restablece los campos de filtro
            // Comprobar si los elementos existen antes de vaciar sus valores
            if (document.getElementById('usuarioReceptorFilter')) {
                document.getElementById('usuarioReceptorFilter').value = '';
            }
            if (document.getElementById('usuarioEmisorFilter')) {
                document.getElementById('usuarioEmisorFilter').value = '';
            }
            if (document.getElementById('usuarioAtencionFilter')) {
                document.getElementById('usuarioAtencionFilter').value = '';
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
            if (document.getElementById('clasificacionServicioFilter')) {
                document.getElementById('clasificacionServicioFilter').value = '';
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
    }
    // Función para crear los botones de paginación



    function createPagination() {
    if(paginationContainer){
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

    if(prevPageButton){
        // Navegación con botones de flecha
        prevPageButton.addEventListener("click", () => {
            if (currentPage > 1) {
                currentPage--;
                showPage(currentPage);
            }
        });
    }

    if(nextPageButton){
        nextPageButton.addEventListener("click", () => {
            if (currentPage < Math.ceil(filteredRows.length / rowsPerPage)) {
                currentPage++;
                showPage(currentPage);
            }
        });
    }



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