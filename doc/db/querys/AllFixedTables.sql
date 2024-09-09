USE ticketsIPOR;
GO
select * from rol_usuario;
GO
select * from fase_ticket;
GO
select * from clasificacion_incidencia;
GO
select * from clasificacion_servicio;
GO
select * from clasificacion_urgencia;
GO
select * from archivo_adjunto;
GO
select * from recepcion;
GO
select * from servicio;
GO
select * from ticket;
GO
select * from usuario;
GO

INSERT INTO rol_usuario (nombre)
VALUES ('Usuario'),
       ('Admin'),
       ('Soporte');
GO

-- password = "password"
INSERT INTO usuario (nombre, password, username, id_rol_usuario)
VALUES 
    ('usuario', '$2a$12$5U7q9Zq.ZPV7nMYcUDVTXenbC8FJ8IXqjDoGQIJF6yxk2d/.l98xS', 'usuario', (SELECT id FROM rol_usuario WHERE nombre = 'Usuario')),
    ('admin', '$2a$12$5U7q9Zq.ZPV7nMYcUDVTXenbC8FJ8IXqjDoGQIJF6yxk2d/.l98xS', 'admin', (SELECT id FROM rol_usuario WHERE nombre = 'Admin')),
    ('soporte', '$2a$12$5U7q9Zq.ZPV7nMYcUDVTXenbC8FJ8IXqjDoGQIJF6yxk2d/.l98xS', 'soporte', (SELECT id FROM rol_usuario WHERE nombre = 'Soporte'));
	GO
