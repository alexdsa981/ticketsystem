/*
2.CREA ROLES DE USUARIO:
*/

INSERT INTO [ticketSystem].[dbo].[rol_usuario] ([nombre])
VALUES 
('Usuario'),
('Soporte'),
('Admin'),
('Dirección');


/*
3.CREAR CUENTA N°1 ADMINISTRADOR:

	CONTRASEÑA: password = $2a$12$7SW6dd16qcrYSdV0L4Uzp.qzCEe6ricYOH9fdr1r/bGlF2ItBun4a
	ENCRIPTADOR DE CONTRASEÑA: https://bcrypt-generator.com/

	USUARIO ADMINISTRADOR: 
		-USERNAME: admin
		-NOMBRE: ADMINISTRADOR
		-CONTRASEÑA: password
	
*/
INSERT INTO [ticketSystem].[dbo].[usuario] (is_active, nombre, password, username, id_rol_usuario, changed_pass)
		VALUES(1, 'ADMINISTRADOR', '$2a$12$7SW6dd16qcrYSdV0L4Uzp.qzCEe6ricYOH9fdr1r/bGlF2ItBun4a', 'admin', 3, 0);


/*
4. CREA FASES DE TICKET:
*/
INSERT INTO [ticketSystem].[dbo].[fase_ticket] ([nombre])
VALUES 
('Enviado'),
('Recepcionado - En Proceso'),
('Cerrado - Atendido'),
('Desestimado'),
('Espera en Dirección');
GO
/*
5. CREA TIPOS DE URGENCIA
*/
INSERT INTO [ticketSystem].[dbo].[clasificacion_urgencia] ([nombre], [is_active])
VALUES
('Baja', 1),
('Media', 1),
('Alta', 1);
GO
/*
6. CREA VISTA PARA FUNCIÓN EXPORTAR TICKETS
*/

USE [ticketSystem];
GO
CREATE VIEW vw_ticket_detalle as
SELECT 
    t.id AS id_ticket, 
    ft.nombre AS fase_ticket, 
    t.fecha AS fecha_ticket, 
    t.hora AS hora_ticket, 
    tu.nombre AS usuario_ticket, 
    ci.nombre AS clas_incidencia, 
    r.fecha AS fecha_recepcion, 
    r.hora AS hora_recepcion, 
    ru.nombre AS usuario_recepcion, 
    cu.nombre AS clas_urgencia, 
    s.fecha AS fecha_servicio, 
    s.hora AS hora_servicio, 
    su.nombre AS usuario_servicio, 
    cs.nombre AS clas_servicio, 
    DATEDIFF(SECOND, CAST(t.fecha AS DATETIME) + CAST(t.hora AS DATETIME), CAST(r.fecha AS DATETIME) + CAST(r.hora AS DATETIME)) AS tiempo_espera_recepcion, 
    DATEDIFF(SECOND, CAST(t.fecha AS DATETIME) + CAST(t.hora AS DATETIME), CAST(s.fecha AS DATETIME) + CAST(s.hora AS DATETIME)) AS tiempo_espera_servicio
FROM 
    dbo.ticket AS t 
    INNER JOIN dbo.usuario AS tu ON t.id_usuario = tu.id 
    INNER JOIN dbo.fase_ticket AS ft ON t.id_fase_ticket = ft.id 
    INNER JOIN dbo.clasificacion_incidencia AS ci ON t.id_clasificacion_incidencia = ci.id 
    LEFT OUTER JOIN dbo.recepcion AS r ON r.id_ticket = t.id 
    LEFT OUTER JOIN dbo.usuario AS ru ON r.id_usuario = ru.id 
    LEFT OUTER JOIN dbo.clasificacion_urgencia AS cu ON r.id_clasificacion_urgencia = cu.id 
    LEFT OUTER JOIN dbo.servicio AS s ON s.id_ticket = t.id 
    LEFT OUTER JOIN dbo.usuario AS su ON s.id_usuario = su.id 
    LEFT OUTER JOIN dbo.clasificacion_servicio AS cs ON s.id_clasificacion_servicio = cs.id;
GO


