package aplicacion.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ColeccionCreadaEvent extends ApplicationEvent {
    private final String coleccionId;

    public ColeccionCreadaEvent(Object source, String coleccionId) {
        super(source);
        this.coleccionId = coleccionId;
    }
}

