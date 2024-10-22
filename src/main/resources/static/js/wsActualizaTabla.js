
   // Contador basado en la cantidad de tickets cargados inicialmente
    let ticketCounter = document.querySelectorAll('#ticketTableBody tr').length + 1;

    // Función para manejar el ticket recibido
export function ActualizaTablaRecibidos(ticketRecord) {
        console.log('Ticket recibido:', ticketRecord);

        // Obtener el cuerpo de la tabla donde se agregarán las filas
        const ticketTableBody = document.getElementById('ticketTableBody');

        // Crear una nueva fila
        const newRow = document.createElement('tr');

        // Generar las celdas de la fila con los datos del ticket
        newRow.innerHTML = `
            <td>${ticketCounter++}</td>
            <td>${ticketRecord.fechaFormateada}</td>
            <td>${ticketRecord.horaFormateada}</td>
            <td>${ticketRecord.nombreUsuario}</td>
        <td>
            <span class="nombreClasificacionIncidencia">${ticketRecord.nombreClasificacionIncidencia}:<br></span>
            <span>${ticketRecord.descripcion}</span><br>
        </td>

        <td>
            <ul>
                ${ticketRecord.listaArchivosAdjuntos.map(adjunto => `
                    <li>
                        <a href="/app/tickets/adjunto/descargar/${adjunto.id}">${adjunto.nombre}</a> -
                        ${adjunto.pesoContenido} mb
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
                    data-ticket-clasificacion="${ticketRecord.nombreClasificacionIncidencia}"
                    data-ticket-fase="${ticketRecord.nombreFaseTicket}"
                    data-ticket-fecha="${ticketRecord.fechaFormateada}"
                    data-ticket-hora="${ticketRecord.horaFormateada}">
                Recepción
            </button>
        </td>
        `;

        // Agregar la nueva fila al cuerpo de la tabla
        ticketTableBody.appendChild(newRow);
    }