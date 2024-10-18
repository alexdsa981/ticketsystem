// Marcar si el usuario ya ha interactuado con la página
let userInteracted = false;

// Capturar cualquier interacción del usuario (clic o toque) en la página
document.addEventListener('click', () => {
    userInteracted = true;
});


// Función para mostrar una notificación personalizada
export function mostrarNotificacionPersonalizada(mensaje) {
    const notificationDiv = document.getElementById('customNotification');
    const notificationMessage = document.getElementById('notificationMessage');

    // Establecer el mensaje de la notificación
    notificationMessage.textContent = mensaje;

    // Mostrar la notificación
    notificationDiv.style.display = 'block';  // Asegúrate de que el div esté visible
    notificationDiv.classList.add('show');

    // Solo reproducir el sonido si el usuario ya ha interactuado
    if (userInteracted) {
        const notificationSound = document.getElementById('notificationSound');
        notificationSound.play().catch((error) => {
            console.error('Error al reproducir sonido:', error);
        });
    } else {
        console.log('No se puede reproducir el sonido porque el usuario no ha interactuado con la página.');
    }

    // Ocultar la notificación después de 4 segundos
    setTimeout(() => {
        notificationDiv.style.display = 'none';  // Ocultar el div después del tiempo
        notificationDiv.classList.remove('show');
    }, 7000);
}