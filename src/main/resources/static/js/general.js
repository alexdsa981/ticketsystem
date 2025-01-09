document.addEventListener('DOMContentLoaded', () => {
    const notificacionesBtn = document.getElementById('notificaciones-btn');
    const notificacionesContador = document.getElementById('notificaciones-contador');
    const notificacionesDropdown = document.getElementById('notificaciones-dropdown');

    async function actualizarContador() {
        try {
            const response = await fetch('/app/notificaciones');
            if (response.ok) {
                const notificaciones = await response.json();

                // Filtrar notificaciones no leídas
                const noLeidas = notificaciones.filter(n => !n.leido).length;

                if (noLeidas > 0) {
                    notificacionesContador.textContent = noLeidas;
                    notificacionesContador.classList.remove('d-none'); // Mostrar el contador
                } else {
                    reiniciarContador(); // Reinicia el contador si no hay notificaciones no leídas
                }
            } else {
                console.error('Error al cargar el contador de notificaciones:', response.statusText);
            }
        } catch (error) {
            console.error('Error en la solicitud del contador de notificaciones:', error);
        }
    }

    async function cargarNotificaciones() {
        try {
            const response = await fetch('/app/notificaciones');
            if (response.ok) {
                const notificaciones = await response.json();

                // Limpia el dropdown
                notificacionesDropdown.innerHTML = '';

                // Verifica si hay notificaciones
                if (notificaciones.length > 0) {
                    notificaciones.forEach(notificacion => {
                        const item = document.createElement('li');
                        item.innerHTML = `
                            <a style="text-decoration: none" href="${notificacion.url}" class="notificacion-item" data-id="${notificacion.id}">
                                <div class="dropdown-item ${notificacion.abierto ? 'notificacion-abierta' : ''}">
                                    <strong>${notificacion.idFormateado}</strong>
                                    <p class="mb-0">${notificacion.descripcion}</p>
                                    <small class="text-muted">
                                    ${notificacion.fechaFormateada} ${notificacion.horaFormateada} ${notificacion.abierto ? '- Abierto' : ''}                                                            </small>
                                </div>
                            </a>
                        `;
                        notificacionesDropdown.appendChild(item);
                    });
                } else {
                    const item = document.createElement('li');
                    item.innerHTML = `<span class="dropdown-item-text text-muted">Sin notificaciones nuevas</span>`;
                    notificacionesDropdown.appendChild(item);
                }

                // Agregar evento para marcar como abierto
                document.querySelectorAll('.notificacion-item').forEach(item => {
                    item.addEventListener('click', async (e) => {
                        const notificacionId = e.currentTarget.getAttribute('data-id');
                        await marcarNotificacionComoAbierto(notificacionId);
                        e.currentTarget.querySelector('.dropdown-item').classList.add('notificacion-abierta');
                    });
                });
            } else {
                console.error('Error al cargar las notificaciones:', response.statusText);
            }
        } catch (error) {
            console.error('Error en la solicitud de notificaciones:', error);
        }
    }

    async function marcarNotificacionComoAbierto(id) {
        try {
            const response = await fetch(`/app/notificaciones/marcar-abierto/${id}`, {
                method: 'POST',
            });
            if (!response.ok) {
                console.error('Error al marcar notificación como abierta:', response.statusText);
            }
        } catch (error) {
            console.error('Error en la solicitud para marcar notificación como abierta:', error);
        }
    }

    async function marcarNotificacionesComoLeidas() {
        try {
            const response = await fetch('/app/notificaciones/marcar-leidas', {
                method: 'POST',
            });
            if (response.ok) {
                reiniciarContador(); // Reinicia el contador al marcar como leídas
            } else {
                console.error('Error al marcar notificaciones como leídas:', response.statusText);
            }
        } catch (error) {
            console.error('Error en la solicitud para marcar notificaciones como leídas:', error);
        }
    }

    function reiniciarContador() {
        notificacionesContador.textContent = '0';
        notificacionesContador.classList.add('d-none'); // Asegúrate de que se esconda si no hay notificaciones
    }

    notificacionesBtn.addEventListener('click', async () => {
        await marcarNotificacionesComoLeidas();
        await cargarNotificaciones();
    });

    // Actualiza el contador al cargar la página
    actualizarContador();
});

    function actualizarHora() {
        const ahora = new Date(); // Obtiene la fecha y hora actual del cliente
        const horas = String(ahora.getHours()).padStart(2, '0'); // Obtiene las horas y añade un 0 al inicio si es necesario
        const minutos = String(ahora.getMinutes()).padStart(2, '0'); // Obtiene los minutos
        const segundos = String(ahora.getSeconds()).padStart(2, '0'); // Obtiene los segundos

        // Actualiza el contenido del span con la hora formateada
        document.getElementById('horaActual').textContent = `${horas}:${minutos}:${segundos}`;
    }

    // Actualiza la hora cada segundo
    setInterval(actualizarHora, 1000);

document.addEventListener('DOMContentLoaded', function () {
    const forms = document.querySelectorAll('form');

    forms.forEach(form => {
        form.addEventListener('submit', function () {
            const submitButton = form.querySelector('.submit-btn');
            if (submitButton && !submitButton.disabled) {
                submitButton.disabled = true;
                submitButton.innerHTML = '<span class="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span> Enviando...';
            }
        });
    });
});