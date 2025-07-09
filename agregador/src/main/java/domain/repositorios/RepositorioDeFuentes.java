package domain.repositorios;

import domain.colecciones.fuentes.Fuente;
import domain.colecciones.fuentes.FuenteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RepositorioDeFuentes extends JpaRepository<Fuente, FuenteId>, RepositorioDeFuentesCustom {
}