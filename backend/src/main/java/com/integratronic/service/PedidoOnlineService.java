package com.integratronic.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.integratronic.dto.RegistrarPedidoOnlineLineaRequestDTO;
import com.integratronic.dto.RegistrarPedidoOnlineRequestDTO;
import com.integratronic.model.Cliente;
import com.integratronic.model.LineaPedido;
import com.integratronic.model.MovimientoStock;
import com.integratronic.model.PedidoOnline;
import com.integratronic.model.Producto;
import com.integratronic.model.Stock;
import com.integratronic.repository.ClienteRepository;
import com.integratronic.repository.LineaPedidoRepository;
import com.integratronic.repository.MovimientoStockRepository;
import com.integratronic.repository.PedidoOnlineRepository;
import com.integratronic.repository.ProductoRepository;
import com.integratronic.repository.StockRepository;

@Service
public class PedidoOnlineService {

    private final PedidoOnlineRepository pedidoOnlineRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final StockRepository stockRepository;
    private final LineaPedidoRepository lineaPedidoRepository;
    private final MovimientoStockRepository movimientoStockRepository;

    public PedidoOnlineService(PedidoOnlineRepository pedidoOnlineRepository, ClienteRepository clienteRepository,
            ProductoRepository productoRepository, StockRepository stockRepository,
            LineaPedidoRepository lineaPedidoRepository, MovimientoStockRepository movimientoStockRepository) {
        this.pedidoOnlineRepository = pedidoOnlineRepository;
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
        this.stockRepository = stockRepository;
        this.lineaPedidoRepository = lineaPedidoRepository;
        this.movimientoStockRepository = movimientoStockRepository;
    }

    public List<PedidoOnline> listar() {
        return pedidoOnlineRepository.findAll();
    }

    public Optional<PedidoOnline> buscarPorId(Integer id) {
        return pedidoOnlineRepository.findById(id);
    }

    public PedidoOnline guardar(PedidoOnline pedidoOnline) {
        return pedidoOnlineRepository.save(pedidoOnline);
    }

    public void eliminar(Integer id) {
        pedidoOnlineRepository.deleteById(id);
    }

    @Transactional
    public PedidoOnline registrarPedidoOnline(RegistrarPedidoOnlineRequestDTO request) {
        validarRequestRegistrarPedidoOnline(request);

        Cliente cliente = clienteRepository.findById(request.getIdCliente())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

        Map<Integer, Producto> productosPorId = new HashMap<>();
        Map<Integer, Stock> stocksPorProducto = new HashMap<>();
        Map<Integer, Integer> cantidadesPorProducto = new HashMap<>();
        BigDecimal totalPedido = BigDecimal.ZERO;

        for (RegistrarPedidoOnlineLineaRequestDTO lineaRequest : request.getLineas()) {
            validarLineaRegistrarPedidoOnline(lineaRequest);

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
            totalPedido = totalPedido.add(calcularSubtotal(producto, lineaRequest.getCantidad()));
        }

        PedidoOnline pedidoOnline = new PedidoOnline(
                LocalDateTime.now(),
                "CONFIRMADO",
                totalPedido,
                cliente);
        PedidoOnline pedidoGuardado = pedidoOnlineRepository.save(pedidoOnline);

        for (RegistrarPedidoOnlineLineaRequestDTO lineaRequest : request.getLineas()) {
            Producto producto = productosPorId.get(lineaRequest.getIdProducto());
            Stock stock = stocksPorProducto.get(lineaRequest.getIdProducto());
            BigDecimal subtotal = calcularSubtotal(producto, lineaRequest.getCantidad());

            LineaPedido lineaPedido = new LineaPedido(
                    lineaRequest.getCantidad(),
                    producto.getPrecio(),
                    subtotal,
                    pedidoGuardado,
                    producto);
            lineaPedidoRepository.save(lineaPedido);

            stock.setCantidadDisponible(stock.getCantidadDisponible() - lineaRequest.getCantidad());
            stockRepository.save(stock);

            MovimientoStock movimientoStock = new MovimientoStock(
                    "SALIDA",
                    lineaRequest.getCantidad(),
                    LocalDateTime.now(),
                    "Salida por pedido online",
                    producto);
            movimientoStockRepository.save(movimientoStock);
        }

        return pedidoGuardado;
    }

    private void validarRequestRegistrarPedidoOnline(RegistrarPedidoOnlineRequestDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("El pedido no puede ser nulo");
        }

        if (request.getIdCliente() == null) {
            throw new IllegalArgumentException("El cliente es obligatorio");
        }

        if (request.getLineas() == null || request.getLineas().isEmpty()) {
            throw new IllegalArgumentException("El pedido debe tener al menos una linea");
        }
    }

    private void validarLineaRegistrarPedidoOnline(RegistrarPedidoOnlineLineaRequestDTO lineaRequest) {
        if (lineaRequest == null) {
            throw new IllegalArgumentException("La linea de pedido no puede ser nula");
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
