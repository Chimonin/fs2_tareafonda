
//modela reglas y decisiones de la app, reglas de negocio 
package cl.dsy1104.fonda.service;

import cl.dsy1104.fonda.exception.VentaRechazadaException;
import cl.dsy1104.fonda.model.*;
import cl.dsy1104.fonda.repository.BebidaRepository;
import cl.dsy1104.fonda.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final BebidaRepository bebidaRepository;
    private final BebidaService bebidaService;

    //lo trae de application.properties
    @Value("${fonda.limite-unidades-por-cliente}")
    private int limiteUnidadesPorCliente;

    @Autowired
    public VentaService(VentaRepository ventaRepository,
                         BebidaRepository bebidaRepository,
                         BebidaService bebidaService) {
        this.ventaRepository = ventaRepository;
        this.bebidaRepository = bebidaRepository;
        this.bebidaService = bebidaService;
    }

    public Venta registrar(Long bebidaId, int unidades) {
        Bebida bebida = bebidaRepository.findById(bebidaId)
                .orElseThrow(() -> new NoSuchElementException("Bebida no encontrada"));

        if (bebida.isVentaRestringida()) {
            registrarRechazo(bebida, unidades, "VENTA_RESTRINGIDA");
            throw new VentaRechazadaException("VENTA_RESTRINGIDA",
                    "La bebida " + bebida.getNombre() + " tiene venta restringida.");
        }

        if (bebida.getTipo() == TipoBebida.ALCOHOLICA && unidades > limiteUnidadesPorCliente) {
            registrarRechazo(bebida, unidades, "LIMITE_EXCEDIDO");
            throw new VentaRechazadaException("LIMITE_EXCEDIDO",
                    unidades + " unidades superan el limite de " + limiteUnidadesPorCliente + " por cliente.");
        }

        if (bebida.getStock() < unidades) {
            registrarRechazo(bebida, unidades, "STOCK_INSUFICIENTE");
            throw new VentaRechazadaException("STOCK_INSUFICIENTE",
                    "Stock insuficiente: hay " + bebida.getStock() + " unidades disponibles.");
        }

        int precioUnitario = bebidaService.calcularPrecio(bebida);
        int total = precioUnitario * unidades;

        bebida.setStock(bebida.getStock() - unidades);
        bebidaRepository.save(bebida);

        Venta venta = new Venta();
        venta.setBebida(bebida);
        venta.setUnidades(unidades);
        venta.setTotal(total);
        venta.setEstado(EstadoVenta.AUTORIZADA);

        return ventaRepository.save(venta);
    }

    private void registrarRechazo(Bebida bebida, int unidades, String motivo) {
        Venta venta = new Venta();
        venta.setBebida(bebida);
        venta.setUnidades(unidades);
        venta.setTotal(0);
        venta.setEstado(EstadoVenta.RECHAZADA);
        venta.setMotivo(motivo);
        ventaRepository.save(venta);
    }

    //historial de ventas
    public List<Venta> listar() {
        return ventaRepository.findAll();
    }
}