package com.integratronic.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.integratronic.dto.RegistrarVentaLineaRequestDTO;
import com.integratronic.dto.RegistrarVentaRequestDTO;
import com.integratronic.model.LineaVenta;
import com.integratronic.model.MovimientoStock;
import com.integratronic.model.Producto;
import com.integratronic.model.Stock;
import com.integratronic.model.Usuario;
import com.integratronic.model.Venta;
import com.integratronic.repository.LineaVentaRepository;
import com.integratronic.repository.MovimientoStockRepository;
import com.integratronic.repository.ProductoRepository;
import com.integratronic.repository.StockRepository;
import com.integratronic.repository.UsuarioRepository;
import com.integratronic.repository.VentaRepository;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    private final StockRepository stockRepository;
    private final LineaVentaRepository lineaVentaRepository;
    private final MovimientoStockRepository movimientoStockRepository;

    public VentaService(VentaRepository ventaRepository, UsuarioRepository usuarioRepository,
            ProductoRepository productoRepository, StockRepository stockRepository,
            LineaVentaRepository lineaVentaRepository, MovimientoStockRepository movimientoStockRepository) {
        this.ventaRepository = ventaRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
        this.stockRepository = stockRepository;
        this.lineaVentaRepository = lineaVentaRepository;
        this.movimientoStockRepository = movimientoStockRepository;
    }

    public List<Venta> listar() {
        return ventaRepository.findAll();
    }

    public Optional<Venta> buscarPorId(Integer id) {
        return ventaRepository.findById(id);
    }

    public Venta guardar(Venta venta) {
        return ventaRepository.save(venta);
    }

    public void eliminar(Integer id) {
        ventaRepository.deleteById(id);
    }

    @Transactional
    public Venta registrarVenta(RegistrarVentaRequestDTO request) {
        validarRequestRegistrarVenta(request);

        Usuario usuario = usuarioRepository.findById(request.getIdUsuario())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Map<Integer, Producto> productosPorId = new HashMap<>();
        Map<Integer, Stock> stocksPorProducto = new HashMap<>();
        Map<Integer, Integer> cantidadesPorProducto = new HashMap<>();
        BigDecimal sumaSubtotales = BigDecimal.ZERO;

        for (RegistrarVentaLineaRequestDTO lineaRequest : request.getLineas()) {
            validarLineaRegistrarVenta(lineaRequest);

            Producto producto = productosPorId.computeIfAbsent(lineaRequest.getIdProducto(), idProducto ->
                    productoRepository.findById(idProducto)
                            .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado")));

            Stock stock = stocksPorProducto.computeIfAbsent(lineaRequest.getIdProducto(), idProducto ->
                    stockRepository.findByProducto_IdProducto(idProducto)
                            .orElseThrow(() -> new IllegalArgumentException("Stock no encontrado")));

            Integer cantidadAcumulada = cantidadesPorProducto.getOrDefault(lineaRequest.getIdProducto(), 0)
                    + lineaRequest.getCantidad();
            if (stock.getCantidadDisponible() < cantidadAcumulada) {
                throw new IllegalArgumentException("No hay stock suficiente");
            }

            cantidadesPorProducto.put(lineaRequest.getIdProducto(), cantidadAcumulada);
            sumaSubtotales = sumaSubtotales.add(calcularSubtotal(producto, lineaRequest.getCantidad()));
        }

        BigDecimal total = sumaSubtotales.subtract(request.getDescuento());
        if (total.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El total no puede ser negativo");
        }

        LocalDateTime fechaActual = LocalDateTime.now();
        Venta venta = new Venta(fechaActual, request.getDescuento(), total, usuario);
        Venta ventaGuardada = ventaRepository.save(venta);

        for (RegistrarVentaLineaRequestDTO lineaRequest : request.getLineas()) {
            Producto producto = productosPorId.get(lineaRequest.getIdProducto());
            Stock stock = stocksPorProducto.get(lineaRequest.getIdProducto());
            BigDecimal subtotal = calcularSubtotal(producto, lineaRequest.getCantidad());

            LineaVenta lineaVenta = new LineaVenta(
                    lineaRequest.getCantidad(),
                    producto.getPrecio(),
                    subtotal,
                    ventaGuardada,
                    producto);
            lineaVentaRepository.save(lineaVenta);

            stock.setCantidadDisponible(stock.getCantidadDisponible() - lineaRequest.getCantidad());
            stockRepository.save(stock);

            MovimientoStock movimientoStock = new MovimientoStock(
                    "SALIDA",
                    lineaRequest.getCantidad(),
                    LocalDateTime.now(),
                    "Salida por venta fisica",
                    producto);
            movimientoStockRepository.save(movimientoStock);
        }

        return ventaGuardada;
    }

    private void validarRequestRegistrarVenta(RegistrarVentaRequestDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("La venta no puede ser nula");
        }

        if (request.getIdUsuario() == null) {
            throw new IllegalArgumentException("El usuario es obligatorio");
        }

        if (request.getDescuento() == null) {
            throw new IllegalArgumentException("El descuento es obligatorio");
        }

        if (request.getDescuento().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El descuento no puede ser negativo");
        }

        if (request.getLineas() == null || request.getLineas().isEmpty()) {
            throw new IllegalArgumentException("La venta debe tener al menos una línea");
        }
    }

    private void validarLineaRegistrarVenta(RegistrarVentaLineaRequestDTO lineaRequest) {
        if (lineaRequest == null) {
            throw new IllegalArgumentException("La línea de venta no puede ser nula");
        }

        if (lineaRequest.getIdProducto() == null) {
            throw new IllegalArgumentException("El producto es obligatorio");
        }

        if (lineaRequest.getCantidad() == null || lineaRequest.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que 0");
        }
    }

    private BigDecimal calcularSubtotal(Producto producto, Integer cantidad) {
        return producto.getPrecio().multiply(BigDecimal.valueOf(cantidad));
    }
}
