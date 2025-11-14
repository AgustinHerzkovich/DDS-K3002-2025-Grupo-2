package aplicacion.domain.agregadorID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class AgregadorID {

    @Id
    private Long id = 1L; // siempre la misma fila

    private String componentUuid;
    public AgregadorID() {
    }
    public AgregadorID(String componentUuid) {
        this.componentUuid = componentUuid;
    }
}