DROP DATABASE sistema_reparaciones_ac;
CREATE DATABASE IF NOT EXISTS sistema_reparaciones_ac;
USE sistema_reparaciones_ac;

CREATE TABLE Roles (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    descripcion VARCHAR(255)
);

CREATE TABLE Usuarios (
    id INT PRIMARY KEY AUTO_INCREMENT,
    usuario VARCHAR(100) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(100) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    contrasena VARCHAR(255) NOT NULL,
    id_rol INT NOT NULL,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    ultimo_acceso DATETIME,
    activo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id_rol) REFERENCES Roles(id)
);

CREATE TABLE Estados_Solicitud (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL, -- pendiente, en proceso, completada, cancelada
    descripcion VARCHAR(255)
);

CREATE TABLE Clientes (
	id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    nombre_completo VARCHAR(255) NOT NULL,
    correo VARCHAR(100),
    telefono VARCHAR(20),
    direccion TEXT NOT NULL
);

CREATE TABLE Solicitudes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    id_cliente INT NOT NULL,
    fecha_solicitud DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_programada DATE NOT NULL,
    hora_programada TIME NOT NULL,
    id_estado INT NOT NULL,
    notas TEXT,
    id_usuario_registro INT NOT NULL, -- empleado que registró la solicitud
    id_tecnico INT, -- técnico asignado a la reparación
    FOREIGN KEY (id_estado) REFERENCES Estados_Solicitud(id),
    FOREIGN KEY (id_usuario_registro) REFERENCES Usuarios(id),
    FOREIGN KEY (id_tecnico) REFERENCES Usuarios(id)
);

CREATE TABLE Reportes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    titulo VARCHAR(100) NOT NULL,
    descripcion TEXT,
    fecha_generacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    id_usuario INT NOT NULL, -- usuario que genero el reporte
    contenido LONGTEXT, -- podría almacenar formato JSON o XML para el reporte
    FOREIGN KEY (id_usuario) REFERENCES Usuarios(id)
);

INSERT INTO Roles (nombre, descripcion) VALUES
('Administrador', 'Control total del sistema'),
('Técnico', 'Encargado de realizar las reparaciones'),
('Empleado', 'Registra solicitudes y gestiona agenda');

INSERT INTO Estados_Solicitud (nombre, descripcion) VALUES
('Pendiente', 'Solicitud registrada pero sin asignar'),
('Asignada', 'Solicitud asignada a un técnico'),
('En Proceso', 'Reparación iniciada'),
('Completada', 'Reparación finalizada con éxito'),
('Cancelada', 'Solicitud cancelada');

INSERT INTO Usuarios (usuario, nombre, correo, contrasena, id_rol) VALUES ('shiroh' ,'Shiroh', 'srshiroh@gmail.com', '$2a$10$u8ugkNFw3R.uxC2Ps.87uec3sSsGiDnI8.09Ih/8rTpsn/Se0CNr.', 2);
INSERT INTO Usuarios (usuario, nombre, correo, contrasena, id_rol) VALUES ('facu' ,'Facundo', 'facundo@gmail.com', '$2a$10$9yU.Pa5VBxFhKt7q.tkBNecS0.OjQHDcsQyjKElnH6ysmBnn8Bq26', 2);

INSERT INTO Solicitudes (nombre_solicitante, correo, telefono, direccion, fecha_programada, hora_programada, id_estado, id_usuario_registro)
VALUES ('Carlos Santos', 'Shiroh@example.com', '091662667', 'Idk', '2025-07-24', '19:25:00', 1, 1);
INSERT INTO Solicitudes (nombre_solicitante, correo, telefono, direccion, fecha_programada, hora_programada, id_estado, id_usuario_registro)
VALUES ('Carlos Santos', 'Juan@example.com', '091411872', 'nose', '2026-02-27', '14:25:00', 1, 1);
INSERT INTO Solicitudes (nombre_solicitante, correo, telefono, direccion, fecha_programada, hora_programada, id_estado, id_usuario_registro)
VALUES ('Marta González', 'marta.g@example.com', '095432187', 'Calle Uruguay 456', '2025-09-15', '14:00:00', 2, 2);
INSERT INTO Solicitudes (nombre_solicitante, correo, telefono, direccion, fecha_programada, hora_programada, id_estado, id_usuario_registro)
VALUES ('Juan Rodríguez', 'juan.rodriguez@example.com', '098765432', 'Boulevard Artigas 789', '2025-10-05', '09:00:00', 1, 1);
INSERT INTO Solicitudes (nombre_solicitante, correo, telefono, direccion, fecha_programada, hora_programada, id_estado, id_usuario_registro)
VALUES ('Ana María Silva', 'ana.silva@example.com', '097654321', 'Avenida Italia 1010', '2025-11-12', '16:45:00', 3, 2);
INSERT INTO Solicitudes (nombre_solicitante, correo, telefono, direccion, fecha_programada, hora_programada, id_estado, id_usuario_registro)
VALUES ('Roberto Méndez', 'roberto.mendez@example.com', '096543210', 'Calle Colonia 222', '2026-01-08', '10:15:00', 1, 1);
INSERT INTO Solicitudes (nombre_solicitante, correo, telefono, direccion, fecha_programada, hora_programada, id_estado, id_usuario_registro)
VALUES ('Lucía Fernández', 'lucia.f@example.com', '099876123', 'Rambla República 3030', '2026-02-14', '15:30:00', 2, 2);
INSERT INTO Solicitudes (nombre_solicitante, correo, telefono, direccion, fecha_programada, hora_programada, id_estado, id_usuario_registro)
VALUES ('Carlos Santos', 'carlos.santos@example.com', '091234567', 'Plaza Independencia 150', '2025-12-24', '11:00:00', 1, 1);
INSERT INTO Solicitudes (nombre_solicitante, correo, telefono, direccion, fecha_programada, hora_programada, id_estado, id_usuario_registro)
VALUES ('Daniela Torres', 'daniela.t@example.com', '098765123', 'Avenida 8 de Octubre 456', '2026-03-18', '13:45:00', 4, 2);
INSERT INTO Solicitudes (nombre_solicitante, correo, telefono, direccion, fecha_programada, hora_programada, id_estado, id_usuario_registro)
VALUES ('Eduardo Castro', 'eduardo.castro@example.com', '095123456', 'Calle Ejido 789', '2025-09-30', '17:00:00', 1, 1);
INSERT INTO Solicitudes (nombre_solicitante, correo, telefono, direccion, fecha_programada, hora_programada, id_estado, id_usuario_registro)
VALUES ('Patricia López', 'patricia.l@example.com', '092345678', 'Bulevar España 1234', '2025-10-22', '09:30:00', 3, 2);
INSERT INTO Solicitudes (nombre_solicitante, correo, telefono, direccion, fecha_programada, hora_programada, id_estado, id_usuario_registro)
VALUES ('Miguel Ángel Ruiz', 'miguel.ruiz@example.com', '098765444', 'Calle Rivera 555', '2026-04-05', '14:15:00', 1, 1);
INSERT INTO Solicitudes (nombre_solicitante, correo, telefono, direccion, fecha_programada, hora_programada, id_estado, id_usuario_registro)
VALUES ('Gabriela Sosa', 'gabriela.s@example.com', '097654222', 'Avenida 18 de Julio 987', '2026-01-19', '10:45:00', 2, 2);
INSERT INTO Solicitudes (nombre_solicitante, correo, telefono, direccion, fecha_programada, hora_programada, id_estado, id_usuario_registro)
VALUES ('Fernando Martínez', 'fernando.m@example.com', '091112233', 'Calle Maldonado 321', '2025-11-28', '16:00:00', 1, 1);
INSERT INTO Solicitudes (nombre_solicitante, correo, telefono, direccion, fecha_programada, hora_programada, id_estado, id_usuario_registro)
VALUES ('Valeria Gómez', 'valeria.g@example.com', '099887766', 'Plaza Varela 456', '2026-02-03', '11:30:00', 5, 2);
INSERT INTO Solicitudes (nombre_solicitante, correo, telefono, direccion, fecha_programada, hora_programada, id_estado, id_usuario_registro)
VALUES ('Alejandro Pérez', 'alejandro.p@example.com', '095544332', 'Avenida General Rivera 789', '2025-08-20', '15:00:00', 1, 1);
INSERT INTO Solicitudes (nombre_solicitante, correo, telefono, direccion, fecha_programada, hora_programada, id_estado, id_usuario_registro)
VALUES ('Carolina Díaz', 'carolina.d@example.com', '098123456', 'Calle Soriano 123', '2025-12-10', '09:15:00', 2, 2);
INSERT INTO Solicitudes (nombre_solicitante, correo, telefono, direccion, fecha_programada, hora_programada, id_estado, id_usuario_registro)
VALUES ('Diego Ferreiro', 'diego.f@example.com', '097654987', 'Bulevar Artigas 1500', '2026-03-27', '16:30:00', 1, 1);
INSERT INTO Solicitudes (nombre_solicitante, correo, telefono, direccion, fecha_programada, hora_programada, id_estado, id_usuario_registro)
VALUES ('Laura Mendoza', 'laura.m@example.com', '091598762', 'Avenida Brasil 654', '2026-05-15', '10:00:00', 3, 2);
INSERT INTO Solicitudes (nombre_solicitante, correo, telefono, direccion, fecha_programada, hora_programada, id_estado, id_usuario_registro)
VALUES ('Ricardo Benítez', 'ricardo.b@example.com', '099876545', 'Calle Constituyente 987', '2025-10-18', '13:30:00', 1, 1);
INSERT INTO Solicitudes (nombre_solicitante, correo, telefono, direccion, fecha_programada, hora_programada, id_estado, id_usuario_registro)
VALUES ('Natalia Viera', 'natalia.v@example.com', '098234567', 'Plaza Cagancha 789', '2026-01-25', '15:45:00', 4, 2);