
SET IDENTITY_INSERT [clasificacion_incidencia] ON 

INSERT [clasificacion_incidencia] ([id], [is_active], [nombre]) VALUES (1, 1, N'Relacionado a Impresora')
SET IDENTITY_INSERT [clasificacion_incidencia] OFF
GO
SET IDENTITY_INSERT [fase_ticket] ON 

INSERT [fase_ticket] ([id], [nombre]) VALUES (1, N'Enviado')
INSERT [fase_ticket] ([id], [nombre]) VALUES (2, N'Recepcionado - En Proceso')
INSERT [fase_ticket] ([id], [nombre]) VALUES (3, N'Cerrado - Atendido')
INSERT [fase_ticket] ([id], [nombre]) VALUES (4, N'Desactivado')
SET IDENTITY_INSERT [fase_ticket] OFF
GO
SET IDENTITY_INSERT [rol_usuario] ON 

INSERT [rol_usuario] ([id], [nombre]) VALUES (1, N'Usuario')
INSERT [rol_usuario] ([id], [nombre]) VALUES (2, N'Soporte')
INSERT [rol_usuario] ([id], [nombre]) VALUES (3, N'Admin')
SET IDENTITY_INSERT [rol_usuario] OFF
GO
SET IDENTITY_INSERT [usuario] ON 

INSERT [usuario] ([id], [is_active], [nombre], [password], [username], [id_rol_usuario]) VALUES (1, 1, N'admin', N'$2a$12$KHutHxci1z3u.9Hm77UgeupxbDYh0ecZ3YcYgTMUdPkdbwAhq0R1G', N'admin', 3)
INSERT [usuario] ([id], [is_active], [nombre], [password], [username], [id_rol_usuario]) VALUES (2, 1, N'Usuario de Prueba', N'$2a$10$fWvjxpDitX8z8VkECnyACOJPcHfHk3KmRejWaK8uMSLV6io53Qybm', N'usuario', 1)
INSERT [usuario] ([id], [is_active], [nombre], [password], [username], [id_rol_usuario]) VALUES (3, 1, N'Soporte de Prueba', N'$2a$10$HDj.abLmLD2NhCxd8OLFfe8IGXpuUXOV9oG6lAfyEJXDaQNDgHK4K', N'soporte', 2)
SET IDENTITY_INSERT [usuario] OFF
GO
SET IDENTITY_INSERT [clasificacion_servicio] ON 

INSERT [clasificacion_servicio] ([id], [is_active], [nombre]) VALUES (1, 1, N'Incidencia Resuelta')
SET IDENTITY_INSERT [clasificacion_servicio] OFF
GO
SET IDENTITY_INSERT [clasificacion_urgencia] ON 

INSERT [clasificacion_urgencia] ([id], [is_active], [nombre]) VALUES (1, 1, N'Baja')
INSERT [clasificacion_urgencia] ([id], [is_active], [nombre]) VALUES (2, 1, N'Media')
INSERT [clasificacion_urgencia] ([id], [is_active], [nombre]) VALUES (3, 1, N'Alta')
SET IDENTITY_INSERT [clasificacion_urgencia] OFF
GO
