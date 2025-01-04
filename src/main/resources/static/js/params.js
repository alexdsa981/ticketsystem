
            // Detectar parámetros de la URL
            const params = new URLSearchParams(window.location.search);

            if (params.has('successful')) {
                const successType = params.get('successful');
                let successMessage;

                // Determinar el mensaje según el tipo de éxito
                switch (successType) {
                    case 'recepcion':
                        successMessage = 'El ticket fue recepcionado correctamente.';
                        break;
                    case 'redireccion':
                        successMessage = 'Redirección realizada con éxito.';
                        break;
                    case 'desestimacion':
                        successMessage = 'Desestimación realizada con éxito.';
                        break;
                    case 'atencion':
                        successMessage = 'El ticket fue atendido correctamente.';
                        break;
                    case 'revision':
                        successMessage = 'Revisión realizada.';
                        break;
                    case 'newUser':
                        successMessage = 'Usuario creado exitosamente.';
                        break;
                    case 'updateUser':
                        successMessage = 'Usuario modificado exitosamente.';
                        break;
                    default:
                        successMessage = 'Operación realizada con éxito.';
                        break;

                }

                Swal.fire({
                    icon: 'success',
                    title: 'Éxito',
                    text: successMessage,
                    confirmButtonText: 'Entendido',
                }).then(() => {
                    // Limpiar el parámetro de la URL sin recargar
                    const cleanUrl = window.location.href.split('?')[0];
                    window.history.replaceState({}, document.title, cleanUrl);
                });
            }

            if (params.has('error')) {
            const errorType = params.get('error');
            let errorMessage;

            // Determinar el mensaje según el tipo de error
            switch (errorType) {
                case 'recepcion-duplicated':
                    errorMessage = 'El ticket ya ha sido recepcionado por otro usuario.';
                    break;
                case 'recepcion-general':
                    errorMessage = 'Error inesperado al recepcionar el ticket.';
                    break;

                case 'redireccion-moved':
                    errorMessage = 'El ticket ya se encuentra en otra fase.';
                    break;

                case 'desestimacion-duplicated':
                    errorMessage = 'El ticket ya ha sido desestimado por otro usuario.';
                    break;
                case 'desestimacion-general':
                    errorMessage = 'Error inesperado al desestimar el ticket.';
                    break;
                case 'desestimacion-moved':
                    errorMessage = 'El ticket ya se encuentra en otra fase.';
                    break;
                case 'atencion-duplicated':
                    errorMessage = 'El ticket ya ha sido atendido por otro usuario.';
                    break;
                case 'atencion-general':
                    errorMessage = 'Error inesperado al atender el ticket.';
                    break;
                case 'revision-duplicated':
                    errorMessage = 'El ticket ya ha sido revisado por otro usuario.';
                    break;
                case 'revision-general':
                    errorMessage = 'Error inesperado al enviar revisión el ticket.';
                    break;
                case 'duplicated-user':
                    errorMessage = 'Error usuario ya existente.';
                    break;
                case 'general-user':
                    errorMessage = 'Error inesperado.';
                    break;
                default:
                    errorMessage = 'Ocurrió un problema inesperado. Por favor, inténtalo nuevamente.';
                    break;
            }

            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: errorMessage,
                confirmButtonText: 'Entendido',
            }).then(() => {
                // Limpiar el parámetro de la URL sin recargar
                const cleanUrl = window.location.href.split('?')[0];
                window.history.replaceState({}, document.title, cleanUrl);
            });
        }
    if (params.has('changePassword')) {
        const changePasswordModal = new bootstrap.Modal(document.getElementById('changePasswordModal'));
        changePasswordModal.show();
    }