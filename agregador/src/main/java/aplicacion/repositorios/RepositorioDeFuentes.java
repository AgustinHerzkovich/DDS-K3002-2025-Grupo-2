package aplicacion.repositorios;

import aplicacion.domain.colecciones.fuentes.Fuente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repositorio de Fuentes general
@Repository
public interface RepositorioDeFuentes extends JpaRepository<Fuente, String> {
}