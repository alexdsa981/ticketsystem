const charts = {}; // Almacena los gráficos creados
const configGraficos = {
  sede: {
    tipo: "pie",
    color: "rgba(54, 162, 235, 0.6)",
    borde: "rgba(54, 162, 235, 1)",
    titulo: "Tickets por Sede"
  },
  areaAtencion: {
    tipo: "bar",
    color: "rgba(255, 99, 132, 0.6)",
    borde: "rgba(255, 99, 132, 1)",
    titulo: "Tickets por Área de Atención"
  },
  categoriaIncidencia: {
    tipo: "pie",
    titulo: "Tickets por Categoría"
  },
  subCategoriaIncidencia: {
    tipo: "doughnut",
    titulo: "Tickets por Subcategoría"
  },
  tipoIncidencia: {
    tipo: "bar",
    titulo: "Tickets por Tipo de Incidencia"
  },
  clasificacionUrgencia: {
    tipo: "pie",
    titulo: "Tickets por Urgencia"
  }
};

// 🟢 Función para crear gráficos Chart.js
function crearGrafico(clasificador, etiquetas, valores, config) {
  const ctx = document.getElementById(`grafico-${clasificador}`);
  if (!ctx) {
    console.warn(`⚠️ No se encontró canvas para ${clasificador}`);
    return;
  }

  const tipo = config.tipo || 'bar';
  const titulo = config.titulo || `Gráfico por ${clasificador}`;

  let backgroundColor, borderColor;

  if (config.color && config.borde) {
    backgroundColor = Array.isArray(config.color)
      ? config.color
      : etiquetas.map(() => config.color);
    borderColor = Array.isArray(config.borde)
      ? config.borde
      : etiquetas.map(() => config.borde);
  } else {
    const { colores, bordes } = generarColoresSuaves(etiquetas.length);
    backgroundColor = colores;
    borderColor = bordes;
  }

  // 🔄 Destruir el gráfico anterior si ya existe
  if (charts[clasificador]) {
    charts[clasificador].destroy();
  }

  // Animación de opacidad suave (opcional)
  ctx.style.opacity = 0;
  setTimeout(() => {
    const chart = new Chart(ctx, {
      type: tipo,
      data: {
        labels: etiquetas,
        datasets: [{
          label: titulo,
          data: valores,
          backgroundColor: backgroundColor,
          borderColor: borderColor,
          borderWidth: 1
        }]
      },
      options: {
        maintainAspectRatio: false,
        responsive: true,
        plugins: {
          legend: { display: tipo !== 'bar' },
          title: {
            display: true,
            text: titulo
          }
        },
        scales: tipo === 'bar' ? {
          y: {
            beginAtZero: true,
            ticks: { precision: 0 }
          }
        } : {}
      }
    });

    charts[clasificador] = chart;
    ctx.style.opacity = 1;
  }, 50); // pequeño delay para dar tiempo al DOM
}










// 🎨 Genera colores únicos para gráficos circulares
function generarColoresSuaves(cantidad) {
  const baseColors = [
    { bg: 'rgba(54, 162, 235, 0.4)', border: 'rgba(54, 162, 235, 0.8)' },     // azul
    { bg: 'rgba(255, 99, 132, 0.4)', border: 'rgba(255, 99, 132, 0.8)' },     // rojo
    { bg: 'rgba(255, 206, 86, 0.4)', border: 'rgba(255, 206, 86, 0.8)' },     // amarillo
    { bg: 'rgba(75, 192, 192, 0.4)', border: 'rgba(75, 192, 192, 0.8)' },     // verde agua
    { bg: 'rgba(153, 102, 255, 0.4)', border: 'rgba(153, 102, 255, 0.8)' },   // morado
    { bg: 'rgba(255, 159, 64, 0.4)', border: 'rgba(255, 159, 64, 0.8)' },     // naranja
    { bg: 'rgba(201, 203, 207, 0.4)', border: 'rgba(201, 203, 207, 0.8)' },   // gris
    { bg: 'rgba(100, 181, 246, 0.4)', border: 'rgba(100, 181, 246, 0.8)' },   // celeste
    { bg: 'rgba(174, 213, 129, 0.4)', border: 'rgba(174, 213, 129, 0.8)' },   // verde claro
    { bg: 'rgba(255, 138, 128, 0.4)', border: 'rgba(255, 138, 128, 0.8)' }    // rosado suave
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






























