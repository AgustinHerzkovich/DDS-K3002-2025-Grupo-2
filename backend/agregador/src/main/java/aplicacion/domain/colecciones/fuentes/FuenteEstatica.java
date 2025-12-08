package aplicacion.domain.colecciones.fuentes;


import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Arrays;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class FuenteEstatica extends Fuente {
    private Boolean fueConsultada = false;
    public FuenteEstatica(String id) {
        super(id, transformarId(id));
        this.fueConsultada = false;
    }

    private static String transformarId(String id) {
        try {
            if (id == null || id.isBlank()) return "Fuente Estatica sin nombre";

            String base = id.replaceAll("\\.[^.]+$", "");

            String[] palabras = base.split("[_\\-\\.\\s]+");

            return Arrays.stream(palabras)
                    .filter(s -> !s.isBlank())
                    .map(s -> {
                        if (s.length() == 1) return s.toUpperCase();
                        return s.substring(0,1).toUpperCase() + s.substring(1).toLowerCase();
                    })
                    .collect(Collectors.joining(" "));
        } catch (Exception e) {
            return !id.isBlank() ? id : "Fuente Estatica sin nombre";
        }
    }




}
