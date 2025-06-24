const charts = {}; // Almacena los gráficos creados
const configGraficos = {
  sede: {
    tipo: "bar",
    titulo: "Tickets por Sede"
  },
  areaAtencion: {
    tipo: "bar",
    titulo: "Tickets por Área (TOP 10)"
  },
  categoriaIncidencia: {
    tipo: "pie",
    titulo: "Tickets por Categoría Incidencia"
  },
  subCategoriaIncidencia: {
    tipo: "bar",
    titulo: "Tickets por Subcategoría Incidencia (TOP 5)"
  },
  tipoIncidencia: {
    tipo: "bar",
    titulo: "Tickets por Tipo de Incidencia (TOP 10)"
  },
  clasificacionUrgencia: {
    tipo: "pie",
    titulo: "Tickets por Urgencia"
  }
};

function crearGrafico(clasificador, etiquetas, valores, config) {
  const ctx = document.getElementById(`grafico-${clasificador}`);
  if (!ctx) return;

  const tipo = config.tipo || 'bar';
  const titulo = config.titulo || `Gráfico por ${clasificador}`;
  const total = config.total || valores.reduce((acc, v) => acc + v, 0);

  const { colores, bordes } = generarColoresSuaves(etiquetas.length);
  const backgroundColor = colores;
  const borderColor = bordes;

  if (charts[clasificador]) {
    charts[clasificador].destroy();
  }

  ctx.style.opacity = 0;
  setTimeout(() => {
    const chart = new Chart(ctx, {
      type: tipo,
      data: {
        labels: etiquetas,
        datasets: [{
          label: titulo,
          data: valores,
          backgroundColor,
          borderColor,
          borderWidth: 1
        }]
      },
      options: {
        maintainAspectRatio: false,
        responsive: true,
        indexAxis: ['areaAtencion', 'tipoIncidencia'].includes(clasificador) ? 'y' : 'x',
        plugins: {
          legend: {
            display: tipo !== 'bar',
            position: 'top'
          },
          title: {
            display: true,
            text: titulo
          },
          tooltip: {
            callbacks: {
              label: function(context) {
                const value = context.raw;
                const porcentaje = total > 0 ? ((value / total) * 100).toFixed(1) : 0;
                return `${context.label}: ${value} (${porcentaje}%)`;
              }
            }
          }
        },
        scales: tipo === 'bar' ? {
          x: { beginAtZero: true, ticks: { precision: 0 } },
          y: { beginAtZero: true, ticks: { precision: 0 } }
        } : {}
      }
    });

    charts[clasificador] = chart;
    ctx.style.opacity = 1;
  }, 50);
}









// 🎨 Genera colores únicos para gráficos circulares
function generarColoresSuaves(cantidad) {
const baseColors = [
  { bg: 'rgba(54, 162, 235, 0.6)', border: 'rgba(54, 162, 235, 1)' },       // azul
  { bg: 'rgba(255, 99, 132, 0.6)', border: 'rgba(255, 99, 132, 1)' },       // rojo
  { bg: 'rgba(255, 206, 86, 0.6)', border: 'rgba(255, 206, 86, 1)' },       // amarillo
  { bg: 'rgba(75, 192, 192, 0.6)', border: 'rgba(75, 192, 192, 1)' },       // verde agua
  { bg: 'rgba(153, 102, 255, 0.6)', border: 'rgba(153, 102, 255, 1)' },     // morado
  { bg: 'rgba(255, 159, 64, 0.6)', border: 'rgba(255, 159, 64, 1)' },       // naranja
  { bg: 'rgba(201, 203, 207, 0.6)', border: 'rgba(201, 203, 207, 1)' },     // gris claro
  { bg: 'rgba(100, 181, 246, 0.6)', border: 'rgba(100, 181, 246, 1)' },     // celeste
  { bg: 'rgba(174, 213, 129, 0.6)', border: 'rgba(174, 213, 129, 1)' },     // verde claro
  { bg: 'rgba(255, 138, 128, 0.6)', border: 'rgba(255, 138, 128, 1)' },     // rosado
  { bg: 'rgba(121, 85, 72, 0.6)', border: 'rgba(121, 85, 72, 1)' },         // marrón
  { bg: 'rgba(0, 150, 136, 0.6)', border: 'rgba(0, 150, 136, 1)' },         // teal
  { bg: 'rgba(63, 81, 181, 0.6)', border: 'rgba(63, 81, 181, 1)' },         // azul fuerte
  { bg: 'rgba(244, 67, 54, 0.6)', border: 'rgba(244, 67, 54, 1)' },         // rojo fuerte
  { bg: 'rgba(255, 235, 59, 0.6)', border: 'rgba(255, 235, 59, 1)' },       // amarillo brillante
  { bg: 'rgba(0, 188, 212, 0.6)', border: 'rgba(0, 188, 212, 1)' },         // cian
  { bg: 'rgba(156, 39, 176, 0.6)', border: 'rgba(156, 39, 176, 1)' },       // púrpura
  { bg: 'rgba(205, 220, 57, 0.6)', border: 'rgba(205, 220, 57, 1)' },       // lima
  { bg: 'rgba(255, 87, 34, 0.6)', border: 'rgba(255, 87, 34, 1)' },         // naranja oscuro
  { bg: 'rgba(96, 125, 139, 0.6)', border: 'rgba(96, 125, 139, 1)' }        // azul grisáceo
];


  const colores = [];
  const bordes = [];

  for (let i = 0; i < cantidad; i++) {
    const color = baseColors[i % baseColors.length];
    colores.push(color.bg);
    bordes.push(color.border);
  }

  return { colores, bordes };
}






























