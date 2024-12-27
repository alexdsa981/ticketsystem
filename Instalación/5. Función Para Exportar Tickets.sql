
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


