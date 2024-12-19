import { mostrarNotificacionPersonalizada } from './notificacionPersonalizada.js';
import { ActualizaTablaRecibidos } from './wsActualizaTabla.js';


// Crear una conexión WebSocket
const socket = new SockJS('/ws');
const stompClient = Stomp.over(socket);

// Conectar al servidor
    stompClient.connect({}, (frame) => {
        console.log('Conectado: ' + frame);

    // Verifica si estás en la página específica antes de suscribirte
    if (window.location.pathname === '/soporte/Recepcionar') {
        stompClient.subscribe('/topic/tickets', (message) => {
            // Aquí recibes el mensaje
            const ticketRecord = JSON.parse(message.body);
            // Llamar a una función para manejar el ticket recibido
            ActualizaTablaRecibidos(ticketRecord);
        });
    }



            // Verificar el rol del usuario antes de suscribirse
            fetch('/app/usuarios/rol')
                .then(response => response.text())
                .then(role => {
                    if (role === 'soporte') {
                        // Solo suscribirse a las notificaciones si el usuario es soporte
                        stompClient.subscribe('/topic/notificaciones/soporte', (message) => {
                            console.log('Notificación recibida DESDE EL SERVIDOR:', message.body);
                            // Aquí puedes mostrar la notificación
                            // Mostrar notificación personalizada
                            mostrarNotificacionPersonalizada(message.body);



                        });
                    } else {
                        console.log('El usuario no tiene permisos para recibir notificaciones de soporte.');
                    }
                })
                .catch(error => console.error('Error al obtener el rol del usuario:', error));


    });



