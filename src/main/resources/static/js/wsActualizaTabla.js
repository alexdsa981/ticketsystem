let ticketTableBody = null;
let noResultsMessage = null;

document.addEventListener("DOMContentLoaded", function () {
    ticketTableBody = document.getElementById('ticketTableBody');
    noResultsMessage = document.getElementById('noResultsMessage');
    updateTicketCount();
});

function mostrarMensajeSiNoHayTickets() {
    if (ticketTableBody && noResultsMessage) {
        const rows = ticketTableBody.getElementsByTagName('tr');
        noResultsMessage.style.display = rows.length === 0 ? '' : 'none';
    }
}

export function EliminarTicketDeTabla(ticketId) {
    const ticketCell = document.getElementById(ticketId);

    if (ticketCell) {
        const row = ticketCell.closest('tr');
        if (row) {
            row.remove();
        } else {
            console.error(`No se encontró una fila para el ticket con ID ${ticketId}`);
        }
    } else {
        console.error(`No se encontró una celda con ID ${ticketId}`);
    }
    mostrarMensajeSiNoHayTickets();
}

function updateTicketCount() {
    if (ticketTableBody) {
        const totalTickets = ticketTableBody.querySelectorAll('tr').length;
        const ticketCount = document.getElementById('ticketCount');
        if (ticketCount) {
            ticketCount.textContent = totalTickets;
        }
    }
    mostrarMensajeSiNoHayTickets();
}

//
//
//
// AGREGA REGISTRO A TABLA RECEPCION SOPORTE
//
//
//
export function ActualizaTablasSoporteRecepcion(ticketRecord) {

    // Obtener el cuerpo de la tabla donde se agregarán las filas
    const ticketTableBody = document.getElementById('ticketTableBody');

    // Crear una nueva fila
    const newRow = document.createElement('tr');

    // Generar las celdas de la fila con los datos del ticket
    newRow.innerHTML = `
        <td></td>

<td id="${ticketRecord.id}" class="ticket-code-cell">
    <a href="/ticket/${ticketRecord.idFormateado}" class="ticket-link">
        ${ticketRecord.idFormateado}
    </a>
</td>

        <td>${ticketRecord.fechaFormateada}</td>
        <td>${ticketRecord.horaFormateada}</td>
        <td>${ticketRecord.nombreUsuario}</td>
        <td>${ticketRecord.descripcion}</td>

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
        // Si la fila tiene la clase "tr-con-componentes", no se toca "fondo-blanco"
        if (!newRow.classList.contains('tr-con-componentes')) {
            cells.forEach(cell => {
                cell.classList.remove('nuevo-ticket');
                cell.classList.add('fondo-blanco'); // Cambiar el fondo de cada celda a blanco
            });
        } else {
            // Si tiene la clase "tr-con-componentes", solo se elimina "nuevo-ticket"
            cells.forEach(cell => {
                cell.classList.remove('nuevo-ticket');
            });
        }
    });

    // Actualiza la lista de filas filtradas
    filteredRows.unshift(newRow); // Añadir la nueva fila al principio del arreglo de filas filtradas

    // Llama a la función para actualizar las filas y la paginación
    updateFilteredRows();
    updateTicketCount();
}





//
//
//
//AGREGA REGISTRO DE RECEPCION A TABLA ATENCION SOPORTE:
//
//
//


export function ActualizaTablaAtencionSoporte(ticketRecord) {

    // Obtener el cuerpo de la tabla donde se agregarán las filas
    const ticketTableBody = document.getElementById('ticketTableBody');

    // Crear una nueva fila
    const newRow = document.createElement('tr');

    // Generar las celdas de la fila con los datos del ticket
    newRow.innerHTML = `
        <td></td> <!-- Este número se actualizará luego -->
        <td id="${ticketRecord.idTicket}" class="ticket-code-cell">
            <a href="/ticket/${ticketRecord.idFormateadoTicket}" class="ticket-link">
                ${ticketRecord.idFormateadoTicket}
            </a>
        </td>
        <td>${ticketRecord.fechaFormateadaRecepcion}</td>
        <td>${ticketRecord.horaFormateadaRecepcion}</td>
        <td>${ticketRecord.nombreUsuarioTicket}</td>
        <td>${ticketRecord.nombreUsuarioRecepcion}</td>
        <td>
            <span>
                ${ticketRecord.descripcionTicket.length > 150
            ? ticketRecord.descripcionTicket.substring(0, 150) + '...'
            : ticketRecord.descripcionTicket}
            </span><br>
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
            <span>
                ${ticketRecord.mensajeRecepcion.length > 150
            ? ticketRecord.mensajeRecepcion.substring(0, 150) + '...'
            : ticketRecord.mensajeRecepcion}
            </span>

        </td>
        <td class="td-accion">
            <button type="button" class="btn btn-success" data-bs-toggle="modal"
                data-bs-target="#atencionModal" data-ticket-id="${ticketRecord.idTicket}"
                data-ticket-usuario="${ticketRecord.nombreUsuarioTicket}"
                data-ticket-descripcion="${ticketRecord.descripcionTicket}"
                data-ticket-fase="${ticketRecord.nombreFaseTicket}"
                data-ticket-fecha="${ticketRecord.fechaFormateadaTicket}"
                data-ticket-hora="${ticketRecord.horaFormateadaTicket}">
                <i class="bi bi-check2-square"></i>
            </button>
            <button type="button" class="btn btn-danger" data-bs-toggle="modal"
                data-bs-target="#desestimacionModal" data-ticket-id="${ticketRecord.idTicket}"
                data-ticket-usuario="${ticketRecord.nombreUsuarioTicket}"
                data-ticket-descripcion="${ticketRecord.descripcionTicket}"
                data-ticket-fase="${ticketRecord.nombreFaseTicket}"
                data-ticket-fecha="${ticketRecord.fechaFormateadaTicket}"
                data-ticket-hora="${ticketRecord.horaFormateadaTicket}">
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
            cell.classList.add('fondo-blanco');
        });
    });

    // Actualiza la lista de filas filtradas
    filteredRows.unshift(newRow);

    // Llama a la función para actualizar las filas y la paginación
    updateFilteredRows();
    updateTicketCount();
}



//
//
//
//AGREGA REGISTRO DE ATENCION AL HISTORIAL-CERRADOS DE SOPORTE:
//
//
//


export function ActualizaTablaSoporteHistorial(ticketRecord) {

    // Obtener el cuerpo de la tabla donde se agregarán las filas
    const ticketTableBody = document.getElementById('ticketTableBody');

    // Crear una nueva fila
    const newRow = document.createElement('tr');

    // Generar las celdas de la fila con los datos del ticket
    newRow.innerHTML = `
        <td></td> <!-- Este número se actualizará luego -->
        <td id="${ticketRecord.idTicket}" class="ticket-code-cell">
            <a href="/ticket/${ticketRecord.idFormateadoTicket}" class="ticket-link">
                ${ticketRecord.idFormateadoTicket}
            </a>
        </td>
        <td>${ticketRecord.fechaFormateadaRecepcion}</td>
        <td>${ticketRecord.horaFormateadaRecepcion}</td>

        <td>${ticketRecord.nombreUsuarioTicket}</td>
        <td>${ticketRecord.nombreUsuarioRecepcion}</td>
        <td>${ticketRecord.nombreUsuarioAtencion}</td>

        <td>
            <span class="nombreTipoIncidencia clasificacion">
                ${ticketRecord.nombreSubCatIncidencia}:
                ${ticketRecord.nombreTipoIncidencia} <br>
            </span>
            <span>
                ${ticketRecord.descripcionTicket.length > 150
            ? ticketRecord.descripcionTicket.substring(0, 150) + '...'
            : ticketRecord.descripcionTicket}
            </span><br>
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
            <span class="nombreTipoIncidencia urgencia">
               Urgencia ${ticketRecord.nombreUrgencia}:<br>
            </span>
            <span>
                ${ticketRecord.mensajeRecepcion.length > 150
            ? ticketRecord.mensajeRecepcion.substring(0, 150) + '...'
            : ticketRecord.mensajeRecepcion}
            </span>
        </td>

                <td>
            <span class="nombreTipoIncidencia clasificacion-atencion">
             Área ${ticketRecord.nombreAreaAtencion} - ${ticketRecord.nombreClasificacionAtencion}:<br>
            </span>
            <span>
                ${ticketRecord.descripcionAtencion.length > 150
            ? ticketRecord.descripcionAtencion.substring(0, 150) + '...'
            : ticketRecord.descripcionAtencion}
            </span>
        </td>

        <td>
            <!-- Botón de recepción que abre el modal -->
            <button type="button" class="btn btn-primary" data-bs-toggle="modal"
                data-bs-target="#atencionModal"
                data-ticket-id="${ticketRecord.idTicket}"
                data-ticket-usuario="${ticketRecord.nombreUsuarioTicket}"
                data-ticket-descripcion="${ticketRecord.descripcionTicket}"
                data-ticket-clasificacion="${ticketRecord.nombreTipoIncidencia}"
                data-ticket-fase="${ticketRecord.nombreFaseTicket}"
                data-ticket-fecha="${ticketRecord.fechaFormateadaTicket}"
                data-ticket-hora="${ticketRecord.horaFormateadaTicket}"
                data-recepcion-usuario="${ticketRecord.nombreUsuarioRecepcion}"
                data-recepcion-mensaje="${ticketRecord.mensajeRecepcion}"
                data-recepcion-clasificacion="${ticketRecord.nombreUrgencia}"
                data-recepcion-fecha="${ticketRecord.fechaFormateadaRecepcion}"
                data-recepcion-hora="${ticketRecord.horaFormateadaRecepcion}"
                data-atencion-usuario="${ticketRecord.nombreUsuarioAtencion}"
                data-atencion-descripcion="${ticketRecord.descripcionAtencion}"
                data-area-clasificacion="${ticketRecord.nombreAreaAtencion}"
                data-atencion-clasificacion="${ticketRecord.nombreClasificacionAtencion}"
                data-atencion-fecha="${ticketRecord.fechaFormateadaAtencion}"
                data-atencion-hora="${ticketRecord.horaFormateadaAtencion}">

                <i class="bi bi-clock-history"></i>
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
            cell.classList.add('fondo-blanco');
        });
    });

    // Actualiza la lista de filas filtradas
    filteredRows.unshift(newRow);

    // Llama a la función para actualizar las filas y la paginación
    updateFilteredRows();
    updateTicketCount();
}


//
//
//
//AGREGA REGISTRO DE ATENCION AL HISTORIAL-DESESTIMADOS DE SOPORTE:
//
//
//


export function ActualizaTablaDesestimacionHistorial(ticketRecord) {

    // Obtener el cuerpo de la tabla donde se agregarán las filas
    const ticketTableBody = document.getElementById('ticketTableBody');

    // Crear una nueva fila
    const newRow = document.createElement('tr');

    // Generar las celdas de la fila con los datos del ticket
    newRow.innerHTML = `
        <td></td> <!-- Este número se actualizará luego -->
        <td id="${ticketRecord.idTicket}" class="ticket-code-cell">
            <a href="/ticket/${ticketRecord.idFormateadoTicket}" class="ticket-link">
                ${ticketRecord.idFormateadoTicket}
            </a>
        </td>
        <td>${ticketRecord.fechaFormateadaDesestimacion}</td>
        <td>${ticketRecord.horaFormateadaDesestimacion}</td>

        <td>${ticketRecord.nombreUsuarioDesestimacion}</td>
        <td>${ticketRecord.nombreUsuarioTicket}</td>

        <td>
            <span>
                ${ticketRecord.descripcionTicket.length > 150
            ? ticketRecord.descripcionTicket.substring(0, 150) + '...'
            : ticketRecord.descripcionTicket}
            </span><br>
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
            <span class="nombreTipoIncidencia urgencia">
                ${ticketRecord.nombreClasificacionDesestimacion}:<br>
            </span>
            <span>
                ${ticketRecord.descripcionDesestimacion.length > 150
            ? ticketRecord.descripcionDesestimacion.substring(0, 150) + '...'
            : ticketRecord.descripcionDesestimacion}
            </span>
            
        </td>


        <td>
            <button type="button" class="btn btn-primary" data-bs-toggle="modal"
                data-bs-target="#desestimacionModal"
                data-ticket-id="${ticketRecord.idTicket}"
                data-ticket-usuario="${ticketRecord.nombreUsuarioTicket}"
                data-ticket-descripcion="${ticketRecord.descripcionTicket}"

                data-ticket-fecha="${ticketRecord.fechaFormateadaTicket}"
                data-ticket-hora="${ticketRecord.horaFormateadaTicket}"

                data-desestimacion-usuario="${ticketRecord.nombreUsuarioDesestimacion}"
                data-desestimacion-mensaje="${ticketRecord.descripcionDesestimacion}"
                data-desestimacion-clasificacion="${ticketRecord.nombreClasificacionDesestimacion}"
                data-desestimacion-fecha="${ticketRecord.fechaFormateadaDesestimacion}"
                data-desestimacion-hora="${ticketRecord.horaFormateadaDesestimacion}">
  

                <i class="bi bi-clock-history"></i>
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
            cell.classList.add('fondo-blanco');
        });
    });

    // Actualiza la lista de filas filtradas
    filteredRows.unshift(newRow);

    // Llama a la función para actualizar las filas y la paginación
    updateFilteredRows();
    updateTicketCount();
}

//
//
//
//AGREGA REGISTRO DE RECEPCION A TABLA RECEPCIONADOS USUARIO:
//
//
//


export function ActualizaTablaUsuarioRecepcionados(ticketRecord) {

    // Obtener el cuerpo de la tabla donde se agregarán las filas
    const ticketTableBody = document.getElementById('ticketTableBody');

    // Crear una nueva fila
    const newRow = document.createElement('tr');

    // Generar las celdas de la fila con los datos del ticket
    newRow.innerHTML = `
        <td></td> <!-- Este número se actualizará luego -->
        <td id="${ticketRecord.idTicket}" class="ticket-code-cell">
            <a href="/ticket/${ticketRecord.idFormateadoTicket}" class="ticket-link">
                ${ticketRecord.idFormateadoTicket}
            </a>
        </td>
        <td>${ticketRecord.fechaFormateadaRecepcion}</td>
        <td>${ticketRecord.horaFormateadaRecepcion}</td>
        <td>
            <span>
                ${ticketRecord.descripcionTicket.length > 150
            ? ticketRecord.descripcionTicket.substring(0, 150) + '...'
            : ticketRecord.descripcionTicket}
            </span><br>
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
            <span>
                ${ticketRecord.mensajeRecepcion.length > 150
            ? ticketRecord.mensajeRecepcion.substring(0, 150) + '...'
            : ticketRecord.mensajeRecepcion}
            </span>
        </td>
        <td>
        ${ticketRecord.nombreFaseTicket}
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

}






//
//
//
//AGREGA REGISTRO DE ATENCION A TABLA ATENDIDOS DE USUARIO:
//
//
//


export function ActualizaTablaUsuarioAtendidos(ticketRecord) {

    // Obtener el cuerpo de la tabla donde se agregarán las filas
    const ticketTableBody = document.getElementById('ticketTableBody');

    // Crear una nueva fila
    const newRow = document.createElement('tr');

    // Generar las celdas de la fila con los datos del ticket
    newRow.innerHTML = `
        <td></td> <!-- Este número se actualizará luego -->
        <td id="${ticketRecord.idTicket}" class="ticket-code-cell">
            <a href="/ticket/${ticketRecord.idFormateadoTicket}" class="ticket-link">
                ${ticketRecord.idFormateadoTicket}
            </a>
        </td>
        <td>${ticketRecord.fechaFormateadaAtencion}</td>
        <td>${ticketRecord.horaFormateadaAtencion}</td>


        <td>
            <span>
                ${ticketRecord.descripcionTicket.length > 150
            ? ticketRecord.descripcionTicket.substring(0, 150) + '...'
            : ticketRecord.descripcionTicket}
            </span><br>
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
            <span>
                ${ticketRecord.descripcionAtencion.length > 150
            ? ticketRecord.descripcionAtencion.substring(0, 150) + '...'
            : ticketRecord.descripcionAtencion}
            </span>
        </td>
        <td>
        Cerrado - Atendido
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
            cell.classList.add('fondo-blanco');
        });
    });

    // Actualiza la lista de filas filtradas
    filteredRows.unshift(newRow);

    // Llama a la función para actualizar las filas y la paginación
    updateFilteredRows();
    updateTicketCount();
}



//
//
//
//AGREGA REGISTRO DE ATENCION A TABLA DESESTIMADO DE USUARIO:
//
//
//


export function ActualizaTablaUsuarioDesestimados(ticketRecord) {

    // Obtener el cuerpo de la tabla donde se agregarán las filas
    const ticketTableBody = document.getElementById('ticketTableBody');

    // Crear una nueva fila
    const newRow = document.createElement('tr');

    // Generar las celdas de la fila con los datos del ticket
    newRow.innerHTML = `
        <td></td> <!-- Este número se actualizará luego -->
        <td id="${ticketRecord.idTicket}" class="ticket-code-cell">
            <a href="/ticket/${ticketRecord.idFormateadoTicket}" class="ticket-link">
                ${ticketRecord.idFormateadoTicket}
            </a>
        </td>
        <td>${ticketRecord.fechaFormateadaDesestimacion}</td>
        <td>${ticketRecord.horaFormateadaDesestimacion}</td>

        <td>
            <span>
                ${ticketRecord.descripcionTicket.length > 150
            ? ticketRecord.descripcionTicket.substring(0, 150) + '...'
            : ticketRecord.descripcionTicket}
            </span><br>
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
            <span class="nombreTipoIncidencia clasificacion-atencion">
                ${ticketRecord.nombreClasificacionDesestimacion}:<br>
            </span>
            <span>
                ${ticketRecord.descripcionDesestimacion.length > 150
            ? ticketRecord.descripcionDesestimacion.substring(0, 150) + '...'
            : ticketRecord.descripcionDesestimacion}
            </span>
        </td>
        <td>
        Desestimado
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
            cell.classList.add('fondo-blanco');
        });
    });

    // Actualiza la lista de filas filtradas
    filteredRows.unshift(newRow);

    // Llama a la función para actualizar las filas y la paginación
    updateFilteredRows();
    updateTicketCount();
}



//
//
//
// AGREGA REGISTRO A TABLA USUARIO ENVIADOS
//
//
//
export function ActualizaTablaUsuarioEnviados(ticketRecord) {

    // Obtener el cuerpo de la tabla donde se agregarán las filas
    const ticketTableBody = document.getElementById('ticketTableBody');

    // Crear una nueva fila
    const newRow = document.createElement('tr');

    // Generar las celdas de la fila con los datos del ticket
    newRow.innerHTML = `
        <td></td> <!-- Este número se actualizará luego -->

<td id="${ticketRecord.id}" class="ticket-code-cell">
    <a href="/ticket/${ticketRecord.idFormateado}" class="ticket-link">
        ${ticketRecord.idFormateado}
    </a>
</td>
        <td>${ticketRecord.fechaFormateada}</td>
        <td>${ticketRecord.horaFormateada}</td>
        <td>
            <!-- Descripción truncada -->
            <span>${ticketRecord.descripcion.length > 150
            ? ticketRecord.descripcion.substring(0, 150) + '...'
            : ticketRecord.descripcion}</span><br>
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
        ${ticketRecord.nombreFaseTicket}
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
        // Si la fila tiene la clase "tr-con-componentes", no se toca "fondo-blanco"
        if (!newRow.classList.contains('tr-con-componentes')) {
            cells.forEach(cell => {
                cell.classList.remove('nuevo-ticket');
                cell.classList.add('fondo-blanco'); // Cambiar el fondo de cada celda a blanco
            });
        } else {
            // Si tiene la clase "tr-con-componentes", solo se elimina "nuevo-ticket"
            cells.forEach(cell => {
                cell.classList.remove('nuevo-ticket');
            });
        }
    });

    // Actualiza la lista de filas filtradas
    filteredRows.unshift(newRow); // Añadir la nueva fila al principio del arreglo de filas filtradas

    // Llama a la función para actualizar las filas y la paginación
    updateFilteredRows();
    updateTicketCount();


}

