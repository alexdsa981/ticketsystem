USE [ticketSystem]
GO
/****** Object:  Table [dbo].[clasificacion_incidencia]    Script Date: 4/11/2024 22:10:06 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[clasificacion_incidencia](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[is_active] [bit] NOT NULL,
	[nombre] [varchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[clasificacion_servicio]    Script Date: 4/11/2024 22:10:06 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[clasificacion_servicio](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[is_active] [bit] NOT NULL,
	[nombre] [varchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[clasificacion_urgencia]    Script Date: 4/11/2024 22:10:06 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[clasificacion_urgencia](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[is_active] [bit] NOT NULL,
	[nombre] [varchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[fase_ticket]    Script Date: 4/11/2024 22:10:06 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[fase_ticket](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[nombre] [varchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[recepcion]    Script Date: 4/11/2024 22:10:06 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[recepcion](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[fecha] [date] NOT NULL,
	[hora] [time](7) NOT NULL,
	[mensaje] [varchar](max) NULL,
	[id_clasificacion_urgencia] [bigint] NOT NULL,
	[id_ticket] [bigint] NOT NULL,
	[id_usuario] [bigint] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[servicio]    Script Date: 4/11/2024 22:10:06 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[servicio](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[descripcion] [varchar](max) NULL,
	[fecha] [date] NOT NULL,
	[hora] [time](7) NOT NULL,
	[id_clasificacion_servicio] [bigint] NOT NULL,
	[id_ticket] [bigint] NOT NULL,
	[id_usuario] [bigint] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[ticket]    Script Date: 4/11/2024 22:10:06 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ticket](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[descripcion] [varchar](max) NULL,
	[fecha] [date] NOT NULL,
	[hora] [time](7) NOT NULL,
	[id_clasificacion_incidencia] [bigint] NOT NULL,
	[id_fase_ticket] [bigint] NOT NULL,
	[id_usuario] [bigint] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[usuario]    Script Date: 4/11/2024 22:10:06 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[usuario](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[is_active] [bit] NOT NULL,
	[nombre] [varchar](255) NOT NULL,
	[password] [varchar](255) NOT NULL,
	[username] [varchar](255) NOT NULL,
	[id_rol_usuario] [bigint] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  View [dbo].[vw_ticket_detalle]    Script Date: 4/11/2024 22:10:06 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[vw_ticket_detalle] AS
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
    
    -- Tiempo de espera desde la creación del ticket hasta la recepción
    DATEDIFF(MINUTE, CAST(t.fecha AS DATETIME) + CAST(t.hora AS DATETIME), 
                    CAST(r.fecha AS DATETIME) + CAST(r.hora AS DATETIME)) AS tiempo_espera_recepcion,
    
    -- Tiempo de espera desde la creación del ticket hasta el servicio
    DATEDIFF(MINUTE, CAST(t.fecha AS DATETIME) + CAST(t.hora AS DATETIME), 
                    CAST(s.fecha AS DATETIME) + CAST(s.hora AS DATETIME)) AS tiempo_espera_servicio

FROM ticket t
JOIN usuario tu ON t.id_usuario = tu.id
JOIN fase_ticket ft ON t.id_fase_ticket = ft.id
JOIN clasificacion_incidencia ci ON t.id_clasificacion_incidencia = ci.id
LEFT JOIN recepcion r ON r.id_ticket = t.id
LEFT JOIN usuario ru ON r.id_usuario = ru.id
LEFT JOIN clasificacion_urgencia cu ON r.id_clasificacion_urgencia = cu.id
LEFT JOIN servicio s ON s.id_ticket = t.id
LEFT JOIN usuario su ON s.id_usuario = su.id
LEFT JOIN clasificacion_servicio cs ON s.id_clasificacion_servicio = cs.id;
GO
/****** Object:  Table [dbo].[archivo_adjunto]    Script Date: 4/11/2024 22:10:06 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[archivo_adjunto](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[archivo] [varbinary](max) NULL,
	[nombre] [varchar](255) NULL,
	[peso_contenido] [float] NULL,
	[tipo_contenido] [varchar](255) NULL,
	[id_ticket] [bigint] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[rol_usuario]    Script Date: 4/11/2024 22:10:06 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[rol_usuario](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[nombre] [varchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
SET IDENTITY_INSERT [dbo].[clasificacion_incidencia] ON 
GO
INSERT [dbo].[clasificacion_incidencia] ([id], [is_active], [nombre]) VALUES (1, 1, N'Relacionado a Impresora')
GO
SET IDENTITY_INSERT [dbo].[clasificacion_incidencia] OFF
GO
SET IDENTITY_INSERT [dbo].[clasificacion_servicio] ON 
GO
INSERT [dbo].[clasificacion_servicio] ([id], [is_active], [nombre]) VALUES (1, 1, N'Incidencia Resuelta')
GO
SET IDENTITY_INSERT [dbo].[clasificacion_servicio] OFF
GO
SET IDENTITY_INSERT [dbo].[clasificacion_urgencia] ON 
GO
INSERT [dbo].[clasificacion_urgencia] ([id], [is_active], [nombre]) VALUES (1, 1, N'Baja')
GO
INSERT [dbo].[clasificacion_urgencia] ([id], [is_active], [nombre]) VALUES (2, 1, N'Media')
GO
INSERT [dbo].[clasificacion_urgencia] ([id], [is_active], [nombre]) VALUES (3, 1, N'Alta')
GO
SET IDENTITY_INSERT [dbo].[clasificacion_urgencia] OFF
GO
SET IDENTITY_INSERT [dbo].[fase_ticket] ON 
GO
INSERT [dbo].[fase_ticket] ([id], [nombre]) VALUES (1, N'Enviado')
GO
INSERT [dbo].[fase_ticket] ([id], [nombre]) VALUES (2, N'Recepcionado - En Proceso')
GO
INSERT [dbo].[fase_ticket] ([id], [nombre]) VALUES (3, N'Cerrado - Atendido')
GO
INSERT [dbo].[fase_ticket] ([id], [nombre]) VALUES (4, N'Desactivado')
GO
SET IDENTITY_INSERT [dbo].[fase_ticket] OFF
GO
SET IDENTITY_INSERT [dbo].[recepcion] ON 
GO
INSERT [dbo].[recepcion] ([id], [fecha], [hora], [mensaje], [id_clasificacion_urgencia], [id_ticket], [id_usuario]) VALUES (1, CAST(N'2024-11-04' AS Date), CAST(N'20:31:03.1666667' AS Time), N'recepcion1', 3, 4, 3)
GO
INSERT [dbo].[recepcion] ([id], [fecha], [hora], [mensaje], [id_clasificacion_urgencia], [id_ticket], [id_usuario]) VALUES (2, CAST(N'2024-11-04' AS Date), CAST(N'20:31:08.8166667' AS Time), N'recepcion2', 1, 2, 3)
GO
INSERT [dbo].[recepcion] ([id], [fecha], [hora], [mensaje], [id_clasificacion_urgencia], [id_ticket], [id_usuario]) VALUES (3, CAST(N'2024-11-04' AS Date), CAST(N'20:48:28.8200000' AS Time), N'aaa', 1, 3, 3)
GO
SET IDENTITY_INSERT [dbo].[recepcion] OFF
GO
SET IDENTITY_INSERT [dbo].[rol_usuario] ON 
GO
INSERT [dbo].[rol_usuario] ([id], [nombre]) VALUES (1, N'Usuario')
GO
INSERT [dbo].[rol_usuario] ([id], [nombre]) VALUES (2, N'Soporte')
GO
INSERT [dbo].[rol_usuario] ([id], [nombre]) VALUES (3, N'Admin')
GO
SET IDENTITY_INSERT [dbo].[rol_usuario] OFF
GO
SET IDENTITY_INSERT [dbo].[servicio] ON 
GO
INSERT [dbo].[servicio] ([id], [descripcion], [fecha], [hora], [id_clasificacion_servicio], [id_ticket], [id_usuario]) VALUES (1, N'servicio 1', CAST(N'2024-11-04' AS Date), CAST(N'20:31:24.0700000' AS Time), 1, 2, 3)
GO
INSERT [dbo].[servicio] ([id], [descripcion], [fecha], [hora], [id_clasificacion_servicio], [id_ticket], [id_usuario]) VALUES (2, N'atendido 2047', CAST(N'2024-11-04' AS Date), CAST(N'20:47:45.6200000' AS Time), 1, 4, 3)
GO
SET IDENTITY_INSERT [dbo].[servicio] OFF
GO
SET IDENTITY_INSERT [dbo].[ticket] ON 
GO
INSERT [dbo].[ticket] ([id], [descripcion], [fecha], [hora], [id_clasificacion_incidencia], [id_fase_ticket], [id_usuario]) VALUES (1, N'prueba 1', CAST(N'2024-11-04' AS Date), CAST(N'20:20:45.5766667' AS Time), 1, 1, 2)
GO
INSERT [dbo].[ticket] ([id], [descripcion], [fecha], [hora], [id_clasificacion_incidencia], [id_fase_ticket], [id_usuario]) VALUES (2, N'prueba 2', CAST(N'2024-11-04' AS Date), CAST(N'20:20:51.2166667' AS Time), 1, 3, 2)
GO
INSERT [dbo].[ticket] ([id], [descripcion], [fecha], [hora], [id_clasificacion_incidencia], [id_fase_ticket], [id_usuario]) VALUES (3, N'prueba 3', CAST(N'2024-11-04' AS Date), CAST(N'20:20:56.6066667' AS Time), 1, 2, 2)
GO
INSERT [dbo].[ticket] ([id], [descripcion], [fecha], [hora], [id_clasificacion_incidencia], [id_fase_ticket], [id_usuario]) VALUES (4, N'prueba 4', CAST(N'2024-11-04' AS Date), CAST(N'20:21:03.3333333' AS Time), 1, 3, 2)
GO
SET IDENTITY_INSERT [dbo].[ticket] OFF
GO
SET IDENTITY_INSERT [dbo].[usuario] ON 
GO
INSERT [dbo].[usuario] ([id], [is_active], [nombre], [password], [username], [id_rol_usuario]) VALUES (1, 1, N'admin', N'$2a$12$KHutHxci1z3u.9Hm77UgeupxbDYh0ecZ3YcYgTMUdPkdbwAhq0R1G', N'admin', 3)
GO
INSERT [dbo].[usuario] ([id], [is_active], [nombre], [password], [username], [id_rol_usuario]) VALUES (2, 1, N'Usuario de Prueba', N'$2a$10$fWvjxpDitX8z8VkECnyACOJPcHfHk3KmRejWaK8uMSLV6io53Qybm', N'usuario', 1)
GO
INSERT [dbo].[usuario] ([id], [is_active], [nombre], [password], [username], [id_rol_usuario]) VALUES (3, 1, N'Soporte de Prueba', N'$2a$10$HDj.abLmLD2NhCxd8OLFfe8IGXpuUXOV9oG6lAfyEJXDaQNDgHK4K', N'soporte', 2)
GO
SET IDENTITY_INSERT [dbo].[usuario] OFF
GO
/****** Object:  Index [UKt5849vpx5gb4s8ba51duom2pa]    Script Date: 4/11/2024 22:10:06 ******/
ALTER TABLE [dbo].[recepcion] ADD  CONSTRAINT [UKt5849vpx5gb4s8ba51duom2pa] UNIQUE NONCLUSTERED 
(
	[id_ticket] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
/****** Object:  Index [UKciptbrpqn648rj7k3s5lni8en]    Script Date: 4/11/2024 22:10:06 ******/
ALTER TABLE [dbo].[servicio] ADD  CONSTRAINT [UKciptbrpqn648rj7k3s5lni8en] UNIQUE NONCLUSTERED 
(
	[id_ticket] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [UK863n1y3x0jalatoir4325ehal]    Script Date: 4/11/2024 22:10:06 ******/
ALTER TABLE [dbo].[usuario] ADD  CONSTRAINT [UK863n1y3x0jalatoir4325ehal] UNIQUE NONCLUSTERED 
(
	[username] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
ALTER TABLE [dbo].[archivo_adjunto]  WITH CHECK ADD  CONSTRAINT [FKawc0clcs08njnjeggc5vuf9n4] FOREIGN KEY([id_ticket])
REFERENCES [dbo].[ticket] ([id])
GO
ALTER TABLE [dbo].[archivo_adjunto] CHECK CONSTRAINT [FKawc0clcs08njnjeggc5vuf9n4]
GO
ALTER TABLE [dbo].[recepcion]  WITH CHECK ADD  CONSTRAINT [FK4xm4wq0enewlcutimlh0o6btf] FOREIGN KEY([id_clasificacion_urgencia])
REFERENCES [dbo].[clasificacion_urgencia] ([id])
GO
ALTER TABLE [dbo].[recepcion] CHECK CONSTRAINT [FK4xm4wq0enewlcutimlh0o6btf]
GO
ALTER TABLE [dbo].[recepcion]  WITH CHECK ADD  CONSTRAINT [FK5v68bgx977uifqppjt547do8o] FOREIGN KEY([id_ticket])
REFERENCES [dbo].[ticket] ([id])
GO
ALTER TABLE [dbo].[recepcion] CHECK CONSTRAINT [FK5v68bgx977uifqppjt547do8o]
GO
ALTER TABLE [dbo].[recepcion]  WITH CHECK ADD  CONSTRAINT [FKif9p6gwku1t1t3ij5w6es1jlp] FOREIGN KEY([id_usuario])
REFERENCES [dbo].[usuario] ([id])
GO
ALTER TABLE [dbo].[recepcion] CHECK CONSTRAINT [FKif9p6gwku1t1t3ij5w6es1jlp]
GO
ALTER TABLE [dbo].[servicio]  WITH CHECK ADD  CONSTRAINT [FK82y7oo0rm1so5sr7t8e84egpp] FOREIGN KEY([id_usuario])
REFERENCES [dbo].[usuario] ([id])
GO
ALTER TABLE [dbo].[servicio] CHECK CONSTRAINT [FK82y7oo0rm1so5sr7t8e84egpp]
GO
ALTER TABLE [dbo].[servicio]  WITH CHECK ADD  CONSTRAINT [FKbish5bxr49xxb6nf392np7acl] FOREIGN KEY([id_ticket])
REFERENCES [dbo].[ticket] ([id])
GO
ALTER TABLE [dbo].[servicio] CHECK CONSTRAINT [FKbish5bxr49xxb6nf392np7acl]
GO
ALTER TABLE [dbo].[servicio]  WITH CHECK ADD  CONSTRAINT [FKeoek0n1le7c90p9nskm9a3hqf] FOREIGN KEY([id_clasificacion_servicio])
REFERENCES [dbo].[clasificacion_servicio] ([id])
GO
ALTER TABLE [dbo].[servicio] CHECK CONSTRAINT [FKeoek0n1le7c90p9nskm9a3hqf]
GO
ALTER TABLE [dbo].[ticket]  WITH CHECK ADD  CONSTRAINT [FK3k34ulxchhavnak8fitnwn0u7] FOREIGN KEY([id_clasificacion_incidencia])
REFERENCES [dbo].[clasificacion_incidencia] ([id])
GO
ALTER TABLE [dbo].[ticket] CHECK CONSTRAINT [FK3k34ulxchhavnak8fitnwn0u7]
GO
ALTER TABLE [dbo].[ticket]  WITH CHECK ADD  CONSTRAINT [FK8lb6f8fy37ai997ky8fvvftak] FOREIGN KEY([id_fase_ticket])
REFERENCES [dbo].[fase_ticket] ([id])
GO
ALTER TABLE [dbo].[ticket] CHECK CONSTRAINT [FK8lb6f8fy37ai997ky8fvvftak]
GO
ALTER TABLE [dbo].[ticket]  WITH CHECK ADD  CONSTRAINT [FKcmcylhedvw17xrxepgpy0trnm] FOREIGN KEY([id_usuario])
REFERENCES [dbo].[usuario] ([id])
GO
ALTER TABLE [dbo].[ticket] CHECK CONSTRAINT [FKcmcylhedvw17xrxepgpy0trnm]
GO
ALTER TABLE [dbo].[usuario]  WITH CHECK ADD  CONSTRAINT [FKc21092qbiwyg7qct9epgpvj1k] FOREIGN KEY([id_rol_usuario])
REFERENCES [dbo].[rol_usuario] ([id])
GO
ALTER TABLE [dbo].[usuario] CHECK CONSTRAINT [FKc21092qbiwyg7qct9epgpvj1k]
GO
