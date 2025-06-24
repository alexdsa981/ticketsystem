document.addEventListener("DOMContentLoaded", () => {
  const categoriaSelect = document.getElementById("filtro-categoria");
  const subcategoriaSelect = document.getElementById("filtro-subcategoria");
  const tipoIncidenciaSelect = document.getElementById("filtro-tipoincidencia");
  const sedeSelect = document.getElementById("filtro-sede");
  const areaSelect = document.getElementById("filtro-area");

  const grupoSub = document.getElementById("grupo-subcategoria");
  const grupoTipo = document.getElementById("grupo-tipoincidencia");
  const grupoArea = document.getElementById("grupo-area");

  const fechaInicioInput = document.getElementById("filtro-fecha-inicio");
  const fechaFinInput = document.getElementById("filtro-fecha-fin");

  // 🗓️ Establecer fechas por defecto
  const hoy = new Date();
  const anio = hoy.getFullYear();
  const mes = hoy.getMonth();
  const primerDia = new Date(anio, mes, 1);
  const ultimoDia = new Date(anio, mes + 1, 0);

  const formato = (fecha) => fecha.toISOString().split("T")[0];
  fechaInicioInput.value = formato(primerDia);
  fechaFinInput.value = formato(ultimoDia);

  // 🔁 Categoría -> Subcategoría
  categoriaSelect.addEventListener("change", () => {
    const categoriaId = categoriaSelect.value;
    subcategoriaSelect.innerHTML = '<option value="">-- Seleccionar --</option>';
    tipoIncidenciaSelect.innerHTML = '<option value="">-- Seleccionar --</option>';
    grupoTipo.classList.add("d-none");

    if (categoriaId) {
      fetch(`/app/clasificadores/listar/subCatIncidenciaActivos/${categoriaId}`)
        .then(res => res.json())
        .then(data => {
          data.forEach(sub => {
            const option = document.createElement("option");
            option.value = sub.id;
            option.textContent = sub.nombre;
            subcategoriaSelect.appendChild(option);
          });
          grupoSub.classList.remove("d-none");
        })
        .catch(err => {
          console.error("Error al cargar subcategorías:", err);
          grupoSub.classList.add("d-none");
        });
    } else {
      grupoSub.classList.add("d-none");
    }
  });

  // 🔁 Subcategoría -> Tipo de Incidencia
  subcategoriaSelect.addEventListener("change", () => {
    const subcatId = subcategoriaSelect.value;
    tipoIncidenciaSelect.innerHTML = '<option value="">-- Seleccionar --</option>';

    if (subcatId) {
      fetch(`/app/clasificadores/listar/tipoIncidenciaActivos/${subcatId}`)
        .then(res => res.json())
        .then(data => {
          data.forEach(tipo => {
            const option = document.createElement("option");
            option.value = tipo.id;
            option.textContent = tipo.nombre;
            tipoIncidenciaSelect.appendChild(option);
          });
          grupoTipo.classList.remove("d-none");
        })
        .catch(err => {
          console.error("Error al cargar tipos de incidencia:", err);
          grupoTipo.classList.add("d-none");
        });
    } else {
      grupoTipo.classList.add("d-none");
    }
  });

  // 🔁 Sede -> Área
  sedeSelect.addEventListener("change", () => {
    const sedeId = sedeSelect.value;
    areaSelect.innerHTML = '<option value="">-- Seleccionar --</option>';

    if (sedeId) {
      fetch(`/app/clasificadores/area/${sedeId}`)
        .then(res => res.json())
        .then(data => {
          data.forEach(area => {
            const option = document.createElement("option");
            option.value = area.id;
            option.textContent = area.nombre;
            areaSelect.appendChild(option);
          });
          grupoArea.classList.remove("d-none");
        })
        .catch(err => {
          console.error("Error al cargar áreas:", err);
          grupoArea.classList.add("d-none");
        });
    } else {
      grupoArea.classList.add("d-none");
    }
  });
});
