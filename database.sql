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

CREATE TABLE Solicitudes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre_solicitante VARCHAR(100) NOT NULL,
    correo VARCHAR(100),
    telefono VARCHAR(20),
    direccion TEXT NOT NULL,
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

CREATE TABLE Horarios (
    id INT PRIMARY KEY AUTO_INCREMENT,
    fecha DATE NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    disponible BOOLEAN DEFAULT TRUE,
    id_solicitud INT,
    FOREIGN KEY (id_solicitud) REFERENCES Solicitudes(id),
    UNIQUE (fecha, hora_inicio, hora_fin) -- Evitar duplicados de horarios
);

CREATE TABLE Seguimiento_Reparaciones (
    id INT PRIMARY KEY AUTO_INCREMENT,
    id_solicitud INT NOT NULL,
    fecha_hora DATETIME DEFAULT CURRENT_TIMESTAMP,
    descripcion TEXT NOT NULL,
    id_usuario INT NOT NULL, -- usuario que registra el seguimiento
    FOREIGN KEY (id_solicitud) REFERENCES Solicitudes(id),
    FOREIGN KEY (id_usuario) REFERENCES Usuarios(id)
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

-- Roles
INSERT INTO Roles (nombre, descripcion) VALUES 
('Administrador', 'Control total del sistema'),
('Técnico', 'Encargado de realizar las reparaciones'),
('Empleado', 'Registra solicitudes y gestiona agenda');

-- Estados de solicitudes
INSERT INTO Estados_Solicitud (nombre, descripcion) VALUES 
('Pendiente', 'Solicitud registrada pero sin asignar'),
('Asignada', 'Solicitud asignada a un técnico'),
('En Proceso', 'Reparación iniciada'),
('Completada', 'Reparación finalizada con éxito'),
('Cancelada', 'Solicitud cancelada');


INSERT INTO Usuarios (usuario, nombre, correo, contrasena, id_rol) VALUES ('shiroh' ,'Shiroh', 'srshiroh@gmail.com', 'Shiroasd', 1);
INSERT INTO Usuarios (usuario, nombre, correo, contrasena, id_rol) VALUES ('shirohe' ,'Shiroh', 'shiroh@gmail.com', 'Shiroasd', 3);


INSERT INTO Solicitudes (nombre_solicitante, correo, telefono, direccion, fecha_programada, hora_programada, id_estado, id_usuario_registro)
VALUES ('Carlos Santos', 'Shiroh@example.com', '091662667', 'Idk', '24-07-2025', '19:25:00', 1, 3);
INSERT INTO Solicitudes (nombre_solicitante, correo, telefono, direccion, fecha_programada, hora_programada, id_estado, id_usuario_registro)
VALUES ('Carlos Santos', 'Juan@example.com', '091411872', 'nose', '27-02-2026', '14:25:00', 1, 3);
