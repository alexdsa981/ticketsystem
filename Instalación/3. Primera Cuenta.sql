/*
CREAR CUENTA N�1 ADMINISTRADOR:

CONTRASE�A: password = $2a$12$7SW6dd16qcrYSdV0L4Uzp.qzCEe6ricYOH9fdr1r/bGlF2ItBun4a
ENCRIPTADOR DE CONTRASE�A: https://bcrypt-generator.com/

USUARIO ADMINISTRADOR: 
	-USERNAME: admin
	-NOMBRE: ADMINISTRADOR
	-CONTRASE�A: password
	
*/
INSERT INTO [ticketSystem].[dbo].[usuario] (is_active, nombre, password, username, id_rol_usuario, changed_pass)
		VALUES(1, 'ADMINISTRADOR', '$2a$12$7SW6dd16qcrYSdV0L4Uzp.qzCEe6ricYOH9fdr1r/bGlF2ItBun4a', 'admin', 3, 0);