package domain.repositorios;

import domain.hechos.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;


// REPOSITORIO DE HECHOS
public interface RepositorioDeHechos extends JpaRepository<Hecho, Long> {
}
