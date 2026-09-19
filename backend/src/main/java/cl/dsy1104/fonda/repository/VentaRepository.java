package cl.dsy1104.fonda.repository;

import cl.dsy1104.fonda.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    //ya viene incluido save(bebida), findAll(), findById(id), deleteById(id), existsById(id)
}