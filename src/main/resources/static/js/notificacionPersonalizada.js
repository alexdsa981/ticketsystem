// Marcar si el usuario ya ha interactuado con la página
let userInteracted = false;

// Capturar cualquier interacción del usuario (clic o toque) en la página
document.addEventListener('click', () => {
    userInteracted = true;
});

// Solicitar permiso de notificaciones si aún no se ha hecho
if (Notification.permission === 'default') {
    Notification.requestPermission().then((permission) => {
        console.log('Permiso de notificaciones:', permission);
    });
}

// Función para mostrar una notificación personalizada y del sistema
export function mostrarNotificacionPersonalizada(mensaje) {
    const notificationDiv = document.getElementById('customNotification');
    const notificationMessage = document.getElementById('notificationMessage');

    // Establecer el mensaje de la notificación visual en la página
    notificationMessage.textContent = mensaje;

    // Mostrar la notificación visual después de 1 segundo
    setTimeout(() => {
        notificationDiv.style.display = 'block';
        notificationDiv.classList.add('show');

        // Reproducir sonido solo si el usuario ha interactuado
        if (userInteracted) {
            const notificationSound = document.getElementById('notificationSound');
            notificationSound.play().catch((error) => {
                console.error('Error al reproducir sonido:', error);
            });
        } else {
            console.log('No se puede reproducir el sonido porque el usuario no ha interactuado con la página.');
        }

        // Mostrar notificación del sistema (independientemente de interacción)
        if (Notification.permission === "granted") {
            const systemNotification = new Notification("Nuevo ticket", {
                body: mensaje,
                icon: "/images/IPOR.png" // Opcional: coloca aquí la ruta de tu ícono si tienes uno
            });

            // Si se hace clic en la notificación, enfocar la ventana
            systemNotification.onclick = function () {
                window.focus();

                // También puedes hacer que suene aquí si no hubo interacción previa
                if (!userInteracted) {
                    const notificationSound = document.getElementById('notificationSound');
                    notificationSound.play().catch((error) => {
                        console.error('Error al reproducir sonido desde notificación:', error);
                    });
                }
            };
        } else {
            console.log('Permiso de notificaciones del sistema no concedido.');
        }

        // Ocultar la notificación visual después de 7 segundos
        setTimeout(() => {
            notificationDiv.style.display = 'none';
            notificationDiv.classList.remove('show');
        }, 7000);
    }, 1000);
}
