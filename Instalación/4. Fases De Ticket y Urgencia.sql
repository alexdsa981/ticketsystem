INSERT INTO [ticketSystem].[dbo].[fase_ticket] ([nombre])
VALUES 
('Enviado'),
('Recepcionado - En Proceso'),
('Cerrado - Atendido'),
('Desestimado'),
('Espera en Direcci�n');
GO
INSERT INTO [ticketSystem].[dbo].[clasificacion_urgencia] ([nombre])
VALUES
('Baja'),
('Media'),
('Alta');
GO