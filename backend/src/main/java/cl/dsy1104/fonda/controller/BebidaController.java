package cl.dsy1104.fonda.controller;

import cl.dsy1104.fonda.dto.BebidaRequest;
import cl.dsy1104.fonda.model.Bebida;
import cl.dsy1104.fonda.service.BebidaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
//api bebidas es el prefijo de ruta para todos los metodos
@RequestMapping("/api/bebidas")
public class BebidaController {

    private final BebidaService bebidaService;

    public BebidaController(BebidaService bebidaService) {
        this.bebidaService = bebidaService;
    }

    @GetMapping
    public List<Map<String, Object>> listar(@RequestParam(required = false) String nombre) {
        return bebidaService.listar(nombre).stream()
                .map(this::aRespuesta)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> buscarPorId(@PathVariable Long id) {
        Bebida bebida = bebidaService.buscarPorId(id)
                .orElseThrow(() -> new NoSuchElementException("Bebida no encontrada"));
        return ResponseEntity.ok(aRespuesta(bebida));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(@Valid @RequestBody BebidaRequest request) {
        Bebida bebida = aEntidad(request, new Bebida());
        Bebida creada = bebidaService.crear(bebida);

        URI location = URI.create("/api/bebidas/" + creada.getId());
        return ResponseEntity.created(location).body(aRespuesta(creada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizar(@PathVariable Long id,
                                                            @Valid @RequestBody BebidaRequest request) {
        Bebida datos = aEntidad(request, new Bebida());
        Bebida actualizada = bebidaService.actualizar(id, datos)
                .orElseThrow(() -> new NoSuchElementException("Bebida no encontrada"));
        return ResponseEntity.ok(aRespuesta(actualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!bebidaService.eliminar(id)) {
            throw new NoSuchElementException("Bebida no encontrada");
        }
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/restriccion")
    public ResponseEntity<Map<String, Object>> marcarRestriccion(@PathVariable Long id) {
        Bebida bebida = bebidaService.marcarRestriccion(id)
                .orElseThrow(() -> new NoSuchElementException("Bebida no encontrada"));
        return ResponseEntity.ok(aRespuesta(bebida));
    }

    //de bebidarequest(dto) a bebida
    private Bebida aEntidad(BebidaRequest r, Bebida bebida) {
        bebida.setNombre(r.getNombre());
        bebida.setTipo(r.getTipo());
        bebida.setVolumenML(r.getVolumenML());
        bebida.setStock(r.getStock());
        bebida.setGradosAlcohol(r.getGradosAlcohol());
        bebida.setCertificada(r.getCertificada());
        bebida.setAzucarPorLitro(r.getAzucarPorLitro());
        return bebida;
    }

    //json de salida
    private Map<String, Object> aRespuesta(Bebida b) {
        Map<String, Object> r = new java.util.LinkedHashMap<>();
        r.put("id", b.getId());
        r.put("nombre", b.getNombre());
        r.put("tipo", b.getTipo());
        r.put("volumenML", b.getVolumenML());
        r.put("stock", b.getStock());
        if (b.getGradosAlcohol() != null) r.put("gradosAlcohol", b.getGradosAlcohol());
        if (b.getCertificada() != null) r.put("certificada", b.getCertificada());
        if (b.getAzucarPorLitro() != null) r.put("azucarPorLitro", b.getAzucarPorLitro());
        r.put("ventaRestringida", b.isVentaRestringida());
        r.put("precio", bebidaService.calcularPrecio(b));
        return r;
    }
}