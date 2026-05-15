CREATE DATABASE IF NOT EXISTS integratronic_db;
USE integratronic_db;

-- Datos de prueba para IntegraTronic
-- Este archivo está pensado para facilitar la corrección del proyecto.
-- Primero debe arrancarse el backend al menos una vez para que Hibernate cree las tablas.

-- Roles
INSERT INTO rol (nombre_rol, descripcion)
SELECT 'Administrador', 'Rol de administrador para pruebas'
WHERE NOT EXISTS (
    SELECT 1 FROM rol WHERE nombre_rol = 'Administrador'
);

INSERT INTO rol (nombre_rol, descripcion)
SELECT 'Vendedor', 'Rol de vendedor para pruebas'
WHERE NOT EXISTS (
    SELECT 1 FROM rol WHERE nombre_rol = 'Vendedor'
);

INSERT INTO rol (nombre_rol, descripcion)
SELECT 'Gestor de stock', 'Rol de gestor de stock para pruebas'
WHERE NOT EXISTS (
    SELECT 1 FROM rol WHERE nombre_rol = 'Gestor de stock'
);

-- Usuario administrador de prueba
-- La contraseña inicial es admin123.
-- En el primer login correcto, el backend la migrará automáticamente a BCrypt.
INSERT INTO usuario (nombre, email, password_hash, activo, id_rol)
SELECT
    'Admin Prueba',
    'admin@integratronic.com',
    'admin123',
    TRUE,
    r.id_rol
FROM rol r
WHERE r.nombre_rol = 'Administrador'
AND NOT EXISTS (
    SELECT 1 FROM usuario WHERE email = 'admin@integratronic.com'
);

-- Familias de productos
INSERT INTO familia_producto (nombre, descripcion)
SELECT 'Portátiles', 'Ordenadores portátiles de diferentes gamas'
WHERE NOT EXISTS (
    SELECT 1 FROM familia_producto WHERE nombre = 'Portátiles'
);

INSERT INTO familia_producto (nombre, descripcion)
SELECT 'Componentes', 'Componentes internos para equipos informáticos'
WHERE NOT EXISTS (
    SELECT 1 FROM familia_producto WHERE nombre = 'Componentes'
);

INSERT INTO familia_producto (nombre, descripcion)
SELECT 'Periféricos', 'Teclados, ratones, monitores y otros accesorios'
WHERE NOT EXISTS (
    SELECT 1 FROM familia_producto WHERE nombre = 'Periféricos'
);

-- Productos de prueba
INSERT INTO producto (sku, nombre, descripcion, precio, activo, id_familia)
SELECT
    'PORT-001',
    'Portátil Lenovo ThinkBook',
    'Portátil de prueba para el catálogo',
    699.99,
    TRUE,
    f.id_familia
FROM familia_producto f
WHERE f.nombre = 'Portátiles'
AND NOT EXISTS (
    SELECT 1 FROM producto WHERE sku = 'PORT-001'
);

INSERT INTO producto (sku, nombre, descripcion, precio, activo, id_familia)
SELECT
    'COMP-001',
    'Disco SSD 1TB',
    'Unidad SSD de prueba para stock',
    89.99,
    TRUE,
    f.id_familia
FROM familia_producto f
WHERE f.nombre = 'Componentes'
AND NOT EXISTS (
    SELECT 1 FROM producto WHERE sku = 'COMP-001'
);

INSERT INTO producto (sku, nombre, descripcion, precio, activo, id_familia)
SELECT
    'PER-001',
    'Ratón inalámbrico',
    'Periférico de prueba',
    24.99,
    TRUE,
    f.id_familia
FROM familia_producto f
WHERE f.nombre = 'Periféricos'
AND NOT EXISTS (
    SELECT 1 FROM producto WHERE sku = 'PER-001'
);

-- Stock inicial
INSERT INTO stock (cantidad_disponible, stock_minimo, id_producto)
SELECT 10, 2, p.id_producto
FROM producto p
WHERE p.sku = 'PORT-001'
AND NOT EXISTS (
    SELECT 1 FROM stock WHERE id_producto = p.id_producto
);

INSERT INTO stock (cantidad_disponible, stock_minimo, id_producto)
SELECT 25, 5, p.id_producto
FROM producto p
WHERE p.sku = 'COMP-001'
AND NOT EXISTS (
    SELECT 1 FROM stock WHERE id_producto = p.id_producto
);

INSERT INTO stock (cantidad_disponible, stock_minimo, id_producto)
SELECT 40, 8, p.id_producto
FROM producto p
WHERE p.sku = 'PER-001'
AND NOT EXISTS (
    SELECT 1 FROM stock WHERE id_producto = p.id_producto
);

-- Cliente de prueba
INSERT INTO cliente (nombre, email, telefono)
SELECT
    'Cliente Prueba',
    'cliente.prueba@integratronic.com',
    '600000000'
WHERE NOT EXISTS (
    SELECT 1 FROM cliente WHERE email = 'cliente.prueba@integratronic.com'
);
