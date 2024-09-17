USE ticketsIPOR;
GO
-- Inserción de roles 
INSERT INTO dbo.rol_usuario (nombre) VALUES
    ('Usuario'),
    ('Soporte'),
    ('Admin');

-- Inserción de usuarios
INSERT INTO dbo.usuario (nombre, password, username, id_rol_usuario) VALUES
    ('usuario', '$2a$12$KHutHxci1z3u.9Hm77UgeupxbDYh0ecZ3YcYgTMUdPkdbwAhq0R1G', 'usuario', 1),
    ('soporte', '$2a$12$KHutHxci1z3u.9Hm77UgeupxbDYh0ecZ3YcYgTMUdPkdbwAhq0R1G', 'soporte', 2),
    ('admin', '$2a$12$KHutHxci1z3u.9Hm77UgeupxbDYh0ecZ3YcYgTMUdPkdbwAhq0R1G', 'admin', 3);

-- Inserción de fases
INSERT INTO dbo.fase_ticket (nombre) VALUES
    ('Recepcionado'),
    ('Recepcionado - En Proceso'),
    ('Cerrado');

-- Inserción de clasificaciones de servicio
INSERT INTO dbo.clasificacion_servicio (nombre) VALUES
    ('Solucionado'),
    ('Falta de equipos'),
    ('Delegado a proveedor'),
    ('Escalado a soporte técnico'),
    ('Requiere intervención externa');

-- Inserción de niveles de urgencia
INSERT INTO dbo.clasificacion_urgencia (nombre) VALUES
    ('Alta'),
    ('Media'),
    ('Baja');

-- Inserción de clasificaciones de incidencia
INSERT INTO dbo.clasificacion_incidencia (nombre) VALUES
    ('Computadora fallando'),
    ('Falta de tinta en impresora'),
    ('Error de conexión a internet'),
    ('Problema con software instalado'),
    ('Problema con acceso a servidor');

-- Inserción de tickets ficticios en la tabla ticket
INSERT INTO dbo.ticket (descripcion, fecha, hora, id_clasificacion_incidencia, id_fase_ticket, id_usuario) VALUES
    ('Computadora no enciende', '2024-09-16', '09:30:00', 1, 1, 1),  -- Usuario "usuario"
    ('Error de impresora', '2024-09-16', '10:00:00', 2, 1, 1),        -- Usuario "usuario"
    ('Problema con software', '2024-09-16', '10:15:00', 4, 1, 2),     -- Usuario "soporte"
    ('Computadora no conecta a internet', '2024-09-16', '10:45:00', 3, 1, 1), -- Usuario "usuario"
    ('Problema de servidor', '2024-09-16', '11:00:00', 5, 1, 2),      -- Usuario "soporte"
    ('Falta de tinta en impresora', '2024-09-16', '11:15:00', 2, 1, 1), -- Usuario "usuario"
    ('Problema de acceso remoto', '2024-09-16', '11:30:00', 4, 1, 2), -- Usuario "soporte"
    ('Teclado no funciona', '2024-09-16', '12:00:00', 1, 1, 1),       -- Usuario "usuario"
    ('Pantalla con problemas de brillo', '2024-09-16', '12:15:00', 3, 1, 1), -- Usuario "usuario"
    ('Problema al iniciar sesión', '2024-09-16', '12:30:00', 5, 1, 3); -- Usuario "admin"

-- Inserción de recepciones ficticias en la tabla recepcion
INSERT INTO dbo.recepcion (fecha, hora, mensaje, id_clasificacion_urgencia, id_ticket, id_usuario) VALUES
    ('2024-09-16', '10:45:00', 'Recibido y en proceso', 1, 1, 2), -- Recepcionado por "soporte", ticket 1
    ('2024-09-16', '11:15:00', 'Recibido y en proceso', 2, 2, 2), -- Recepcionado por "soporte", ticket 2
    ('2024-09-16', '11:45:00', 'Recibido y en proceso', 3, 4, 2), -- Recepcionado por "soporte", ticket 4
    ('2024-09-16', '12:15:00', 'Recibido y en proceso', 2, 6, 2), -- Recepcionado por "soporte", ticket 6
    ('2024-09-16', '12:30:00', 'Recibido y en proceso', 1, 9, 2); -- Recepcionado por "soporte", ticket 9

-- Inserción de servicios ficticios en la tabla servicio
INSERT INTO dbo.servicio (descripcion, fecha, hora, id_clasificacion_atencion, id_ticket, id_usuario) VALUES
    ('Reemplazo de equipo', '2024-09-16', '14:00:00', 1, 1, 2), -- Cerrado por "soporte", ticket 1
    ('Cambio de piezas', '2024-09-16', '14:30:00', 1, 4, 2),   -- Cerrado por "soporte", ticket 4
    ('Solución de problema de pantalla', '2024-09-16', '15:00:00', 1, 9, 2); -- Cerrado por "soporte", ticket 9

-- Actualización de tickets a fase 2 (Recepcionado)
UPDATE dbo.ticket SET id_fase_ticket = 2 WHERE id IN (1, 2, 4, 6, 9);

-- Actualización de tickets a fase 3 (Cerrado)
UPDATE dbo.ticket SET id_fase_ticket = 3 WHERE id IN (1, 4, 9);

-- Inserción de archivos adjuntos ficticios en la tabla archivo_adjunto

-- Para ticket 1 (dos archivos)
INSERT INTO dbo.archivo_adjunto (archivo, nombre, tipo_contenido, id_ticket) VALUES
    (0x255044462D312E350A25E2E3CFD30A312030206F626A0A3C3C2F4C656E6774682031, 'ticket1_documento.pdf', 'application/pdf', 1),
    (0xFFD8FFE000104A46494600010101004800480000FFDB00430008060607060508070707, 'ticket1_imagen.jpg', 'image/jpeg', 1);

-- Para ticket 2
INSERT INTO dbo.archivo_adjunto (archivo, nombre, tipo_contenido, id_ticket) VALUES
    (0x255044462D312E350A25E2E3CFD30A312030206F626A0A3C3C2F4C656E6774682031, 'ticket2_documento.pdf', 'application/pdf', 2);

-- Para ticket 3
INSERT INTO dbo.archivo_adjunto (archivo, nombre, tipo_contenido, id_ticket) VALUES
    (0xFFD8FFE000104A46494600010101004800480000FFDB00430008060607060508070707, 'ticket3_imagen.jpg', 'image/jpeg', 3);

-- Para ticket 4
INSERT INTO dbo.archivo_adjunto (archivo, nombre, tipo_contenido, id_ticket) VALUES
    (0x255044462D312E350A25E2E3CFD30A312030206F626A0A3C3C2F4C656E6774682031, 'ticket4_documento.pdf', 'application/pdf', 4);
