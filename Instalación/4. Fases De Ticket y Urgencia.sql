INSERT INTO [ticketSystem].[dbo].[fase_ticket] ([nombre])
VALUES 
('Enviado'),
('Recepcionado - En Proceso'),
('Cerrado - Atendido'),
('Desestimado'),
('Espera en Dirección');
GO
INSERT INTO [ticketSystem].[dbo].[clasificacion_urgencia] ([nombre])
VALUES
('Baja'),
('Media'),
('Alta');
GO