package aplicacion.domain.algoritmos;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import aplicacion.domain.hechos.Hecho;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

// Regla de negocio: Consideramos hecho repetido por fuente a aquellos que contengan los mismos atributos que incluimos en `EqualsAndHashCode` en el Hecho.
//@Entity
@Getter
@Setter
public abstract class AlgoritmoConsenso {
    public abstract List<Hecho> curarHechos(Map<Hecho, Long> cantidadPorHecho, Long totalFuentes);
}