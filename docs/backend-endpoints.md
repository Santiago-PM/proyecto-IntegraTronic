# Endpoints del backend - IntegraTronic

El backend de IntegraTronic expone una API REST organizada por modulos. Esta documentacion resume los endpoints actuales para facilitar el desarrollo del frontend y la redaccion de la memoria del TFG.

Base URL local:

```text
http://localhost:8080
```

Los ejemplos usan formato JSON.

## 1. Roles

Ruta base:

```text
/api/roles
```

Endpoints:

- `GET /api/roles`
- `GET /api/roles/{id}`
- `POST /api/roles`
- `PUT /api/roles/{id}`
- `DELETE /api/roles/{id}`

Ejemplo `POST /api/roles`:

```json
{
  "nombreRol": "VENDEDOR",
  "descripcion": "Usuario encargado de registrar ventas fisicas"
}
```

## 2. Usuarios

Ruta base:

```text
/api/usuarios
```

Endpoints:

- `GET /api/usuarios`
- `GET /api/usuarios/{id}`
- `POST /api/usuarios`
- `PUT /api/usuarios/{id}`
- `DELETE /api/usuarios/{id}`

Ejemplo `POST /api/usuarios`:

```json
{
  "nombre": "Vendedor Prueba",
  "email": "vendedor@integratronic.com",
  "passwordHash": "prueba123",
  "activo": true,
  "idRol": 1
}
```

Nota: `UsuarioResponseDTO` no devuelve `passwordHash`.

## 3. Familias de producto

Ruta base:

```text
/api/familias-producto
```

Endpoints:

- `GET /api/familias-producto`
- `GET /api/familias-producto/{id}`
- `POST /api/familias-producto`
- `PUT /api/familias-producto/{id}`
- `DELETE /api/familias-producto/{id}`

Ejemplo `POST /api/familias-producto`:

```json
{
  "nombre": "Portatiles",
  "descripcion": "Ordenadores portatiles"
}
```

## 4. Productos

Ruta base:

```text
/api/productos
```

Endpoints:

- `GET /api/productos`
- `GET /api/productos/{id}`
- `POST /api/productos`
- `PUT /api/productos/{id}`
- `DELETE /api/productos/{id}`

Ejemplo `POST /api/productos`:

```json
{
  "sku": "PORT-001",
  "nombre": "Portatil Lenovo prueba",
  "descripcion": "Portatil de prueba",
  "precio": 699.99,
  "activo": true,
  "idFamilia": 1
}
```

## 5. Stock

Ruta base:

```text
/api/stocks
```

Endpoints:

- `GET /api/stocks`
- `GET /api/stocks/{id}`
- `POST /api/stocks`
- `PUT /api/stocks/{id}`
- `DELETE /api/stocks/{id}`
- `PUT /api/stocks/{id}/incrementar?cantidad=...`
- `PUT /api/stocks/{id}/reducir?cantidad=...`

Ejemplo `POST /api/stocks`:

```json
{
  "cantidadDisponible": 10,
  "stockMinimo": 2,
  "idProducto": 1
}
```

## 6. Movimientos de stock

Ruta base:

```text
/api/movimientos-stock
```

Endpoints:

- `GET /api/movimientos-stock`
- `GET /api/movimientos-stock/{id}`
- `POST /api/movimientos-stock`
- `PUT /api/movimientos-stock/{id}`
- `DELETE /api/movimientos-stock/{id}`

Ejemplo `POST /api/movimientos-stock`:

```json
{
  "tipoMovimiento": "ENTRADA",
  "cantidad": 5,
  "fechaMovimiento": "2026-05-15T10:00:00",
  "descripcion": "Entrada manual de stock",
  "idProducto": 1
}
```

## 7. Ventas fisicas

Ruta base:

```text
/api/ventas
```

Endpoints CRUD:

- `GET /api/ventas`
- `GET /api/ventas/{id}`
- `POST /api/ventas`
- `PUT /api/ventas/{id}`
- `DELETE /api/ventas/{id}`

Endpoint de flujo de negocio:

- `POST /api/ventas/registrar`

Ejemplo `POST /api/ventas/registrar`:

```json
{
  "idUsuario": 1,
  "descuento": 50.00,
  "lineas": [
    {
      "idProducto": 1,
      "cantidad": 2
    }
  ]
}
```

Este flujo:

- Comprueba stock.
- Calcula subtotales.
- Aplica descuento.
- Guarda venta y lineas.
- Reduce stock.
- Registra `MovimientoStock` como `SALIDA`.

## 8. Lineas de venta

Ruta base:

```text
/api/lineas-venta
```

Endpoints:

- `GET /api/lineas-venta`
- `GET /api/lineas-venta/{id}`
- `POST /api/lineas-venta`
- `PUT /api/lineas-venta/{id}`
- `DELETE /api/lineas-venta/{id}`

## 9. Clientes

Ruta base:

```text
/api/clientes
```

Endpoints:

- `GET /api/clientes`
- `GET /api/clientes/{id}`
- `POST /api/clientes`
- `PUT /api/clientes/{id}`
- `DELETE /api/clientes/{id}`

Ejemplo `POST /api/clientes`:

```json
{
  "nombre": "Cliente Online Prueba",
  "email": "clienteonline@integratronic.com",
  "telefono": "600111222"
}
```

## 10. Pedidos online

Ruta base:

```text
/api/pedidos-online
```

Endpoints CRUD:

- `GET /api/pedidos-online`
- `GET /api/pedidos-online/{id}`
- `POST /api/pedidos-online`
- `PUT /api/pedidos-online/{id}`
- `DELETE /api/pedidos-online/{id}`

Endpoint de flujo de negocio:

- `POST /api/pedidos-online/registrar`

Ejemplo `POST /api/pedidos-online/registrar`:

```json
{
  "idCliente": 1,
  "lineas": [
    {
      "idProducto": 1,
      "cantidad": 1
    }
  ]
}
```

Este flujo:

- Comprueba cliente.
- Comprueba productos.
- Comprueba stock.
- Calcula total.
- Guarda pedido y lineas.
- Reduce stock.
- Registra `MovimientoStock` como `SALIDA`.
- No crea `PagoOnline` automaticamente.

## 11. Lineas de pedido

Ruta base:

```text
/api/lineas-pedido
```

Endpoints:

- `GET /api/lineas-pedido`
- `GET /api/lineas-pedido/{id}`
- `POST /api/lineas-pedido`
- `PUT /api/lineas-pedido/{id}`
- `DELETE /api/lineas-pedido/{id}`

## 12. Pagos online

Ruta base:

```text
/api/pagos-online
```

Endpoints CRUD:

- `GET /api/pagos-online`
- `GET /api/pagos-online/{id}`
- `POST /api/pagos-online`
- `PUT /api/pagos-online/{id}`
- `DELETE /api/pagos-online/{id}`

Endpoint de flujo de negocio:

- `POST /api/pagos-online/registrar`

Ejemplo `POST /api/pagos-online/registrar`:

```json
{
  "idPedido": 1,
  "importe": 699.99,
  "estadoPago": "PAGADO",
  "metodoPago": "TARJETA"
}
```

Este flujo:

- Comprueba que el pedido existe.
- Comprueba que el importe coincide con el total del pedido.
- Evita registrar pagos duplicados.
- No integra pasarela real externa.
- No modifica stock.

## 13. Informes

Ruta base:

```text
/api/informes
```

Endpoint:

- `GET /api/informes/ventas`

Respuesta esperada:

```json
{
  "totalVentasFisicas": 1,
  "totalPedidosOnline": 1,
  "totalPagosOnline": 1,
  "importeTotalVentasFisicas": 1349.98,
  "importeTotalPedidosOnline": 699.99,
  "importeTotalPagosOnline": 699.99
}
```

Este informe:

- Calcula totales e importes a partir de ventas fisicas, pedidos online y pagos online.
- No crea tabla nueva de informes.
