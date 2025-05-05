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
    });
  },
});