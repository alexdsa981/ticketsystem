        document.addEventListener('DOMContentLoaded', function () {
            var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
            tooltipTriggerList.forEach(function (tooltipTriggerEl) {
                new bootstrap.Tooltip(tooltipTriggerEl, {
                    html: true
                });
            });
        });


        const codigoTicket = document.getElementById('ticketInfo').dataset.codigo;

        function actualizarVistaTicket() {
            fetch(`/app/tickets/datos/${codigoTicket}`)
                .then(response => {
                    if (!response.ok) {
                        throw new Error("No se pudo obtener el ticket");
                    }
                    return response.json();
                })
                .then(data => {
                    //console.log("Datos actualizados del ticket:", data);

                    const fase = data.nombreFaseTicket;
                    const cardRecepcion = document.getElementById("card-recepcion");
                    const cardAtencion = document.getElementById("card-atencion");
                    const cardDesestimacion = document.getElementById("card-desestimacion");

                    cardRecepcion.style.display = "none";
                    cardAtencion.style.display = "none";
                    cardDesestimacion.style.display = "none";
                    document.getElementById("cards-principales").style.display = "none";


                    const btnRecepcionar = document.getElementById('btn-recepcionar');
                    const btnAtender = document.getElementById('btn-atender');
                    const btnEspera = document.getElementById('btn-espera');
                    const btnDesestimar = document.getElementById('btn-desestimar');
                    if (btnRecepcionar) btnRecepcionar.style.display = 'none';
                    if (btnAtender) btnAtender.style.display = 'none';
                    if (btnEspera) btnEspera.style.display = 'none';
                    if (btnDesestimar) btnDesestimar.style.display = 'none';


                    const btnSoporte = document.getElementById('btn-ir-seccion-soporte');
                    const btnUsuario = document.getElementById('btn-ir-seccion-usuario');
                    const rutasSoporte = {
                        'Enviado': '/soporte/Recepcionar',
                        'Recepcionado - En Proceso': '/soporte/Atender',
                        'Cerrado - Atendido': '/soporte/Tickets-Cerrados',
                        'Desestimado': '/soporte/Tickets-Desestimados',
                        'En Espera': '/soporte/Atender-Espera',
                    };

                    const rutasUsuario = {
                        'Enviado': '/inicio',
                        'Recepcionado - En Proceso': '/TicketsEnProceso',
                        'Cerrado - Atendido': '/TicketsAtendidos',
                        'Desestimado': '/TicketsDesestimados',
                        'En Espera': '/TicketsEnEspera',
                    };
                    if (btnSoporte && rutasSoporte[fase]) {
                        btnSoporte.setAttribute('href', rutasSoporte[fase]);
                    }

                    if (btnUsuario && rutasUsuario[fase]) {
                        btnUsuario.setAttribute('href', rutasUsuario[fase]);
                    }
                    if (fase === "Desestimado") {
                        cardRecepcion.style.display = "none";
                        cardAtencion.style.display = "none";
                        document.getElementById("card-desestimacion").style.display = "block";

                        document.getElementById("descripcion-desestimacion").textContent = data.descripcionDesestimacion || "";
                        document.getElementById("usuario-desestimacion").textContent = data.nombreUsuarioDesestimacion || "";
                        document.getElementById("fecha-desestimacion").textContent = data.fechaFormateadaDesestimacion || "";
                        document.getElementById("hora-desestimacion").textContent = data.horaFormateadaDesestimacion || "";
                        document.getElementById("clasificacion-desestimacion").textContent = data.nombreClasificacionDesestimacion || "";

                        const adjuntosContainerDesestimacion = document.getElementById("adjuntos-desestimacion");
                        adjuntosContainerDesestimacion.innerHTML = ""; // Limpiar por si acaso

                        if (Array.isArray(data.listaArchivosAdjuntosDesestimacion)) {
                          data.listaArchivosAdjuntosDesestimacion.forEach(adjunto => {
                            const p = document.createElement("p");
                            p.classList.add("mb-0");

                            const icon = document.createElement("i");
                            icon.classList.add("bi", "bi-paperclip");
                            p.appendChild(icon);

                            const espacio = document.createTextNode(" ");
                            p.appendChild(espacio);

                            const a = document.createElement("a");
                            a.href = `/app/tickets/adjuntoDesestimacion/descargar/${adjunto.id}`;
                            a.textContent = adjunto.nombre;
                            p.appendChild(a);

                            const peso = document.createTextNode(` - ${adjunto.pesoContenido}`);
                            p.appendChild(peso);

                            adjuntosContainerDesestimacion.appendChild(p);
                          });
                        }


                    } else {
                        document.getElementById("cards-principales").style.display = "block";

                        if (fase === "Enviado") {
                            cardRecepcion.style.display = "block";
                            cardRecepcion.querySelector(".info-recepcion").style.display = "none";
                            cardRecepcion.querySelector(".pendiente-recepcion").style.display = "block";

                            cardAtencion.style.display = "block";
                            cardAtencion.querySelector(".info-atencion").style.display = "none";
                            cardAtencion.querySelector(".pendiente-atencion").style.display = "block";

                            if (btnRecepcionar) btnRecepcionar.style.display = 'inline-block';
                            if (btnDesestimar) btnDesestimar.style.display = 'inline-block';

                        } else if (fase === "Recepcionado - En Proceso") {
                            cardRecepcion.style.display = "block";
                            cardRecepcion.querySelector(".info-recepcion").style.display = "block";
                            cardRecepcion.querySelector(".pendiente-recepcion").style.display = "none";

                            cardAtencion.style.display = "block";
                            cardAtencion.querySelector(".info-atencion").style.display = "none";
                            cardAtencion.querySelector(".pendiente-atencion").style.display = "block";

                            document.getElementById("mensaje-recepcion").textContent = data.mensajeRecepcion || "";
                            document.getElementById("usuario-recepcion").textContent = data.nombreUsuarioRecepcion || "";
                            document.getElementById("fecha-recepcion").textContent = data.fechaFormateadaRecepcion || "";
                            document.getElementById("hora-recepcion").textContent = data.horaFormateadaRecepcion || "";


                            if (btnAtender) btnAtender.style.display = 'inline-block';
                            if (btnEspera) btnEspera.style.display = 'inline-block';
                            if (btnDesestimar) btnDesestimar.style.display = 'inline-block';


                        } else if (fase === "En Espera") {
                             cardRecepcion.style.display = "block";
                             cardRecepcion.querySelector(".info-recepcion").style.display = "block";
                             cardRecepcion.querySelector(".pendiente-recepcion").style.display = "none";

                             cardAtencion.style.display = "block";
                             cardAtencion.querySelector(".info-atencion").style.display = "none";
                             cardAtencion.querySelector(".pendiente-atencion").style.display = "block";

                             document.getElementById("mensaje-recepcion").textContent = data.mensajeRecepcion || "";
                             document.getElementById("usuario-recepcion").textContent = data.nombreUsuarioRecepcion || "";
                             document.getElementById("fecha-recepcion").textContent = data.fechaFormateadaRecepcion || "";
                             document.getElementById("hora-recepcion").textContent = data.horaFormateadaRecepcion || "";


                             if (btnAtender) btnAtender.style.display = 'inline-block';
                             if (btnDesestimar) btnDesestimar.style.display = 'inline-block';


                         } else if (fase === "Cerrado - Atendido") {
                            cardRecepcion.style.display = "block";
                            cardRecepcion.querySelector(".info-recepcion").style.display = "block";
                            cardRecepcion.querySelector(".pendiente-recepcion").style.display = "none";

                            cardAtencion.style.display = "block";
                            cardAtencion.querySelector(".info-atencion").style.display = "block";
                            cardAtencion.querySelector(".pendiente-atencion").style.display = "none";



                            document.getElementById("mensaje-recepcion").textContent = data.mensajeRecepcion || "";
                            document.getElementById("usuario-recepcion").textContent = data.nombreUsuarioRecepcion || "";
                            document.getElementById("fecha-recepcion").textContent = data.fechaFormateadaRecepcion || "";
                            document.getElementById("hora-recepcion").textContent = data.horaFormateadaRecepcion || "";


                            document.getElementById("descripcion-atencion").textContent = data.descripcionAtencion || "";
                            document.getElementById("usuario-atencion").textContent = data.nombreUsuarioAtencion || "";

                            const areaAtencionElem = document.getElementById("area-atencion");
                            if (areaAtencionElem) {
                              areaAtencionElem.textContent = data.nombreAreaAtencion || "";
                            }

                            const subcatIncidenciaElem = document.getElementById("subcat-incidencia-atencion");
                            if (subcatIncidenciaElem) {
                              subcatIncidenciaElem.textContent = data.nombreSubCatIncidencia || "";
                            }

                            const incidenciaAtencionElem = document.getElementById("incidencia-atencion");
                            if (incidenciaAtencionElem) {
                              incidenciaAtencionElem.textContent = data.nombreTipoIncidencia || "";
                            }

                            const urgenciaAtencionElem = document.getElementById("urgencia-atencion");
                            if (urgenciaAtencionElem) {
                              urgenciaAtencionElem.textContent = `Nivel Urgencia: ${data.nombreUrgencia || ""}`;
                            }

                            const clasificacionAtencionElem = document.getElementById("clasificacion-atencion");
                            if (clasificacionAtencionElem) {
                              clasificacionAtencionElem.textContent = data.nombreClasificacionAtencion || "";
                            }

                            document.getElementById("fecha-atencion").textContent = data.fechaFormateadaAtencion || "";
                            document.getElementById("hora-atencion").textContent = data.horaFormateadaAtencion || "";
                            const adjuntosContainer = document.getElementById("adjuntos-atencion");
                            adjuntosContainer.innerHTML = ""; // Limpiar por si acaso

                            if (Array.isArray(data.listaArchivosAdjuntosAtencion)) {
                              data.listaArchivosAdjuntosAtencion.forEach(adjunto => {
                                const p = document.createElement("p");
                                p.classList.add("mb-0");

                                const icon = document.createElement("i");
                                icon.classList.add("bi", "bi-paperclip");
                                p.appendChild(icon);

                                const espacio = document.createTextNode(" ");
                                p.appendChild(espacio);

                                const a = document.createElement("a");
                                a.href = `/app/tickets/adjuntoAtencion/descargar/${adjunto.id}`;
                                a.textContent = adjunto.nombre;
                                p.appendChild(a);

                                const peso = document.createTextNode(` - ${adjunto.pesoContenido}`);
                                p.appendChild(peso);

                                adjuntosContainer.appendChild(p);
                              });
                            }



                        }
                    }




const contenedorEspera = document.getElementById("contenedor-detalles-espera");
const acordeonItem = document.querySelector("#accordionEspera");

contenedorEspera.innerHTML = ""; // Limpiar contenido anterior

if (Array.isArray(data.listaDetalleEspera) && data.listaDetalleEspera.length > 0) {
  acordeonItem.style.display = "block"; // Mostrar el acordeón
  data.listaDetalleEspera.forEach((espera) => {
    const col = document.createElement("div");
    col.classList.add("col-md-6");

    const card = document.createElement("div");
    card.classList.add("card", "border-0", "shadow-sm", "bg-warning-subtle", "small");

    const cardBody = document.createElement("div");
    cardBody.classList.add("card-body", "p-3");

    // Título
    const titulo = document.createElement("h6");
    titulo.classList.add("fw-bold", "mb-2");
    titulo.innerHTML = `<i class="bi bi-info-circle"></i> ${espera.clasificacion?.nombre || "Sin clasificación"}`;
    cardBody.appendChild(titulo);

    // Descripción
    if (espera.descripcion) {
      const desc = document.createElement("p");
      desc.classList.add("mb-2");
      desc.textContent = espera.descripcion;
      cardBody.appendChild(desc);
    }

    // Desde
    const desde = document.createElement("p");
    desde.classList.add("mb-1", "text-muted");
    desde.innerHTML = `<i class="bi bi-clock"></i> <strong>Desde:</strong> ${espera.fecha || "-"} ${espera.hora || "-"}`;
    cardBody.appendChild(desde);

    // Hasta
    const hasta = document.createElement("p");
    hasta.classList.add("mb-1", "text-muted");
    const fechaFin = espera.fechaFin || "<span class='text-secondary fst-italic'>Aún no finaliza</span>";
    const horaFin = espera.horaFin || "";
    hasta.innerHTML = `<i class="bi bi-clock-history"></i> <strong>Hasta:</strong> ${fechaFin} ${horaFin}`;
    cardBody.appendChild(hasta);

    // Adjuntos
    if (Array.isArray(espera.listaArchivos) && espera.listaArchivos.length > 0) {
      const archivosTitulo = document.createElement("p");
      archivosTitulo.classList.add("fw-bold", "mb-1", "mt-2");
      archivosTitulo.textContent = "Adjuntos:";
      cardBody.appendChild(archivosTitulo);

      espera.listaArchivos.forEach((archivo) => {
        const p = document.createElement("p");
        p.classList.add("mb-0", "text-truncate");

        const icon = document.createElement("i");
        icon.classList.add("bi", "bi-paperclip");
        p.appendChild(icon);

        p.appendChild(document.createTextNode(" "));

        const a = document.createElement("a");
        a.href = `/app/tickets/adjuntoEspera/descargar/${archivo.id}`;
        a.textContent = archivo.nombre;
        a.classList.add("text-decoration-none");
        p.appendChild(a);

        const peso = document.createTextNode(` - ${archivo.pesoContenido || ""}`);
        p.appendChild(peso);

        cardBody.appendChild(p);
      });
    }

    card.appendChild(cardBody);
    col.appendChild(card);
    contenedorEspera.appendChild(col);
  });
} else {
  acordeonItem.style.display = "none"; // Ocultar si no hay esperas
}



                })
                .catch(error => {
                    console.error("Error al obtener datos del ticket:", error);
                });
        }





        document.addEventListener("DOMContentLoaded", function () {
            actualizarVistaTicket(); // Ejecutar al cargar la página

            const codigoTicket = document.getElementById('ticketInfo').dataset.codigo;
            let stompClient = null;
            let conectado = false;
            const destino = `/topic/actualizar/ticket/${codigoTicket}`;

            function conectarWebSocket() {
                const socket = new SockJS('/ws');
                stompClient = Stomp.over(socket);
                stompClient.heartbeat.outgoing = 20000; // 20s ping saliente
                stompClient.heartbeat.incoming = 0;     // No esperamos ping del servidor

                stompClient.connect({}, () => {
                    conectado = true;
                    console.log("WebSocket conectado");

                    stompClient.subscribe(destino, function (mensaje) {
                        if (mensaje.body === 'actualizar') {
                            console.log("Se actualizó el ticket, recargando datos...");
                            actualizarVistaTicket();
                        }
                    });
                }, (error) => {
                    conectado = false;
                    console.warn("WebSocket desconectado. Reintentando conexión...", error);
                });
            }

            function verificarConexionYReconectar() {
                if (!conectado || !stompClient.connected) {
                    console.log("Intentando reconectar WebSocket...");
                    conectarWebSocket();
                }
            }

            // Iniciar conexión
            conectarWebSocket();

            // Verificar cada 5 segundos si se desconectó y reconectar
            setInterval(verificarConexionYReconectar, 5000);
        });





        // Al abrir el modal, actualizar su contenido con los datos del ticket seleccionado
        const atencionModal = document.getElementById('atencionModal');
        atencionModal.addEventListener('show.bs.modal', function (event) {
            // Botón que disparó el modal
            const button = event.relatedTarget;

            // Extraer información del ticket desde los atributos data-*
            const ticketId = button.getAttribute('data-ticket-id');
        const formattedTicketId = button.getAttribute('data-codigo-ticket');
            const ticketUsuario = button.getAttribute('data-ticket-usuario');
            const ticketDescripcion = button.getAttribute('data-ticket-descripcion');
            const ticketClasificacion = button.getAttribute('data-ticket-clasificacion');
            const ticketFase = button.getAttribute('data-ticket-fase');
            const ticketFecha = button.getAttribute('data-ticket-fecha');
            const ticketHora = button.getAttribute('data-ticket-hora');

            // Actualizar los elementos del modal con la información del ticket
            document.getElementById('TicketIdFormateado').textContent = formattedTicketId;
            document.getElementById('modalTicketUsuario').textContent = ticketUsuario;
            document.getElementById('modalTicketDescripcion').textContent = ticketDescripcion;


            // Actualizar la acción del formulario para que envíe el ticket correspondiente
            const form = document.getElementById('atencionForm');
            form.setAttribute('action', `/app/tickets/atencion/${ticketId}`);
        });









                // Al abrir el modal, actualizar su contenido con los datos del ticket seleccionado
                const recepcionModal = document.getElementById('recepcionModal');
                recepcionModal.addEventListener('show.bs.modal', function (event) {
                    // Botón que disparó el modal
                    const button = event.relatedTarget;

                    // Extraer información del ticket desde los atributos data-*
                    const ticketId = button.getAttribute('data-ticket-id');
                const formattedTicketId = button.getAttribute('data-codigo-ticket');

                    const ticketUsuario = button.getAttribute('data-ticket-usuario');
                    const ticketDescripcion = button.getAttribute('data-ticket-descripcion');
                    const ticketFase = button.getAttribute('data-ticket-fase');
                    const ticketFecha = button.getAttribute('data-ticket-fecha');
                    const ticketHora = button.getAttribute('data-ticket-hora');

                    // Actualizar los elementos del modal con la información del ticket
                    document.getElementById('modalTicketIdFormateado').textContent = formattedTicketId;
                    document.getElementById('modalTicketUsuarioRecepcion').textContent = ticketUsuario;
                    document.getElementById('modalTicketDescripcionRecepcion').textContent = ticketDescripcion;

                    // Actualizar la acción del formulario para que envíe el ticket correspondiente
                    const form = document.getElementById('recepcionForm');
                    form.setAttribute('action', `/app/tickets/recepcion/${ticketId}`);
                });



              // Al abrir el modal de desestimación, actualizar su contenido con los datos del ticket seleccionado
              const desestimacionModal = document.getElementById('desestimacionModal');
              desestimacionModal.addEventListener('show.bs.modal', function (event) {
                  // Botón que disparó el modal
                  const button = event.relatedTarget;

                  // Extraer información del ticket desde los atributos data-*
                  const ticketId = button.getAttribute('data-ticket-id');
                  const formattedTicketId = button.getAttribute('data-codigo-ticket');
                  const ticketUsuario = button.getAttribute('data-ticket-usuario');
                  const ticketDescripcion = button.getAttribute('data-ticket-descripcion');
                  const ticketFecha = button.getAttribute('data-ticket-fecha');
                  const ticketHora = button.getAttribute('data-ticket-hora');
                  const ticketNumeroFase = button.getAttribute('data-numero-fase');


                  // Actualizar los elementos del modal con la información del ticket
                  document.getElementById('desestimacionTicketIdFormateado').textContent = formattedTicketId;
                  document.getElementById('desestimacionTicketUsuario').textContent = ticketUsuario;
                  document.getElementById('desestimacionTicketDescripcion').textContent = ticketDescripcion;
                  document.getElementById('desestimacionTicketNumeroFase').value = ticketNumeroFase;

                  // Actualizar la acción del formulario para que envíe el ticket correspondiente
                  const form = document.getElementById('desestimacionForm');
                  form.setAttribute('action', `/app/tickets/desestimacion/${ticketId}`);
              });










document.addEventListener("DOMContentLoaded", () => {
    const catSelect = document.getElementById("Lista_cat_incidencia");
    const subCatSelect = document.getElementById("Lista_subcat_incidencia");
    const tipoSelect = document.getElementById("tipo_incidencia");

    const subcatWrapper = document.getElementById("subcategoria-wrapper");
    const tipoWrapper = document.getElementById("tipo-wrapper");

    catSelect.addEventListener("change", () => {
        const idCategoria = catSelect.value;
        fetch(`/app/clasificadores/listar/subCatIncidenciaActivos/${idCategoria}`)
            .then(response => response.json())
            .then(data => {
                subCatSelect.classList.remove("select-error");
                subCatSelect.innerHTML = '<option selected disabled value="">Selecciona la subcategoría</option>';
                tipoSelect.innerHTML = '<option selected disabled value="">Selecciona el tipo de Incidencia</option>';
                tipoWrapper.classList.add("d-none");

                if (data.length > 0) {
                    subcatWrapper.classList.remove("d-none");
                    data.forEach(subcat => {
                        const option = document.createElement("option");
                        option.value = subcat.id;
                        option.text = subcat.nombre;
                        subCatSelect.appendChild(option);
                    });
                } else {
                    subcatWrapper.classList.remove("d-none");
                    subCatSelect.classList.add("select-error");
                    subCatSelect.innerHTML = '<option disabled selected>No se encontraron subcategorías</option>';
                    Swal.fire({
                        icon: 'warning',
                        title: 'Sin subcategorías',
                        text: 'No se encontraron subcategorías para la categoría seleccionada, contacte con un administrador.',
                        confirmButtonText: 'Entendido'
                    });
                }
            });
    });

    subCatSelect.addEventListener("change", () => {
        const idSubCategoria = subCatSelect.value;
        fetch(`/app/clasificadores/listar/tipoIncidenciaActivos/${idSubCategoria}`)
            .then(response => response.json())
            .then(data => {
                tipoSelect.classList.remove("select-error");
                tipoSelect.innerHTML = '<option selected disabled value="">Selecciona el tipo de Incidencia</option>';
                tipoWrapper.classList.remove("d-none");

                if (data.length > 0) {
                    data.forEach(tipo => {
                        const option = document.createElement("option");
                        option.value = tipo.id;
                        option.text = tipo.nombre;
                        tipoSelect.appendChild(option);
                    });
                } else {
                    tipoSelect.classList.add("select-error");
                    tipoSelect.innerHTML = '<option disabled selected>No se encontraron tipos</option>';
                    Swal.fire({
                        icon: 'warning',
                        title: 'Sin tipos de incidencia',
                        text: 'No se encontraron tipos para la subcategoría seleccionada, contacte con un administrador.',
                        confirmButtonText: 'Entendido'
                    });
                }
            });
    });
});


    document.addEventListener("DOMContentLoaded", () => {
        const sedeSelect = document.getElementById("sede");
        const areaSelect = document.getElementById("areaAtencion");
        const areaWrapper = document.getElementById("area-wrapper");

        sedeSelect.addEventListener("change", () => {
            const sedeId = sedeSelect.value;

            fetch(`/app/clasificadores/listar/areasActivas/${sedeId}`)
                .then(response => response.json())
                .then(data => {
                    areaSelect.classList.remove("select-error");
                    areaSelect.innerHTML = '<option selected disabled value="">Selecciona un área</option>';

                    if (data.length > 0) {
                        areaWrapper.classList.remove("d-none");

                        data.forEach(area => {
                            const option = document.createElement("option");
                            option.value = area.id;
                            option.text = area.nombre;
                            areaSelect.appendChild(option);
                        });
                    } else {
                        areaWrapper.classList.remove("d-none");
                        areaSelect.classList.add("select-error");
                        areaSelect.innerHTML = '<option disabled selected>No se encontraron áreas</option>';
                        Swal.fire({
                            icon: 'warning',
                            title: 'Sin áreas',
                            text: 'No se encontraron áreas para la sede seleccionada. Contacte con un administrador.',
                            confirmButtonText: 'Entendido'
                        });
                    }
                })
                .catch(error => {
                    console.error("Error al obtener áreas:", error);
                    Swal.fire({
                        icon: 'error',
                        title: 'Error',
                        text: 'Hubo un problema al obtener las áreas. Intente nuevamente.',
                        confirmButtonText: 'Entendido'
                    });
                });
        });
    });