package aplicacion.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class FuenteAgregadaAColeccionEvent extends ApplicationEvent {
    private final String coleccionId;
    private final String fuenteId;

    public FuenteAgregadaAColeccionEvent(Object source, String coleccionId, String fuenteId) {
        super(source);
        this.coleccionId = coleccionId;
        this.fuenteId = fuenteId;
    }
}

