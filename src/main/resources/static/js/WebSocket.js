import { mostrarNotificacionPersonalizada } from './notificacionPersonalizada.js';
import { ActualizaTablaRecibidos } from './wsActualizaTabla.js';
import { EliminarTicketDeTabla } from './wsActualizaTabla.js';


// Crear una conexión WebSocket
const socket = new SockJS('/ws');
const stompClient = Stomp.over(socket);

document.addEventListener('DOMContentLoaded', () => {
    const notificacionesContador = document.getElementById('notificaciones-contador');

    // Conectar al servidor
    stompClient.connect({}, (frame) => {
        console.log('Conectado: ' + frame);

        // SOPORTE RECEPCION: OCULTA O ACTUALIZA REGISTROS
        if (window.location.pathname === '/soporte/Recepcionar') {
            // Suscripción para actualizar la tabla cuando se agregan tickets
            stompClient.subscribe('/topic/actualizar/soporte-recepcion', (message) => {
                const ticketRecord = JSON.parse(message.body);
                ActualizaTablaRecibidos(ticketRecord);
            });
            // Suscripción para ocultar un ticket cuando sea recepcionado
            stompClient.subscribe('/topic/ocultar/soporte-recepcion', (message) => {
                const ticketId = message.body.trim(); // Este es el ID sin formato
                EliminarTicketDeTabla(ticketId);
            });
        }

        // SOPORTE ATENCIÓN: OCULTA O ACTUALIZA REGISTROS
        if (window.location.pathname === '/soporte/Atender') {
            // Suscripción para actualizar la tabla cuando se agregan tickets
            stompClient.subscribe('/topic/actualizar/soporte-atencion', (message) => {
                const ticketRecord = JSON.parse(message.body);
                ActualizaTablaRecibidos(ticketRecord);
            });
            // Suscripción para ocultar un ticket cuando sea recepcionado
            stompClient.subscribe('/topic/ocultar/soporte-atencion', (message) => {
                const ticketId = message.body.trim(); // Este es el ID sin formato
                EliminarTicketDeTabla(ticketId);
            });
        }

        // Obtener el ID del usuario logeado para suscribirse al contador de notificaciones
        fetch('/app/usuarios/id')
            .then(response => response.json())
            .then(userId => {
                stompClient.subscribe(`/topic/notificaciones/${userId}`, (message) => {

                    //ACTUALIZAR CONTADOR
                    let contadorActual = parseInt(notificacionesContador.textContent) || 0;
                    contadorActual += 1;
                    notificacionesContador.textContent = contadorActual;
                    notificacionesContador.classList.remove('d-none');
                    //ALERTA
                    mostrarNotificacionPersonalizada(message.body);
                });
            })
            .catch(error => console.error('Error al obtener el ID del usuario:', error));
    }, (error) => {
        console.error('Error al conectar con el WebSocket:', error);
    });
});
