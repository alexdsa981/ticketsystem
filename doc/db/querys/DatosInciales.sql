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
    ('Enviado'),
    ('Recepcionado - En Proceso'),
    ('Cerrado - Atendido');

-- Inserción de clasificaciones de servicio
INSERT INTO dbo.clasificacion_servicio (nombre) VALUES
    ('Incidencia resuelta'),
    ('Falta de equipos'),
    ('Delegado a proveedor'),
    ('Escalado a soporte técnico'),
    ('Requiere intervención externa');

-- Inserción de niveles de urgencia
INSERT INTO dbo.clasificacion_urgencia (nombre) VALUES
    ('Baja'),
    ('Media'),
    ('Alta');

-- Inserción de clasificaciones de incidencia
INSERT INTO dbo.clasificacion_incidencia (nombre) VALUES
    ('Computadora fallando'),
    ('Falta de tinta en impresora'),
    ('Error de conexión a internet'),
    ('Problema con software instalado'),
    ('Problema con acceso a servidor');

