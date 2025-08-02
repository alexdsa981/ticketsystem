function obtenerFiltrosActuales() {
  const fechaInicio = document.getElementById("filtro-fecha-inicio").value;
  const fechaFin = document.getElementById("filtro-fecha-fin").value;
  const idSede = document.getElementById("filtro-sede").value;
  const idArea = document.getElementById("filtro-area").value;
  const idCategoria = document.getElementById("filtro-categoria").value;
  const idSubcategoria = document.getElementById("filtro-subcategoria").value;
  const idTipoIncidencia = document.getElementById("filtro-tipoincidencia").value;
  const idTipoUrgencia = document.getElementById("filtro-tipo-urgencia").value;

  const filtros = {};

  if (fechaInicio) filtros.fechaInicio = fechaInicio;
  if (fechaFin) filtros.fechaFin = fechaFin;
  if (idSede) filtros.idSede = idSede;
  if (idArea) filtros.idArea = idArea;
  if (idCategoria) filtros.idCategoria = idCategoria;
  if (idSubcategoria) filtros.idSubcategoria = idSubcategoria;
  if (idTipoIncidencia) filtros.idTipoIncidencia = idTipoIncidencia;
  if (idTipoUrgencia) filtros.idTipoUrgencia = idTipoUrgencia;

  return filtros;
}



document.getElementById("btn-aplicar-filtros").addEventListener("click", () => {
  const filtros = obtenerFiltrosActuales();
  console.log("📦 Filtros aplicados manualmente:", filtros);
  fetchDatosDashboard(filtros);
});







function fetchDatosDashboard(filtros = {}) {
  const clasificadores = Object.keys(configGraficos);

  const fechaInicio = filtros.fechaInicio || (() => {
    const hoy = new Date();
    return `${hoy.getFullYear()}-${String(hoy.getMonth() + 1).padStart(2, "0")}-01`;
  })();
  const fechaFin = filtros.fechaFin || (() => {
    const hoy = new Date();
    const ultimoDia = new Date(hoy.getFullYear(), hoy.getMonth() + 1, 0).getDate();
    return `${hoy.getFullYear()}-${String(hoy.getMonth() + 1).padStart(2, "0")}-${ultimoDia}`;
  })();

  const limitesPorClasificador = {
    sede: 10,
    areaAtencion: 10,
    subCategoriaIncidencia: 5,
    categoriaIncidencia: 10,
    tipoIncidencia: 10,
    clasificacionUrgencia: 10,
  };

  const promesas = clasificadores.map(clasificador => {
    const url = new URL("/app/dashboard/grafico/tickets", window.location.origin);
    url.searchParams.append("agruparPor", clasificador);
    url.searchParams.append("fechaInicio", fechaInicio);
    url.searchParams.append("fechaFin", fechaFin);

    for (const [key, value] of Object.entries(filtros)) {
      if (value != null && key !== "fechaInicio" && key !== "fechaFin") {
        url.searchParams.append(key, value);
      }
    }

    return fetch(url)
      .then(res => {
        if (!res.ok) throw new Error(`Error en fetch para ${clasificador}`);
        return res.json();
      })
      .then(data => {
        actualizarIndicadorResolucion(data);
        const limite = limitesPorClasificador[clasificador] || 10;
        const etiquetasOriginales = data.etiquetas || [];
        const valoresOriginales = data.datos || [];

        const total = valoresOriginales.reduce((acc, val) => acc + val, 0);
        const etiquetasLimitadas = etiquetasOriginales.slice(0, limite);
        const valoresLimitados = valoresOriginales.slice(0, limite);

        return {
          clasificador,
          etiquetas: etiquetasLimitadas,
          valores: valoresLimitados,
          total: total,
          ids: data.idsFiltrados || [],
        };
      })
      .catch(error => {
        console.error(error);
        return { clasificador, etiquetas: [], valores: [], total: 0 };
      });
  });

  Promise.all(promesas).then(resultados => {
    resultados.forEach(({ clasificador, etiquetas, valores, total }) => {
      crearGrafico(clasificador, etiquetas, valores, {
        ...configGraficos[clasificador],
        total
      });
    });
  });
}


function actualizarIndicadorResolucion(data) {
    const totalCreados = data.total;
    const numResueltos = data.resueltos;
    const porcentajeResueltos = data.porcentajeResueltos.toFixed(1);
    const porcentajeNoResueltos = data.porcentajeNoResueltos.toFixed(1);
    const porcentajeErrorUsuario = data.porcentajeErrorUsuario.toFixed(1);
    const errorUsuario = data.errorUsuario;

    // Totales
    document.getElementById('num-total').textContent = totalCreados;
    document.getElementById('num-atendidos').textContent = numResueltos;
    document.getElementById('num-errorusuario').textContent = errorUsuario;

    // Resueltos
    document.getElementById('porcentaje-resueltos-texto').textContent = `${porcentajeResueltos}%`;

    const barraResueltos = document.getElementById('barra-resueltos');
    barraResueltos.style.width = `${porcentajeResueltos}%`;
    barraResueltos.textContent = `${porcentajeResueltos}%`;

    const barraNoResueltos = document.getElementById('barra-no-resueltos');
    barraNoResueltos.style.width = `${porcentajeNoResueltos}%`;
    barraNoResueltos.querySelector('span').textContent = `${porcentajeNoResueltos}%`;

    // Error de usuario
    document.getElementById('porcentaje-error-usuario-texto').textContent = `${porcentajeErrorUsuario}%`;

    const barraErrorUsuario = document.getElementById('barra-error-usuario');
    barraErrorUsuario.style.width = `${porcentajeErrorUsuario}%`;
    barraErrorUsuario.textContent = `${porcentajeErrorUsuario}%`;

    const barraNoErrorUsuario = document.getElementById('barra-no-error-usuario');
    const restanteErrorUsuario = (100 - porcentajeErrorUsuario).toFixed(1);
    barraNoErrorUsuario.style.width = `${restanteErrorUsuario}%`;
    barraNoErrorUsuario.querySelector('span').textContent = `${restanteErrorUsuario}%`;
}






document.getElementById("btn-exportar").addEventListener("click", () => {
  const btn = document.getElementById("btn-exportar");
  const spinner = document.getElementById("spinner-exportar");
  const texto = document.getElementById("texto-exportar");

  // Bloquear y mostrar spinner
  btn.disabled = true;
  spinner.classList.remove("d-none");
  texto.classList.add("d-none");

  const filtros = obtenerFiltrosActuales();

  const fechaInicio = filtros.fechaInicio || (() => {
    const hoy = new Date();
    return `${hoy.getFullYear()}-${String(hoy.getMonth() + 1).padStart(2, "0")}-01`;
  })();
  const fechaFin = filtros.fechaFin || (() => {
    const hoy = new Date();
    const ultimoDia = new Date(hoy.getFullYear(), hoy.getMonth() + 1, 0).getDate();
    return `${hoy.getFullYear()}-${String(hoy.getMonth() + 1).padStart(2, "0")}-${ultimoDia}`;
  })();

  const url = new URL("/app/dashboard/exporta/tickets/filtrados", window.location.origin);
  url.searchParams.append("fechaInicio", fechaInicio);
  url.searchParams.append("fechaFin", fechaFin);

  for (const [key, value] of Object.entries(filtros)) {
    if (value != null && key !== "fechaInicio" && key !== "fechaFin") {
      url.searchParams.append(key, value);
    }
  }

  fetch(url)
    .then(res => res.blob())
    .then(blob => {
      const link = document.createElement("a");
      link.href = URL.createObjectURL(blob);
      link.download = "tickets_filtrados.xlsx";
      link.click();
      URL.revokeObjectURL(link.href);
    })
    .catch(err => {
      console.error("❌ Error al exportar tickets:", err);
    })
    .finally(() => {
      // Restaurar botón
      btn.disabled = false;
      spinner.classList.add("d-none");
      texto.classList.remove("d-none");
    });
});
