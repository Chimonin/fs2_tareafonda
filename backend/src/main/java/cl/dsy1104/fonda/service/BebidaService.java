package cl.dsy1104.fonda.service;

import cl.dsy1104.fonda.model.Bebida;
import cl.dsy1104.fonda.model.TipoBebida;
import cl.dsy1104.fonda.repository.BebidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BebidaService {

    //atributo
    private final BebidaRepository bebidaRepository;

    @Autowired
    public BebidaService(BebidaRepository bebidaRepository) {
        this.bebidaRepository = bebidaRepository;
    }

    //si el nombre es null o vacio retorna todo
    //si viene con nombre busca el nombre
    //list es una lista de objetos
    public List<Bebida> listar(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            return bebidaRepository.findAll();
        }
        return bebidaRepository.findByNombreContainingIgnoreCase(nombre);
    }

    //directo del repository
    //optional significa que puede que haya un valor o puede que no
    public Optional<Bebida> buscarPorId(Long id) {
        return bebidaRepository.findById(id);
    }

    public Bebida crear(Bebida bebida) {
        return bebidaRepository.save(bebida);
    }


    public int calcularPrecio(Bebida bebida) {
        if (bebida.getTipo() == TipoBebida.ALCOHOLICA) {
            //doubles son decimales
            double precio = 3500;
            //boolean true equals pregunta si el valor de get certificada es true
            if (!Boolean.TRUE.equals(bebida.getCertificada())) {
                precio *= 1.20;
            }
            return (int) precio;
        } else {
            double precio = 2000;
            if (bebida.getAzucarPorLitro() != null && bebida.getAzucarPorLitro() > 80) {
                precio *= 1.10;
            }
            return (int) precio;
        }
    }

    //se setea los valores de existente desde getters datos
    public Optional<Bebida> actualizar(Long id, Bebida datos) {
        return bebidaRepository.findById(id).map(existente -> {
            existente.setNombre(datos.getNombre());
            existente.setTipo(datos.getTipo());
            existente.setVolumenML(datos.getVolumenML());
            existente.setStock(datos.getStock());
            existente.setGradosAlcohol(datos.getGradosAlcohol());
            existente.setCertificada(datos.getCertificada());
            existente.setAzucarPorLitro(datos.getAzucarPorLitro());
            return bebidaRepository.save(existente);
        });
    }

    public boolean eliminar(Long id) {
        //!bebidarepository significa si no existe

        if (!bebidaRepository.existsById(id)) {
            return false;
        }
        bebidaRepository.deleteById(id);
        //return true indica que la operacion fue exitosa
        return true;
    }

    //busca la bebida por id y se la pasa al map, el map la restringe y la guarda y se
    //devuelve un optional que la retorna
    public Optional<Bebida> marcarRestriccion(Long id) {
        return bebidaRepository.findById(id).map(bebida -> {
            bebida.setVentaRestringida(true);
            return bebidaRepository.save(bebida);
        });
    }
}