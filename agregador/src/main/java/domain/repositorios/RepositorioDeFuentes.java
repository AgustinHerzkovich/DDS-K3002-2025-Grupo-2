package domain.repositorios;

import domain.colecciones.fuentes.Fuente;
import domain.colecciones.fuentes.FuenteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public interface RepositorioDeFuentes extends JpaRepository<Fuente, FuenteId> {
    default List<Fuente> saveOnlyNew(List<Fuente> nuevasFuentes) {
        if (nuevasFuentes == null || nuevasFuentes.isEmpty()) return List.of();

        // Extraer los IDs de las nuevas fuentes
        Set<FuenteId> idsNuevos = nuevasFuentes.stream()
                .map(Fuente::getId)
                .collect(Collectors.toSet());

        // Obtener los IDs existentes
        List<Fuente> existentes = findAllById(idsNuevos);
        Set<FuenteId> idsExistentes = existentes.stream()
                .map(Fuente::getId)
                .collect(Collectors.toSet());

        // Filtrar solo las fuentes que no tienen ID repetido
        List<Fuente> noRepetidas = nuevasFuentes.stream()
                .filter(f -> !idsExistentes.contains(f.getId()))
                .collect(Collectors.toList());

        return saveAll(noRepetidas);
    }
}