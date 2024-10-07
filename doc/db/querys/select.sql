SELECT * FROM dbo.archivo_adjunto;
SELECT * FROM dbo.clasificacion_incidencia;
SELECT * FROM dbo.clasificacion_servicio;
SELECT * FROM dbo.clasificacion_urgencia;
SELECT * FROM dbo.fase_ticket;

SELECT * FROM dbo.rol_usuario;




SELECT * FROM dbo.usuario;
SELECT * FROM dbo.ticket;
SELECT * FROM dbo.recepcion;
SELECT * FROM dbo.servicio;

SELECT * FROM recepcion r INNER JOIN ticket t ON r.id_ticket = t.id;
SELECT * FROM recepcion r INNER JOIN ticket t ON r.id_ticket = t.id where t.id_usuario = 1;

SELECT * FROM recepcion r INNER JOIN ticket t ON r.id_ticket = t.id where t.id_usuario = 1;