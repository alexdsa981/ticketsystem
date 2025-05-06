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

    if (idCanvas === 'graficoEstadoActualTickets') {
        if (graficoEstadoActualTickets) {
            graficoEstadoActualTickets.data.labels = etiquetas;
            graficoEstadoActualTickets.data.datasets[0].data = datos;
            graficoEstadoActualTickets.update();
        } else {
            graficoEstadoActualTickets = new Chart(ctx, {
                type: 'doughnut',
                data: {
                    labels: etiquetas,
                    datasets: [{
                        label: label,
                        data: datos,
                        backgroundColor: colores.background,
                        borderColor: colores.border,
                        borderWidth: 1
                    }]
                },
                options: {
                    responsive: false,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {
                            position: 'bottom'
                        },
                        tooltip: {
                            callbacks: {
                                label: function (context) {
                                    const value = context.raw || 0;
                                    return `${context.label}: ${value}`;
                                }
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

            const datos = data.datos.slice(0, 4);

            document.getElementById("nRecibidos").textContent = datos[0] || 0;
            document.getElementById("ntotalRecepcionados").textContent = datos[1] || 0;
            document.getElementById("ntotalAtendidos").textContent = datos[2] || 0;
            document.getElementById("ntotalDesestimados").textContent = datos[3] || 0;

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
                        'rgba(200, 200, 200, 0.6)'
                    ],
                    border: [
                        'rgba(255, 99, 132, 1)',
                        'rgba(255, 206, 86, 1)',
                        'rgba(75, 192, 192, 1)',
                        'rgba(200, 200, 200, 1)'
                    ]
                }
            );

            // Actualizar el título con las fechas seleccionadas o el día actual
 const tituloDashboard = document.getElementById("tituloDashboard");
if (fechaInicio && fechaFin) {
    const opciones = { day: 'numeric', month: 'long', year: 'numeric' };

    const hoy = new Date();
    const hoyStr = formatearFechaLocal(hoy);


    if (fechaInicio === hoyStr && fechaFin === hoyStr) {
        const fechaHoyFormateada = hoy.toLocaleDateString('es-ES', opciones);
        tituloDashboard.textContent = `SOPORTE: Estado Actual (Hoy: ${fechaHoyFormateada})`;
    } else {
        const inicio = new Date(fechaInicio + 'T00:00:00');
        const fin = new Date(fechaFin + 'T23:59:59');
        const inicioFormateado = inicio.toLocaleDateString('es-ES', opciones);
        const finFormateado = fin.toLocaleDateString('es-ES', opciones);
        tituloDashboard.textContent = `Estado: Dashboard (${inicioFormateado} - ${finFormateado})`;
    }
} else {
    const fechaHoy = new Date();
    const opciones = { day: 'numeric', month: 'long', year: 'numeric' };
    const fechaHoyFormateada = fechaHoy.toLocaleDateString('es-ES', opciones);
    tituloDashboard.textContent = `SOPORTE: Estado Actual (Hoy: ${fechaHoyFormateada})`;
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


const socket = new SockJS('/ws');
const stompClient = Stomp.over(socket);

stompClient.connect({}, () => {
    stompClient.subscribe('/topic/dashboard', function (mensaje) {
        if (mensaje.body === 'actualizar') {
            actualizarDatos(fechaInicioGlobal, fechaFinGlobal);
        }
    });
});
