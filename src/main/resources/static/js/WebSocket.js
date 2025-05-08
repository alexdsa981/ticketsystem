import { mostrarNotificacionPersonalizada } from './notificacionPersonalizada.js';
import {
    ActualizaTablasSoporteRecepcion,
    ActualizaTablaAtencionSoporte,
    ActualizaTablaSoporteHistorial,
    ActualizaTablaDesestimacionHistorial,
    ActualizaTablaUsuarioRecepcionados,
    ActualizaTablaUsuarioAtendidos,
    ActualizaTablaUsuarioDesestimados,
    ActualizaTablaUsuarioEnviados,
    EliminarTicketDeTabla
} from './wsActualizaTabla.js';

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
        const notificacionesContador = document.getElementById('notificaciones-contador');

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
                    let contador = parseInt(notificacionesContador.textContent) || 0;
                    notificacionesContador.textContent = ++contador;
                    notificacionesContador.classList.remove('d-none');
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
