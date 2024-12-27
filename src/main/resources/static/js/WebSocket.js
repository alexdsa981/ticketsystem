import { mostrarNotificacionPersonalizada } from './notificacionPersonalizada.js';
import { ActualizaTablaRecibidos } from './wsActualizaTabla.js';

// Crear una conexión WebSocket
const socket = new SockJS('/ws');
const stompClient = Stomp.over(socket);

document.addEventListener('DOMContentLoaded', () => {
    const notificacionesContador = document.getElementById('notificaciones-contador');

    // Conectar al servidor
    stompClient.connect({}, (frame) => {
        console.log('Conectado: ' + frame);

        // Verifica si estás en la página específica antes de suscribirte
        if (window.location.pathname === '/soporte/Recepcionar') {
            stompClient.subscribe('/topic/tickets', (message) => {
                const ticketRecord = JSON.parse(message.body);
                ActualizaTablaRecibidos(ticketRecord);
            });
        }

        // Verificar el rol del usuario antes de suscribirse
        fetch('/app/usuarios/rol')
            .then(response => response.text())
            .then(role => {
                if (role === 'soporte') {
                    stompClient.subscribe('/topic/notificaciones/soporte', (message) => {
                        console.log('Alerta recibida:', message.body);
                        mostrarNotificacionPersonalizada(message.body);
                    });
                } else {
                    console.log('El usuario no tiene permisos para recibir notificaciones de soporte.');
                }
            })
            .catch(error => console.error('Error al obtener el rol del usuario:', error));

        // Obtener el ID del usuario logeado para suscribirse al contador de notificaciones
        fetch('/app/usuarios/id')
            .then(response => response.json())
            .then(userId => {
                stompClient.subscribe(`/topic/notificaciones/contador/${userId}`, (message) => {
                    console.log('Mensaje recibido:', message.body);

                    let contadorActual = parseInt(notificacionesContador.textContent) || 0;
                    contadorActual += 1;

                    notificacionesContador.textContent = contadorActual;
                    notificacionesContador.classList.remove('d-none');
                });
            })
            .catch(error => console.error('Error al obtener el ID del usuario:', error));
    }, (error) => {
        console.error('Error al conectar con el WebSocket:', error);
    });
});
