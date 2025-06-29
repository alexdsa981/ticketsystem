// ==========================
// VARIABLES GLOBALES
// ==========================
const graficos = {}; // Gráficos de clasificadores
let datos = [0, 0, 0, 0]; // Creados, Recepcionados, Atendidos, Desestimados
let graficoRegistros = null;
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
       actualizarDashboardCompleto(fechaInicioGlobal, fechaFinGlobal);
    });
  },
});

// ==========================
// GRÁFICO DE REGISTROS (BARRAS)
// ==========================
function actualizarGraficoREGISTROS() {
  const ctx = document.getElementById("graficoTicketsxFase").getContext("2d");
  const etiquetas = ["Creados", "Recepcionados", "Atendidos", "Desestimados"];

  if (graficoRegistros) {
    graficoRegistros.data.datasets[0].data = datos;
    graficoRegistros.update();
  } else {
    graficoRegistros = new Chart(ctx, {
      type: "bar",
      data: {
        labels: etiquetas,
        datasets: [{
          label: "Tickets",
          data: datos,
        backgroundColor: [
          "rgba(230, 248, 252, 1)",
          "rgba(228, 247, 233, 1)",
          "rgba(255, 250, 222, 1)",
          "rgba(252, 231, 233, 1)"
        ],
        borderColor: "rgba(60, 72, 88, 1)",
          borderWidth: 1,
        }],
      },
      options: {
        responsive: false,
        maintainAspectRatio: false,
        scales: {
          y: {
            beginAtZero: true,
            ticks: {
              stepSize: 1,
              callback: value => Number.isInteger(value) ? value : null,
            },
          },
        },
      },
    });
  }
}

// ==========================
// CLASIFICADORES (GRÁFICO + TABLA)
// ==========================
function inicializarGraficosDeClasificadores(idCanvas, idTabla, endpoint, titulo) {
  const canvas = document.getElementById(idCanvas);
  const tablaBody = document.getElementById(idTabla);

  if (!canvas || !tablaBody) {
    console.error(`Error: No existe '${idCanvas}' o '${idTabla}'`);
    return;
  }

  async function actualizarDashboard() {
    const urlConFechas = (fechaInicioGlobal && fechaFinGlobal)
      ? `${endpoint}?fechaInicio=${fechaInicioGlobal}&fechaFin=${fechaFinGlobal}`
      : endpoint;

    try {
      const res = await fetch(urlConFechas);
      const data = await res.json();
      const etiquetas = data.etiquetas || [];
      const datos = data.datos || [];

      actualizarGrafico(etiquetas, datos);
      actualizarTabla(etiquetas, datos);
    } catch (err) {
      console.error(`Error al cargar datos para ${idCanvas}:`, err);
    }
  }

function actualizarGrafico(etiquetas, datos) {
  const ctx = canvas.getContext("2d");

  if (graficos[idCanvas]) {
    graficos[idCanvas].data.labels = etiquetas;
    graficos[idCanvas].data.datasets[0].data = datos;
    graficos[idCanvas].update();
  } else {
    graficos[idCanvas] = new Chart(ctx, {
      type: "bar",
      data: {
        labels: etiquetas,
        datasets: [{
          label: titulo,
          data: datos,
          backgroundColor: [
            "rgba(169, 169, 169, 0.2)",
            "rgba(135, 206, 235, 0.2)",
            "rgba(75, 192, 192, 0.2)",
            "rgba(255, 206, 86, 0.2)",
            "rgba(153, 102, 255, 0.2)",
            "rgba(255, 159, 64, 0.2)",
          ],
          borderColor: [
            "rgba(169, 169, 169, 1)",
            "rgba(135, 206, 235, 1)",
            "rgba(75, 192, 192, 1)",
            "rgba(255, 206, 86, 1)",
            "rgba(153, 102, 255, 1)",
            "rgba(255, 159, 64, 1)",
          ],
          borderWidth: 1,
        }],
      },
      options: {
        indexAxis: 'y',
        responsive: true,
        maintainAspectRatio: false,
        scales: {
          y: {
            ticks: {
              display: false // oculta nombres del eje Y
            }
          }
        },
        plugins: {
          legend: { position: "top" },
          title: { display: false, text: titulo },
        },
      },
    });
  }
}


  function actualizarTabla(etiquetas, datos) {
    tablaBody.innerHTML = "";
    const total = datos.reduce((acc, val) => acc + val, 0);

    etiquetas.forEach((etiqueta, i) => {
      const porcentaje = total > 0 ? ((datos[i] / total) * 100).toFixed(2) : 0;
      const row = document.createElement("tr");
      row.innerHTML = `
        <td>${etiqueta}</td>
        <td>${datos[i]}</td>
        <td>${porcentaje}%</td>`;
      tablaBody.appendChild(row);
    });
  }

  actualizarDashboard();
}

// ==========================
// ACTUALIZA TODO EL DASHBOARD
// ==========================
 async function actualizarDashboardCompleto(fechaInicio = "", fechaFin = "") {
  const queryParams = (fechaInicio && fechaFin) ? `?fechaInicio=${fechaInicio}&fechaFin=${fechaFin}` : "";

  try {
    // Contadores
    const [total, recepcionados, atendidos, desestimados] = await Promise.all([
      fetch(`/app/dashboard/contador/total${queryParams}`).then(r => r.json()),
      fetch(`/app/dashboard/contador/recepcionados${queryParams}`).then(r => r.json()),
      fetch(`/app/dashboard/contador/atendidos${queryParams}`).then(r => r.json()),
      fetch(`/app/dashboard/contador/desestimados${queryParams}`).then(r => r.json()),
    ]);

    document.getElementById("ntotalTickets").textContent = total;
    document.getElementById("ntotalRecepcionados").textContent = recepcionados;
    document.getElementById("ntotalAtendidos").textContent = atendidos;
    document.getElementById("ntotalDesestimados").textContent = desestimados;

    datos = [total, recepcionados, atendidos, desestimados];
    actualizarGraficoREGISTROS();

    // Clasificadores
    const clasificadores = [
      { idCanvas: "graficoIncidencia", idTabla: "tablaIncidencia", endpoint: "/app/dashboard/grafico/TicketsporIncidencia", titulo: "Incidencia" },
      { idCanvas: "graficoSubcategoria", idTabla: "tablaSubcategoria", endpoint: "/app/dashboard/grafico/TicketsporSubcategoria", titulo: "Subcategoría" },
      { idCanvas: "graficoCategoria", idTabla: "tablaCategoria", endpoint: "/app/dashboard/grafico/TicketsporCategoria", titulo: "Categoría" },
      { idCanvas: "graficoUrgencia", idTabla: "tablaUrgencia", endpoint: "/app/dashboard/grafico/TicketsporUrgencia", titulo: "Urgencia" },
      { idCanvas: "graficoArea", idTabla: "tablaArea", endpoint: "/app/dashboard/grafico/TicketsporArea", titulo: "Área" },
      { idCanvas: "graficoSede", idTabla: "tablaSede", endpoint: "/app/dashboard/grafico/TicketsporSede", titulo: "Sede" }
    ];


    clasificadores.forEach(({ idCanvas, idTabla, endpoint, titulo }) =>
      inicializarGraficosDeClasificadores(idCanvas, idTabla, endpoint, titulo)
    );

    // Promedios
    const promedios = [
      { url: "/app/dashboard/promedioRecepcion", id: "promedioRecepcion", msg: "Promedio de Recepción: {data} minutos. Creación hasta Recepción." },
      { url: "/app/dashboard/promedioAtencion", id: "promedioAtencion", msg: "Promedio de Atención: {data} minutos. Recepción hasta Atención/Cierre." },
      { url: "/app/dashboard/promedioInicioFin", id: "promedioInicioFin", msg: "Promedio General: {data} minutos. Creación hasta Atención/Cierre." }
    ];

    for (const { url, id, msg } of promedios) {
      const data = await fetch(`${url}${queryParams}`).then(r => r.text());
      document.getElementById(id).innerText = msg.replace("{data}", data);
    }

// Tiempo efectivo por usuario
    await cargarTiempoEfectivoUsuarios(queryParams);



    const tituloDashboard = document.getElementById("tituloDashboard");

    if (fechaInicio && fechaFin) {
    const opciones = { day: 'numeric', month: 'long', year: 'numeric' };

    // Convertimos las fechas de cadena a objetos Date, asegurándonos de que no haya alteraciones.
    const inicio = new Date(fechaInicio + 'T00:00:00'); // Forzamos el inicio del día en UTC
    const fin = new Date(fechaFin + 'T23:59:59'); // Forzamos el fin del día en UTC

    // Ahora aplicamos el formato
    const inicioFormateado = inicio.toLocaleDateString('es-ES', opciones);
    const finFormateado = fin.toLocaleDateString('es-ES', opciones);

    //console.log("Fecha Inicio:", fechaInicio);
    //console.log("Fecha Fin:", fechaFin);
    //console.log("Fecha Inicio:", inicioFormateado);
    //console.log("Fecha Fin:", finFormateado);

    tituloDashboard.textContent = `ADMIN: Dashboard (${inicioFormateado} - ${finFormateado})`;

    } else {
      tituloDashboard.textContent = "ADMIN: Dashboard (Todos los registros)";
    }


  } catch (error) {
    console.error("Error al actualizar el dashboard:", error);
  }

}



// ==========================
// INICIALIZAR DASHBOARD
// ==========================

function inicializarDashboard() {
    // Asignar fechas actuales al filtro
    const hoy = new Date();
    const primerDiaMes = new Date(hoy.getFullYear(), hoy.getMonth(), 1);
    const ultimoDiaMes = new Date(hoy.getFullYear(), hoy.getMonth() + 1, 0);

    fechaInicioGlobal = formatearFechaLocal(primerDiaMes);
    fechaFinGlobal = formatearFechaLocal(ultimoDiaMes);

    document.getElementById("fechaInicio").value = fechaInicioGlobal;
    document.getElementById("fechaFin").value = fechaFinGlobal;

    // Cargar datos iniciales del dashboard
    actualizarDashboardCompleto(fechaInicioGlobal, fechaFinGlobal);

    // Inicializar gráficos por clasificadores
    inicializarGraficosDeClasificadores(
        "graficoIncidencia", "tablaIncidencia",
        "/app/dashboard/grafico/TicketsporIncidencia",
        "Tickets por Tipo de Incidencia"
    );

    inicializarGraficosDeClasificadores(
        "graficoSubcategoria", "tablaSubcategoria",
        "/app/dashboard/grafico/TicketsporSubcategoria",
        "Tickets por Subcategoría de Incidencia"
    );

    inicializarGraficosDeClasificadores(
        "graficoCategoria", "tablaCategoria",
        "/app/dashboard/grafico/TicketsporCategoria",
        "Tickets por Categoría de Incidencia"
    );

    inicializarGraficosDeClasificadores(
        "graficoUrgencia", "tablaUrgencia",
        "/app/dashboard/grafico/TicketsporUrgencia",
        "Tickets por Nivel de Urgencia"
    );

    inicializarGraficosDeClasificadores(
        "graficoArea", "tablaArea",
        "/app/dashboard/grafico/TicketsporArea",
        "Tickets por Área Responsable"
    );

    inicializarGraficosDeClasificadores(
        "graficoSede", "tablaSede",
        "/app/dashboard/grafico/TicketsporSede",
        "Tickets por Sede"
    );
}

  document.addEventListener("DOMContentLoaded", inicializarDashboard);


function formatearFechaLocal(date) {
  return date.getFullYear() + '-' +
         String(date.getMonth() + 1).padStart(2, '0') + '-' +
         String(date.getDate()).padStart(2, '0');
}











async function cargarTiempoEfectivoUsuarios(queryParams = "") {
  try {
    const res = await fetch(`/app/dashboard/tiempo-efectivo-usuario${queryParams}`);
    const data = await res.json();

    const tbody = document.getElementById('tablaTiempoPorUsuario');
    tbody.innerHTML = '';

    if (!data || data.length === 0) {
      const fila = document.createElement('tr');
      fila.innerHTML = `<td colspan="4" class="text-muted">No hay datos disponibles.</td>`;
      tbody.appendChild(fila);
      return;
    }
        //<td>${usuario.rol}</td>

    data.forEach(usuario => {
      const fila = document.createElement('tr');
      fila.innerHTML = `
        <td>${usuario.usuario}</td>
        <td>${usuario.minutosEfectivos} min</td>
        <td>${usuario.cantidadTickets}</td> <!-- Nueva celda -->
      `;
      tbody.appendChild(fila);
    });

  } catch (err) {
    console.error('Error al cargar tiempos efectivos por usuario:', err);
  }
}












let stompClient = null;
let socket = null;
let reconnectInterval = 5000; // 5 segundos
let wasDisconnected = false;

function conectarDashboardWebSocket() {
    socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.debug = () => {}; // Desactiva logs del STOMP

    stompClient.connect({}, () => {
        if (wasDisconnected) {
            console.log('Reconectado. Actualizando dashboard...');
            actualizarDashboardCompleto(fechaInicioGlobal, fechaFinGlobal);
            wasDisconnected = false;
        }

        stompClient.subscribe('/topic/dashboard', function (mensaje) {
            if (mensaje.body === 'actualizar') {
                actualizarDashboardCompleto(fechaInicioGlobal, fechaFinGlobal);
            }
        });
    }, (error) => {
        if (!wasDisconnected) {
            console.warn('WebSocket del dashboard desconectado. Reintentando en 5 segundos...');
            wasDisconnected = true;
        }

        setTimeout(() => {
            conectarDashboardWebSocket(); // Reintenta conexión
        }, reconnectInterval);
    });
}

document.addEventListener('DOMContentLoaded', () => {
    conectarDashboardWebSocket();
});
