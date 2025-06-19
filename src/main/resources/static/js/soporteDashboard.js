let fechaInicioGlobal = null;
let fechaFinGlobal = null;

// ==========================
// PICKER DE FECHAS
// ==========================
const picker = new Litepicker({
  element: document.getElementById("rangoFechas"),
  singleMode: false,
  format: "YYYY-MM-DD",
  setup: (picker) => {
    picker.on("selected", (startDate, endDate) => {
      fechaInicioGlobal = startDate.format("YYYY-MM-DD");
      fechaFinGlobal = endDate.format("YYYY-MM-DD");
      document.getElementById("fechaInicio").value = fechaInicioGlobal;
      document.getElementById("fechaFin").value = fechaFinGlobal;

      // Actualizar los datos con las nuevas fechas seleccionadas
      actualizarDatos(fechaInicioGlobal, fechaFinGlobal);
    });
  },
});

// ==========================
// GRAFICO Y CONTADORES DE ESTADO ACTUAL
// ==========================
let graficoEstadoActualTickets = null;

// Función para crear o actualizar el gráfico de dona
function crearGrafico(etiquetas, datos, idCanvas, label, colores) {
    const ctx = document.getElementById(idCanvas).getContext('2d');

    const linksPorSegmento = [
        "/soporte/Recepcionar",
        "/soporte/Atender",
        "/soporte/Atender-Espera",
        "/soporte/Tickets-Cerrados",
        "/soporte/Tickets-Desestimados"
    ];

    const etiquetasConConteo = etiquetas.map((nombre, i) => `${nombre} [${datos[i]}]`);

    if (idCanvas === 'graficoEstadoActualTickets') {
        if (graficoEstadoActualTickets) {
            graficoEstadoActualTickets.data.labels = etiquetasConConteo;
            graficoEstadoActualTickets.data.datasets[0].data = datos;
            graficoEstadoActualTickets.update();
        } else {
            graficoEstadoActualTickets = new Chart(ctx, {
                type: 'doughnut',
                data: {
                    labels: etiquetasConConteo,
                    datasets: [{
                        label: label,
                        data: datos,
                        backgroundColor: colores.background,
                        borderColor: colores.border,
                        borderWidth: 1
                    }]
                },
                options: {
                    maintainAspectRatio: true,
                    plugins: {
                        legend: {
                            position: 'top'
                        },
                        tooltip: {
                            callbacks: {
                                label: function (context) {
                                    const value = context.raw || 0;
                                    return `${context.label}: ${value}`;
                                }
                            }
                        }
                    },
                    onClick: (evt, activeElements) => {
                        if (activeElements.length > 0) {
                            const index = activeElements[0].index;
                            const link = linksPorSegmento[index];
                            if (link) {
                                window.location.href = link;
                            }
                        }
                    }
                }
            });
        }
    }
}



// Función para actualizar los datos en el dashboard
function actualizarDatos(fechaInicio = null, fechaFin = null) {
    let url = '/app/dashboard/grafico/EstadoActual';

    // Agregar parámetros si hay fechas
    if (fechaInicio && fechaFin) {
        url += `?fechaInicio=${fechaInicio}&fechaFin=${fechaFin}`;
    }

    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error("Error en la respuesta del servidor");
            }
            return response.json();
        })
        .then(data => {
            const etiquetas = data.etiquetas.slice(0, 4);
            etiquetas[0] = "Solicitados";
            etiquetas[1] = "Recepcionados";
            etiquetas[2] = "Atendidos";
            etiquetas[3] = "Desestimados";
            etiquetas[4] = "Espera";


            const datos = data.datos.slice(0, 5);


            crearGrafico(
                etiquetas,
                datos,
                'graficoEstadoActualTickets',
                'Estado actual de los tickets',
                {
                    background: [
                        'rgba(255, 99, 132, 0.6)',
                        'rgba(255, 206, 86, 0.6)',
                        'rgba(75, 192, 192, 0.6)',
                        'rgba(200, 200, 200, 0.6)',
                        'rgba(54, 162, 235, 0.6)'

                    ],
                    border: [
                        'rgba(255, 99, 132, 1)',
                        'rgba(255, 206, 86, 1)',
                        'rgba(75, 192, 192, 1)',
                        'rgba(200, 200, 200, 1)',
                        'rgba(54, 162, 235, 1)'
                    ]
                }
            );

             const tituloDashboard = document.getElementById("tituloDashboard");
            if (fechaInicio && fechaFin) {
                const opciones = { day: 'numeric', month: 'long', year: 'numeric' };

                const hoy = new Date();
                const hoyStr = formatearFechaLocal(hoy);


                if (fechaInicio === hoyStr && fechaFin === hoyStr) {
                    const fechaHoyFormateada = hoy.toLocaleDateString('es-ES', opciones);
                    tituloDashboard.textContent = `ESTADO ACTUAL (Hoy: ${fechaHoyFormateada})`;
                } else {
                    const inicio = new Date(fechaInicio + 'T00:00:00');
                    const fin = new Date(fechaFin + 'T23:59:59');
                    const inicioFormateado = inicio.toLocaleDateString('es-ES', opciones);
                    const finFormateado = fin.toLocaleDateString('es-ES', opciones);
                    tituloDashboard.textContent = `ESTADO: (${inicioFormateado} - ${finFormateado})`;
                }
            } else {
                const fechaHoy = new Date();
                const opciones = { day: 'numeric', month: 'long', year: 'numeric' };
                const fechaHoyFormateada = fechaHoy.toLocaleDateString('es-ES', opciones);
                tituloDashboard.textContent = `ESTADO ACTUAL (Hoy: ${fechaHoyFormateada})`;
            }


        })
        .catch(error => console.error('Error al obtener los datos:', error));
}

// Llamar a la función para crear el gráfico al cargar, pasando las fechas predeterminadas (hoy)
const hoyLocal = formatearFechaLocal(new Date());
fechaInicioGlobal = hoyLocal;
fechaFinGlobal = hoyLocal;
actualizarDatos(fechaInicioGlobal, fechaFinGlobal);

function formatearFechaLocal(date) {
  return date.getFullYear() + '-' +
         String(date.getMonth() + 1).padStart(2, '0') + '-' +
         String(date.getDate()).padStart(2, '0');
}

function cargarTicketsRecientes() {
    fetch('/app/dashboard/tickets/recientes')
        .then(response => response.json())
        .then(data => {
            const tbody = document.querySelector('#tablaTicketsRecientes tbody');
            tbody.innerHTML = '';

            if (data.tickets.length === 0) {
                const filaVacia = document.createElement('tr');
                filaVacia.innerHTML = `
                    <td colspan="5" class="text-center text-muted">
                        <i class="bi bi-check-circle me-2 text-success"></i>
                        No hay tickets pendientes. Todos han sido resueltos.
                    </td>
                `;
                tbody.appendChild(filaVacia);
                return;
            }


            data.tickets.forEach(ticket => {
                const fila = document.createElement('tr');

                fila.innerHTML = `
                    <td class="text-nowrap">
                        <a href="/ticket/${ticket.idFormateado}" class="ticket-link" target="_blank" rel="noopener noreferrer">
                            ${ticket.idFormateado}
                        </a>
                    </td>
                    <td>${ticket.fechaFormateada} ${ticket.horaFormateada}</td>
                    <td>${ticket.nombreUsuario}</td>
                    <td>${ticket.descripcion}</td>
                    <td><span class="badge ${claseFase(ticket.nombreFaseTicket)}">${ticket.nombreFaseTicket}</span></td>
                `;

                tbody.appendChild(fila);
            });
        })
        .catch(error => console.error('Error al cargar tickets recientes:', error));
}


function claseFase(fase) {
    switch (fase) {
        case 'Enviado': return 'bg-secondary text-white';
        case 'Recepcionado - En Proceso': return 'bg-warning text-dark';
        case 'En Espera': return 'bg-primary';
        case 'Cerrado - Atendido': return 'bg-success';
        case 'Desestimado': return 'bg-danger';
        default: return 'bg-light text-dark';
    }
}

document.addEventListener('DOMContentLoaded', cargarTicketsRecientes);







let stompClient = null;
let socket = null;
let reconnectInterval = 5000; // 5 segundos
let wasDisconnected = false;

function conectarWebSocketDashboard() {
    socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.debug = () => {}; // Silencia logs

    stompClient.connect({}, () => {
        if (wasDisconnected) {
            console.log('Reconectado al WebSocket del dashboard. Actualizando datos...');
            actualizarDatos(fechaInicioGlobal, fechaFinGlobal);
            cargarTicketsRecientes();


            wasDisconnected = false;
        }

        stompClient.subscribe('/topic/estadoActual', function (mensaje) {
            if (mensaje.body === 'actualizar') {
                actualizarDatos(fechaInicioGlobal, fechaFinGlobal);
                cargarTicketsRecientes();


            }
        });
    }, (error) => {
        if (!wasDisconnected) {
            console.warn('WebSocket del dashboard desconectado. Reintentando en 5 segundos...');
            wasDisconnected = true;
        }

        setTimeout(() => {
            conectarWebSocketDashboard(); // Reintenta conexión
        }, reconnectInterval);
    });
}

document.addEventListener('DOMContentLoaded', () => {
    conectarWebSocketDashboard();
});
