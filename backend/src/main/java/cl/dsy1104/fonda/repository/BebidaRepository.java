package cl.dsy1104.fonda.repository;

import cl.dsy1104.fonda.model.Bebida;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BebidaRepository extends JpaRepository<Bebida, Long> {

    //find by nombre containing ese texto, ignorando mayusculas o minusculas
    //ya viene incluido save(bebida), findAll(), findById(id), deleteById(id), existsById(id)en q
    List<Bebida> findByNombreContainingIgnoreCase(String nombre);
}