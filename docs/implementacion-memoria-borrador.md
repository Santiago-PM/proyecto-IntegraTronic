# 7. Implementación

## 7.1 Preparación del entorno

Para el desarrollo de IntegraTronic se preparó un entorno formado por backend, frontend y base de datos. El control de versiones se realizó con Git y GitHub, trabajando mediante ramas y Pull Requests para separar los cambios y revisarlos antes de integrarlos en la rama principal.

El backend se desarrolló con Java y Spring Boot. Para editar el código se utilizó Visual Studio Code, junto con terminal PowerShell para ejecutar comandos de Git y arrancar el proyecto. La base de datos utilizada fue MySQL, ya que encaja bien con un sistema ERP basado en entidades relacionadas.

El frontend se implementó con Expo / React Native Web, lo que permite trabajar con una misma base de componentes y probar la aplicación desde el navegador durante el desarrollo.

## 7.2 Implementación del backend

El backend se organizó siguiendo una arquitectura por capas. Esta estructura ayuda a separar responsabilidades y facilita el mantenimiento del proyecto.

Las capas principales son:

- `model`: contiene las entidades del dominio.
- `repository`: contiene los repositorios de acceso a datos.
- `service`: contiene la lógica de negocio.
- `controller`: expone los endpoints REST.
- `dto`: define los objetos usados para entrada y salida de datos en la API.

Los módulos implementados en el backend son:

- Usuarios y roles.
- Productos y familias.
- Stock y movimientos de stock.
- Ventas físicas.
- Clientes.
- Pedidos online.
- Pagos online.
- Informes básicos.
- Login básico.

Primero se implementaron entidades, repositorios y servicios básicos. Después se crearon controladores REST y DTOs para evitar devolver directamente las entidades en la API. Finalmente, se desarrollaron algunos flujos de negocio principales, como registrar una venta física, registrar un pedido online y registrar un pago online.

## 7.3 Implementación de la base de datos

La base de datos utilizada es MySQL. El backend usa JPA e Hibernate para trabajar con las entidades y sus relaciones. Las tablas se corresponden con los módulos principales del sistema.

Las relaciones más importantes del modelo son:

- Usuario-Rol.
- Producto-FamiliaProducto.
- Producto-Stock.
- Producto-MovimientoStock.
- Venta-LineaVenta.
- Cliente-PedidoOnline.
- PedidoOnline-LineaPedido.
- PedidoOnline-PagoOnline.

Estas relaciones permiten representar las operaciones básicas de la tienda. Por ejemplo, un producto pertenece a una familia, tiene un stock asociado y puede aparecer en ventas físicas o pedidos online.

## 7.4 Implementación del frontend

El frontend se implementó con Expo / React Native Web. La aplicación contiene pantallas para los módulos principales del sistema:

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

Durante la implementación se ajustó la interfaz al alcance real del proyecto. Esto fue importante para evitar que la aplicación mostrara funciones que todavía no existen en el backend, como informes avanzados, seguridad completa o funcionalidades fuera del alcance del TFG.

## 7.5 Integración frontend-backend

La comunicación entre frontend y backend se realiza mediante una API REST. El frontend llama a los endpoints del backend para consultar, crear y actualizar datos.

Para permitir la conexión desde Expo Web en local se configuró CORS en el backend. De esta forma, el navegador puede realizar peticiones al backend durante el desarrollo.

Las pantallas principales del frontend consumen endpoints reales del backend. Por ejemplo, las pantallas de productos, stock, ventas, pedidos, clientes, usuarios e informes trabajan con datos obtenidos desde la API.

## 7.6 Implementación del login básico

El login se implementó de forma básica para permitir el acceso funcional al sistema. El frontend envía email y contraseña al endpoint:

```text
POST /api/auth/login
```

El backend busca el usuario por email, comprueba que esté activo y valida la contraseña. Si el login es correcto, devuelve los datos básicos que necesita el frontend:

- Identificador del usuario.
- Nombre.
- Email.
- Nombre del rol.

Esta implementación permite iniciar sesión y utilizar la aplicación, pero no incluye todavía JWT ni protección avanzada de endpoints. La seguridad completa queda como mejora futura.

## 7.7 Pruebas realizadas

Durante el desarrollo se realizaron pruebas manuales y funcionales para comprobar que las partes principales funcionaban correctamente.

Entre las pruebas realizadas se incluyen:

- Compilación del backend.
- Arranque del backend con Spring Boot.
- Prueba de endpoints REST.
- Login correcto con usuario de prueba.
- Login incorrecto devolviendo error `401 Unauthorized`.
- Acceso al dashboard.
- Navegación por pantallas principales.
- Conexión entre frontend y backend.

Estas pruebas permitieron validar que el sistema es funcional en local y que los módulos principales están integrados.

## 7.8 Estado final de la implementación

El proyecto cuenta con una versión funcional formada por backend, frontend y base de datos. El backend expone una API REST organizada por módulos, el frontend consume esos endpoints y MySQL almacena los datos del sistema.

Actualmente el sistema permite gestionar productos, familias, stock, movimientos, ventas físicas, clientes, pedidos online, pagos online, usuarios, roles e informes básicos. También dispone de login básico para acceder al frontend.

El estado actual permite presentar una versión funcional del proyecto, cubriendo los módulos principales previstos y manteniendo un nivel de complejidad razonable.

## 7.9 Limitaciones y mejoras futuras

Aunque el proyecto es funcional, quedan mejoras que se podrían implementar en futuras versiones:

- Seguridad completa con JWT.
- Permisos por rol.
- Protección de endpoints en backend.
- Mejorar la entrada de mercancía con una operación transaccional.
- Revisar vulnerabilidades npm relacionadas con dependencias de Expo.
- Mejorar los informes con filtros y consultas más avanzadas.
- Preparar datos finales y limpiar datos de prueba antes de la entrega.


