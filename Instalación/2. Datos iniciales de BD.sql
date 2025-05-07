/*
2.CREA ROLES DE USUARIO:
*/

INSERT INTO [ticketSystem].[dbo].[rol_usuario] ([nombre])
VALUES 
('Usuario'),
('Soporte'),
('Admin');


/*
3.CREAR CUENTA N�1 ADMINISTRADOR:

	CONTRASE�A: password = $2a$12$7SW6dd16qcrYSdV0L4Uzp.qzCEe6ricYOH9fdr1r/bGlF2ItBun4a
	ENCRIPTADOR DE CONTRASE�A: https://bcrypt-generator.com/

	USUARIO ADMINISTRADOR: 
		-USERNAME: admin
		-NOMBRE: ADMINISTRADOR
		-CONTRASE�A: password
	
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
('Desestimado');
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

INSERT INTO clasificacion_desestimacion(nombre,is_active)
VALUES ('Usuario Desactivado', 1);
GO


/*
6. CREA VISTA PARA FUNCI�N EXPORTAR TICKETS
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
    ti.nombre AS clas_incidencia, 
    r.fecha AS fecha_recepcion, 
    r.hora AS hora_recepcion, 
    ru.nombre AS usuario_recepcion, 
    cu.nombre AS clas_urgencia, 
    a.fecha AS fecha_atencion, 
    a.hora AS hora_atencion, 
    su.nombre AS usuario_atencion, 
    ca.nombre AS clas_atencion, 
    DATEDIFF(SECOND, CAST(t.fecha AS DATETIME) + CAST(t.hora AS DATETIME), CAST(r.fecha AS DATETIME) + CAST(r.hora AS DATETIME)) AS tiempo_espera_recepcion, 
    DATEDIFF(SECOND, CAST(t.fecha AS DATETIME) + CAST(t.hora AS DATETIME), CAST(a.fecha AS DATETIME) + CAST(a.hora AS DATETIME)) AS tiempo_espera_atencion
FROM 
    dbo.ticket AS t 
    INNER JOIN dbo.usuario AS tu ON t.id_usuario = tu.id 
    INNER JOIN dbo.fase_ticket AS ft ON t.id_fase_ticket = ft.id 
    INNER JOIN dbo.tipo_incidencia AS ti ON t.id_tipo_incidencia = ti.id
    LEFT OUTER JOIN dbo.recepcion AS r ON r.id_ticket = t.id 
    LEFT OUTER JOIN dbo.usuario AS ru ON r.id_usuario = ru.id 
    LEFT OUTER JOIN dbo.clasificacion_urgencia AS cu ON r.id_clasificacion_urgencia = cu.id 
    LEFT OUTER JOIN dbo.atencion AS a ON a.id_ticket = t.id 
    LEFT OUTER JOIN dbo.usuario AS au ON a.id_usuario = au.id 
    LEFT OUTER JOIN dbo.clasificacion_atencion AS ca ON a.id_clasificacion_atencion = ca.id;
GO


