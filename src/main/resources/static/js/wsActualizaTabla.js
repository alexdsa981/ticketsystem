document.addEventListener("DOMContentLoaded", function () {
    updateTicketCount();
});

function updateTicketCount() {
    const ticketTableBody = document.getElementById('ticketTableBody');
    const ticketCount = document.getElementById('ticketCount');
    if (ticketTableBody) {
        const totalTickets = ticketTableBody.querySelectorAll('tr').length;
        if (ticketCount) {
            ticketCount.textContent = totalTickets;
        }

    }

}


export function AumentarContadorNotificacion(){

}


// Función para manejar el ticket recibido
export function ActualizaTablaRecibidos(ticketRecord) {
    console.log('Ticket recibido en wsActualizaTabla.js con ws:', ticketRecord);

    // Obtener el cuerpo de la tabla donde se agregarán las filas
    const ticketTableBody = document.getElementById('ticketTableBody');

    // Crear una nueva fila
    const newRow = document.createElement('tr');

    // Generar las celdas de la fila con los datos del ticket
    newRow.innerHTML = `
        <td></td> <!-- Este número se actualizará luego -->

        <td>${ticketRecord.idFormateado}</td>

        <td>${ticketRecord.fechaFormateada}</td>
        <td>${ticketRecord.horaFormateada}</td>
        <td>${ticketRecord.nombreUsuario}</td>
        <td>
            <span>${ticketRecord.descripcion}</span>
        </td>
        <td>
            <ul>
                ${ticketRecord.listaArchivosAdjuntos.map(adjunto => `
                    <li>
                        <a href="/app/tickets/adjunto/descargar/${adjunto.id}">${adjunto.nombre}</a> -
                        ${adjunto.pesoContenido}
                    </li>
                `).join('')}
            </ul>
        </td>
        <td>
            <button type="button" class="btn btn-primary"
                    data-bs-toggle="modal" data-bs-target="#recepcionModal"
                    data-ticket-id="${ticketRecord.id}"
                    data-ticket-usuario="${ticketRecord.nombreUsuario}"
                    data-ticket-descripcion="${ticketRecord.descripcion}"
                    data-ticket-fase="${ticketRecord.nombreFaseTicket}"
                    data-ticket-fecha="${ticketRecord.fechaFormateada}"
                    data-ticket-hora="${ticketRecord.horaFormateada}">
                    <i class="bi bi-pencil-square"></i>
            </button>

            <button type="button" class="btn btn-secondary"
                    data-ticket-id="${ticketRecord.id}"
                    onclick="confirmRedirect(this)">
                <i class="bi bi-send"></i>
            </button>

            <button type="button" class="btn btn-danger" data-bs-toggle="modal"
                    data-bs-target="#desestimacionModal" data-ticket-id="${ticketRecord.id}"
                    data-ticket-usuario="${ticketRecord.nombreUsuario}"
                    data-ticket-descripcion="${ticketRecord.descripcion}"
                    data-ticket-fase="${ticketRecord.nombreFaseTicket}"
                    data-ticket-fecha="${ticketRecord.fechaFormateada}"
                    data-ticket-hora="${ticketRecord.horaFormateada}">
                <i class="bi bi-trash"></i>
            </button>

        </td>
    `;

    // Insertar la nueva fila al inicio de la tabla
    ticketTableBody.insertBefore(newRow, ticketTableBody.firstChild);

    // Actualizar el número de cada fila
    const rows = ticketTableBody.querySelectorAll('tr');
    rows.forEach((row, index) => {
        row.querySelector('td').textContent = index + 1;
    });

    // Añadir clase de fondo verde suave a cada celda (td) de la nueva fila
    const cells = newRow.querySelectorAll('td');
    cells.forEach(cell => {
        cell.classList.add('nuevo-ticket');
    });

    // Añadir evento para cambiar el fondo de cada celda a blanco de manera irreversible
    newRow.addEventListener('mouseover', function () {
        cells.forEach(cell => {
            cell.classList.remove('nuevo-ticket');
            cell.classList.add('fondo-blanco'); // Cambiar el fondo de cada celda a blanco
        });
    });

    // Actualiza la lista de filas filtradas
    filteredRows.unshift(newRow); // Añadir la nueva fila al principio del arreglo de filas filtradas

    // Llama a la función para actualizar las filas y la paginación
    updateFilteredRows();
    updateTicketCount();
}
