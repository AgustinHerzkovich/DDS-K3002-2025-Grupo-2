package aplicacion.events;

import aplicacion.domain.colecciones.Coleccion;
import aplicacion.domain.colecciones.fuentes.Fuente;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ColeccionCreadaEvent extends ApplicationEvent {
    private final String coleccionId;
    private final List<String> fuentesId;
    public ColeccionCreadaEvent(Object source, Coleccion coleccion) {
        super(source);
        this.coleccionId = coleccion.getId();
        this.fuentesId = new ArrayList<>(coleccion.getFuentes().stream().map(Fuente::getId).toList());
    }
}

