package domain.repositorios;

import domain.colecciones.fuentes.Fuente;
import domain.colecciones.fuentes.FuenteId;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RepositorioDeFuentesImpl implements RepositorioDeFuentesCustom {
    @PersistenceContext
    private EntityManager em;

    @Autowired // Inyecto el repositorio de fuentes normal
    private RepositorioDeFuentes repositorioDeFuentes;

    @Override
    public void saveIfNotExists(Fuente nuevaFuente) {
        if (nuevaFuente == null) return;

        // Verificar si la fuente ya existe
        var id = nuevaFuente.getId();

        // Buscar la fuente existente por ID
        repositorioDeFuentes.findById(id)
                .orElseGet(() -> repositorioDeFuentes.save(nuevaFuente)); // Si no existe, se guarda la nueva fuente
    }

    @Override
    public void saveAllIfNotExists(List<Fuente> nuevasFuentes) {
        if (nuevasFuentes == null || nuevasFuentes.isEmpty()) return;

        // Obtener ids de las nuevas fuentes
        List<FuenteId> idsNuevos = nuevasFuentes.stream()
                .map(Fuente::getId)
                .toList();

        // Obtener los ids existentes en la base de datos
        var existentes = repositorioDeFuentes.findAllById(idsNuevos);
        var idsExistentes = existentes.stream()
                .map(Fuente::getId)
                .collect(java.util.stream.Collectors.toSet());

        // Filtrar solo las fuentes que no están en la BD
        List<Fuente> soloNuevas = nuevasFuentes.stream()
                .filter(f -> !idsExistentes.contains(f.getId()))
                .toList();

        // Solo guardar las fuentes que no existen
        repositorioDeFuentes.saveAll(soloNuevas);
    }
}
