USE ticketsIPOR;
GO
-- Inserci�n de roles 
INSERT INTO dbo.rol_usuario (nombre) VALUES
    ('Usuario'),
    ('Soporte'),
    ('Admin');

-- Inserci�n de usuarios
INSERT INTO dbo.usuario (nombre, password, username, id_rol_usuario) VALUES
    ('usuario', '$2a$12$KHutHxci1z3u.9Hm77UgeupxbDYh0ecZ3YcYgTMUdPkdbwAhq0R1G', 'usuario', 1),
    ('soporte', '$2a$12$KHutHxci1z3u.9Hm77UgeupxbDYh0ecZ3YcYgTMUdPkdbwAhq0R1G', 'soporte', 2),
    ('admin', '$2a$12$KHutHxci1z3u.9Hm77UgeupxbDYh0ecZ3YcYgTMUdPkdbwAhq0R1G', 'admin', 3);

-- Inserci�n de fases
INSERT INTO dbo.fase_ticket (nombre) VALUES
    ('Enviado'),
    ('Recepcionado - En Proceso'),
    ('Cerrado - Atendido');

-- Inserci�n de clasificaciones de servicio
INSERT INTO dbo.clasificacion_servicio (nombre) VALUES
    ('Incidencia resuelta'),
    ('Falta de equipos'),
    ('Delegado a proveedor'),
    ('Escalado a soporte t�cnico'),
    ('Requiere intervenci�n externa');

-- Inserci�n de niveles de urgencia
INSERT INTO dbo.clasificacion_urgencia (nombre) VALUES
    ('Baja'),
    ('Media'),
    ('Alta');

-- Inserci�n de clasificaciones de incidencia
INSERT INTO dbo.clasificacion_incidencia (nombre) VALUES
    ('Computadora fallando'),
    ('Falta de tinta en impresora'),
    ('Error de conexi�n a internet'),
    ('Problema con software instalado'),
    ('Problema con acceso a servidor');

