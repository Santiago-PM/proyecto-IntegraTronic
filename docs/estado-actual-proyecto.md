# Estado actual del proyecto IntegraTronic

## 1. Resumen general

IntegraTronic es un ERP para una tienda física y online de productos informáticos. El proyecto está formado por un backend desarrollado con Java y Spring Boot, un frontend desarrollado con Expo / React Native Web y una base de datos MySQL.

El objetivo actual del proyecto es disponer de una versión funcional y defendible para el TFG, manteniendo un alcance realista. La aplicación permite gestionar productos, familias, stock, ventas físicas, clientes, pedidos online, pagos online, usuarios, roles e informes básicos.

## 2. Backend implementado

El backend está organizado siguiendo una arquitectura por capas:

- `controller`
- `service`
- `repository`
- `dto`
- `model`

Actualmente se han implementado entidades y repositorios para los siguientes módulos:

- Usuarios y roles.
- Productos y familias de productos.
- Stock.
- Movimientos de stock.
- Ventas físicas.
- Líneas de venta.
- Clientes.
- Pedidos online.
- Líneas de pedido.
- Pagos online.

También existen servicios básicos para estos módulos, controladores REST y DTOs de entrada y salida para controlar los datos que se reciben y devuelven por la API.

Además de las operaciones CRUD básicas, el backend incluye estos flujos de negocio:

- Venta física mediante `POST /api/ventas/registrar`.
- Pedido online mediante `POST /api/pedidos-online/registrar`.
- Pago online mediante `POST /api/pagos-online/registrar`.

El sistema también incluye un informe básico mediante:

- `GET /api/informes/ventas`

Este informe devuelve totales e importes de ventas físicas, pedidos online y pagos online.

También se ha configurado CORS para permitir la conexión desde el frontend local y desde Expo Web.

Por último, se ha implementado un login básico mediante:

- `POST /api/auth/login`

## 3. Frontend implementado

El frontend está desarrollado con Expo / React Native Web. La interfaz incluye pantallas para los módulos principales del proyecto:

- Login.
- Dashboard.
- Productos.
- Familias.
- Consulta de stock.
- Entrada de mercancía.
- Venta física.
- Pedidos online.
- Clientes.
- Usuarios.
- Informes.

La interfaz se ha ajustado al alcance real del proyecto, evitando mostrar funcionalidades avanzadas que no están implementadas en el backend.

## 4. Integración frontend-backend

El frontend consume la API REST expuesta por el backend en local. El backend se ejecuta normalmente en `http://localhost:8080` y el frontend en Expo Web.

La configuración CORS permite que el frontend local pueda llamar a las rutas `/api/**` del backend. Las pantallas principales del frontend consumen endpoints reales para obtener y registrar datos.

## 5. Login básico

Se ha creado un login básico funcional. El frontend envía email y contraseña al endpoint `POST /api/auth/login`.

El backend valida el usuario contra la tabla de usuarios, comprueba que el usuario esté activo y valida la contraseña. Si el login es correcto, devuelve:

- `idUsuario`
- `nombre`
- `email`
- `nombreRol`

Este login permite el acceso funcional al frontend, pero todavía no es un sistema completo con JWT. La seguridad avanzada y la protección real de endpoints quedan pendientes para una fase posterior.

## 6. Pruebas realizadas

Se han realizado pruebas funcionales básicas sobre el proyecto:

- Compilación del backend.
- Arranque de Spring Boot.
- Respuesta de endpoints REST.
- Login correcto con usuario de prueba.
- Login incorrecto devolviendo `401 Unauthorized`.
- Acceso al dashboard desde el frontend.
- Carga de pantallas principales.
- Comprobación de la conexión frontend-backend.

No se incluyen contraseñas reales ni datos sensibles en esta documentación.

## 7. Pendientes

Quedan pendientes algunos puntos de mejora antes de considerar el proyecto completamente cerrado:

- Implementar JWT completo.
- Aplicar permisos reales por rol.
- Proteger endpoints en backend.






