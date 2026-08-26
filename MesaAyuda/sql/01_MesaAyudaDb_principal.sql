
DROP DATABASE IF EXISTS MesaAyudaDb;
CREATE DATABASE MesaAyudaDb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE MesaAyudaDb;


CREATE TABLE Rol (
    Id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(20) NOT NULL UNIQUE
);

INSERT INTO Rol (Id, Nombre) VALUES
    (1, 'SOLICITANTE'),
    (2, 'AGENTE'),
    (3, 'ADMINISTRADOR');

CREATE TABLE Usuario (
    Id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    Nombre   VARCHAR(100) NOT NULL,
    Correo   VARCHAR(150) NOT NULL UNIQUE,
    Password VARCHAR(64)  NOT NULL COMMENT 'SHA-256 hexadecimal',
    RolId    BIGINT NOT NULL,
    CONSTRAINT FK_Usuario_Rol
        FOREIGN KEY (RolId) REFERENCES Rol(Id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);


CREATE TABLE Categoria (
    Id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(60) NOT NULL UNIQUE
);

INSERT INTO Categoria (Id, Nombre) VALUES
    (1, 'Hardware'),
    (2, 'Software'),
    (3, 'Red'),
    (4, 'Mantenimiento');


CREATE TABLE Prioridad (
    Id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    Nombre   VARCHAR(20) NOT NULL UNIQUE,
    HorasSla INT NOT NULL
);

INSERT INTO Prioridad (Id, Nombre, HorasSla) VALUES
    (1, 'BAJA',    48),
    (2, 'MEDIA',   24),
    (3, 'ALTA',     8),
    (4, 'CRITICA',  2);


CREATE TABLE Ticket (
    Id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    Titulo         VARCHAR(150) NOT NULL,
    Descripcion    TEXT NOT NULL,
    Estado         VARCHAR(30) NOT NULL DEFAULT 'NUEVO',
    FechaCreacion  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FechaLimiteSla DATETIME NULL,
    CategoriaId    BIGINT NOT NULL,
    PrioridadId    BIGINT NOT NULL,
    SolicitanteId  BIGINT NOT NULL,
    AgenteId       BIGINT NULL,
    CodigoCierre   VARCHAR(6) NULL COMMENT 'OTP que el solicitante debe escribir para cerrar (reto adicional)',
    CONSTRAINT FK_Ticket_Categoria
        FOREIGN KEY (CategoriaId) REFERENCES Categoria(Id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT FK_Ticket_Prioridad
        FOREIGN KEY (PrioridadId) REFERENCES Prioridad(Id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT FK_Ticket_Solicitante
        FOREIGN KEY (SolicitanteId) REFERENCES Usuario(Id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT FK_Ticket_Agente
        FOREIGN KEY (AgenteId) REFERENCES Usuario(Id)
        ON UPDATE CASCADE ON DELETE SET NULL
);


CREATE TABLE Comentario (
    Id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    TicketId BIGINT NOT NULL,
    AutorId  BIGINT NOT NULL,
    Texto    TEXT NOT NULL,
    Fecha    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT FK_Comentario_Ticket
        FOREIGN KEY (TicketId) REFERENCES Ticket(Id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT FK_Comentario_Autor
        FOREIGN KEY (AutorId) REFERENCES Usuario(Id)
        ON UPDATE CASCADE ON DELETE RESTRICT
);


-- COMPLEMENTARIO PARA LA BASE DE DATOS

USE MesaAyudaDb;

ALTER TABLE Ticket
    ADD COLUMN FechaAsignacion DATETIME NULL AFTER FechaCreacion,
    ADD COLUMN FechaResolucion DATETIME NULL AFTER FechaAsignacion,
    ADD COLUMN FechaCierre     DATETIME NULL AFTER FechaResolucion;


CREATE INDEX IX_Ticket_Estado ON Ticket (Estado);

CREATE TABLE TicketHistorial (
    Id BIGINT AUTO_INCREMENT PRIMARY KEY,
    TicketId BIGINT NOT NULL,
    EstadoAnterior VARCHAR(30) NULL COMMENT 'NULL cuando el ticket se crea',
    EstadoNuevo VARCHAR(30) NOT NULL,
    UsuarioId BIGINT NOT NULL COMMENT 'Quien provoco la transicion',
    Fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT FK_TicketHistorial_Ticket
        FOREIGN KEY (TicketId)
        REFERENCES Ticket(Id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT FK_TicketHistorial_Usuario
        FOREIGN KEY (UsuarioId)
        REFERENCES Usuario(Id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);


CREATE TABLE Notificacion (
    Id BIGINT AUTO_INCREMENT PRIMARY KEY,
    TicketId BIGINT NOT NULL,
    DestinatarioId BIGINT NOT NULL,
    Canal VARCHAR(20) NOT NULL COMMENT 'CORREO | SMS | APLICACION',
    Asunto VARCHAR(150) NOT NULL,
    Mensaje TEXT NOT NULL,
    FechaEnvio DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    Leida TINYINT(1) NOT NULL DEFAULT 0,
    CONSTRAINT FK_Notificacion_Ticket
        FOREIGN KEY (TicketId)
        REFERENCES Ticket(Id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT FK_Notificacion_Destinatario
        FOREIGN KEY (DestinatarioId)
        REFERENCES Usuario(Id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE INDEX IX_Notificacion_Destinatario
    ON Notificacion (DestinatarioId, Leida);

-- =========================================================
-- 5. USUARIOS DE PRUEBA
-- Password de todos: 123456  (hash SHA-256 hexadecimal)
-- En Java se compara con MessageDigest.getInstance("SHA-256")
-- =========================================================
INSERT INTO Usuario (Id, Nombre, Correo, Password, RolId) VALUES
(1, 'Ana Ramirez',     'ana.ramirez@sena.edu.co',    SHA2('123456', 256), 1),
(2, 'Luis Torres',     'luis.torres@sena.edu.co',    SHA2('123456', 256), 1),
(3, 'Carlos Mendoza',  'carlos.mendoza@sena.edu.co', SHA2('123456', 256), 2),
(4, 'Diana Ruiz',      'diana.ruiz@sena.edu.co',     SHA2('123456', 256), 2),
(5, 'Jorge Pinilla',   'jorge.pinilla@sena.edu.co',  SHA2('123456', 256), 2),
(6, 'Osan Instructor', 'osan@sena.edu.co',           SHA2('123456', 256), 3);


INSERT INTO Ticket (Id, Titulo, Descripcion, Estado, FechaAsignacion,
                    FechaLimiteSla, CategoriaId, PrioridadId,
                    SolicitanteId, AgenteId) VALUES
(1, 'No hay internet en el aula 203',
    'Los equipos del aula 203 no tienen conexion desde la manana.',
    'ASIGNADO', NOW(), DATE_ADD(NOW(), INTERVAL 2 HOUR), 3, 4, 1, 3),
(2, 'Impresora no responde',
    'La impresora de coordinacion no imprime y muestra luz roja.',
    'EN_PROCESO', NOW(), DATE_ADD(NOW(), INTERVAL 24 HOUR), 1, 2, 2, 4),
(3, 'Solicitud de licencia de Office',
    'Necesito la licencia de Office para el equipo de la sala de docentes.',
    'NUEVO', NULL, DATE_ADD(NOW(), INTERVAL 48 HOUR), 2, 1, 1, NULL);

INSERT INTO TicketHistorial (TicketId, EstadoAnterior, EstadoNuevo, UsuarioId) VALUES
(1, NULL,       'NUEVO',      1),
(1, 'NUEVO',    'ASIGNADO',   6),
(2, NULL,       'NUEVO',      2),
(2, 'NUEVO',    'ASIGNADO',   6),
(2, 'ASIGNADO', 'EN_PROCESO', 4),
(3, NULL,       'NUEVO',      1);

INSERT INTO Comentario (TicketId, AutorId, Texto) VALUES
(2, 4, 'Revise el cable de red de la impresora, voy a reiniciar la cola de impresion.'),
(2, 2, 'Gracias, quedo atenta.');

INSERT INTO Notificacion (TicketId, DestinatarioId, Canal, Asunto, Mensaje) VALUES
(1, 1, 'APLICACION', 'Ticket #1 asignado',
    'Su ticket fue asignado al agente Carlos Mendoza.'),
(2, 2, 'APLICACION', 'Ticket #2 en proceso',
    'La agente Diana Ruiz inicio la atencion de su ticket.');

