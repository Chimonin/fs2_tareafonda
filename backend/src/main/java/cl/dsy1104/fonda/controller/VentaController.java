package cl.dsy1104.fonda.controller;

import cl.dsy1104.fonda.dto.VentaRequest;
import cl.dsy1104.fonda.model.Venta;
import cl.dsy1104.fonda.service.VentaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> registrar(@Valid @RequestBody VentaRequest request) {
        Venta venta = ventaService.registrar(request.getBebidaId(), request.getUnidades());

        URI location = URI.create("/api/ventas/" + venta.getId());
        return ResponseEntity.created(location).body(aRespuesta(venta));
    }

    @GetMapping
    public List<Map<String, Object>> listar() {
        return ventaService.listar().stream()
                .map(this::aRespuesta)
                .toList();
    }


    //venta no tiene aEntidad porque no se puede pasar directamente de
    //dto a bebida(entidad)

    //arespuesta es de lo que se guarda a lo que se le muestra al cliente
    private Map<String, Object> aRespuesta(Venta v) {
        Map<String, Object> r = new LinkedHashMap<>();
        r.put("id", v.getId());
        r.put("bebidaId", v.getBebida().getId());
        r.put("nombre", v.getBebida().getNombre());
        r.put("unidades", v.getUnidades());
        r.put("total", v.getTotal());
        r.put("estado", v.getEstado());
        if (v.getMotivo() != null) {
            r.put("motivo", v.getMotivo());
        }
        return r;
    }
}