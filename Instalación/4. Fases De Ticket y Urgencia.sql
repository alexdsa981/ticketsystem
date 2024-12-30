INSERT INTO [ticketSystem].[dbo].[fase_ticket] ([nombre])
VALUES 
('Enviado'),
('Recepcionado - En Proceso'),
('Cerrado - Atendido'),
('Desestimado'),
('Espera en Dirección');
GO
INSERT INTO [ticketSystem].[dbo].[clasificacion_urgencia] ([nombre], [is_active])
VALUES
('Baja', 1),
('Media', 1),
('Alta', 1);
GO