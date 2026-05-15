# Guia de uso del backend - IntegraTronic

## 1. Introduccion

Esta guia resume como arrancar y probar el backend de IntegraTronic. Sirve como apoyo para el desarrollo del frontend y para comprobar que la API REST funciona correctamente.

La URL base local del backend es:

```text
http://localhost:8080
```

Los ejemplos usan PowerShell e `Invoke-RestMethod`.

## 2. Requisitos previos

- Java 21.
- MySQL 8.4.9.
- Visual Studio Code o terminal PowerShell.
- Base de datos `integratronic_db` creada en MySQL.
- Proyecto clonado desde GitHub.
- Configuracion de conexion a MySQL revisada en `application.properties`.

## 3. Actualizar main

Para usar una version estable del proyecto, se recomienda partir de `main` actualizado:

```powershell
git checkout main
git pull origin main
git status
```

`git status` deberia indicar que no hay cambios pendientes antes de empezar a probar.

## 4. Arrancar el backend

Desde la carpeta del backend:

```powershell
cd backend
$env:DB_PASSWORD="Admin1234."
.\mvnw.cmd spring-boot:run
```

Si tienes Maven instalado globalmente, tambien puedes usar:

```powershell
mvn spring-boot:run
```

Cuando el backend arranque correctamente, la API quedara disponible en:

```text
http://localhost:8080
```

## 5. Orden recomendado para crear datos de prueba

Para evitar errores por relaciones obligatorias, se recomienda crear datos en este orden:

1. Familia de producto.
2. Producto.
3. Stock del producto.
4. Rol.
5. Usuario.
6. Venta fisica.
7. Cliente.
8. Pedido online.
9. Pago online.
10. Informe de ventas.

## 6. Pruebas con Invoke-RestMethod

### Crear familia de producto

```powershell
$body = @{
  nombre = "Portatiles"
  descripcion = "Ordenadores portatiles"
} | ConvertTo-Json

Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/familias-producto" -ContentType "application/json" -Body $body
```

### Crear producto

```powershell
$body = @{
  sku = "PORT-001"
  nombre = "Portatil Lenovo prueba"
  descripcion = "Portatil de prueba"
  precio = 699.99
  activo = $true
  idFamilia = 1
} | ConvertTo-Json

Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/productos" -ContentType "application/json" -Body $body
```

### Crear stock

```powershell
$body = @{
  cantidadDisponible = 10
  stockMinimo = 2
  idProducto = 1
} | ConvertTo-Json

Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/stocks" -ContentType "application/json" -Body $body
```

### Crear rol

```powershell
$body = @{
  nombreRol = "VENDEDOR"
  descripcion = "Usuario encargado de registrar ventas fisicas"
} | ConvertTo-Json

Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/roles" -ContentType "application/json" -Body $body
```

### Crear usuario

```powershell
$body = @{
  nombre = "Vendedor Prueba"
  email = "vendedor@integratronic.com"
  passwordHash = "prueba123"
  activo = $true
  idRol = 1
} | ConvertTo-Json

Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/usuarios" -ContentType "application/json" -Body $body
```

Nota: las respuestas de usuario no devuelven `passwordHash`.

### Registrar venta fisica

```powershell
$body = @{
  idUsuario = 1
  descuento = 50.00
  lineas = @(
    @{
      idProducto = 1
      cantidad = 2
    }
  )
} | ConvertTo-Json -Depth 5

Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/ventas/registrar" -ContentType "application/json" -Body $body
```

Este flujo comprueba stock, calcula subtotales, aplica descuento, guarda la venta, reduce stock y registra un movimiento de stock de tipo `SALIDA`.

### Crear cliente

```powershell
$body = @{
  nombre = "Cliente Online Prueba"
  email = "clienteonline@integratronic.com"
  telefono = "600111222"
} | ConvertTo-Json

Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/clientes" -ContentType "application/json" -Body $body
```

### Registrar pedido online

```powershell
$body = @{
  idCliente = 1
  lineas = @(
    @{
      idProducto = 1
      cantidad = 1
    }
  )
} | ConvertTo-Json -Depth 5

Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/pedidos-online/registrar" -ContentType "application/json" -Body $body
```

Este flujo comprueba cliente, productos y stock. Despues calcula el total, guarda el pedido, guarda las lineas, reduce stock y registra un movimiento `SALIDA`. No crea pago automaticamente.

### Registrar pago online

El importe debe coincidir con el total del pedido.

```powershell
$body = @{
  idPedido = 1
  importe = 699.99
  estadoPago = "PAGADO"
  metodoPago = "TARJETA"
} | ConvertTo-Json

Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/pagos-online/registrar" -ContentType "application/json" -Body $body
```

Este flujo registra internamente el pago. No usa una pasarela externa, no modifica stock y evita pagos duplicados para el mismo pedido.

### Consultar informe de ventas

```powershell
Invoke-RestMethod -Method Get -Uri "http://localhost:8080/api/informes/ventas"
```

Devuelve totales e importes de ventas fisicas, pedidos online y pagos online.

## 7. Notas para frontend

- La API trabaja con JSON.
- Los endpoints de alta y modificacion usan DTOs de entrada.
- Las respuestas usan DTOs de salida.
- Los IDs relacionados deben existir antes de usarse. Por ejemplo, un producto necesita una familia existente.
- Si una relacion no existe o los datos no son validos, normalmente se devuelve `400 Bad Request`.
- Si se consulta un registro inexistente por ID, normalmente se devuelve `404 Not Found`.
- La URL base local para consumir la API es `http://localhost:8080`.

## 8. Limitaciones actuales

- No hay login implementado.
- No hay JWT ni control de permisos por rol.
- El campo `passwordHash` se envia como texto de prueba; el cifrado se deja para una fase posterior.
- El pago online es un registro interno, no una integracion real con pasarela externa.
- No se generan tickets, comprobantes, PDF, Excel ni graficos.
- Los informes son basicos y calculan totales generales.

## 9. Errores comunes

- `400 Bad Request`: suele indicar que falta un campo obligatorio, que una relacion no existe o que no hay stock suficiente.
- `404 Not Found`: el ID consultado no existe.
- Error de conexion a MySQL: revisar que MySQL este arrancado y que exista la base de datos `integratronic_db`.
- Puerto ocupado: comprobar si ya hay otra aplicacion usando `8080`.
- Error al registrar pago: revisar que el importe coincida con el total del pedido y que el pedido no tenga ya un pago registrado.
- Error al registrar venta o pedido: revisar que exista stock suficiente para los productos indicados.
