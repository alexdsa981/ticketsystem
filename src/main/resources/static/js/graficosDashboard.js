// Objeto para almacenar los gráficos creados
const graficos = {};

// Variables globales para almacenar las fechas del filtro
let fechaInicioGlobal = null;
let fechaFinGlobal = null;

// Función para inicializar un gráfico y su tabla asociada
function inicializarGrafico(idCanvas, idTabla, endpoint, titulo) {
    const canvas = document.getElementById(idCanvas);
    const tablaBody = document.getElementById(idTabla);

    // Verifica si los elementos existen en la página
    if (!canvas || !tablaBody) {
        console.error(`Error: El canvas o la tabla con ID '${idCanvas}' o '${idTabla}' no existen.`);
        return;
    }

    // Función para actualizar la tabla
    function actualizarTabla(etiquetas, datos) {
        tablaBody.innerHTML = '';
        const totalTickets = datos.reduce((acc, count) => acc + count, 0);

        etiquetas.forEach((etiqueta, index) => {
            const conteo = datos[index];
            const porcentaje = totalTickets > 0 ? ((conteo / totalTickets) * 100).toFixed(2) : 0;

            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${etiqueta}</td>
                <td>${conteo}</td>
                <td>${porcentaje}%</td>
            `;
            tablaBody.appendChild(row);
        });
    }

    // Función para crear o actualizar el gráfico
    function crearOActualizarGrafico(etiquetas, datos) {
        const ctx = canvas.getContext('2d');

        if (graficos[idCanvas]) {
            // Actualiza los datos y etiquetas del gráfico existente
            graficos[idCanvas].data.labels = etiquetas;
            graficos[idCanvas].data.datasets[0].data = datos;
            graficos[idCanvas].update(); // Actualiza el gráfico en pantalla
        } else {
            // Crea un nuevo gráfico si aún no existe
            graficos[idCanvas] = new Chart(ctx, {
                type: 'pie',
                data: {
                    labels: etiquetas,
                    datasets: [{
                        label: titulo,
                        data: datos,
                        backgroundColor: [
                            'rgba(169, 169, 169, 0.2)', 'rgba(135, 206, 235, 0.2)',
                            'rgba(75, 192, 192, 0.2)', 'rgba(255, 206, 86, 0.2)',
                            'rgba(153, 102, 255, 0.2)', 'rgba(255, 159, 64, 0.2)'
                        ],
                        borderColor: [
                            'rgba(169, 169, 169, 1)', 'rgba(135, 206, 235, 1)',
                            'rgba(75, 192, 192, 1)', 'rgba(255, 206, 86, 1)',
                            'rgba(153, 102, 255, 1)', 'rgba(255, 159, 64, 1)'
                        ],
                        borderWidth: 1
                    }]
                },
                options: {
                    responsive: false,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: { position: 'top' },
                        title: { display: true, text: titulo }
                    }
                }
            });
        }
    }

    // Función para obtener los datos del servidor y actualizar el gráfico y la tabla
    function actualizarDatos() {
        // Si hay fechas filtradas, las usamos; si no, usamos las fechas por defecto
        const urlConFiltros = fechaInicioGlobal && fechaFinGlobal
            ? `${endpoint}?fechaInicio=${fechaInicioGlobal}&fechaFin=${fechaFinGlobal}`
            : endpoint;

        fetch(urlConFiltros)
            .then(response => response.json())
            .then(data => {
                const etiquetas = data.etiquetas || [];
                const datos = data.datos || [];

                crearOActualizarGrafico(etiquetas, datos);
                actualizarTabla(etiquetas, datos);
            })
            .catch(error => console.error(`Error al obtener los datos para '${idCanvas}':`, error));
    }

    // Llamada inicial y actualización periódica cada 10 segundos
    actualizarDatos();
    setInterval(actualizarDatos, 10000);
}

// Función para actualizar un gráfico con un filtro de fechas
async function actualizarDashboard(fechaInicio, fechaFin) {
    // Guardamos las fechas del filtro en las variables globales
    fechaInicioGlobal = fechaInicio;
    fechaFinGlobal = fechaFin;

    const urls = [
        '/app/dashboard/grafico/TicketsporIncidencia',
        '/app/dashboard/grafico/TicketsporUrgencia',
        '/app/dashboard/grafico/TicketsporArea'

    ];

    for (const url of urls) {
        const response = await fetch(`${url}?fechaInicio=${fechaInicio}&fechaFin=${fechaFin}`);
        const datos = await response.json();
        await actualizarGrafico(url, datos);
    }
}

// Función para actualizar el gráfico con nuevos datos
function actualizarGrafico(url, datos) {
    const canvasId = url.split('/').pop(); // Usamos el último segmento de la URL para obtener el ID del gráfico
    const etiquetas = datos.etiquetas || [];
    const datosConteo = datos.datos || [];

    const canvas = document.getElementById(canvasId);
    const tablaBody = document.getElementById(`${canvasId}Tabla`);

    if (!canvas || !tablaBody) {
        console.error(`Error: El canvas o la tabla con ID '${canvasId}' no existen.`);
        return;
    }

    const ctx = canvas.getContext('2d');
    if (graficos[canvasId]) {
        graficos[canvasId].data.labels = etiquetas;
        graficos[canvasId].data.datasets[0].data = datosConteo;
        graficos[canvasId].update();
    } else {
        graficos[canvasId] = new Chart(ctx, {
            type: 'pie',
            data: {
                labels: etiquetas,
                datasets: [{
                    label: 'Tickets',
                    data: datosConteo,
                    backgroundColor: [
                        'rgba(169, 169, 169, 0.2)', 'rgba(135, 206, 235, 0.2)',
                        'rgba(75, 192, 192, 0.2)', 'rgba(255, 206, 86, 0.2)',
                        'rgba(153, 102, 255, 0.2)', 'rgba(255, 159, 64, 0.2)'
                    ],
                    borderColor: [
                        'rgba(169, 169, 169, 1)', 'rgba(135, 206, 235, 1)',
                        'rgba(75, 192, 192, 1)', 'rgba(255, 206, 86, 1)',
                        'rgba(153, 102, 255, 1)', 'rgba(255, 159, 64, 1)'
                    ],
                    borderWidth: 1
                }]
            },
            options: {
                responsive: false,
                maintainAspectRatio: false,
                plugins: {
                    legend: { position: 'top' },
                    title: { display: true, text: 'Gráfico de Tickets' }
                }
            }
        });
    }

    actualizarTabla(canvasId, etiquetas, datosConteo);
}

// Función para actualizar la tabla
function actualizarTabla(canvasId, etiquetas, datos) {
    const tablaBody = document.getElementById(`${canvasId}Tabla`);
    tablaBody.innerHTML = '';
    const totalTickets = datos.reduce((acc, count) => acc + count, 0);

    etiquetas.forEach((etiqueta, index) => {
        const conteo = datos[index];
        const porcentaje = totalTickets > 0 ? ((conteo / totalTickets) * 100).toFixed(2) : 0;

        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${etiqueta}</td>
            <td>${conteo}</td>
            <td>${porcentaje}%</td>
        `;
        tablaBody.appendChild(row);
    });
}

// Inicializar los gráficos
function inicializarDashboard() {
    inicializarGrafico('TicketsporIncidencia', 'TicketsporIncidenciaTabla', '/app/dashboard/grafico/TicketsporIncidencia', 'Tickets por Clasificación de Incidencia');
    inicializarGrafico('TicketsporUrgencia', 'TicketsporUrgenciaTabla', '/app/dashboard/grafico/TicketsporUrgencia', 'Tickets por Urgencia');
    inicializarGrafico('TicketsporArea', 'TicketsporAreaTabla', '/app/dashboard/grafico/TicketsporArea', 'Tickets por Area');

}

// Evento de filtro
//spinner para boton filtrar
    document.getElementById('btnFiltrar').addEventListener('click', async function () {
    const btn = document.getElementById('btnFiltrar');
    const btnTexto = document.getElementById('btnFiltrarTexto');

    const fechaInicio = document.getElementById('fechaInicio').value;
    const fechaFin = document.getElementById('fechaFin').value;

    // Mostrar spinner y desactivar botón
    btn.disabled = true;
    btnTexto.innerHTML = `<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Filtrando...`;

    // Ejecutar el filtrado
    await actualizarDashboard(fechaInicio, fechaFin);

    // Restaurar texto y habilitar botón
    btnTexto.innerHTML = 'Filtrar';
    btn.disabled = false;
});