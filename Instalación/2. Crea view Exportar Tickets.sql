USE [helpdesk_ipor];
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
    au.nombre AS usuario_atencion, 
    ca.nombre AS clas_atencion, 
    DATEDIFF(SECOND, CAST(t.fecha AS DATETIME) + CAST(t.hora AS DATETIME), CAST(r.fecha AS DATETIME) + CAST(r.hora AS DATETIME)) AS tiempo_espera_recepcion, 
    DATEDIFF(SECOND, CAST(t.fecha AS DATETIME) + CAST(t.hora AS DATETIME), CAST(a.fecha AS DATETIME) + CAST(a.hora AS DATETIME)) AS tiempo_espera_atencion
FROM 
    dbo.ticket AS t 
    INNER JOIN dbo.usuario AS tu ON t.id_usuario = tu.id 
    INNER JOIN dbo.fase_ticket AS ft ON t.id_fase_ticket = ft.id 
	LEFT OUTER JOIN dbo.atencion AS a ON a.id_ticket = t.id 
	INNER JOIN dbo.tipo_incidencia AS ti ON a.id_tipo_incidencia = ti.id
    LEFT OUTER JOIN dbo.recepcion AS r ON r.id_ticket = t.id 
    LEFT OUTER JOIN dbo.usuario AS ru ON r.id_usuario = ru.id 
    LEFT OUTER JOIN dbo.clasificacion_urgencia AS cu ON a.id_clasificacion_urgencia = cu.id 
    LEFT OUTER JOIN dbo.usuario AS au ON a.id_usuario = au.id 
    LEFT OUTER JOIN dbo.clasificacion_atencion AS ca ON a.id_clasificacion_atencion = ca.id;
GO
