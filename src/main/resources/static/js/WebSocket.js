import { mostrarNotificacionPersonalizada } from './notificacionPersonalizada.js';
import {
    ActualizaTablasSoporteRecepcion,
    ActualizaTablaAtencionSoporte,
    ActualizaTablaEsperaSoporte,
    ActualizaTablaSoporteHistorial,
    ActualizaTablaDesestimacionHistorial,
    ActualizaTablaUsuarioRecepcionados,
    ActualizaTablaUsuarioAtendidos,
    ActualizaTablaUsuarioDesestimados,
    ActualizaTablaUsuarioEnviados,
    ActualizaTablaUsuarioEnEspera,
    EliminarTicketDeTabla
} from './wsActualizaTabla.js';


function actualizarBotonesSoporte(fechaInicio = null, fechaFin = null) {
    const url = new URL('/app/dashboard/grafico/EstadoActual', window.location.origin);
    if (fechaInicio && fechaFin) {
        url.searchParams.append('fechaInicio', fechaInicio);
        url.searchParams.append('fechaFin', fechaFin);
    }

    fetch(url)
        .then(response => {
            if (!response.ok) throw new Error('Error al obtener datos del dashboard');
            return response.json();
        })
        .then(data => {
            const etiquetas = data.etiquetas;
            const valores = data.datos;

            etiquetas.forEach((nombre, i) => {
                const cantidad = valores[i];
                switch (nombre) {
                    case 'Enviado':
                        document.getElementById('countRecepcionar').textContent = cantidad;
                        break;
                    case 'Recepcionado - En Proceso':
                        document.getElementById('countAtender').textContent = cantidad;
                        break;
                    case 'En Espera':
                        document.getElementById('countEspera').textContent = cantidad;
                        break;
                }
            });
        })
        .catch(error => console.error('Error al actualizar los botones de soporte:', error));
}




let stompClient = null;
let connected = false;
let reconnectInterval = 5000; // 5 segundos
let wasDisconnected = false; // Bandera para detectar reconexión

function conectarWebSocket() {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.debug = () => {};

    stompClient.connect({}, (frame) => {
        // Si venía desconectado, recarga la página al reconectar
        if (wasDisconnected) {
            console.log('Reconectado exitosamente. Recargando la página...');
            window.location.reload();
            return;
        }

        connected = true;
        console.log('Conectado: ' + frame);
        const pathname = window.location.pathname;
        const liGenerales = document.querySelectorAll('.li-general');


        // Si estás en cualquier página bajo /soporte, actualiza los botones una vez
        if (pathname.startsWith('/soporte')) {
            actualizarBotonesSoporte(); // Inicial
            stompClient.subscribe('/topic/estadoActual', (mensaje) => {
                if (mensaje.body === 'actualizar') {
                    actualizarBotonesSoporte();
                }
            });
        }





        if (pathname === '/soporte/Recepcionar') {
            stompClient.subscribe('/topic/actualizar/soporte-recepcion', (message) => {
                ActualizaTablasSoporteRecepcion(JSON.parse(message.body));
            });
            stompClient.subscribe('/topic/ocultar/soporte-recepcion', (message) => {
                EliminarTicketDeTabla(message.body.trim());
            });
        }

        if (pathname === '/soporte/Atender') {
            stompClient.subscribe('/topic/actualizar/soporte-atencion', (message) => {
                ActualizaTablaAtencionSoporte(JSON.parse(message.body));
            });
            stompClient.subscribe('/topic/ocultar/soporte-atencion', (message) => {
                EliminarTicketDeTabla(message.body.trim());
            });
        }

        if (pathname === '/soporte/Atender-Espera') {
            stompClient.subscribe('/topic/actualizar/soporte-espera', (message) => {
                const ticketData = JSON.parse(message.body);
                ActualizaTablaEsperaSoporte(ticketData);
            });

            stompClient.subscribe('/topic/ocultar/soporte-espera', (message) => {
                EliminarTicketDeTabla(message.body.trim());
            });
        }


        if (pathname === '/soporte/Tickets-Cerrados') {
            stompClient.subscribe('/topic/actualizar/soporte-historial', (message) => {
                ActualizaTablaSoporteHistorial(JSON.parse(message.body));
            });
        }

        if (pathname === '/soporte/Tickets-Desestimados') {
            stompClient.subscribe('/topic/actualizar/desestimacion-historial', (message) => {
                ActualizaTablaDesestimacionHistorial(JSON.parse(message.body));
            });
        }

        fetch('/app/usuarios/id')
            .then(res => res.json())
            .then(userId => {
                stompClient.subscribe(`/topic/notificaciones/${userId}`, (message) => {
                    liGenerales.forEach(li => {
                        const notificacionesContador = li.querySelector('.notificaciones-contador');
                        let contador = parseInt(notificacionesContador.textContent) || 0;
                        notificacionesContador.textContent = ++contador;
                        notificacionesContador.classList.remove('d-none');
                    });
                    mostrarNotificacionPersonalizada(message.body);
                });

                if (pathname === '/inicio') {
                    stompClient.subscribe(`/topic/actualizar/usuario-enviados/${userId}`, (message) => {
                        ActualizaTablaUsuarioEnviados(JSON.parse(message.body));
                    });
                    stompClient.subscribe(`/topic/ocultar/usuario-enviados/${userId}`, (message) => {
                        EliminarTicketDeTabla(message.body.trim());
                    });
                }

                if (pathname === '/TicketsEnProceso') {
                    stompClient.subscribe(`/topic/actualizar/usuario-recepcionados/${userId}`, (message) => {
                        ActualizaTablaUsuarioRecepcionados(JSON.parse(message.body));
                    });
                    stompClient.subscribe(`/topic/ocultar/usuario-recepcionados/${userId}`, (message) => {
                        EliminarTicketDeTabla(message.body.trim());
                    });
                }

                if (pathname === '/TicketsEnEspera') {
                    stompClient.subscribe(`/topic/actualizar/usuario-espera/${userId}`, (message) => {
                        ActualizaTablaUsuarioEnEspera(JSON.parse(message.body));
                    });
                    stompClient.subscribe(`/topic/ocultar/usuario-espera/${userId}`, (message) => {
                        EliminarTicketDeTabla(message.body.trim());
                    });
                }


                if (pathname === '/TicketsAtendidos') {
                    stompClient.subscribe(`/topic/actualizar/usuario-atendidos/${userId}`, (message) => {
                        ActualizaTablaUsuarioAtendidos(JSON.parse(message.body));
                    });
                }

                if (pathname === '/TicketsDesestimados') {
                    stompClient.subscribe(`/topic/actualizar/usuario-desestimados/${userId}`, (message) => {
                        ActualizaTablaUsuarioDesestimados(JSON.parse(message.body));
                    });
                }
            })
            .catch(error => console.error('Error al obtener el ID del usuario:', error));

    }, (error) => {
        if (!wasDisconnected) {
            console.warn('WebSocket desconectado. Intentando reconectar en 5 segundos...');
            wasDisconnected = true;
        }

        connected = false;

        setTimeout(() => {
            conectarWebSocket(); // Reintenta conexión
        }, reconnectInterval);
    });
}

document.addEventListener('DOMContentLoaded', () => {
    conectarWebSocket();
});
