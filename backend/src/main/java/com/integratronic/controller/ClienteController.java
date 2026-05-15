package com.integratronic.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.integratronic.dto.ClienteRequestDTO;
import com.integratronic.dto.ClienteResponseDTO;
import com.integratronic.model.Cliente;
import com.integratronic.service.ClienteService;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public List<ClienteResponseDTO> listar() {
        return clienteService.listar().stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable Integer id) {
        return clienteService.buscarPorId(id)
                .map(this::convertirAResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> guardar(@RequestBody ClienteRequestDTO clienteRequestDTO) {
        Cliente cliente = convertirAEntidad(clienteRequestDTO);
        Cliente clienteGuardado = clienteService.guardar(cliente);
        return ResponseEntity.ok(convertirAResponseDTO(clienteGuardado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> actualizar(@PathVariable Integer id,
            @RequestBody ClienteRequestDTO clienteRequestDTO) {
        if (clienteService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Cliente cliente = convertirAEntidad(clienteRequestDTO);
        cliente.setIdCliente(id);
        Cliente clienteActualizado = clienteService.guardar(cliente);
        return ResponseEntity.ok(convertirAResponseDTO(clienteActualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (clienteService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        clienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private ClienteResponseDTO convertirAResponseDTO(Cliente cliente) {
        return new ClienteResponseDTO(
                cliente.getIdCliente(),
                cliente.getNombre(),
                cliente.getEmail(),
                cliente.getTelefono());
    }

    private Cliente convertirAEntidad(ClienteRequestDTO clienteRequestDTO) {
        return new Cliente(
                clienteRequestDTO.getNombre(),
                clienteRequestDTO.getEmail(),
                clienteRequestDTO.getTelefono());
    }
}
