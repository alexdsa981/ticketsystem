import { mostrarNotificacionPersonalizada } from './notificacionPersonalizada.js';
import { ActualizaTablasSoporteRecepcion } from './wsActualizaTabla.js';
import { ActualizaTablaAtencionSoporte } from './wsActualizaTabla.js';
import { ActualizaTablaSoporteHistorial } from './wsActualizaTabla.js';
import { ActualizaTablaDesestimacionHistorial } from './wsActualizaTabla.js';
import { ActualizaTablaUsuarioRecepcionados } from './wsActualizaTabla.js';
import { ActualizaTablaUsuarioAtendidos } from './wsActualizaTabla.js';
import { ActualizaTablaUsuarioDesestimados } from './wsActualizaTabla.js';
import { ActualizaTablaUsuarioEnviados } from './wsActualizaTabla.js';





import { EliminarTicketDeTabla } from './wsActualizaTabla.js';


// Crear una conexión WebSocket
const socket = new SockJS('/ws');
const stompClient = Stomp.over(socket);
//quita comentarios debug
stompClient.debug = () => {};

document.addEventListener('DOMContentLoaded', () => {
    const notificacionesContador = document.getElementById('notificaciones-contador');

    // Conectar al servidor
    stompClient.connect({}, (frame) => {
        //console.log('Conectado: ' + frame);

        // SOPORTE RECEPCION: OCULTA O ACTUALIZA REGISTROS
        if (window.location.pathname === '/soporte/Recepcionar') {
            // Suscripción para actualizar la tabla cuando se agregan tickets
            stompClient.subscribe('/topic/actualizar/soporte-recepcion', (message) => {
                const ticketRecord = JSON.parse(message.body);
                ActualizaTablasSoporteRecepcion(ticketRecord);
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
                ActualizaTablaAtencionSoporte(ticketRecord);
            });
            // Suscripción para ocultar un ticket cuando sea recepcionado
            stompClient.subscribe('/topic/ocultar/soporte-atencion', (message) => {
                const ticketId = message.body.trim(); // Este es el ID sin formato
                EliminarTicketDeTabla(ticketId);
            });
        }

        if (window.location.pathname === '/soporte/Tickets-Cerrados') {
            // Suscripción para actualizar la tabla cuando se agregan tickets
            stompClient.subscribe('/topic/actualizar/soporte-historial', (message) => {
                const ticketRecord = JSON.parse(message.body);
                ActualizaTablaSoporteHistorial(ticketRecord);
            });
        }

        //DESESTIMACION HISTORIAL ActualizaTablaDesestimacionHistorial
        if (window.location.pathname === '/soporte/Tickets-Desestimados') {
            // Suscripción para actualizar la tabla de historial cuando se revisan tickets
            stompClient.subscribe('/topic/actualizar/desestimacion-historial', (message) => {
                const ticketRecord = JSON.parse(message.body);
                ActualizaTablaDesestimacionHistorial(ticketRecord);
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






        //USUARIO ENVIADOS: OCULTA O ACTUALIZA REGISTROS
        if (window.location.pathname === '/inicio') {
            fetch('/app/usuarios/id')
                .then(response => response.json())
                .then(userId => {

                    // Suscripción para actualizar la tabla cuando se agregan tickets
                    stompClient.subscribe(`/topic/actualizar/usuario-enviados/${userId}`, (message) => {
                        const ticketRecord = JSON.parse(message.body);
                        ActualizaTablaUsuarioEnviados(ticketRecord);
                    });
                    // Suscripción para ocultar un ticket cuando sea recepcionado
                    stompClient.subscribe(`/topic/ocultar/usuario-enviados/${userId}`, (message) => {
                        const ticketId = message.body.trim(); // Este es el ID sin formato
                        EliminarTicketDeTabla(ticketId);
                    });

                })
                .catch(error => console.error('Error al obtener el ID del usuario:', error));
        }








        //USUARIO RECEPCIONADOS: OCULTA O ACTUALIZA REGISTROS
        if (window.location.pathname === '/TicketsEnProceso') {
            fetch('/app/usuarios/id')
                .then(response => response.json())
                .then(userId => {

                    // Suscripción para actualizar la tabla cuando se agregan tickets
                    stompClient.subscribe(`/topic/actualizar/usuario-recepcionados/${userId}`, (message) => {
                        const ticketRecord = JSON.parse(message.body);
                        ActualizaTablaUsuarioRecepcionados(ticketRecord);
                    });
                    // Suscripción para ocultar un ticket cuando sea recepcionado
                    stompClient.subscribe(`/topic/ocultar/usuario-recepcionados/${userId}`, (message) => {
                        const ticketId = message.body.trim(); // Este es el ID sin formato
                        EliminarTicketDeTabla(ticketId);
                    });

                })
                .catch(error => console.error('Error al obtener el ID del usuario:', error));
        }

        //USUARIO ATENDIDOS: ACTUALIZA REGISTROS
        if (window.location.pathname === '/TicketsAtendidos') {
            fetch('/app/usuarios/id')
                .then(response => response.json())
                .then(userId => {
                    // Suscripción para actualizar la tabla cuando se agregan tickets
                    stompClient.subscribe(`/topic/actualizar/usuario-atendidos/${userId}`, (message) => {
                        const ticketRecord = JSON.parse(message.body);
                        ActualizaTablaUsuarioAtendidos(ticketRecord);
                    });

                })
                .catch(error => console.error('Error al obtener el ID del usuario:', error));
        }
        //USUARIO DESESTIMADOS: ACTUALIZA REGISTROS
        if (window.location.pathname === '/TicketsDesestimados') {
            fetch('/app/usuarios/id')
                .then(response => response.json())
                .then(userId => {
                    // Suscripción para actualizar la tabla cuando se agregan tickets
                    stompClient.subscribe(`/topic/actualizar/usuario-desestimados/${userId}`, (message) => {
                        const ticketRecord = JSON.parse(message.body);
                        ActualizaTablaUsuarioDesestimados(ticketRecord);
                    });

                })
                .catch(error => console.error('Error al obtener el ID del usuario:', error));
        }




    }, (error) => {
        console.error('Error al conectar con el WebSocket:', error);
    });
});
